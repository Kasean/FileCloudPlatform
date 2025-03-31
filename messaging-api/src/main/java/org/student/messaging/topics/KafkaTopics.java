package org.student.messaging.topics;

public class KafkaTopics {

    public static class CrudMeta {
        public static final String SAVE_META_INFO_TOPIC = "save-info-topic";
        public static final String GET_EXT_META_INFO_TOPIC = "get-ext-info-topic";
        public static final String GET_INT_META_INFO_TOPIC = "get-int-info-topic";
        public static final String DEL_META_INFO = "del-info-topic";
    }

    public static class ResponseMeta {
        public static final String SAVE_RESPONSE_TOPIC = "save-response-topic";
        public static final String DEL_RESPONSE_TOPIC = "del-response-topic";
        public static final String GET_EXT_INFO_RESPONSE_TOPIC = "get-ext-response-topic";
        public static final String GET_INT_INFO_RESPONSE_TOPIC = "get-int-response-topic";
        public static final String ERROR_RESPONSE_TOPIC = "error-response-topic";
    }


}

