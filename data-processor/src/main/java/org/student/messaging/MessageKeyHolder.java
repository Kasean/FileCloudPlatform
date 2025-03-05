package org.student.messaging;

public class MessageKeyHolder {
    private static final ThreadLocal<String> messageKeyHolder = new ThreadLocal<>();

    public static void set(String key) {
        messageKeyHolder.set(key);
    }

    public static String get() {
        return messageKeyHolder.get();
    }

    public static void clear() {
        messageKeyHolder.remove();
    }
}
