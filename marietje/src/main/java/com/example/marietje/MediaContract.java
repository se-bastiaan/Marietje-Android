package com.example.marietje;

import android.provider.BaseColumns;

public final class MediaContract {
    public MediaContract() {
    }

    /* Inner class that defines the table contents */
    public static abstract class MediaEntry implements BaseColumns {
        public static final String TABLE_NAME = "media";
        public static final String COLUMN_NAME_ENTRY_ID = "id";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_ARTIST = "artist";
        public static final String COLUMN_NAME_REQCOUNT = "reqcount";
    }
}