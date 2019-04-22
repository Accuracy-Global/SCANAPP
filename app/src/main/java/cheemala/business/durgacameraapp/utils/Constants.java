package cheemala.business.durgacameraapp.utils;

public class Constants {

    public final static String DB_NAME = "ScanAppDB";
    public final static int DB_VERSION =1;
    public final static String DB_TABLE_NAME = "TABLE_CAPTURED_DATA";
    public final static String DB_COLUMN_ID_NAME = "COLUMN_APP_DATA_ID";
    public final static String DB_COLUMN_TIME_STAMP = "COLUMN_CAPTURED_TIME_STAMP_DATA";
    public final static String DB_COLUMN_TEXT_VALUE = "COLUMN_CAPTURED_TEXT_DATA";

    public static String getDbName() {
        return DB_NAME;
    }

    public static int getDbVersion() {
        return DB_VERSION;
    }

    public static String getDbTableName() {
        return DB_TABLE_NAME;
    }

    public static String getDbColumnIdName() {
        return DB_COLUMN_ID_NAME;
    }

    public static String getDbColumnTextValue() {
        return DB_COLUMN_TEXT_VALUE;
    }

    public static String getDbColumnTimeStamp() {
        return DB_COLUMN_TIME_STAMP;
    }

}
