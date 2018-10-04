/**
 * 
 */
package com.base.common.util;

import org.apache.sanselan.ImageReadException;
import org.apache.sanselan.Sanselan;
import org.apache.sanselan.common.byteSources.ByteSource;
import org.apache.sanselan.common.byteSources.ByteSourceArray;
import org.apache.sanselan.formats.jpeg.JpegImageParser;
import org.apache.sanselan.formats.jpeg.segments.UnknownSegment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.color.ColorSpace;
import java.awt.color.ICC_ColorSpace;
import java.awt.color.ICC_Profile;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/***
 * CMYK 이미지 컨버팅 클래스
 */
@Component
public class CMYKConvertUtil {
	@Value(value = "${convert.icc_profile}")
	private String IccProfilePath;
	
	public static final int COLOR_TYPE_RGB = 1;
	public static final int COLOR_TYPE_CMYK = 2;
	public static final int COLOR_TYPE_YCCK = 3;

	private int colorType = COLOR_TYPE_RGB;
	private boolean hasAdobeMarker = false;

	/***
	 * CMYK 이미지 RGB로 전환 (autoRotateImage 함수에서 추출)
	 * mf는 CMYK 이미지일 경우에만 쓴다.
	 * @param mf
	 * @return
	 * @throws Exception
	 */
	public BufferedImage convertToRGB(MultipartFile mf) throws Exception {
		colorType = COLOR_TYPE_RGB;
		hasAdobeMarker = false;
		
		BufferedImage originalImage = null;
		//FileInputStream fi = new FileInputStream(file);
		byte[] buf = mf.getBytes();
		ByteArrayInputStream bi = new ByteArrayInputStream(buf);
		ImageInputStream stream = ImageIO.createImageInputStream(bi);
		Iterator<ImageReader> iter = ImageIO.getImageReaders(stream);
        while (iter.hasNext()) {
            ImageReader reader = iter.next();
            reader.setInput(stream);
            
            ICC_Profile profile = null;
            try{
            	colorType = COLOR_TYPE_CMYK;
                checkAdobeMarker(buf);
                profile = Sanselan.getICCProfile(buf);
                WritableRaster raster = (WritableRaster) reader.readRaster(0, null);
                
                if (raster.getNumBands() > 3) { //진짜 CMYK
                	if (colorType == COLOR_TYPE_YCCK){
                        convertYcckToCmyk(raster);
                    }
                    if (hasAdobeMarker){
                        convertInvertedColors(raster);
                    }
                    originalImage = convertCmykToRgb(raster, profile);   
                }
                else { //RGB인데 Format이 CMYK
                	Raster r2 = (Raster) raster;
                	originalImage = readImage(r2);
                }
            }catch(Exception ee){
            	originalImage = null;
            }finally {
                stream.close(); //원본에는 close해주는 부분이 없어서 추가해 줬음, 위치상으로 애매한데 리턴 전이라 그냥 닫아줌
                bi.close();
            }
        }		
        
        return originalImage;
	}
	
	private void checkAdobeMarker(byte[] buf) throws IOException, ImageReadException {
		JpegImageParser parser = new JpegImageParser();
		ByteSource byteSource = new ByteSourceArray(buf);
		@SuppressWarnings("rawtypes")
        ArrayList segments = parser.readSegments(byteSource, new int[] { 0xffee }, true);
		if (segments != null && segments.size() >= 1) {
			UnknownSegment app14Segment = (UnknownSegment) segments.get(0);
			byte[] data = app14Segment.bytes;
			if (data.length >= 12 && data[0] == 'A' && data[1] == 'd' && data[2] == 'o' && data[3] == 'b'
					&& data[4] == 'e') {
				hasAdobeMarker = true;
				int transform = app14Segment.bytes[11] & 0xff;
				if (transform == 2)
					colorType = COLOR_TYPE_YCCK;
			}
		}
	}

	private void convertYcckToCmyk(WritableRaster raster) {
		int height = raster.getHeight();
		int width = raster.getWidth();
		int stride = width * 4;
		int[] pixelRow = new int[stride];
		for (int h = 0; h < height; h++) {
			raster.getPixels(0, h, width, 1, pixelRow);

			for (int x = 0; x < stride; x += 4) {
				int y = pixelRow[x];
				int cb = pixelRow[x + 1];
				int cr = pixelRow[x + 2];

				int c = (int) (y + 1.402 * cr - 178.956);
				int m = (int) (y - 0.34414 * cb - 0.71414 * cr + 135.95984);
				y = (int) (y + 1.772 * cb - 226.316);

				if (c < 0)
					c = 0;
				else if (c > 255)
					c = 255;
				if (m < 0)
					m = 0;
				else if (m > 255)
					m = 255;
				if (y < 0)
					y = 0;
				else if (y > 255)
					y = 255;

				pixelRow[x] = 255 - c;
				pixelRow[x + 1] = 255 - m;
				pixelRow[x + 2] = 255 - y;
			}

			raster.setPixels(0, h, width, 1, pixelRow);
		}
	}

	public void convertInvertedColors(WritableRaster raster) {
		int height = raster.getHeight();
		int width = raster.getWidth();
		int stride = width * 4;
		int[] pixelRow = new int[stride];
		for (int h = 0; h < height; h++) {
			raster.getPixels(0, h, width, 1, pixelRow);
			for (int x = 0; x < stride; x++)
				pixelRow[x] = 255 - pixelRow[x];
			raster.setPixels(0, h, width, 1, pixelRow);
		}
	}

	private BufferedImage convertCmykToRgb(Raster cmykRaster, ICC_Profile cmykProfile) throws IOException {
		try{
			if (cmykProfile == null)
				//cmykProfile = ICC_Profile.getInstance(JpegReader.class.getResourceAsStream("/ISOcoated_v2_300_eci.icc"));
				cmykProfile = ICC_Profile.getInstance(new FileInputStream(IccProfilePath));
				
			ICC_ColorSpace cmykCS = new ICC_ColorSpace(cmykProfile);
			BufferedImage rgbImage = new BufferedImage(cmykRaster.getWidth(), cmykRaster.getHeight(),
					BufferedImage.TYPE_INT_RGB);
			WritableRaster rgbRaster = rgbImage.getRaster();
			ColorSpace rgbCS = rgbImage.getColorModel().getColorSpace();
			ColorConvertOp cmykToRgb = new ColorConvertOp(cmykCS, rgbCS, null);
			cmykToRgb.filter(cmykRaster, rgbRaster);
			return rgbImage;	
		}catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}
	
	private BufferedImage readImage(Raster raster) throws IOException
    {
        //Iterator<ImageReader> imageReaders = 
        //    ImageIO.getImageReadersBySuffix("jpg");
        //ImageReader imageReader = imageReaders.next();
        //ImageInputStream iis = ImageIO.createImageInputStream(stream);
        //imageReader.setInput(iis, true, true);
        //Raster raster = imageReader.readRaster(0, null);
        int w = raster.getWidth();
        int h = raster.getHeight();

        BufferedImage result =
            new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int rgb[] = new int[3];
        int pixel[] = new int[3];
        for (int x=0; x<w; x++)
        {
            for (int y=0; y<h; y++)
            {
                raster.getPixel(x, y, pixel);
                int Y = pixel[0];
                int CR = pixel[1];
                int CB = pixel[2];
                toRGB(Y, CB, CR, rgb);
                int r = rgb[0];
                int g = rgb[1];
                int b = rgb[2];
                int bgr = 
                    ((b & 0xFF) << 16) | 
                    ((g & 0xFF) <<  8) | 
                     (r & 0xFF);
                result.setRGB(x, y, bgr);
            }
        }
        return result;
    }
	
	 private void toRGB(int y, int cb, int cr, int rgb[])
	 {
		 float Y = y / 255.0f;
		 float Cb = (cb-128) / 255.0f;
		 float Cr = (cr-128) / 255.0f;

		 float R = Y + 1.4f * Cr;
		 float G = Y -0.343f * Cb - 0.711f * Cr;
		 float B = Y + 1.765f * Cb;

		 R = Math.min(1.0f, Math.max(0.0f, R));
		 G = Math.min(1.0f, Math.max(0.0f, G));
		 B = Math.min(1.0f, Math.max(0.0f, B));

		 int r = (int)(R * 255);
		 int g = (int)(G * 255);
		 int b = (int)(B * 255);

		 rgb[0] = r;
		 rgb[1] = g;
		 rgb[2] = b;
	}
}
