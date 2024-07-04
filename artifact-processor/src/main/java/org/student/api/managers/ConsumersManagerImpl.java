package org.student.api.managers;

import org.student.api.consumers.MessageConsumer;

import java.util.List;

public class ConsumersManagerImpl implements ConsumersManager {

    private final List<MessageConsumer> consumers;

    public ConsumersManagerImpl(List<MessageConsumer> consumers) {
        this.consumers = consumers;
    }


    @Override
    public void startListenMessages() {

    }
}
