package app.simpleproject.drinkwater;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "contactDb";
    public static final String TABLE_CONTACTS = "contacts";

    public static final String KEY_ID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_IMG = "img";
    public static final String KEY_TIME = "time";
    public static final String KEY_VOLUME = "volume";
    public static final String KEY_DATE = "date";
    public static final String KEY_PROGRESSBAR = "progressBar";
    public static final String KEY_LASTITEM = "lastItem";

    public static final String KEY_MONTH = "month";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_CONTACTS + "(" + KEY_ID
                + " integer primary key," + KEY_NAME + " text," + KEY_IMG + " text," + KEY_TIME + " text," + KEY_VOLUME + " text," + KEY_DATE + " text," + KEY_PROGRESSBAR + " text," + KEY_LASTITEM + " text," + KEY_MONTH + " text" + ")");
    }

    @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);

    }
}