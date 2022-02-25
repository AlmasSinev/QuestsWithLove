package team.plassrever.questswithlove;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class QuestActivity extends AppCompatActivity implements View.OnClickListener{

    DBHelperUsers helperQuests;

    TextView titleNameTextView, titleTypeTextView, titleAmountTextView, titleIntervalTextView, titleCostTextView;
    TextView conditionsTextView, ageTextView, costTextView, categoryTextView;

    ImageView diffImageView1, diffImageView2, diffImageView3, diffImageView4, diffImageView5;
    ImageView[] diffs;

    String questName = "";
    int questId = -1;
    // 5PYNKP2T42
    Button updateButton, deleteButton, buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quest);

        updateButton = findViewById(R.id.bottom_update_btn);
        deleteButton = findViewById(R.id.bottom_delete_btn);
        buyButton = findViewById(R.id.bottom_buy_btn);

        updateButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);
        buyButton.setOnClickListener(this);

                titleNameTextView = findViewById(R.id.name_text);
        titleTypeTextView = findViewById(R.id.type_text);
        titleAmountTextView = findViewById(R.id.text_in_image_amount);
        titleIntervalTextView = findViewById(R.id.text_in_image_interval);
        titleCostTextView = findViewById(R.id.text_in_image_cost);

        conditionsTextView = findViewById(R.id.condition_text);
        ageTextView = findViewById(R.id.age_text);
        costTextView = findViewById(R.id.cost_text);
        categoryTextView = findViewById(R.id.category_text);

        diffImageView1 = findViewById(R.id.diff_img_int_img_1);
        diffImageView2 = findViewById(R.id.diff_img_int_img_2);
        diffImageView3 = findViewById(R.id.diff_img_int_img_3);
        diffImageView4 = findViewById(R.id.diff_img_int_img_4);
        diffImageView5 = findViewById(R.id.diff_img_int_img_5);

        diffs = new ImageView[]{diffImageView1, diffImageView2, diffImageView3, diffImageView4, diffImageView5};

        helperQuests = new DBHelperUsers(this);
        SQLiteDatabase database = helperQuests.getWritableDatabase();

        Intent myIntent = getIntent(); // gets the previously created intent
        String titleQuest = myIntent.getStringExtra("titleQuest");
        String[] selectionArgs = new String[]{titleQuest};
        Cursor cursor = database.query(DBHelperUsers.TABLE_QUESTS, null, "quest_name = ?",selectionArgs,null,null, null);

        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelperUsers.QUEST_ID);
            int idName = cursor.getColumnIndex(DBHelperUsers.QUEST_NAME);
            int idType = cursor.getColumnIndex(DBHelperUsers.QUEST_TYPE);
            int idAmount = cursor.getColumnIndex(DBHelperUsers.QUEST_AMOUNT);
            int idCost = cursor.getColumnIndex(DBHelperUsers.QUEST_COST);
            int idSpeciff = cursor.getColumnIndex(DBHelperUsers.QUEST_SPECIFICATION);
            int idDiff = cursor.getColumnIndex(DBHelperUsers.QUEST_DIFF);
            int idAge = cursor.getColumnIndex(DBHelperUsers.QUEST_AGE);
            int idInterval = cursor.getColumnIndex(DBHelperUsers.QUEST_INTERVAL);

            titleNameTextView.setText(cursor.getString(idName));
            titleTypeTextView.setText(cursor.getString(idType));

            questId = cursor.getInt(idIndex);
            questName = cursor.getString(idName);

            String amountStr = "1";
            if (cursor.getInt(idAmount) != 1)
                amountStr = "1-" + String.valueOf(cursor.getInt(idAmount));
            titleAmountTextView.setText(amountStr);
            titleIntervalTextView.setText(String.valueOf(cursor.getInt(idInterval)) + "мин");
            titleCostTextView.setText(String.valueOf(cursor.getInt(idCost)) + " р.");

            conditionsTextView.setText(cursor.getString(idSpeciff));
            ageTextView.setText("от " + String.valueOf(cursor.getInt(idAge)) + " лет.");
            costTextView.setText(String.valueOf(cursor.getInt(idCost)) + " рублей.");

            for (int i = 0; i < cursor.getInt(idDiff); i++){
                diffs[i].setImageResource(R.drawable.ic_locked_no_bg);
            }

        } else {
            Toast.makeText(QuestActivity.this, "Ошибка" , Toast.LENGTH_LONG).show();
        }
        String sqlQuery = "select QC.category_name as catename from categories_quests"
                + " inner join quest_categories as QC"
                + " on categories_quests.category = QC.category_id"
                + " where categories_quests.quest = ?";
        cursor = database.rawQuery(sqlQuery, new String[]{String.valueOf(questId)});

        if (cursor.moveToFirst()){
            StringBuilder sb = new StringBuilder("");
            do {
                int idName = cursor.getColumnIndex("catename");
                sb.append(cursor.getString(idName) + "\t\t\t\t");
            } while (cursor.moveToNext());

            categoryTextView.setText(sb.toString());

        } else {
            Toast.makeText(QuestActivity.this, "Категории не найдены", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bottom_delete_btn:
                showDialog(1);
                break;
            case R.id.bottom_update_btn:
                Intent myIntent = new Intent(this, CreateQuestActivity.class);
                myIntent.putExtra("questId", String.valueOf(questId));
                startActivity(myIntent);
                break;
            case R.id.bottom_buy_btn:
                Intent myIntents = new Intent(this, BuyTicketActivity.class);
                myIntents.putExtra("questId", String.valueOf(questId));
                startActivity(myIntents);
                break;

        }
    }

    protected Dialog onCreateDialog(int id) {
        if (id == 1){
            AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle("Внимание");
            // сообщение
            adb.setMessage("Вы действительно хотите удалить этот квест?");
            // кнопка положительного ответа
            adb.setPositiveButton("Да", myClickListener);
            // кнопка отрицательного ответа
            adb.setNegativeButton("Нет", myClickListener);
            // кнопка нейтрального ответа
            adb.setNeutralButton("Отмена", myClickListener);
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    DialogInterface.OnClickListener myClickListener = new DialogInterface.OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                // положительная кнопка
                case Dialog.BUTTON_POSITIVE:
                    //
                    SQLiteDatabase database = helperQuests.getWritableDatabase();
                    database.delete(DBHelperUsers.TABLE_QUESTS, DBHelperUsers.QUEST_NAME + " = ?", new String[]{questName});
                    database.delete(DBHelperUsers.TABLE_CATEGORIES_QUESTS, DBHelperUsers.CQ_QUEST + " = ?", new String[]{String.valueOf(questId)});
                    Intent myIntent = new Intent(QuestActivity.this, ReadActivity.class);
                    myIntent.putExtra("userEmail", "Удалено");
                    startActivity(myIntent);
                    break;
                // негативная кнопка
                case Dialog.BUTTON_NEGATIVE:
                    break;
                // нейтральная кнопка
                case Dialog.BUTTON_NEUTRAL:
                    break;
            }
        }
    };
}
