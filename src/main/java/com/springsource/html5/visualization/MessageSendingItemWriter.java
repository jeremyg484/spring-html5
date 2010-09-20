package com.springsource.html5.visualization;

import java.util.List;

import org.springframework.batch.item.ItemWriter;
import org.springframework.integration.Message;
import org.springframework.integration.MessageChannel;
import org.springframework.integration.support.MessageBuilder;

public class MessageSendingItemWriter<T> implements ItemWriter<T> {

	private final MessageChannel outputChannel;
	
	public MessageSendingItemWriter(MessageChannel outputChannel) {
		this.outputChannel = outputChannel;
	}
	
	public void write(List<? extends T> items) throws Exception {
		for (T item : items) {
			Message<?> message = MessageBuilder.withPayload(item).build();
			outputChannel.send(message);
		}
	}
}
