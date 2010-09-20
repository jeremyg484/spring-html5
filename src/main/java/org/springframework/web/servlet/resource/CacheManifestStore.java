package org.springframework.web.servlet.resource;

public interface CacheManifestStore {

	public CacheManifest get(String key);
	
	public void put(String key, CacheManifest manifest);
}
