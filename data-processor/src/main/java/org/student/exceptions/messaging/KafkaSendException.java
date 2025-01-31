package org.student.exceptions.messaging;

public class KafkaSendException extends Exception{

    public KafkaSendException(String message) {
        super(message);
    }
}
