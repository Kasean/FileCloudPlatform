package org.student;

import org.student.messaging.MessageConsumer;

public class Application {
	public static void main(String[] args) {
		MessageConsumer consumer = new MessageConsumer("localhost:9092", "core-file-garbage-group");
		consumer.consume("archiver-topic");
	}
}