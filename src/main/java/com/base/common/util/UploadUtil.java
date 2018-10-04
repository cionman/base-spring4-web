/**
 * 
 */
package com.base.common.util;

import com.base.common.util.model.FileInfo;
import jcifs.smb.*;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.*;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


@Component
public class UploadUtil {
	@Value(value="${base.upload.path}")
	private String basePath;
	
	@Value(value="${base.image.smb.temp}")
	private String tempPath;
	
	@Value(value = "${base.image.smb.user}")
	private String smbUser;

	@Value(value = "${base.image.smb.password}")
	private String smbPassword;
	
	@Autowired
	private CMYKConvertUtil cmykConvert;
	
	/**
	 * 단일 파일 업로드.
	 * @param mfile
	 * @param subPath
	 * @return
	 */
	public FileInfo uploadFile(MultipartFile mfile, String subPath, String newFileName) {
		FileInfo fi = null;
		
		if(mfile!=null && mfile.getOriginalFilename().length() > 0) {
			String ext = null;
			newFileName = newFileName == null ? Long.toHexString(System.currentTimeMillis()) : newFileName;
			File file = null;
			File dir = new File(basePath+subPath);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			try {
				fi = new FileInfo();
				fi.setLogicalFileName(mfile.getOriginalFilename());
				fi.setFilePath(subPath);
				if(mfile.getOriginalFilename().indexOf(".")>=0) {
					ext = mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf(".")+1);
				}
				fi.setFileExtension(ext);
				fi.setFileLength(mfile.getSize());
				fi.setPhysicalFileName(newFileName+"."+ext);
				
				file =  new File(basePath+subPath,fi.getPhysicalFileName());
				
				mfile.transferTo(file);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		
		return fi;
	}

	/***
	 * 파일복사
	 * @param orginPath
	 * @param destPath
	 * @throws Exception
	 */
	public void copyFileNFS(String orginPath, String destPath) throws Exception {
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", smbUser, smbPassword);
		SmbFile originFile = new SmbFile(orginPath, auth);
		SmbFile destFile = new SmbFile(destPath, auth);
		SmbFile destDirPath= new SmbFile(destPath.substring(0, destPath.lastIndexOf("/")), auth);
		if(!destDirPath.exists()){
			destDirPath.mkdirs();
		}
		originFile.copyTo(destFile);
	}

	/***
	 * 파일 삭제
	 * @param orginPath
	 * @throws Exception
	 */
	public void deleteFile(String orginPath) throws Exception {
		
		File originFile = new File(orginPath);
		FileUtils.forceDelete(originFile);

	}

	/***
	 * 원격지 파일 삭제
	 * @param fullPath
	 */
	public void deleteFileNFS(String fullPath) {
		SmbFile sf = null;
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", smbUser, smbPassword);
		try {
			sf = new SmbFile(fullPath, auth);
			if(sf.exists()) {
				sf.delete();
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/***
	 * 단일 이미지 업로드
	 * @param mfile
	 * @param thumbWidth
	 * @param thumbHeight
	 * @param subPath
	 * @param newFileName
	 * @param targetExt
	 * @return
	 */
	public FileInfo uploadImage(MultipartFile mfile, int thumbWidth, int thumbHeight, String subPath, String newFileName, String targetExt) {
		FileInfo fi = null;
		
		if(mfile!=null && mfile.getOriginalFilename().length() > 0) {
	        int len = 0;

	        String ext = null;
			newFileName = newFileName == null ? Long.toHexString(System.currentTimeMillis()) : newFileName;
			
			File dir = new File(basePath+subPath);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			File file = null;
			InputStream in = null;
			OutputStream out = null;
			ByteArrayOutputStream baos = null;
			byte[] b = new byte[204800];
			try {
				fi = new FileInfo();
				fi.setLogicalFileName(mfile.getOriginalFilename());
				fi.setFilePath(subPath);
				if(mfile.getOriginalFilename().indexOf(".")>=0) {
					ext = targetExt == null ? mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf(".")+1) : targetExt;
				}
				fi.setFileExtension(ext);
				fi.setFileLength(mfile.getSize());
				fi.setPhysicalFileName(newFileName+"."+ext);
				
				file =  new File(basePath+subPath,fi.getPhysicalFileName());
				String fileUpperExt = ext.trim().toUpperCase();
				if("JPG".equals(fileUpperExt) || "PNG".equals(fileUpperExt) || "BMP".equals(fileUpperExt)){
					///이미지 인 경우
					
					//BufferedImage bi = ImageIO.read(mfile.getInputStream());
					BufferedImage bi;
					try {
						bi= ImageIO.read(mfile.getInputStream());
					} catch(Exception e) { //CMYK 이미지인 경우
						bi = cmykConvert.convertToRGB(mfile);
						if(bi==null){
								return null;
						}
					}
					out = new FileOutputStream(file);
					ImageIO.write(bi, ext, out);
					out.close();
							
					// 섬네일 만들기.
					Image srcImg = null;
					
					in = new FileInputStream(file);
					
					if(ext.toLowerCase().equals("png") || ext.toLowerCase().equals("gif")){
						srcImg = ImageIO.read(in);
					}else{
						srcImg = new ImageIcon(file.getAbsolutePath()).getImage();
					}
					int srcWidth = srcImg.getWidth(null);
			        int srcHeight = srcImg.getHeight(null);
			        
			        int destWidth = srcWidth, destHeight = srcHeight;
				
					if ((srcWidth > thumbWidth) || (srcHeight > thumbHeight))
			        {
			            double deltaWidth = (srcWidth - thumbWidth) / thumbWidth;
			            double deltaHeight = (srcHeight - thumbHeight) / thumbHeight;
			            double scalFactor = 1.0;

			            if (deltaHeight > deltaWidth)
			            {
			                scalFactor = (double)thumbHeight / (double)srcHeight;
			            }
			            else
			            {
			                scalFactor = (double)thumbWidth / (double)srcWidth;
			            }

			            destWidth *= scalFactor;
			            destHeight *= scalFactor;
			        }

			        if (destWidth < 1) destWidth = 1;
			        if (destHeight < 1) destHeight = 1;
			       
			        Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
			        int pixels[] = new int[destWidth * destHeight]; 
			        PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth);
			        
			        try {
			            pg.grabPixels();
			        } catch (InterruptedException e) {
			            throw new IOException(e.getMessage());
			        } 
			        
			        BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
			        destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
			        baos = new ByteArrayOutputStream();
			        ImageIO.write(destImg, ext, baos);
			        in.close();
			        in = new ByteArrayInputStream(baos.toByteArray());
			        out = new FileOutputStream(new File(file.getParent(), "s_"+file.getName()));
			        while((len = in.read(b, 0, b.length)) > 0) {
						out.write(b, 0, len);
					}
			        baos.close();
			        baos = null;
			        out.close();
			        out = null;
			        in.close();
			        in = null;
				}else{
					mfile.transferTo(file);
				}
				
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				if(baos != null) {
					try {
						baos.close();
					}
					catch(Exception e){
					}
					baos = null;
				}
				
				if(out != null) {
					try {
						out.close();
					}
					catch(Exception e){
					}
					out = null;
				}
				if(in != null) {
					try {
						in.close();
					}
					catch(Exception e) {
					}
					in = null;
				}
			}
		}
		
		return fi;
	}


	/***
	 * 단일 이미지 업로드(썸네일 없이)
	 * @param mfile
	 * @param thumbWidth
	 * @param thumbHeight
	 * @param subPath
	 * @param newFileName
	 * @param targetExt
	 * @return
	 */
	public FileInfo uploadImageWithoutThumb(MultipartFile mfile, int thumbWidth, int thumbHeight, String subPath, String newFileName, String targetExt) {
		FileInfo fi = null;
		
		if(mfile!=null && mfile.getOriginalFilename().length() > 0) {
	        String ext = null;
			newFileName = newFileName == null ? Long.toHexString(System.currentTimeMillis()) : newFileName;
			
			File dir = new File(basePath+subPath);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			File file = null;
			InputStream in = null;
			OutputStream out = null;
			ByteArrayOutputStream baos = null;
			try {
				fi = new FileInfo();
				fi.setLogicalFileName(mfile.getOriginalFilename());
				fi.setFilePath(subPath);
				if(mfile.getOriginalFilename().indexOf(".")>=0) {
					ext = targetExt == null ? mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf(".")+1) : targetExt ;
				}
				fi.setFileExtension(ext);
				fi.setFileLength(mfile.getSize());
				fi.setPhysicalFileName(newFileName+"."+ext);
				
				file =  new File(basePath+subPath,fi.getPhysicalFileName());
				
				//BufferedImage bi = ImageIO.read(mfile.getInputStream());
				BufferedImage bi;
				try {
					bi= ImageIO.read(mfile.getInputStream());
				} catch(Exception e) { //CMYK 이미지인 경우
					bi = cmykConvert.convertToRGB(mfile);
					if(bi==null){
							return null;
					}
				}
				
				out = new FileOutputStream(file);
				ImageIO.write(bi, ext, out);
				out.close();
						
				// 섬네일 만들기.
				Image srcImg = null;
				
				in = new FileInputStream(file);
				
				if(ext.toLowerCase().equals("png") || ext.toLowerCase().equals("gif")){
					srcImg = ImageIO.read(in);
				}else{
					srcImg = new ImageIcon(file.getAbsolutePath()).getImage();
				}
				int srcWidth = srcImg.getWidth(null);
		        int srcHeight = srcImg.getHeight(null);
		        
		        int destWidth = srcWidth, destHeight = srcHeight;
			
				if ((srcWidth > thumbWidth) || (srcHeight > thumbHeight))
		        {
		            double deltaWidth = (srcWidth - thumbWidth) / thumbWidth;
		            double deltaHeight = (srcHeight - thumbHeight) / thumbHeight;
		            double scalFactor = 1.0;

		            if (deltaHeight > deltaWidth)
		            {
		                scalFactor = (double)thumbHeight / (double)srcHeight;
		            }
		            else
		            {
		                scalFactor = (double)thumbWidth / (double)srcWidth;
		            }

		            destWidth *= scalFactor;
		            destHeight *= scalFactor;
		        }

		        if (destWidth < 1) destWidth = 1;
		        if (destHeight < 1) destHeight = 1;
		       
		        Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
		        int pixels[] = new int[destWidth * destHeight]; 
		        PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth);
		        
		        try {
		            pg.grabPixels();
		        } catch (InterruptedException e) {
		            throw new IOException(e.getMessage());
		        } 
		        
		        BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
		        destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
		        baos = new ByteArrayOutputStream();
		        ImageIO.write(destImg, ext, baos);
		        in.close();
		        baos.close();
		        baos = null;
		        out.close();
		        out = null;
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				if(baos != null) {
					try {
						baos.close();
					}
					catch(Exception e){
					}
					baos = null;
				}
				
				if(out != null) {
					try {
						out.close();
					}
					catch(Exception e){
					}
					out = null;
				}
				if(in != null) {
					try {
						in.close();
					}
					catch(Exception e) {
					}
					in = null;
				}
			}
		}
		
		return fi;
	}

	public FileInfo uploadImageToNFS(MultipartFile mfile, int thumbWidth, int thumbHeight, String smbPath,
                                     String subPath, String newFileName, String targetExt, boolean isAlpha) {

		FileInfo fi = null;
		String ext = null;
		// smb
		SmbFile sf = null;
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", smbUser, smbPassword);
		SmbFileOutputStream sfos = null;
		InputStream is = null;
		Image srcImg = null;
		String imgOriginalName = null;
		newFileName = newFileName == null ? Long.toHexString(System.currentTimeMillis()) : newFileName;
		String imgSuffix = null;

		int len = 0;
		byte[] b = new byte[204800];
		if (mfile != null && mfile.getOriginalFilename().length() > 0) {

			fi = new FileInfo();
			fi.setLogicalFileName(mfile.getOriginalFilename());
			fi.setFilePath(subPath);
			if (mfile.getOriginalFilename().indexOf(".") >= 0) {
				ext = targetExt == null
						? mfile.getOriginalFilename().substring(mfile.getOriginalFilename().lastIndexOf(".") + 1)
						: targetExt;
			}
			fi.setFileExtension(ext);
			fi.setFileLength(mfile.getSize());
			fi.setPhysicalFileName(newFileName + "." + ext);
			fi.setPhysicalThumbName("s_" + fi.getPhysicalFileName());

			imgOriginalName = mfile.getOriginalFilename();
			imgSuffix = imgOriginalName.indexOf(".") > 0 ? imgOriginalName.substring(imgOriginalName.lastIndexOf("."))
					: "";

			try {
				sf = new SmbFile(smbPath + subPath, auth);

				if (!sf.exists()) {
					sf.mkdirs();
				}
				sf = new SmbFile(smbPath + subPath + "/" + fi.getPhysicalFileName(), auth);
				BufferedImage bi;

				/*
				if (ext.replace("e", "").toUpperCase().equals("JPG")) {
					try {
						bi = autoRotateImage(mfile);
					} catch (Exception error) {//CMYK 처리
						bi = convertToRGB(mfile);
						if (bi == null) return null;
					}
				} else {
					Image img = ImageIO.read(mfile.getInputStream());
					bi = toBufferedImage(img, isAlpha);
				}*/
				try {
					Image img = ImageIO.read(mfile.getInputStream());
					bi = toBufferedImage(img, isAlpha);
				}catch (Exception error) {//CMYK 처리
					bi = cmykConvert.convertToRGB(mfile);
					if (bi == null) return null;
				}

				sfos = new SmbFileOutputStream(sf);
				ImageIO.write(bi, ext, sfos);
				sfos.close();

				if (thumbWidth > 0 && thumbHeight > 0) {
					if (imgSuffix.equals("png") || imgSuffix.equals("gif")) {
						srcImg = ImageIO.read(sf.getInputStream());
					} else {
						srcImg = new ImageIcon(ImageIO.read(sf.getInputStream())).getImage();
					}
	
					int srcWidth = srcImg.getWidth(null);
					int srcHeight = srcImg.getHeight(null);
	
					int destWidth = srcWidth, destHeight = srcHeight;
	
					if ((srcWidth > thumbWidth) || (srcHeight > thumbHeight)) {
						double deltaWidth = (srcWidth - thumbWidth) / thumbWidth;
						double deltaHeight = (srcHeight - thumbHeight) / thumbHeight;
						double scalFactor = 1.0;
	
						if (deltaHeight > deltaWidth) {
							scalFactor = (double) thumbHeight / (double) srcHeight;
						} else {
							scalFactor = (double) thumbWidth / (double) srcWidth;
						}
	
						destWidth *= scalFactor;
						destHeight *= scalFactor;
					}
	
					if (destWidth < 1)
						destWidth = 1;
					if (destHeight < 1)
						destHeight = 1;
	
					Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
					int pixels[] = new int[destWidth * destHeight];
					PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth);
	
					try {
						pg.grabPixels();
					} catch (InterruptedException e) {
						throw new IOException(e.getMessage());
					}
	
					BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
					destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
					ByteArrayOutputStream baos = new ByteArrayOutputStream();
					ImageIO.write(destImg, ext, baos);
					sf = new SmbFile(smbPath + subPath + "/" + "s_" + fi.getPhysicalFileName(), auth);
	
					sfos = new SmbFileOutputStream(sf);
					is = new ByteArrayInputStream(baos.toByteArray());
	
					while ((len = is.read(b, 0, b.length)) > 0) {
						sfos.write(b, 0, len);
					}
					
					sfos.close();
					is.close();
					baos.close();
				} 
				else {
					//썸네일이 없을 때 - 썸네일명에 원본파일명을 입력한다.
					fi.setPhysicalThumbName(fi.getPhysicalFileName());
				}
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return fi;
	}
	
	/**
	 * 다중 파일 업로드.
	 * @param mfiles
	 * @param subPath
	 * @return
	 */
	public FileInfo[] uploadFile(MultipartFile[] mfiles, String subPath) {
		FileInfo[] fis = null;
		
		if(mfiles != null) {
			fis = new FileInfo[mfiles.length];
			for(int i = 0; i < mfiles.length; i++) {
				fis[i]= uploadFile(mfiles[i], subPath, null);
			}
		}
		
		return fis;
	}
	
	/** 
	 * 다중 이미지 업로드.
	 * @param mfiles
	 * @param thumbWidth
	 * @param thumbHeight
	 * @param subPath
	 * @return
	 */
	public FileInfo[] uploadImage(MultipartFile[] mfiles, int thumbWidth, int thumbHeight, String smbPath, String subPath) {
		FileInfo[] fis = null;
		
		if(mfiles != null) {
			fis = new FileInfo[mfiles.length];
			for(int i = 0; i < mfiles.length; i++) {
				fis[i]= uploadImageToNFS(mfiles[i], thumbWidth, thumbHeight, smbPath, subPath, null, null, false);
			}
		}
		
		return fis;
	}
	

	/**
	 * 파일 삭제.
	 * @param rootPath
	 * @param subPath
	 * @param fileName
	 */
	public void deleteFile(String rootPath, String subPath, String fileName) {
		File f = new File(rootPath+subPath, fileName);
		if(f.exists()) {
			f.delete();
		}
	}
	
	/**
	 * 이미지 네트워크 상에서 복사 생성 - 임시파일에서 복사하는 경우 전용, 썸네일 생성 옵션
	 * 
	 * @throws MalformedURLException
	 * @throws SmbException
	 */
	public FileInfo copyTempFileNFS(String originPath, String smbPath, String subPath, String fileName,
                                    boolean bThumb, String thumbName, int shrinkWidth, int shrinkHeight,
                                    boolean bCrop, int x1, int y1, int x2, int y2) throws Exception {
		FileInfo fi = null;
		
		try {
			//임시파일 정보
			int startIndex = originPath.lastIndexOf("/");
			String tempFileName = originPath.substring(startIndex+1);
			String originFileName = tempFileName.substring(9); //임시파일 생성시 덧붙여진 ddHHmmss_ 부분 제거
			int extIndex = originFileName.lastIndexOf(".");
			String ext = originFileName.substring(extIndex+1);
			
			//새로생성할 파일 정보
			String destPath = smbPath + subPath;
			fileName = fileName + "." + ext;
			thumbName = thumbName + "." + ext;
			
			//파일 준비
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", smbUser, smbPassword);
			SmbFile originFile = new SmbFile(tempPath + originPath, auth);
			SmbFile destFile = new SmbFile(destPath + "/" + fileName, auth);
			SmbFile destDirPath = new SmbFile(destPath, auth);
			
			//경로 준비
			if (!destDirPath.exists()) {
				destDirPath.mkdirs();
			}
			
			//크롭한 후 저장해야 하는 경우
			if (bCrop) {
				BufferedImage bi;

				//이미지 크롭
				int w = (x2-x1);
				int h = (y2-y1);
				InputStream is = originFile.getInputStream();
				Image cropImg = ImageIO.read(is);
				bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
				bi.getGraphics().drawImage(cropImg, 0, 0, w, h, x1, y1, x2, y2, null);

				SmbFileOutputStream sfos = new SmbFileOutputStream(destFile);
				ImageIO.write(bi, ext, sfos);
				
				is.close();
				sfos.close();
			}
			else { //이미지 복사								
				originFile.copyTo(destFile);
			}
			
			//썸네일이 필요한 경우
			if (bThumb) {
				copyShrinkFileNFS((destPath + "/" + fileName), (destPath + "/" + thumbName), ext, shrinkWidth, shrinkHeight);
			}
			
			//파일 정보 수집
			fi = new FileInfo();
			//fi.setLogicalFileName(originFileName);
			fi.setFilePath(subPath);
			fi.setFileExtension(ext);
			fi.setFileLength(destFile.length());
			fi.setPhysicalFileName(fileName);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return fi;
	}
	
	/**
	 * 이미지 파일 네트워크 상에서 복사 - 사이즈 변경하여 생성
	 * 
	 * @throws MalformedURLException
	 * @throws SmbException
	 */
	public void copyShrinkFileNFS(String orginPath, String destPath, String ext, int shrinkWidth, int shrinkHeight) throws Exception {
		SmbFileOutputStream sfos = null;
		InputStream is = null;
		ByteArrayOutputStream baos = null;
		
		try {
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", smbUser, smbPassword);
			SmbFile originFile = new SmbFile(orginPath, auth);
			SmbFile destFile = new SmbFile(destPath, auth);
			SmbFile destDirPath = new SmbFile(destPath.substring(0, destPath.lastIndexOf("/")), auth);
			
			if (!destDirPath.exists()) {
				destDirPath.mkdirs();
			}
			
			Image srcImg = ImageIO.read(originFile.getInputStream());
			int len = 0;
			byte[] b = new byte[204800];
			
			int srcWidth = srcImg.getWidth(null);
			int srcHeight = srcImg.getHeight(null);
	
			int destWidth = srcWidth, destHeight = srcHeight;
	
			if ((srcWidth > shrinkWidth) || (srcHeight > shrinkHeight)) {
				double deltaWidth = (srcWidth - shrinkWidth) / shrinkWidth;
				double deltaHeight = (srcHeight - shrinkHeight) / shrinkHeight;
				double scalFactor = 1.0;
	
				if (deltaHeight > deltaWidth) {
					scalFactor = (double) shrinkHeight / (double) srcHeight;
				} else {
					scalFactor = (double) shrinkWidth / (double) srcWidth;
				}
	
				destWidth *= scalFactor;
				destHeight *= scalFactor;
			}
	
			if (destWidth < 1)
				destWidth = 1;
			if (destHeight < 1)
				destHeight = 1;
			
			Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
			int pixels[] = new int[destWidth * destHeight];
			PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth);
	
			try {
				pg.grabPixels();
			} catch (InterruptedException e) {
				throw new IOException(e.getMessage());
			}
	
			BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
			destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
			if (baos == null) baos = new ByteArrayOutputStream();
			ImageIO.write(destImg, ext, baos);
	
			sfos = new SmbFileOutputStream(destFile);
			is = new ByteArrayInputStream(baos.toByteArray());
	
			while ((len = is.read(b, 0, b.length)) > 0) {
				sfos.write(b, 0, len);
			}
			
			destImg.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			baos.close();
			is.close();
			sfos.close();
		}
	
	}
	
	/*
	private BufferedImage autoRotateImage(MultipartFile mf) throws Exception {
		BufferedImage originalImage = ImageIO.read(mf.getInputStream());
		Metadata metadata = ImageMetadataReader.readMetadata(mf.getInputStream());
	    Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
	    JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

        int orientation = 1;
        try {
            orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        int width = jpegDirectory.getImageWidth();
        int height = jpegDirectory.getImageHeight();

        AffineTransform affineTransform = new AffineTransform();

        switch (orientation) {
        case 1:
            break;
        case 2: // Flip X
            affineTransform.scale(-1.0, 1.0);
            affineTransform.translate(-width, 0);
            break;
        case 3: // PI rotation
            affineTransform.translate(width, height);
            affineTransform.rotate(Math.PI);
            break;
        case 4: // Flip Y
            affineTransform.scale(1.0, -1.0);
            affineTransform.translate(0, -height);
            break;
        case 5: // - PI/2 and Flip X
            affineTransform.rotate(-Math.PI / 2);
            affineTransform.scale(-1.0, 1.0);
            break;
        case 6: // -PI/2 and -width
            affineTransform.translate(height, 0);
            affineTransform.rotate(Math.PI / 2);
            width = originalImage.getHeight();
            height = originalImage.getWidth();
            break;
        case 7: // PI/2 and Flip
            affineTransform.scale(-1.0, 1.0);
            affineTransform.translate(-height, 0);
            affineTransform.translate(0, width);
            affineTransform.rotate(3 * Math.PI / 2);
            break;
        case 8: // PI / 2
            affineTransform.translate(0, width);
            affineTransform.rotate(3 * Math.PI / 2);
            width = originalImage.getHeight();
            height = originalImage.getWidth();
            break;
        default:
            break;
        }       

        AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);  
        BufferedImage destinationImage = new BufferedImage(width, height, originalImage.getType());
		
		return affineTransformOp.filter(originalImage, destinationImage);
	}

	
	private BufferedImage toBufferedImage(Image src) {
        int w = src.getWidth(null);
        int h = src.getHeight(null);
        int type = BufferedImage.TYPE_INT_ARGB;  // other options
        BufferedImage dest = new BufferedImage(w, h, type);
        Graphics2D g2 = dest.createGraphics();
        g2.drawImage(src, 0, 0, null);
        g2.dispose();
        return dest;
    }*/
	
	private BufferedImage toBufferedImage(Image src, boolean isAlpha) {
		int type = 0;
		int w = src.getWidth(null);
		int h = src.getHeight(null);
		if(isAlpha){
			type = BufferedImage.TYPE_INT_ARGB;
		} else {
			type = BufferedImage.TYPE_INT_BGR;
		}
		BufferedImage dest = new BufferedImage(w, h, type);
		Graphics2D g2 = dest.createGraphics();
		g2.drawImage(src, 0, 0, null);
		g2.dispose();
		return dest;
	}
	
	/* 공지사항 스마트 에디터 파일 업로드용 */
	public FileInfo uploadImageToNFS(HttpServletRequest request, String smbPath, String subPath, String newFileName, String targetExt) {
		

		FileInfo fi = null;
		String ext = null;
		//smb
		SmbFile sf = null;
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", smbUser, smbPassword);
		SmbFileOutputStream sfos = null;
		String imgOriginalName = null;
		newFileName = newFileName == null ? Long.toHexString(System.currentTimeMillis()) : newFileName;
		String imgSuffix = null;
		String fileName = request.getHeader("file-name");
		
		
		if(request != null && fileName.length() > 0){

	        fi = new FileInfo();
			fi.setLogicalFileName(fileName);
			fi.setFilePath(subPath);
			if(fileName.indexOf(".")>=0) {
				ext = targetExt == null ? fileName.substring(fileName.lastIndexOf(".")+1) : targetExt;
			}
			fi.setFileExtension(ext);
			fi.setFileLength(request.getContentLength());
			fi.setPhysicalFileName(newFileName+"."+ext);
			
			imgOriginalName = fileName;
			imgSuffix = imgOriginalName.indexOf(".") > 0 ? imgOriginalName.substring(imgOriginalName.lastIndexOf(".")) : "";
			
			try {
				sf = new SmbFile(smbPath+subPath, auth);
				if(!sf.exists()){
					sf.mkdirs();
				}
				sf = new SmbFile(smbPath+subPath+"/"+fi.getPhysicalFileName(), auth);
				ServletInputStream sIs = request.getInputStream();
			
				sfos = new SmbFileOutputStream(sf);
				int numRead;
				byte b[] = new byte[Integer.parseInt(request.getHeader("file-size"))];
				while((numRead = sIs.read(b,0,b.length)) != -1){
				 sfos.write(b,0,numRead);
				}
				sfos.flush();
				sfos.close();
				if(sIs != null) {
					sIs.close();
				}
				
		
				sfos = null;
				sIs =null;
				sf = null;
				System.gc();
			}
			catch(Exception e) {
				e.printStackTrace();
			}
		}
		return fi;
	}
	
	//임시파일 생성
	public int[] uploadTemporaryFile(MultipartFile mfile, String subPath, String fileName) throws IOException {
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", smbUser, smbPassword);
		
		SmbFile sf = null;
		SmbFileOutputStream sfos = null;
		BufferedImage bi = null;
		InputStream is = null;
		
		int len = 0;
		byte[] b = new byte[2048];
		int [] result = new int[2];
				
		try {
			sf = new SmbFile(tempPath + subPath, auth);
			
			if(!sf.exists()) {
				sf.mkdirs();
			}
			
			sf = new SmbFile(tempPath + subPath + fileName, auth);
			sfos = new SmbFileOutputStream(sf);
			
			//try-catch 중첩문 시작: CMYK 형태의 이미지를 구분하기 위하여
			try { //RGB
				is = mfile.getInputStream();
				
				while((len = is.read(b, 0, 2048)) > 0) {
					sfos.write(b, 0, len);
				}

				bi = ImageIO.read(sf.getInputStream());
				if(bi==null) throw new Exception();
				
				result[0] = bi.getWidth();
				result[1] = bi.getHeight();

			} catch (Exception e) { //CMYK
				int extIndex = fileName.lastIndexOf(".");
				String ext = fileName.substring(extIndex+1).toLowerCase();
				
				bi = cmykConvert.convertToRGB(mfile);
				if(bi==null) return null;
								
				result[0] =  bi.getWidth();
				result[1] = bi.getHeight();
				
				sfos = new SmbFileOutputStream(sf);	
				ImageIO.write(bi, ext, sfos);
				
			}//try-catch 중첩문 끝
		} catch (Exception e) {
			e.printStackTrace();
			result = null;
		} finally {
			is.close();
			sfos.close();
			bi.flush();
		}
		
		return result;
	}
	
	//임시폴더 비우기
	public void emptyTempFolder(String tempFolder) {
		NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", smbUser, smbPassword);
		SmbFile sf = null;

		try {
			sf = new SmbFile(tempPath + "/" + tempFolder + "/", auth);
			SmbFile[] sfs = sf.listFiles();

			if (sfs != null && sfs.length > 0) {
				for(SmbFile eachsf : sfs) {
					try{
						if (eachsf.exists()) {
							eachsf.delete();
						}
					} catch (Exception ee){
						//그냥 넘어감.
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}

	/***
	 * 업로드 압축 올리기
	 * @param mfile
	 * @param thumbWidth
	 * @param thumbHeight
	 * @param smbPath
	 * @param subPath
	 * @param targetExt
	 * @return
	 */
	public List<FileInfo> uploadZipImageFile(MultipartFile mfile, int thumbWidth, int thumbHeight, String smbPath, String subPath, String targetExt) {
		List<FileInfo> result = new ArrayList<FileInfo>();
		try {
			NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication("", smbUser, smbPassword);

			ZipArchiveInputStream zipIn = new ZipArchiveInputStream(mfile.getInputStream(), "euc-kr");
			ArchiveEntry entry =  null;

			while ((entry = zipIn.getNextEntry()) != null) {
				FileInfo fi = null;
				String entryFileName = entry.getName();
				String fileName = entryFileName.indexOf("/") != -1 ? entryFileName.substring(entryFileName.lastIndexOf("/") + 1) : entryFileName;
				if (!entry.isDirectory()) {
					SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmssSSS" );
					Calendar cal = Calendar.getInstance();
					String newFileName=  sf.format(cal.getTime());

					if (fileName != null && fileName.length() > 0) {
						int len = 0;

						String ext = null;
						newFileName = newFileName == null ? Long.toHexString(System.currentTimeMillis()) : newFileName;

						SmbFile dir = new SmbFile(smbPath + subPath, auth);
						if (!dir.exists()) {
							dir.mkdirs();
						}
						SmbFile file = null;
						InputStream in = null;
						OutputStream out = null;
						ByteArrayOutputStream baos = null;
						byte[] b = new byte[204800];
						try {
							fi = new FileInfo();
							fi.setLogicalFileName(fileName);
							fi.setFilePath(subPath);
							if (fileName.indexOf(".") >= 0) {
								ext = targetExt == null ? fileName.substring(fileName.lastIndexOf(".") + 1) : targetExt;
							}
							fi.setFileExtension(ext);
							fi.setFileLength(entry.getSize());
							fi.setPhysicalFileName(newFileName + "." + ext);
							fi.setPhysicalThumbName("s_" + fi.getPhysicalFileName());

							file = new SmbFile(smbPath + subPath + "/" + fi.getPhysicalFileName(), auth);

							String fileUpperExt = ext.trim().toUpperCase();
							if ("JPG".equals(fileUpperExt) || "PNG".equals(fileUpperExt) || "BMP".equals(fileUpperExt)) {
								///이미지 인 경우

								//BufferedImage bi = ImageIO.read(mfile.getInputStream());
								BufferedImage bi;
								try {
									bi = ImageIO.read(zipIn);
									if (bi == null) throw new Exception();
								} catch (Exception e) { //CMYK 이미지인 경우
									bi = cmykConvert.convertToRGB(mfile);
									if (bi == null) {
										return null;
									}
								}
								out = new SmbFileOutputStream(file);
								ImageIO.write(bi, ext, out);
								out.close();

								// 섬네일 만들기.
								Image srcImg = null;

								in = new SmbFileInputStream(file);

								if (ext.toLowerCase().equals("png") || ext.toLowerCase().equals("gif")) {
									srcImg = ImageIO.read(in);
								} else {
									srcImg = new ImageIcon(ImageIO.read(file.getInputStream())).getImage();
								}
								int srcWidth = srcImg.getWidth(null);
								int srcHeight = srcImg.getHeight(null);

								int destWidth = srcWidth, destHeight = srcHeight;

								if ((srcWidth > thumbWidth) || (srcHeight > thumbHeight)) {
									double deltaWidth = (srcWidth - thumbWidth) / thumbWidth;
									double deltaHeight = (srcHeight - thumbHeight) / thumbHeight;
									double scalFactor = 1.0;

									if (deltaHeight > deltaWidth) {
										scalFactor = (double) thumbHeight / (double) srcHeight;
									} else {
										scalFactor = (double) thumbWidth / (double) srcWidth;
									}

									destWidth *= scalFactor;
									destHeight *= scalFactor;
								}

								if (destWidth < 1) destWidth = 1;
								if (destHeight < 1) destHeight = 1;

								Image imgTarget = srcImg.getScaledInstance(destWidth, destHeight, Image.SCALE_SMOOTH);
								int pixels[] = new int[destWidth * destHeight];
								PixelGrabber pg = new PixelGrabber(imgTarget, 0, 0, destWidth, destHeight, pixels, 0, destWidth);

								try {
									pg.grabPixels();
								} catch (InterruptedException e) {
									throw new IOException(e.getMessage());
								}

								BufferedImage destImg = new BufferedImage(destWidth, destHeight, BufferedImage.TYPE_INT_RGB);
								destImg.setRGB(0, 0, destWidth, destHeight, pixels, 0, destWidth);
								baos = new ByteArrayOutputStream();
								ImageIO.write(destImg, ext, baos);
								in.close();
								in = new ByteArrayInputStream(baos.toByteArray());
								out = new SmbFileOutputStream(new SmbFile(file.getParent(), "s_" + file.getName(),auth));
								while ((len = in.read(b, 0, b.length)) > 0) {
									out.write(b, 0, len);
								}
								baos.close();
								baos = null;
								out.close();
								out = null;
								in.close();
								in = null;
							} else {
								continue;
							}
						} catch (Exception e) {
							e.printStackTrace();
						} finally {
							if (baos != null) {
								try {
									baos.close();
								} catch (Exception e) {
								}
								baos = null;
							}

							if (out != null) {
								try {
									out.close();
								} catch (Exception e) {
								}
								out = null;
							}
							if (in != null) {
								try {
									in.close();
								} catch (Exception e) {
								}
								in = null;
							}
						}
					}
					result.add(fi);
				}
			}
			zipIn.close();
		}catch(Exception e){
			e.printStackTrace();
		}

		return result;
	}

}
