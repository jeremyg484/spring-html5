package org.springframework.web.servlet.resource;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCacheManifestStore implements CacheManifestStore {

	private final Map<String, CacheManifest> manifests = new ConcurrentHashMap<String, CacheManifest>();
	
	public CacheManifest get(String key) {
		return manifests.get(key);
	}

	public void put(String key, CacheManifest manifest) {
		manifests.put(key, manifest);		
	}
}
