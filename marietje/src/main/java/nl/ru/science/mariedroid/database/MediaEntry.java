package nl.ru.science.mariedroid.database;

import android.provider.BaseColumns;

public class MediaEntry implements BaseColumns {
    public static final String TABLE_NAME = "media";
    public static final String COLUMN_NAME_ENTRY_ID = "id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_ARTIST = "artist";
    public static final String COLUMN_NAME_REQCOUNT = "reqcount";
    public static final String COLUMN_NAME_LENGTH = "length";
}