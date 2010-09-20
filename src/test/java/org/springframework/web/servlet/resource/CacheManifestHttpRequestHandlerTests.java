package org.springframework.web.servlet.resource;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.ServletContext;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.Resource;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.ServletContextResource;
import org.springframework.web.servlet.HandlerMapping;


public class CacheManifestHttpRequestHandlerTests {

	private static final Log log = LogFactory.getLog(CacheManifestHttpRequestHandlerTests.class);
	
	private CacheManifestHttpRequestHandler handler;
	
	@Before
	public void setUp() {
		ServletContext context = new TestServletContext();
		Resource[] resources = new Resource[]{ new ServletContextResource(context, "/styles/foo.css"), new ServletContextResource(context, "/js/bar.js") };
		handler = new CacheManifestHttpRequestHandler();
		handler.setCachedResources(resources);
		handler.setCacheSeconds(3600);
		handler.setServletContext(context);
	}
	
	@Test
	public void getManifest() throws Exception {
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE, "/test.manifest");
		request.setMethod("GET");
		MockHttpServletResponse response = new MockHttpServletResponse();
		handler.handleRequest(request, response);
		String content = response.getContentAsString();
		assertEquals("text/cache-manifest;charset=UTF-8", response.getContentType());
		assertTrue(content.startsWith("CACHE MANIFEST\n"));
		assertTrue(content.contains("CACHE:\n"));
		assertTrue(content.contains("styles/foo.css\n"));
		assertTrue(content.contains("js/bar.js\n"));
		assertTrue(content.endsWith("#SHA-1: bfe17515e51f9acb56426ca17e2a7476ab755180\n"));
		log.debug("/test.manifest:\n"+content);
		log.debug(content.length());
	}
	
	private static class TestServletContext extends MockServletContext {

		@Override
		public String getMimeType(String filePath) {
			if (filePath.endsWith(".css")) {
				return "text/css";
			}
			else if (filePath.endsWith(".js")) {
				return "text/javascript";
			}
			else {
				return super.getMimeType(filePath);
			}
		}
		
		@Override
		public InputStream getResourceAsStream(String path) {
			return new ByteArrayInputStream(path.getBytes());
		}
	}
}
