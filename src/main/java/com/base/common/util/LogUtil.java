package com.base.common.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;

@Component
public class LogUtil {

	@Value("${base.log.root}")
	private String logRoot;
	
	/**
	 * 년월일로 폴더 생성을 위해 년월일 정보를 받아옴.
	 * @return
	 */
	private String tmpPath() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		
		return sdf.format(cal.getTime());
	}
	
	/**
	 * 로그 등록 일시를 내용에 넣기 위해 일시를 문자열로 생성.
	 * @return
	 */
	private String getTimestamp() {
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
		
		return sdf.format(cal.getTime());
	}
	
	/**
	 * 로그 저장 경로가 없을 경우 생성 시키고, 로그 파일 생성할 경로를 요청.
	 * @param no
	 * @return
	 */
	private String getDir(String domain) {
		File f = new File(logRoot+"/"+domain,tmpPath());
		
		if(!f.exists()) {
			f.mkdirs();
		}
		
		return f.getPath();
	}
	
	/**
	 * 일반 로그 저장.
	 * @param id
	 * @param msg
	 */
	public void info(String serverName, String msg) {
		String targetDir = getDir(serverName);
		File f = new File(targetDir, "info.log");
		
		BufferedWriter out = null;
		
		try {
			out = new BufferedWriter(new FileWriter(f, true));
			out.write(getTimestamp()+" - "+msg);
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
	 * 디버깅을 위한 로그 저장.
	 */
	public void service(String serverName, String msg) {
		String targetDir = getDir(serverName);
		File f = new File(targetDir, "service.log");
		
		BufferedWriter out = null;
		
		try {
			out = new BufferedWriter(new FileWriter(f, true));
			out.write(getTimestamp()+" - "+msg);
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
	 * 디버깅을 위한 로그 저장.
	 */
	public void controller(String serverName, String msg) {
		String targetDir = getDir(serverName);
		File f = new File(targetDir, "controller.log");
		
		BufferedWriter out = null;
		
		try {
			out = new BufferedWriter(new FileWriter(f, true));
			out.write(getTimestamp()+" - "+msg);
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
	 * 디버깅을 위한 로그 저장.
	 */
	public void dao(String serverName, String msg) {
		String targetDir = getDir(serverName);
		File f = new File(targetDir, "dao.log");
		
		BufferedWriter out = null;
		
		try {
			out = new BufferedWriter(new FileWriter(f, true));
			out.write(getTimestamp()+" - "+msg);
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
	 * @param id
	 * @param e
	 */
	public void error(String serverName, Exception e) {
		error(serverName, null, e);
	}

	/**
	 * 에러 로그 저장.
	 * @param id
	 * @param msg
	 */
	public void error(String serverName, String msg) {
		error(serverName, msg, null);
	}
	
	/**
	 * 에러 로그 저장.
	 * @param id
	 * @param msg
	 * @param e
	 */
	public void error(String serverName, String msg, Exception e) {
		String targetDir = getDir(serverName);
		File f = new File(targetDir, "error.log");
		BufferedWriter out = null;
		 
		try {
			out = new BufferedWriter(new FileWriter(f, true));
			if(msg!=null) {
				out.write(getTimestamp()+" - "+msg);
			}
			else {
				out.write(getTimestamp()+" - "+e.getMessage());
				
			}
			out.newLine();
			out.flush();
		}
		catch(Exception e1) {
			e.printStackTrace();
		}
		finally {
			try {
				if(out!=null) out.close();
				out = null;
			} catch(Exception e1){}
		}
		
		if(e!=null) {
			writeException(f, e);
		}
 	}

	/**
	 * Exception을 에러 파일 로그로 저장.
	 * @param targetFile
	 * @param e
	 */
	private void writeException(File targetFile, Exception e) {
		PrintWriter out = null;
		
		try {
			out = new PrintWriter(new FileWriter(targetFile, true));
			e.printStackTrace(out);
		}
		catch(Exception e1) {
			e1.printStackTrace();
		}
		finally {
			try {
				if(out!=null) out.close();
				out = null;
			} catch(Exception e1){}
		}
	}
}
