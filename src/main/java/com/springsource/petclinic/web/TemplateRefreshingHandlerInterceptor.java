package com.springsource.petclinic.web;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.jssrc.SoyJsSrcOptions;

public class TemplateRefreshingHandlerInterceptor extends WebContentInterceptor{

	//private AtomicLong
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler)
			throws ServletException {
		SoyFileSet sfs = super.getApplicationContext().getBean(SoyFileSet.class);
		SoyJsSrcOptions options = new SoyJsSrcOptions();
		List<String> jsSrc = sfs.compileToJsSrc(options, null);
		Resource targetDir = getApplicationContext().getResource("templates/");
		if (targetDir.exists()) {
			String combinedSource = "";
			for (String src : jsSrc) {
				combinedSource += src;
			}
			try {
				File target = new File(targetDir.getFile(), "template.js");
				FileCopyUtils.copy(combinedSource.getBytes(), target);
			} catch (IOException e) {
				throw new ServletException("Failed to regenerate Soy JS Templates", e);
			}
		}
		return true;
	}

	

}
