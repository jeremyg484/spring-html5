package com.springsource.petclinic.web;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/commitRecordProcessor")
public class CommitRecordProcessorController {

	@Autowired
	private JobLauncher jobLauncher;
	
	@Autowired
	private Job job;
	
	@RequestMapping(params="command=start")
	public String startJob() throws Exception {
		jobLauncher.run(job, new JobParameters());
		return "redirect:/comettest.html";
	}
}
