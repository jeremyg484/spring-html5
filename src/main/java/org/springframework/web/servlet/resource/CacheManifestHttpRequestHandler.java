package org.springframework.web.servlet.resource;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.HttpRequestHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.support.WebContentGenerator;

public class CacheManifestHttpRequestHandler extends WebContentGenerator implements HttpRequestHandler {

	private static final String PREAMBLE = "CACHE MANIFEST";
	
	private static final String CACHE_SECTION = "CACHE:";
	
	private static final String NETWORK_SECTION = "NETWORK:";
	
	private static final String FALLBACK_SECTION = "FALLBACK:";
	
	private static final MediaType CONTENT_TYPE = new MediaType("text","cache-manifest", Charset.forName("UTF-8"));
	
	//TODO - This is a crude way of handling Chrome's duplicate request - 
	//could be improved later with a configurable refresh period, but since this is 
	//intended to be a development-time setting, this might be good enough
	//private AtomicBoolean expired = new AtomicBoolean(true);
	
	private boolean alwaysRegenerate = false;
	
	private List<Resource> cachedResources;
	
	private CacheManifestStore store = new InMemoryCacheManifestStore();
	
	public CacheManifestHttpRequestHandler() {
		super(METHOD_GET, METHOD_HEAD);
		//if (alwaysRegenerate) {
		//	setCacheSeconds(5);
		//}
	}
	
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		checkAndPrepare(request, response, true);
		
		CacheManifest manifest = getCacheManifest(request);
		
		setHeaders(response, manifest);
		if (new ServletWebRequest(request, response).checkNotModified(manifest.getTimestamp())) {
			logger.debug("Resource not modified - returning 304");
			return;
		}
		
		writeContent(response, manifest);
	}
	
	public void setCachedResources(Resource[] cachedResources) {
		Assert.notEmpty(cachedResources, "Cached resource list must not be empty");
		this.cachedResources = Arrays.asList(cachedResources);
	}
	
	protected CacheManifest getCacheManifest(HttpServletRequest request) throws IOException {
		//TODO - Implement handling for NETWORK and FALLBACK sections of manifest
		String key = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		CacheManifest manifest = store.get(key);
		if (manifest != null && alwaysRegenerate && (manifest.getTimestamp() + 5000L) >= new Date().getTime()) {
			return manifest;
		}
		String hash = getHash();
		if (manifest != null && manifest.getHash().equals(hash)) {
			return manifest;
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintWriter writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(out, Charset.forName("UTF-8"))));
		writer.println(PREAMBLE);
		writer.println(CACHE_SECTION);
		for (Resource resource : cachedResources) {
			Assert.isInstanceOf(ServletContextResource.class, resource, "Cache Manifest resources must be relative to the ServletContext");
			String path = ((ServletContextResource)resource).getPathWithinContext();
			writer.println(path.startsWith("/") ? path.substring(1) : path);
		}
		writer.println(NETWORK_SECTION);
		writer.println("*");
		writer.println("#SHA-1: "+hash);
		writer.flush();
		writer.close();
		
		manifest = new CacheManifest(out.toByteArray(), new Date().getTime(), hash);
		store.put(key, manifest);
		return manifest;
	}
	
	protected String getHash() throws IOException {
		//TODO - add further config options such as a minimum time period to wait before re-calculating the hash
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException ex) {
			throw new IOException("Unable to generate unique key for cache manifest.", ex);
		}
		if (alwaysRegenerate) {
			md.update(String.valueOf(new Date().getTime()).getBytes());
		} else {
			for (Resource resource : cachedResources) {
				md.update(FileCopyUtils.copyToByteArray(resource.getInputStream()));
			}
		}
		return new String(Hex.encode(md.digest()));
	}

	protected void setHeaders(HttpServletResponse response, CacheManifest manifest) throws IOException {
		long length = manifest.getContent().length;
		if (length > Integer.MAX_VALUE) {
			throw new IOException("Cache manifest content too long (beyond Integer.MAX_VALUE): " + manifest);
		}
		response.setContentLength((int) length);
		response.setContentType(CONTENT_TYPE.toString());
	}
	
	protected void writeContent(HttpServletResponse response, CacheManifest manifest) throws IOException {
		FileCopyUtils.copy(manifest.getContent(), response.getOutputStream());
	}
	
	private static final class Hex {
		private static final char[] HEX = {
	        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
	    };

	    public static char[] encode(byte[] bytes) {
	        final int nBytes = bytes.length;
	        char[] result = new char[2*nBytes];

	        int j = 0;
	        for (int i=0; i < nBytes; i++) {
	            // Char for top 4 bits
	            result[j++] = HEX[(0xF0 & bytes[i]) >>> 4 ];
	            // Bottom 4
	            result[j++] = HEX[(0x0F & bytes[i])];
	        }
	        return result;
	    }
	}

	public void setAlwaysRegenerate(boolean alwaysRegenerate) {
		this.alwaysRegenerate = alwaysRegenerate;
	}
}
