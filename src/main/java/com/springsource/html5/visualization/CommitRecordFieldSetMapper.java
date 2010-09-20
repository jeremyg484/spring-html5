package com.springsource.html5.visualization;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class CommitRecordFieldSetMapper implements FieldSetMapper<CommitRecord> {

	private final Pattern modulePattern = Pattern.compile("(/trunk/org\\.springframework\\.)([a-z\\.]*)/(.*)");
	
	public CommitRecord mapFieldSet(FieldSet fieldSet) throws BindException {
		long timestamp = fieldSet.readLong(0);
		String username = fieldSet.readString(1);
		String commitType = fieldSet.readString(2);
		String rawFileName = fieldSet.readString(3);
		
		String moduleName = "infrastructure";
		Matcher moduleMatcher = modulePattern.matcher(rawFileName);
		if (moduleMatcher.matches()) {
			moduleName = moduleMatcher.group(2);
		}
		
		return new CommitRecord(timestamp, username, commitType, moduleName);
	}

}
