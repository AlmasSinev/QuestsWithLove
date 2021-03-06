package team.plassrever.questswithlove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView registerTextView;
    private TextView forgotPasswordTextView;
    DBHelperUsers helperUsers;
    private Button signInBtn;
    private EditText emailEdit, passwordEdit;
    private TextView bannerText;

    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        registerTextView = findViewById(R.id.register_text);
        forgotPasswordTextView = findViewById(R.id.forgot_password_text);
        signInBtn = findViewById(R.id.sign_in_btn);

        emailEdit = findViewById(R.id.email_edit);
        passwordEdit = findViewById(R.id.password_edit);

        registerTextView.setOnClickListener(this);
        forgotPasswordTextView.setOnClickListener(this);
        signInBtn.setOnClickListener(this);
        helperUsers = new DBHelperUsers(this);

        bannerText = findViewById(R.id.banner);
        bannerText.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        SQLiteDatabase database = helperUsers.getWritableDatabase();

        switch (v.getId()){
            case R.id.register_text:
                startActivity(new Intent(this, RegisterActivity.class));
                break;
            case R.id.banner:
                counter++;
                if (counter>5){
                    Toast.makeText(this, "?????????????????????? ???????? ???????????????? ??????????????.", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(this, CheckTicketActivity.class);
                    startActivity(myIntent);
                }
                break;
            case R.id.forgot_password_text:
                Toast.makeText(this, "???? ??????????????????????", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sign_in_btn:
                final String email = emailEdit.getText().toString().trim();
                final String password = passwordEdit.getText().toString().trim();

                if (email.isEmpty()){
                    emailEdit.setError("Email ???? ?????????? ???????? ????????????!");
                    emailEdit.requestFocus();
                    return;
                }

                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    emailEdit.setError("?????????????? ???????????????????? Email!");
                    emailEdit.requestFocus();
                    return;
                }

                if (password.isEmpty()){
                    passwordEdit.setError("?????????????? ????????????!");
                    passwordEdit.requestFocus();
                    return;
                }

                List<User> users = new ArrayList<>();
                boolean signSuccsess = false;
                Cursor cursor = database.query(DBHelperUsers.TABLE_USERS, null, null,null,null,null,null);

                if (cursor.moveToFirst()){
                    int idIndex = cursor.getColumnIndex(DBHelperUsers.USER_ID);
                    int idFName = cursor.getColumnIndex(DBHelperUsers.USER_FNAME);
                    int idLName = cursor.getColumnIndex(DBHelperUsers.USER_LNAME);
                    int idEmail = cursor.getColumnIndex(DBHelperUsers.USER_EMAIL);
                    int idNumber = cursor.getColumnIndex(DBHelperUsers.USER_NUMBER);
                    int idPass = cursor.getColumnIndex(DBHelperUsers.USER_PASSWORD);

                    do {
                        User newUser = new User(cursor.getInt(idIndex), cursor.getString(idFName), cursor.getString(idLName), cursor.getString(idEmail), cursor.getString(idNumber), cursor.getString(idPass));
                        users.add(newUser);
                    } while (cursor.moveToNext());

                } else {
                    Toast.makeText(MainActivity.this, "???????????????????????? ???? ????????????!", Toast.LENGTH_LONG).show();
                }

                cursor.close();
                helperUsers.close();
                String userEmail = "";
                for (User user : users){
                    if (user.email.equals(email)){
                        if (user.password.equals(password)){
                            signSuccsess = true;
                            userEmail = user.email;
                        }

                    }
                }

                if (signSuccsess){
                    Toast.makeText(MainActivity.this, "??????????????!", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(this, ReadActivity.class);
                    myIntent.putExtra("userEmail", userEmail);
                    startActivity(myIntent);
                } else{
                    Toast.makeText(MainActivity.this, "????????????????, ???? ?????????????????????? ?????????? ????????????", Toast.LENGTH_LONG).show();
                }
        }
    }
}

/**
 *
 * public User[] users1 = {
 *             new User(1, "????????????", "??????????", "iildarado@gmail.com", "9600860729", "123456"),
 *             new User(2, "????????????", "??????????????????????????", "nataride@gmail.com", "9036667389", "000000"),
 *             new User(3, "????????", "????????????????????","rina@mail.ru", "9067652938", "202020"),
 *             new User(4, "????????????", "????????????????", "lycka@gmail.com", "9631234567", "81378321"),
 *             new User(5, "????????????", "??????????????", "ramakama@gmail.com", "9600664567", "31684901")
 *     };
 *
 *
 *                      String[] types = { "??????????????????????", "VR-??????????", "??????????????????", "???? ??????????", "????????????????????", "???????????????????? ???? ??????????", "?????????????????? ????????????????????" };
 *
 *                     for (int i = 0; i < types.length; i++) {
 *                         ContentValues contentValues = new ContentValues();
 *
 *                         contentValues.put(DBHelperUsers.QTYPE_ID, i);
 *                         contentValues.put(DBHelperUsers.QTYPE_NAME, types[i]);
 *
 *                         database.insert(DBHelperUsers.TABLE_QTYPES, null, contentValues);
 *                     }
 *
 *                     Toast.makeText(this, "???????????????? ??????????.", Toast.LENGTH_SHORT).show();
 *                     String[] categories = {"?????? ????????????????", "????????????????", "?????????????????? ?? ??????????", "??????????????", "?? ????????????????", "?????? ?????????????? ????????????????", "?????? 10", "??????????????????????", "??????????????", "?????????????? IQ", "???? ????????????????", "????????????????????????", "??????????????????????", "?????????????? ??????????", "??????????????", "???????????? ????????????", "????????????????????" };
 *                     for (int i = 0; i < categories.length; i++) {
 *                         ContentValues contentValues = new ContentValues();
 *
 *                         contentValues.put(DBHelperUsers.QCATEGORY_ID, i);
 *                         contentValues.put(DBHelperUsers.QCATEGORY_NAME, categories[i]);
 *
 *                         database.insert(DBHelperUsers.TABLE_QCATEGORIES, null, contentValues);
 *                     }
 *
 *                     Toast.makeText(this, "???????????????? ??????????2.", Toast.LENGTH_SHORT).show();
 *                     String[] methods = {"YooMoney", "????????????????", "????????????????", "WebMoney", "Qiwi", "??????????-????????", "Visa", "Mastercard", "????????????.????????????", "PayPal"};
 *                     for (int i = 0; i < methods.length; i++) {
 *                         ContentValues contentValues = new ContentValues();
 *
 *                         contentValues.put(DBHelperUsers.PMETHOD_ID, i);
 *                         contentValues.put(DBHelperUsers.PMETHOD_NAME, methods[i]);
 *
 *                         database.insert(DBHelperUsers.TABLE_PAYMENT_METHODS, null, contentValues);
 *                     }
 *                     Toast.makeText(this, "???????????????? ??????????3.", Toast.LENGTH_SHORT).show();
 *
 *                     String[][] discounts = {
 *                             {"???????????????? ??????????????", "5", "???????????? 5% ?????? ??????????????, ???????????????????????? ?? ?????????? ?????????????? ?????????? 5 ??????."},
 *                             {"?????????????????? ????????????????", "10", "???????????? 10% ?????? ??????????????, ???????????????????????? ?? ?????????? ?????????????? ?????????? 10 ??????."},
 *                             {"???????? ????????????????", "15", "???????????? 15% ?????? ??????????????????????. ?????????? ??????????????????  3 ?????? ???? ??  3 ?????? ?????????? ?????? ???????????????? ???? ????????????????."},
 *                             {"???????????? ??????????????", "20", "???????????? 20% ?? 23 ???? 29 ????????????."},
 *                             {"?????? ????????????", "25", "???????????? 25% ?? ?????????? ???????????? ??????????????????."}};
 *
 *                     for (int i = 0; i < discounts.length; i++) {
 *
 *                         ContentValues contentValues = new ContentValues();
 *                         contentValues.put(DBHelperUsers.DISCOUNT_ID, i);
 *                         contentValues.put(DBHelperUsers.DISCOUNT_NAME, discounts[i][0]);
 *                         contentValues.put(DBHelperUsers.DISCOUNT_PERCENTAGE, Integer.parseInt(discounts[i][1]));
 *                         contentValues.put(DBHelperUsers.DISCOUNT_CONDITIONS, discounts[i][2]);
 *
 *                         database.insert(DBHelperUsers.TABLE_DISCOUNTS, null, contentValues);
 *                     }
 *
 *
 *                     Quest[] quests = {
 *                             new Quest(1, "???????????????? ????????????????", types[4], 4, 2750, "?????? ???????????? ?????????????????? ???????????? ?????????????? ?? ??????????. ???? ?????????????????? ?????? ?????????? ??????????, ?? ?????? ?????????????????????? ?????????????? ??? ?????????????? ????????????, ?????????????? ?????????????? ???????????? ?? ???????????????????? ????????, ?????? ???? ???? ?????????? ???????? ?????????????????? ???? ???????? ????????????????????. ???? ???????????????? ?? ?????????????????????? ????????????????????, ?????? ?????? ???????????????? ??????????????. ?????????? ???? ?????????????? ???????????????????? ???????? ?????????????? ???????????? ?? ??????????????????, ??, ????????????????, ???????????? ???? ??? ?????? ?????????? ??????????????????...\n" +
 *                                                  "???? ?? ?????????? ???????????????? ???????? ???? ?????????????? ???????????? ?? ???????????????????????? ?????????? ???????? ??????????????, ???? ???????????? ?? ???????? ??????-???? ??????????????????????????, ???????????? ???????????????? ?????? ?????????????????? ??????????????. ?? ???????? ???????? ??????????????, ?????????????? ?????? ???????????? ???? ?????????????? ?????? ???? ??????????????. ??????, ?????? ???????? ?????????????? ?????? ?? ???????? ??????????????????, ???????????? ????????????, ?????????????????? ???????????????? ???? ????????????????. ???????????? ?????? ????????????????????, ?????? ?????? ?? ?????????? ???? ???????????? ???????????????? ???????????????? ??????????, ????-???? ?????????????? ?? ???????????? ?????????? ?????????????? ???? ?????? ?????? ???????????? ?????????????????? ??????, ?????????????????????? ?? ??????????????????, ?????????? ????????...", 3, 16, 60),
 *                             new Quest(2, "?????????????????? ??????????????????", types[0], 4, 2000, "?? ???????????? ?????????????? ?????? ?????????????????? ???? ?????????? ?? ?????????????????? ?? ??????????????. ?????? ???? ???????????? ?????? ????????????, ?????? ???? ???????????? ??????????????. ???????????? ???????????? ???? ?? ????????????. ?????????? ???? ?????? ?????? ???? ?????????? ?????? ??????????, ???????? ???? ?????? ?????????? ??????...", 4, 14, 45),
 *                             new Quest(3, "??????????", types[2], 1, 1500, "?????????????? ??? ?????? ??????-????????????????????, ?????? ???????????? ?????????????????? ????????????????????????, ?? ?????????????? ?????????????? ???????? ??????????????, ?????????????????????? ???????????? ????????????????. ???? ???????????????????? ???????? ???????? ?????? ?????????? ???????????????????????? ?????????????????????????? ??????????????????????. ???? ?????????????? ?????????????????????? ?????????????? ?????????????? ???? ?????? ??????, ???????? ???? ?????????????? ???????????? ???????????????? ?????????? ???? ??????????????, ?????????????? ??????????, ???????????????? ?????????????????? ???????????????? ????????????????. ?????????????? ???? ???? ??????????????? ???????????? ?????????????????????? ?? ?????????????????? ?????????? ?? ?????????! ?? ?????????? ???????????? ?????? ???????????????? ???????????? ???? ???? ????????????????...", 5, 18, 60),
 *                             new Quest(4, "???????????? ????????????", types[3], 2, 1500, "??????-???? ?? ???????????? ???????????????????????? ?????????????????? ????????????, ?????? ???????????????? ?? ???????????????? ???????????? ???????????????? ?????????????? ?? ???????????? ????????????????????, ???????????????? ?? ?????????????????????? ???????????? ?????????????\n" +
 *                                     "???????????????????? ???????????? ?? ?????????????? ???????????? ?????????????????? ???? ?????????????????????? ?????????????? ???????? ???????????? ???? ???????? ?????????????????? ?????????? ???????????? ??????????. ?????? ???????? ?????????????? ??????????????. ?????????????? ???? ?????????????????????????? ???????????????????? ????????????????: ?????????????? ???????????????????????? ?? ?????????????????????? ??????????????, ?????????????? ???????????????????????? ?????????? ?? ??????????.\n" +
 *                                     "???????????????? ???????? ?????????????????? ????????????????, ???????????? ???????????????????? ?? ?????????? ?????????????????????? ?????????? ??????????????????, ?????????????????????? ???? ???? ???????????? ?? ???????????????????? ?????????????????????? ?????????????? ???????????????????????? ?????? ???????????????? ???????? ???? ?????????????? ?????????? ???????????? ??????????.\n" +
 *                                     "?? ???????????? ?????? ???????????????? ???????????????????? ???????????? ???????????? ????????????????????. ?????????????????? ???????? ??????.", 3, 14, 45),
 *                             new Quest(5, "?????? ???? ??????????", types[0], 4, 2000, "?? ?????????? ???? ?????????????????????? ???????????? ???? ?????? ?????????????? ???????????? ???????????????????????????? ???????????????????? ?????????????? ???????????????? ???? ??????????. ?? ?????????????????????????? ??????????????????, ?????????? ???????????????? ????????????, ?????????????????????????? ?????????????????????? ?? ?????????????????? ?????????? ?????? ?????????????????? ???????????? ?????????????????? ??????????????. ?????????????????? ???????????? ?????????????????? ?? ?????????? ???? ???????????????? ???????????? ??????????, ???? ?????????? ???????????????????? ??????, ?????? ?????????????????????? ???????????????? ?? ???????????? ????. ?????????? ?????????????????????? ???? ????????, ?? ?????????????????????? ????????????????????. ?????????????? ???? ???? ?????????? ?????????????????????? ?? ???????? ??????????????????????, ?????????????? ???????????? ??????????...", 5, 14, 90),
 *                             new Quest(6, "Kepler-68", types[1], 4, 2500, "2098 ??????, ???? ?????????? ???????????? ?????????????? ??????????, ?????????????????? ???????????????????? ?????? ????????????????????????.\n" +
 *                                     "?? ?????????????? ?????????????? ???????? ???????????????????????? ?????????????? ????????????, ???????????????????? ???? ???????????????????? ?????????? ??????????????, ?? ?????????? ?????????????????????? ?????????????? ???? ???????????? ?????? ?????????????????????????????? ????????????????????, ?????????????????? ???? ?????????????????????? Kepler-186.\n" +
 *                                     "???????????????????? ???????? ???????????????????? ???? 120 ????????, ???? ???????????? ?????? ?????????????? ???????????? SOS ???? ????????????, ?? ?????????? ????????????????????. ???? ????????????, ???????????????????? ?? ???????????????????? ?????????????? ???????????????? ???????????????? ????????????, ???? ???????????? ?? ????????????, ?????? ?????????????? ???????? ????????????????????, ???? ??????-???? ?????????????????????? ?????????????????? ?????????? ?? ???????????????????? ???? ?? ????????????????.\n" +
 *                                     "???? ???????????? ?????????????????????? Kepler-186 ?????????????????? ?????????????? ?????????????? ?????????? 4??, ???? ?????????????? ???????????????? ?????????????????? ???????? ?????? ?????????????? ?? ?????????????????? ?????????????????????? ???????????? 5, ??????, ???? ???????????? ????????????????, ?? ?????????????????? ?????????????? ??????????????.\n" +
 *                                     "???? ??? ???????????????????????????????? ??????????????, ???????????????????? ???????????? ?????????????????????????? ?????????????? ???????????? ??????????????????. ???????????? ???? ?????? ?????????????? ???????????? ?????????? ????????????????????????...", 5, 18, 30),
 *                             new Quest(7, "????????", types[1], 4, 2000, "???????? ???????????? ???? ???????????????? ?????? ???????????????? ?????? ?? ??????????, ???? ???????????????? ?????????? ???????????????????? ???????????? ???????????????? ???????? ????????????. ???????? ???????????????????? ?????? ???? ?????????????????????????? ?????????? ?? ???????????? ????????, ?? ???????????? ???????????????? ???????????? ?????????????? ?? ?????????????????????? ??????. ?????????? ???????????? ???????????????? ??????????, ?????????? ???????????????????? ???????? ?????????????????????? ?? ?????????????? ???????????? ???? ???????????? ?????????? ??? ?????????? ???????????????? ???????????? ??????????????????????, ?????????????????? ???????????? ?? ?????????? ?????????? ????????????????????????????????????, ???????????????? ?????????????? ??????????????????, ???????????????????? ???????????????????? ????????????????????????????.\n" +
 *                                     "?? 1919 ???????? ???? ?????????? ???????????????? ???? ???????? ?????????? ?????? ?????? ???????????????? ?????????????? ?????????? ?????? ???????? ?????????????????????? ?? ?????????????????? ???? ???????? ?? ???????????? ??????????. ?????????? ?????? ???????? ?????????????? ????????. ?????????? ?????? ???????????????? ???????? ?????????????????? ?????? ?? ?????????????? ??????????????, ?????? ?????? ???????? ???? ???????????? ????????????. ?? ?????? ?????? ???????????? ?????????? ????????????????, ?????? ?????? ???????? ???? ?????????? ?? ?????????????? ???????? ?? ?????????? ????????????, ?? ?????? ??????????????, ?? ???????????? ???????????????? ?????????????? ?? ?????????", 3, 16, 60),
 *                             new Quest(8, "??????????", types[0], 6, 3000, "?? 1919 ???????? ???? ?????????? ???????????????? ???? ???????? ?????????? ?????? ?????? ???????????????? ?????????????? ?????????? ?????? ???????? ?????????????????????? ?? ?????????????????? ???? ???????? ?? ???????????? ??????????. ?????????? ?????? ???????? ?????????????? ????????. ?????????? ?????? ???????????????? ???????? ?????????????????? ?????? ?? ?????????????? ??????????????, ?????? ?????? ???????? ???? ???????????? ????????????. ?? ?????? ?????? ???????????? ?????????? ????????????????, ?????? ?????? ???????? ???? ?????????? ?? ?????????????? ???????? ?? ?????????? ????????????, ?? ?????? ??????????????, ?? ???????????? ???????????????? ?????????????? ?? ?????????", 5, 18, 80),
 *                             new Quest(9, "???????????? ????????????????", types[5], 2, 1500, "1934 ??????. ????????????.\n" +
 *                                     "???????????????????? ???????????????? ???? ???????????????????????? ??????????????. ?????????????? ???????????? ????????????.\n" +
 *                                     "?? ?????????????????? ???????????????? ?????????????????? ?????????? ???????????????????????? ??????????????. ?????????????????? ???? ???????????? ?????????????????? ???????? ?? ?????? ?????????? ???????? ???????????????????? 10 ?????????????????????? ??????. ?????? ???????? ???????????? ???????????? ???????????????????? ?????????? ?????????? ?????????? ?? ???????? ?????????????????????? ????????. ?????? ?????? ???????? ???????????????????? ???????? ???????????????? ?? ???????????? ????????. ???????????????? ?????? ???????????? ???? ????????????, ???????? ?????? ?????????????????? ???????????? ???????????????????????? ????????????. ???? ??? ???????????? ??????????????????????, ???????????? ?????????? ???????? ?? ???????? ???????? ??, ???? ?????? ???? ???? ???? ??????????, ???????????? ???????????????????????? ???????????????????? ??????????????, ??????????????????????, ?????? ?? ?????? ?????? ????????, ?? ?????????? ?????????????????? ????????????.", 3, 18, 60),
 *                             new Quest(10, "??????????????", types[6], 1, 3000, "?????? ?? ???????????????? ???????????????????? ???????? ??????????, ?????????????????? ?? ?????????????????????????? ???????????????????? ??????, ???????????????????????? ?????????????? ?? ???????? ??????.\n" +
 *                                     "?? ???????? ???????? ?????????????? ???? ?????????? ?????????????? ??????????????????. ?? ?????????? ?? ?????? ?????? ?????????????????????????? ???????????????????????? ?????????? ???????????????????? ????????. ?????? ?????????? ???????????? ???????????????????? ???? ???????? ????????????????. ?????????? ?????? ????????????????????????, ?? ???????? ?????? ?????????? ???????? ????????????. ?????????? ????????, ???????????????? ?????????????? ????????????????????, ?????? ???????? ???????????????? ?????????? ?????????????????????????????? ??????????????????. ???? ???????????? ?????? ?????????? ??????????????????.\n" +
 *                                     "???? ?????????? ?? ?????????? ???????????????? ???? ????????????????????, ?????????????? ?????????????? ??????????????????. ???? ???????????????? ?????????? ??, ????????????, ???????????? ??????.\n" +
 *                                     "?????????????? ??????????, ???? ?????????????????????? ?? ?????????????????????????? ???? ?????????? ?? ???????? ??????????, ?????? ?? ???????? ?????????????? ?????????????????????? ??????????????.", 4, 16, 60)
 *                     };
 *
 *                     for (Quest q : quests) {
 *                         ContentValues contentValues = new ContentValues();
 *
 *                         contentValues.put(DBHelperUsers.QUEST_ID, q.id);
 *                         contentValues.put(DBHelperUsers.QUEST_NAME, q.name);
 *                         contentValues.put(DBHelperUsers.QUEST_TYPE, q.type);
 *                         contentValues.put(DBHelperUsers.QUEST_AMOUNT, q.playerAmount);
 *                         contentValues.put(DBHelperUsers.QUEST_COST, q.cost);
 *                         contentValues.put(DBHelperUsers.QUEST_SPECIFICATION, q.specification);
 *                         contentValues.put(DBHelperUsers.QUEST_DIFF, q.difficulty);
 *                         contentValues.put(DBHelperUsers.QUEST_AGE, q.recomentAge);
 *                         contentValues.put(DBHelperUsers.QUEST_INTERVAL, q.interval);
 *
 *                         database.insert(DBHelperUsers.TABLE_QUESTS, null, contentValues);
 *                     }
 *
 *
 *                     int[][] cq = {
 *                                                          {6, 0}, {6, 1}, {6, 2}, {6, 3}, {6, 4}, {6, 5}, {6, 6}, {6, 7},
 *                               {6, 8}, {6, 9}, {2, 7}, {2, 0}, {2, 4}, {2, 3}, {5, 0}, {5, 1}, {5, 2},
 *                               {11, 8}, {9, 7}, {9, 0}, {4, 1}, {4, 2}, {3, 8}, {3, 7}, {3, 0}, {1, 2},
 *                               {1, 7}, {0, 7}, {0, 1}, {0, 5}, {0, 4}, {7, 2}, {8, 6}, {8, 3}, {8, 8},
 *                               {10, 1}, {11, 0}, {11, 8}, {12, 1}, {12, 3}, {12, 2}, {13, 6}, {14, 7},
 *                               {14, 5}, {15, 2}, {13, 7}, {15, 0}, {16, 4}, {16, 5}, {11, 7}, {11, 8},
 *                               {15, 3}, {7, 1}, {8, 7}, {10, 0}, {14, 2}, {2, 6}
 *                       };
 *
 *                     for (int i = 0; i < cq.length; i++) {
 *
 *                           ContentValues contentValues = new ContentValues();
 *                           contentValues.put(DBHelperUsers.CQ_ID, i);
 *                           contentValues.put(DBHelperUsers.CQ_CATEGORY, cq[i][0]);
 *                           contentValues.put(DBHelperUsers.CQ_QUEST, cq[i][1]);
 *
 *                           database.insert(DBHelperUsers.TABLE_CATEGORIES_QUESTS, null, contentValues);
 *                       }
 *
 *                       helperUsers.close();
 *
 * */
