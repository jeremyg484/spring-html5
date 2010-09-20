package com.springsource.html5.appcache.template;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.google.template.soy.data.SoyMapData;
import com.google.template.soy.tofu.SoyTofu;


public class SoyConfigurationTests {

	private SoyConfiguration soyConfig;
	
	@Before
	public void setUp() {
		soyConfig = new SoyConfiguration();
		soyConfig.setResourceLoader(new TestResourceResolver());
	}
	
	@Test
	public void testTemplateRender() throws IOException {
		SoyTofu tofu = soyConfig.soyTofu();
		System.out.println(tofu.render("examples.simple.helloWorld", (SoyMapData) null, null));
	}
	
	@Test
	public void testJsTemplateGeneration() throws IOException {
		System.out.println(soyConfig.soyJavaScript());
	}
	
	private static final class TestResourceResolver extends PathMatchingResourcePatternResolver {
		@Override
		public Resource[] getResources(String locationPattern)
				throws IOException {
			return new Resource[] {new ClassPathResource("test.soy", getClass())};
		}
	}
}
