package com.springsource.html5.visualization;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

public class CommitRecordItemWriter implements ItemWriter<CommitRecord> {

	private final MessageChannel outputChannel;
	
	private CommitRecord prevRecord = null;
	
	public CommitRecordItemWriter(MessageChannel outputChannel) {
		this.outputChannel = outputChannel;
	}

	@Override
	public void write(List<? extends CommitRecord> items) throws Exception {
		for (CommitRecord item : items) {
			if (prevRecord == null || !item.getUsername().equals(prevRecord.getUsername()) || item.getTimestamp() != prevRecord.getTimestamp()) { //!item.getModuleName().equals(prevRecord.getModuleName()) || item.getTimestamp() != prevRecord.getTimestamp()) {
				Message<?> message = MessageBuilder.withPayload(item).build();
				outputChannel.send(message);
				prevRecord = item;
			}
			//No-op otherwise
		}
	}
}
