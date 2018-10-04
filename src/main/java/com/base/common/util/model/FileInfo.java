package com.base.common.util.model;

public class FileInfo {
	private String logicalFileName; // 원본 파일 명
	private String physicalFileName; // 실제 저장 파일 명
	private String fileExtension; // 파일 확장자
	private String filePath; // root 하위 저장 경로
	private long fileLength; // 파일 크기
	private String physicalThumbName; // 썸네일 파일 명
	
	/**
	 * @return the physicalThumbName
	 */
	public String getPhysicalThumbName() {
		return physicalThumbName;
	}
	/**
	 * @param physicalThumbName the physicalThumbName to set
	 */
	public void setPhysicalThumbName(String physicalThumbName) {
		this.physicalThumbName = physicalThumbName;
	}
	/**
	 * @return the logicalFileName
	 */
	public String getLogicalFileName() {
		return logicalFileName;
	}
	/**
	 * @param logicalFileName the logicalFileName to set
	 */
	public void setLogicalFileName(String logicalFileName) {
		this.logicalFileName = logicalFileName;
	}
	/**
	 * @return the physicalFileName
	 */
	public String getPhysicalFileName() {
		return physicalFileName;
	}
	/**
	 * @param physicalFileName the physicalFileName to set
	 */
	public void setPhysicalFileName(String physicalFileName) {
		this.physicalFileName = physicalFileName;
	}
	/**
	 * @return the fileExtension
	 */
	public String getFileExtension() {
		return fileExtension;
	}
	/**
	 * @param fileExtension the fileExtension to set
	 */
	public void setFileExtension(String fileExtension) {
		this.fileExtension = fileExtension;
	}
	/**
	 * @return the filePath
	 */
	public String getFilePath() {
		return filePath;
	}
	/**
	 * @param filePath the filePath to set
	 */
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	/**
	 * @return the fileLength
	 */
	public long getFileLength() {
		return fileLength;
	}
	/**
	 * @param fileLength the fileLength to set
	 */
	public void setFileLength(long fileLength) {
		this.fileLength = fileLength;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("FileInfo [logicalFileName=");
		builder.append(logicalFileName);
		builder.append(", physicalFileName=");
		builder.append(physicalFileName);
		builder.append(", fileExtension=");
		builder.append(fileExtension);
		builder.append(", filePath=");
		builder.append(filePath);
		builder.append(", fileLength=");
		builder.append(fileLength);
		builder.append(", physicalThumbName=");
		builder.append(physicalThumbName);
		builder.append("]");
		return builder.toString();
	}
}
