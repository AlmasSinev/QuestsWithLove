package team.plassrever.questswithlove;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DBHelperUsers extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "quests1";

    public static final String TABLE_USERS = "users";
    public static final String USER_ID = "user_id";
    public static final String USER_FNAME = "user_fname";
    public static final String USER_LNAME = "user_lname";
    public static final String USER_EMAIL = "user_email";
    public static final String USER_NUMBER = "user_number";
    public static final String USER_PASSWORD = "user_password";

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


    public static final String TABLE_PAYMENT_METHODS = "payment_methods";
    public static final String PMETHOD_ID = "pmethod_id";
    public static final String PMETHOD_NAME = "pmethod_name";

    public static final String TABLE_DISCOUNTS = "discounts";
    public static final String DISCOUNT_ID = "discount_id";
    public static final String DISCOUNT_NAME = "discount_name";
    public static final String DISCOUNT_PERCENTAGE = "discount_percentage";
    public static final String DISCOUNT_CONDITIONS = "discount_conditions";

    public static final String TABLE_QTYPES = "quest_types";
    public static final String QTYPE_ID = "type_id";
    public static final String QTYPE_NAME = "type_name";

    public static final String TABLE_QCATEGORIES = "quest_categories";
    public static final String QCATEGORY_ID = "category_id";
    public static final String QCATEGORY_NAME = "category_name";

    public static final String TABLE_CATEGORIES_QUESTS = "categories_quests";
    public static final String CQ_ID = "id";
    public static final String CQ_CATEGORY = "category";
    public static final String CQ_QUEST = "quest";

    public static final String TABLE_RESULTS = "results";
    public static final String RESULT_ID = "result_id";
    public static final String RESULT_QUEST = "result_quest";
    public static final String RESULT_TIME = "result_time";

    public static final String TABLE_TICKETS = "tickets";
    public static final String TICKET_ID = "ticket_id";
    public static final String TICKET_CODE = "ticket_code";
    public static final String TICKET_DATE = "ticket_date";
    public static final String TICKET_COST = "ticket_cost";
    public static final String TICKET_QUEST = "ticket_quest";

    public static final String TABLE_PAYMENT = "payments";
    public static final String PAYMENT_ID = "payment_id";
    public static final String PAYMENT_METHOD = "payment_method";
    public static final String PAYMENT_TICKET = "payment_ticket";
    public static final String PAYMENT_CERTIFICATE = "payment_certificate";
    public static final String PAYMENT_DATE = "payment_date";
    public static final String PAYMENT_BUYER = "payment_buyer";
    public static final String PAYMENT_COST = "payment_cost";
    public static final String PAYMENT_DISCOUNT = "payment_discount";


    public DBHelperUsers(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_USERS + "(" + USER_ID + " integer primary key, " + USER_FNAME + " text, " + USER_LNAME + " text, " + USER_EMAIL + " text, " + USER_NUMBER + " text, " + USER_PASSWORD + " text" + ")");

        db.execSQL("create table " + TABLE_QUESTS + "("
                + QUEST_ID + " integer primary key, "
                + QUEST_NAME + " text, "
                + QUEST_TYPE + " text, "
                + QUEST_AMOUNT + " integer, "
                + QUEST_COST + " integer, "
                + QUEST_SPECIFICATION + " text, "
                + QUEST_DIFF + " integer, "
                + QUEST_AGE + " integer, "
                + QUEST_INTERVAL + " integer " + ")");

        db.execSQL("create table " + TABLE_RESULTS + "("
                + RESULT_ID + " integer primary key, "
                + RESULT_QUEST + " integer, "
                + RESULT_TIME + " integer " + ")");

        db.execSQL("create table " + TABLE_QCATEGORIES + "("
                + QCATEGORY_ID + " integer primary key, "
                + QCATEGORY_NAME + " text " + ")");

        db.execSQL("create table " + TABLE_CATEGORIES_QUESTS + "("
                + CQ_ID + " integer primary key, "
                + CQ_CATEGORY + " integer, "
                + CQ_QUEST + " integer, "
                + " FOREIGN KEY (" + CQ_CATEGORY + ") REFERENCES " + TABLE_QCATEGORIES + " (" + QCATEGORY_ID + ") ON DELETE CASCADE  ON UPDATE NO ACTION,"
                + " FOREIGN KEY (" + CQ_QUEST + ") REFERENCES " + TABLE_QUESTS + " (" + QUEST_ID + ") ON DELETE CASCADE  ON UPDATE NO ACTION"
                + ")");

        db.execSQL("create table " + TABLE_QTYPES + "("
                + QTYPE_ID + " integer primary key, "
                + QTYPE_NAME + " text " + ")");

        db.execSQL("create table " + TABLE_DISCOUNTS + "("
                + DISCOUNT_ID + " integer primary key, "
                + DISCOUNT_NAME + " text, "
                + DISCOUNT_PERCENTAGE + " integer, "
                + DISCOUNT_CONDITIONS + " text " + ")");

        db.execSQL("create table " + TABLE_PAYMENT_METHODS + "("
                + PMETHOD_ID + " integer primary key, "
                + PMETHOD_NAME + " text " + ")");

        db.execSQL("create table " + TABLE_TICKETS + "("
                + TICKET_ID + " integer primary key, "
                + TICKET_CODE + " text, "
                + TICKET_DATE + " text, "
                + TICKET_COST + " integer, "
                + TICKET_QUEST + " integer " + ")");

        db.execSQL("create table " + TABLE_PAYMENT + "("
                + PAYMENT_ID + " integer primary key, "
                + PAYMENT_METHOD + " integer, "
                + PAYMENT_TICKET + " integer, "
                + PAYMENT_CERTIFICATE + " integer, "
                + PAYMENT_DATE + " text, "
                + PAYMENT_BUYER + " integer, "
                + PAYMENT_COST + " integer, "
                + PAYMENT_DISCOUNT + " integer " + ")");


        //db.execSQL();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addQuest(Quest quest){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUEST_NAME, quest.name);
        contentValues.put(QUEST_COST, quest.cost);
        contentValues.put(QUEST_AMOUNT, quest.playerAmount);
        db.insert(TABLE_QUESTS,null,contentValues);
        db.close();
    }

    public Quest getQuestById(int id){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select * from " + TABLE_QUESTS + " where id="+id+"", null);


        if(cursor != null)
            cursor.moveToFirst();
        Quest quest  = new Quest(
                Integer.parseInt(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                Integer.parseInt(cursor.getString(3)),
                Integer.parseInt(cursor.getString(4)),
                cursor.getString(5),
                Integer.parseInt(cursor.getString(6)),
                Integer.parseInt(cursor.getString(7)),
                Integer.parseInt(cursor.getString(8))
                );

        return quest;
    }

    public boolean updateQuest(int id, String name, int cost, String amount){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(QUEST_NAME, name);
        contentValues.put(QUEST_COST, cost);
        contentValues.put(QUEST_AMOUNT, amount);
        db.update(TABLE_QUESTS, contentValues, QUEST_ID + " = ? " ,
                new String[]{String.valueOf(id)});

        return true;
    }


    public void deleteQuestById(Quest quest){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_QUESTS, QUEST_ID + " = ? ",
                new String[]{String.valueOf(quest.id)});
        db.close();
    }

    public List<Quest> getQuestsLikeName(String name){
        List studentsList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_QUESTS + " WHERE quest_name LIKE %" + name + "%";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()){
            do{
                Quest quest = new Quest();
                quest.id = Integer.parseInt(cursor.getString(0));
                quest.name = cursor.getString(1);
                quest.type = cursor.getString(2);
                quest.playerAmount = Integer.parseInt(cursor.getString(3));
                quest.cost = Integer.parseInt(cursor.getString(4));
                quest.specification = cursor.getString(5);
                quest.difficulty = Integer.parseInt(cursor.getString(6));
                quest.recomentAge = Integer.parseInt(cursor.getString(7));
                quest.interval = Integer.parseInt(cursor.getString(8));
                studentsList.add(quest);
            }while(cursor.moveToNext());
        }
        return studentsList;
    }


    public int getQuestsCount() {
        int count=0;
        String countQuery = "SELECT  * FROM " + TABLE_QUESTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        if(cursor != null && !cursor.isClosed()){
            count = cursor.getCount();
            cursor.close();
        }

        return count;
    }
}
