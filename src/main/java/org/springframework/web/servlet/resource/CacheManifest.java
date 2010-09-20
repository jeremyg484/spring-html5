package org.springframework.web.servlet.resource;

import java.io.Serializable;

public class CacheManifest implements Serializable {

	private static final long serialVersionUID = 1L;

	private final byte[] content;
	
	private final long timestamp;
	
	private final String hash;

	public CacheManifest(byte[] content, long timestamp, String hash) {
		this.content = content;
		this.timestamp = timestamp;
		this.hash = hash;
	}

	public byte[] getContent() {
		return content;
	}

	public long getTimestamp() {
		return timestamp;
	}
	
	public String getHash() {
		return hash;
	}
}
