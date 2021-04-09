package team.plassrever.questswithlove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.FontRequest;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class BuyTicketActivity extends AppCompatActivity {

    private TextView ticketNumberTextView, questTextView, costTextView;
    private Spinner buyerSpinner, methodSpinner, discountSpinner;
    private CalendarView calendarView;
    private Button buyButton;

    private String buyerFullName, methodName, discountName, questId, date, generatedNumber;

    private int cost;
    int newcost;


    DBHelperUsers helperQuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_ticket);

        ticketNumberTextView = findViewById(R.id.ticket_text);
        questTextView = findViewById(R.id.quest_text);
        costTextView = findViewById(R.id.final_cost_text);
        buyerSpinner = findViewById(R.id.buyer_spinner);
        methodSpinner = findViewById(R.id.method_spinner);
        discountSpinner = findViewById(R.id.discount_spinner);
        calendarView = findViewById(R.id.calendar_view);
        buyButton = findViewById(R.id.pay_btn);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                date = dayOfMonth + "." + (month+1) + "." + year;
                Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();// TODO Auto-generated method stub

            }
        });


        generatedNumber = generateNumber();
        ticketNumberTextView.setText(generatedNumber);

        helperQuests = new DBHelperUsers(this);
        SQLiteDatabase database = helperQuests.getWritableDatabase();

        Intent myIntent = getIntent(); // gets the previously created intent
        questId = myIntent.getStringExtra("questId");

        Cursor cursor = database.query(DBHelperUsers.TABLE_QUESTS, null, "quest_id = ?", new String[]{questId},null,null, null);

        if (cursor.moveToFirst()){
            int idName = cursor.getColumnIndex(DBHelperUsers.QUEST_NAME);
            int idAge = cursor.getColumnIndex(DBHelperUsers.QUEST_AGE);
            int idCost = cursor.getColumnIndex(DBHelperUsers.QUEST_COST);
            questTextView.setText(cursor.getString(idName) + "(" + String.valueOf(cursor.getInt(idAge)) + "+)");
            costTextView.setText(String.valueOf(cursor.getInt(idCost)) + " р.");
            cost = cursor.getInt(idCost);

        } else {
            Toast.makeText(BuyTicketActivity.this, "Ошибка" , Toast.LENGTH_LONG).show();
        }

        cursor = database.query(DBHelperUsers.TABLE_USERS, null, null,null,null,null,null);
        List<String> buyers = new ArrayList<>();
        if (cursor.moveToFirst()){
            int idFName = cursor.getColumnIndex(DBHelperUsers.USER_FNAME);
            int idLName = cursor.getColumnIndex(DBHelperUsers.USER_LNAME);

            do {
                buyers.add(cursor.getString(idLName) + " " + cursor.getString(idFName));
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(BuyTicketActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
        }
        cursor.close();
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, buyers);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        buyerSpinner.setAdapter(adapter);
        buyerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                buyerFullName = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                buyerFullName = "";
            }
        });

        cursor = database.query(DBHelperUsers.TABLE_PAYMENT_METHODS, null, null,null,null,null,null);
        List<String> methods = new ArrayList<>();
        if (cursor.moveToFirst()){
            int idFName = cursor.getColumnIndex(DBHelperUsers.PMETHOD_NAME);
            do {
                methods.add(cursor.getString(idFName));
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(BuyTicketActivity.this, "Ошибка2", Toast.LENGTH_LONG).show();
        }
        cursor.close();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, methods);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        methodSpinner.setAdapter(adapter);
        methodSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                methodName = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                methodName = "";
            }
        });

        cursor = database.query(DBHelperUsers.TABLE_DISCOUNTS, null, null,null,null,null,null);
        List<String> discounts = new ArrayList<>();
        if (cursor.moveToFirst()){
            int idName = cursor.getColumnIndex(DBHelperUsers.DISCOUNT_NAME);
            int idPercentage = cursor.getColumnIndex(DBHelperUsers.DISCOUNT_PERCENTAGE);

            do {
                discounts.add(cursor.getString(idName) + " (" + cursor.getString(idPercentage) + "%)");
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(BuyTicketActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
        }
        cursor.close();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, discounts);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        discountSpinner.setAdapter(adapter);
        discountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                discountName = (String)parent.getItemAtPosition(position);
                SQLiteDatabase database = helperQuests.getWritableDatabase();
                Cursor cursor = database.query(DBHelperUsers.TABLE_DISCOUNTS, null, "discount_name = ?", new String[]{discountName.split(" ")[0] + " " + discountName.split(" ")[1]},null,null, null);

                if (cursor.moveToFirst()){
                    int idPercentage = cursor.getColumnIndex(DBHelperUsers.DISCOUNT_PERCENTAGE);
                    newcost = cost - (int) ((cost*cursor.getInt(idPercentage))/100);
                    costTextView.setText(String.valueOf(newcost) + " р.");

                } else {
                    Toast.makeText(BuyTicketActivity.this, "Ошибка" , Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                discountName = "";
            }
        });

        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createTicket();
                createPayment();
                Toast.makeText(BuyTicketActivity.this, "Успешно!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(BuyTicketActivity.this, MainActivity.class));
            }
        });


    }

    private void createTicket(){
        SQLiteDatabase database = helperQuests.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelperUsers.TICKET_CODE, generatedNumber);
        contentValues.put(DBHelperUsers.TICKET_COST, cost);
        contentValues.put(DBHelperUsers.TICKET_DATE, date);
        contentValues.put(DBHelperUsers.TICKET_QUEST, Integer.valueOf(questId));

        database.insert(DBHelperUsers.TABLE_TICKETS, null, contentValues);
    }

    private void createPayment(){


        SQLiteDatabase database = helperQuests.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        int method = -1, ticket = -1, buyer = -1, discount = -1;


        database = helperQuests.getWritableDatabase();
        Cursor cursor = database.query(DBHelperUsers.TABLE_DISCOUNTS, null, "discount_name = ?", new String[]{discountName.split(" ")[0] + " " + discountName.split(" ")[1]},null,null, null);

        if (cursor.moveToFirst()){
            discount = cursor.getColumnIndex(DBHelperUsers.DISCOUNT_ID);
        } else {
            Toast.makeText(BuyTicketActivity.this, "Ошибка" , Toast.LENGTH_LONG).show();
        }

        cursor = database.query(DBHelperUsers.TABLE_USERS, null, "user_lname = ? and user_fname = ?", new String[]{buyerFullName.split(" ")[0], buyerFullName.split(" ")[1]},null,null, null);

        if (cursor.moveToFirst()){
            buyer = cursor.getColumnIndex(DBHelperUsers.USER_ID);
        } else {
            Toast.makeText(BuyTicketActivity.this, "Ошибка U" , Toast.LENGTH_LONG).show();
        }

        cursor = database.query(DBHelperUsers.TABLE_TICKETS, null, "ticket_code = ?", new String[]{generatedNumber},null,null, null);

        if (cursor.moveToFirst()){
            ticket = cursor.getColumnIndex(DBHelperUsers.TICKET_ID);
        } else {
            Toast.makeText(BuyTicketActivity.this, "Ошибка U" , Toast.LENGTH_LONG).show();
        }

        cursor = database.query(DBHelperUsers.TABLE_PAYMENT_METHODS, null, "pmethod_name = ?", new String[]{methodName},null,null, null);

        if (cursor.moveToFirst()){
            method = cursor.getColumnIndex(DBHelperUsers.PMETHOD_ID);
        } else {
            Toast.makeText(BuyTicketActivity.this, "Ошибка M" , Toast.LENGTH_LONG).show();
        }

        contentValues.put(DBHelperUsers.PAYMENT_METHOD, method); //inner
        contentValues.put(DBHelperUsers.PAYMENT_TICKET, ticket); //inner
        contentValues.put(DBHelperUsers.PAYMENT_CERTIFICATE, 1);
        contentValues.put(DBHelperUsers.PAYMENT_DATE, "today");
        contentValues.put(DBHelperUsers.PAYMENT_BUYER, buyer); // inner
        contentValues.put(DBHelperUsers.PAYMENT_COST, newcost);
        contentValues.put(DBHelperUsers.PAYMENT_DISCOUNT, discount); // inner

        database.insert(DBHelperUsers.TABLE_PAYMENT, null, contentValues);
    }

    private String generateNumber(){

        String[] arr = {
                "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "Q", "W", "E", "R", "T",
                "Y", "U", "I", "O", "P", "A", "S", "D", "F", "G", "H", "J", "K", "L", "Z",
                "X", "C", "V", "B", "N", "M"
        };

        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < 10; i++){
            int index = (int)(Math.random()*arr.length);
            sb.append(arr[index]);
        }

        return sb.toString();
    }
}
