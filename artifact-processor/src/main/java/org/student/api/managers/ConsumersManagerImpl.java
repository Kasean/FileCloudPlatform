package org.student.api.managers;

import org.student.api.consumers.MessageConsumer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConsumersManagerImpl implements ConsumersManager {

    private final List<? extends MessageConsumer> consumers;
    private final ExecutorService executor;


    public ConsumersManagerImpl(List<? extends MessageConsumer> consumers) {
        this.consumers = consumers;

        executor = Executors.newFixedThreadPool(consumers.size());
    }


    @Override
    public void startListenMessages() {

        System.out.println("Start consumers...");

        consumers.forEach(consumer -> executor.submit(consumer::consume));

        System.out.println("Consumers started.");
    }

    @Override
    public void shutdown() {

        System.out.println("Shutting down consumers...");

        executor.shutdown();

        System.out.println("Shutdown consumers completed.");
    }
}
