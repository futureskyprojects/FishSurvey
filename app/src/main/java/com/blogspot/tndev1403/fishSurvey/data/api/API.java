package com.blogspot.tndev1403.fishSurvey.data.api;

public class API {

    /* Type format of Captain in REST API */
    public static class Captain {
        public static final String ID = "id";
        public static final String FULL_NAME = "fullname";
        public static final String PHONE = "phone";
        public static final String VESSEL = "vessel";
    }

    /* Type format of Record in REST API */
    public static class Record {
        public final static String CAPTAIN_ID = "captain_id";

        public static class trip {
            public final static String FROM_DATE = "from_date";
            public final static String TO_DATE = "to_date";
            public final static String DESCRIPTION = "description";
        }

        public final static String FISH_ID = "fish_id";
        public final static String LONG = "long";
        public final static String WEIGHT = "weight";
        public final static String LAT = "lat";
        public final static String LNG = "lng";
        public final static String CATCHED_AT = "catched_at";
    }

    /* Type format of Image in REST API */
    public static class Image {
        public final static String RECORD_ID = "record_id";
        public final static String BASE64_CONTENT = "base64_content";
    }
}
