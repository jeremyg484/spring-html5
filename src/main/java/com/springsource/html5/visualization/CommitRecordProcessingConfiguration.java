package com.springsource.html5.visualization;

import javax.annotation.Resource;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;
import org.springframework.integration.MessageChannel;

//@Configuration
public class CommitRecordProcessingConfiguration implements ResourceLoaderAware{

	private ResourceLoader resourceLoader;
	
	private MessageChannel commitRecordChannel;
	
	@Bean
	public ItemReader<CommitRecord> commitRecordReader() {
		FlatFileItemReader<CommitRecord> reader = new FlatFileItemReader<CommitRecord>();
		reader.setResource(resourceLoader.getResource("classpath:spring-gource.log"));
		DefaultLineMapper<CommitRecord> lineMapper = new DefaultLineMapper<CommitRecord>();
		lineMapper.setLineTokenizer(new DelimitedLineTokenizer('|'));
		lineMapper.setFieldSetMapper(new CommitRecordFieldSetMapper());
		reader.setLineMapper(lineMapper);
		return reader;
	}
	
	@Bean
	public ItemWriter<CommitRecord> commitRecordWriter() {
		MessageSendingItemWriter<CommitRecord> writer = new MessageSendingItemWriter<CommitRecord>(commitRecordChannel);
		return writer;
	}

	@Resource
	public void setCommitRecordChannel(MessageChannel commitRecordChannel) {
		this.commitRecordChannel = commitRecordChannel;
	}

	public void setResourceLoader(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}
}
