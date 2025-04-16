package org.student.messaging.topics;

public class KafkaTopics {

    public static class CrudMeta {
        @Deprecated
        public static final String SAVE_META_INFO_TOPIC = "save-info-topic";
        @Deprecated
        public static final String GET_EXT_META_INFO_TOPIC = "get-ext-info-topic";
        @Deprecated
        public static final String GET_INT_META_INFO_TOPIC = "get-int-info-topic";
        @Deprecated
        public static final String DEL_META_INFO = "del-info-topic";

        public static final String SAVE_USER_META_INFO_TOPIC = "save-user-info-topic";
        public static final String GET_USER_EXT_META_INFO_TOPIC = "get-user-ext-info-topic";
        public static final String GET_USER_INT_META_INFO_TOPIC = "get-user-int-info-topic";
        public static final String GET_USER_ALL_EXT_META_INFO = "get-all-user-ext-info-topic";
        public static final String DEL_USER_META_INFO = "del-user-info-topic";
    }

    public static class ResponseMeta {
        public static final String SAVE_RESPONSE_TOPIC = "save-response-topic";
        public static final String DEL_RESPONSE_TOPIC = "del-response-topic";
        public static final String GET_EXT_INFO_RESPONSE_TOPIC = "get-ext-response-topic";
        public static final String GET_INT_INFO_RESPONSE_TOPIC = "get-int-response-topic";
        public static final String GET_USER_ALL_EXT_META_INFO_RESPONSE = "get-all-user-ext-info-response-topic";

        public static final String ERROR_RESPONSE_TOPIC = "error-response-topic";
    }


}

