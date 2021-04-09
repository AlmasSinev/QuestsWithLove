package team.plassrever.questswithlove.ui;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelperQuests extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "natarina";

    public static final String TABLE_QUESTS = "quests";
    public static final String QUEST_ID = "quest_id";
    public static final String QUEST_NAME = "quest_name";
    public static final String QUEST_TYPE = "quest_type";
    public static final String QUEST_AMOUNT = "quest_amount";
    public static final String QUEST_COST = "quest_cost";
    public static final String QUEST_SPECIFICATION = "quest_specification";
    public static final String QUEST_DIFF = "quest_diff";
    public static final String QUEST_AGE = "quest_age";
    public static final String QUEST_INTERVAL = "quest_interval";

    public DBHelperQuests(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_QUESTS + "("
                + QUEST_ID + " integer primary key, "
                + QUEST_NAME + " text, "
                + QUEST_TYPE + " text, "
                + QUEST_AMOUNT + " integer, "
                + QUEST_COST + " integer, "
                + QUEST_SPECIFICATION + " text, "
                + QUEST_DIFF + " integer, "
                + QUEST_AGE + " integer, "
                + QUEST_INTERVAL + " integer "
                + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
