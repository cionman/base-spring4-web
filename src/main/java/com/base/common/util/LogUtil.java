package com.base.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class LogUtil {

	@Value("${base.log.root}")
	private String logRoot;

	@Value("${project.name}")
	private String projectName;
	
	/**
	 * 년월일로 폴더 생성을 위해 년월일 정보를 받아옴.
	 * @return
	 */
	private String tmpPath() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		return sdf.format(cal.getTime());
	}

	private String getLogHeader(){
		return getLogHeader("", null);
	}


	/**
	 * 로그 등록 일시를 내용에 넣기 위해 일시를 문자열로 생성.
	 * @return
	 */
	private String getLogHeader(String msg, Exception e) {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
		StringBuilder header = new StringBuilder();

		header.append(sdf.format(cal.getTime()))
				.append('[').append(projectName).append(']');

		if(msg != null) {
			header.append('[').append(msg).append(']');
		}

		if(e != null){
			header.append('[').append(e.getMessage()).append(']');
		}

		return header.toString() ;
	}
	
	/**
	 * 로그 저장 경로가 없을 경우 생성 시키고, 로그 파일 생성할 경로를 요청.
	 * @return
	 */
	private String getDir() {
		File f = new File(logRoot+"/"+projectName,tmpPath());
		
		if(!f.exists()) {
			f.mkdirs();
		}
		
		return f.getPath();
	}
	
	/**
	 * 일반 로그 저장.
	 */
	public void info(String msg) {
		String targetDir = getDir();
		File f = new File(targetDir, "info.log");
		
		BufferedWriter out = null;
		
		try {
			out = new BufferedWriter(new FileWriter(f, true));
			out.write(getLogHeader()+" - "+msg);
			out.newLine();
			out.flush();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			try {
				if(out!=null) out.close();
				out = null;
			} catch(Exception e){}
		}
	}

	/**
	 * 에러 로그 저장.
	 * @param e
	 */
	public void error(Exception e) {
		error(null, e);
	}

	/**
	 * 에러 로그 저장.
	 * @param msg
	 * @param e
	 */
	public void error(String msg, Exception e) {
		String targetDir = getDir();
		File f = new File(targetDir, "error.log");

		//java try with resource 구문
		try(BufferedWriter out = new BufferedWriter(new FileWriter(f, true));
			StringWriter errors = new StringWriter();
			PrintWriter printWriter = new PrintWriter(errors);
			) {

			out.write(getLogHeader(msg, e));
			out.newLine();
			if(e != null){
				e.printStackTrace(printWriter);
			}
			out.write(errors.toString());
			out.flush();
		}
		catch(Exception e1) {
			e.printStackTrace();
		}
 	}

}
