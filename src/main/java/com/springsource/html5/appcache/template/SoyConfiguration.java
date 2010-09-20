package com.springsource.html5.appcache.template;

import static com.google.template.soy.SoyFileSet.Builder;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.util.Assert;
import org.springframework.util.FileCopyUtils;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.jssrc.SoyJsSrcOptions;
import com.google.template.soy.tofu.SoyTofu;

@Configuration
public class SoyConfiguration implements ResourceLoaderAware {

	private ResourcePatternResolver resourceResolver;
	
	@Bean
	@Scope(value="prototype")
	public SoyFileSet soyFileSet() throws IOException {
		List<Resource> resources = Arrays.asList(resourceResolver.getResources("WEB-INF/soy/*.soy"));
		Builder builder = new Builder();
		for (Resource resource : resources) {
			builder.add(resource.getURL());
		}
		return builder.build();
	}
	
	@Bean
	public SoyTofu soyTofu() throws IOException {
		return soyFileSet().compileToJavaObj();
	}
	
	/*@Bean
	public List<String> soyJavaScript() throws IOException {
		SoyJsSrcOptions options = new SoyJsSrcOptions();
		List<String> jsSrc = soyFileSet().compileToJsSrc(options, null);
		Resource targetDir = resourceResolver.getResource("templates/");
		if (targetDir.exists()) {
			String combinedSource = "";
			for (String src : jsSrc) {
				combinedSource += src;
			}
			File target = new File(targetDir.getFile(), "template.js");
			FileCopyUtils.copy(combinedSource.getBytes(), target);
		}
		return jsSrc;
	}*/
	
	@Override
	public void setResourceLoader(ResourceLoader resourceLoader) {
		Assert.isInstanceOf(ResourcePatternResolver.class, resourceLoader, "Expected an instance of ResourcePatternResolver");
		this.resourceResolver = (ResourcePatternResolver) resourceLoader;
	}
}
