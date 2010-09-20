package com.springsource.html5.visualization;

public class CommitRecord {

	private final long timestamp;
	
	private final String username;
	
	private final String commitType;
	
	private final String moduleName;

	public CommitRecord(long timestamp, String username, String commitType,
			String moduleName) {
		
		this.timestamp = timestamp;
		this.username = username;
		this.commitType = commitType;
		this.moduleName = moduleName;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public String getUsername() {
		return username;
	}

	public String getCommitType() {
		return commitType;
	}

	public String getModuleName() {
		return moduleName;
	}

	@Override
	public String toString() {
		return "CommitRecord [commitType=" + commitType + ", moduleName="
				+ moduleName + ", timestamp=" + timestamp + ", username="
				+ username + "]";
	}
}
