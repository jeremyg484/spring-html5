package com.springsource.html5.visualization;

import static org.junit.Assert.*;

import org.junit.Test;
import org.springframework.batch.item.file.transform.DefaultFieldSet;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import com.springsource.html5.visualization.CommitRecord;
import com.springsource.html5.visualization.CommitRecordFieldSetMapper;


public class CommitRecordFieldSetMapperTest {

	private CommitRecordFieldSetMapper mapper = new CommitRecordFieldSetMapper();
	
	@Test
	public void ModuleRecord() throws BindException {
		String[] tokens = new String[] {"1215783290", "bhale", "A", "/trunk/org.springframework.expression/src/main/java/org/springframework/expression/ParserException.java"};
		FieldSet fieldSet = new DefaultFieldSet(tokens);
		CommitRecord result = mapper.mapFieldSet(fieldSet);
		assertNotNull(result);
		assertEquals(1215783290L, result.getTimestamp());
		assertEquals("bhale", result.getUsername());
		assertEquals("A", result.getCommitType());
		assertEquals("expression", result.getModuleName());
	}
	
	@Test
	public void DotSeperatedModuleRecord() throws BindException {
		String[] tokens = new String[] {"1215783290", "bhale", "A", "/trunk/org.springframework.web.servlet/src/main/java/org/springframework/expression/ParserException.java"};
		FieldSet fieldSet = new DefaultFieldSet(tokens);
		CommitRecord result = mapper.mapFieldSet(fieldSet);
		assertNotNull(result);
		assertEquals(1215783290L, result.getTimestamp());
		assertEquals("bhale", result.getUsername());
		assertEquals("A", result.getCommitType());
		assertEquals("web.servlet", result.getModuleName());
	}
	
	@Test 
	public void InfrastructureRecord() throws BindException {
		String[] tokens = new String[] {"1215783290", "bhale", "A", "trunk/build-spring-framework/publish-top-level.xml"};
		FieldSet fieldSet = new DefaultFieldSet(tokens);
		CommitRecord result = mapper.mapFieldSet(fieldSet);
		assertNotNull(result);
		assertEquals(1215783290L, result.getTimestamp());
		assertEquals("bhale", result.getUsername());
		assertEquals("A", result.getCommitType());
		assertEquals("infrastructure", result.getModuleName());
	}
}
