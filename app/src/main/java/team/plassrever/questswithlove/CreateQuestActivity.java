package team.plassrever.questswithlove;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import team.plassrever.questswithlove.ui.DBHelperQuests;

public class CreateQuestActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText nameEdit;
    private EditText costEdit;
    private EditText speciffEdit;
    private EditText diffEdit;
    private EditText ageEdit;
    private EditText intervalEdit;


    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, checkBox5, checkBox6,
            checkBox7, checkBox8, checkBox9, checkBox10, checkBox11, checkBox12,
            checkBox13, checkBox14, checkBox15, checkBox16;


    private Spinner typeSpinner;
    private String typeString;

    private SeekBar amountSeekBar;
    private int amountInt = 2;

    String questId = "";

    private Button createQuestButton;

    DBHelperUsers helperQuests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_quest);

        checkBox1 = findViewById(R.id.checkbox1);
        checkBox2 = findViewById(R.id.checkbox2);
        checkBox3 = findViewById(R.id.checkbox3);
        checkBox4 = findViewById(R.id.checkbox4);
        checkBox5 = findViewById(R.id.checkbox5);
        checkBox6 = findViewById(R.id.checkbox6);
        checkBox7 = findViewById(R.id.checkbox7);
        checkBox8 = findViewById(R.id.checkbox8);
        checkBox9 = findViewById(R.id.checkbox9);
        checkBox10 = findViewById(R.id.checkbox10);
        checkBox11 = findViewById(R.id.checkbox11);
        checkBox12 = findViewById(R.id.checkbox12);
        checkBox13 = findViewById(R.id.checkbox13);
        checkBox14 = findViewById(R.id.checkbox14);
        checkBox15 = findViewById(R.id.checkbox15);
        checkBox16 = findViewById(R.id.checkbox16);

        nameEdit = findViewById(R.id.quest_name_edit);
        costEdit = findViewById(R.id.quest_cost_edit);
        speciffEdit = findViewById(R.id.quest_speciff_edit);
        diffEdit = findViewById(R.id.quest_diff_edit);
        ageEdit = findViewById(R.id.quest_age_edit);
        intervalEdit = findViewById(R.id.quest_interval_edit);

        typeSpinner = findViewById(R.id.type_spinner);
        amountSeekBar = findViewById(R.id.amount_seekbar);

        helperQuests = new DBHelperUsers(this);
        createQuestButton = findViewById(R.id.quest_create_btn);
        createQuestButton.setOnClickListener(this);

        Intent myIntent = getIntent(); // gets the previously created intent
        questId = myIntent.getStringExtra("questId");

        SQLiteDatabase database = helperQuests.getWritableDatabase();

        if (!questId.equals("create")){
            Cursor cursor = database.query(DBHelperUsers.TABLE_QUESTS, null, "quest_id = ?", new String[]{questId},null,null, null);

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

                nameEdit.setText(cursor.getString(idName));
                amountSeekBar.setProgress(cursor.getInt(idAmount));
                costEdit.setText(String.valueOf(cursor.getInt(idCost)));
                speciffEdit.setText(cursor.getString(idSpeciff));
                diffEdit.setText(String.valueOf(cursor.getInt(idDiff)));
                ageEdit.setText(String.valueOf(cursor.getInt(idAge)));
                intervalEdit.setText(String.valueOf(cursor.getInt(idInterval)));
                createQuestButton.setText("Изменить");

                Toast.makeText(CreateQuestActivity.this, cursor.getString(idName), Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(CreateQuestActivity.this, "Ошибка" , Toast.LENGTH_LONG).show();
            }

            String sqlQuery = "select QC.category_name as catename from categories_quests"
                    + " inner join quest_categories as QC"
                    + " on categories_quests.category = QC.category_id"
                    + " where categories_quests.quest = ?";

            cursor = database.rawQuery(sqlQuery, new String[]{String.valueOf(questId)});

            if (cursor.moveToFirst()){
                int idName = cursor.getColumnIndex("catename");
                List<String> cates = new ArrayList<>();
                do {
                    cates.add(cursor.getString(idName));
                } while (cursor.moveToNext());

                updateCheckbox(cates);
            } else {
                Toast.makeText(CreateQuestActivity.this, "Категории не найдены", Toast.LENGTH_LONG).show();
            }
        }




        Cursor cursor = database.query(DBHelperUsers.TABLE_QTYPES, null, null,null,null,null,null);
        List<String> types = new ArrayList<>();

        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelperUsers.QTYPE_ID);
            int idName = cursor.getColumnIndex(DBHelperUsers.QTYPE_NAME);

            do {
                types.add(cursor.getString(idName));
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(CreateQuestActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
        }
        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, types);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeSpinner.setAdapter(adapter);

        typeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeString = (String)parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                typeString = "";
            }
        });

        amountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                Toast.makeText(CreateQuestActivity.this, String.valueOf(seekBar.getProgress()), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                StringBuilder sb = new StringBuilder("");
                sb.append("Вы выбрали: ");
                sb.append( String.valueOf(seekBar.getProgress()));

                Toast.makeText(CreateQuestActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
                amountInt = seekBar.getProgress();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.quest_create_btn:
                createQuest();
                break;
        }
    }



    private void createQuest() {
        final String questName = nameEdit.getText().toString().trim();
        final String questType = typeString;
        final int questAmount = amountInt;
        final int questCost = Integer.valueOf(costEdit.getText().toString().trim());
        final String questSpeciff = speciffEdit.getText().toString().trim();
        final int questDiff = Integer.valueOf(diffEdit.getText().toString().trim());
        final int questAge = Integer.valueOf(ageEdit.getText().toString().trim());
        final int questInterval = Integer.valueOf(intervalEdit.getText().toString().trim());

        if (questName.isEmpty()){
            nameEdit.setError("Имя должно быть заполнено!");
            nameEdit.requestFocus();
            return;
        }

        if (questType.equals("")){
            Toast.makeText(CreateQuestActivity.this, "Не выбран тип квеста!", Toast.LENGTH_SHORT).show();
            typeSpinner.requestFocus();
            return;
        }


        if (String.valueOf(questCost).isEmpty()){
            costEdit.setError("Укажите стоимость!");
            costEdit.requestFocus();
            return;
        }

        if (questSpeciff.isEmpty()){
            speciffEdit.setError("Опишите ваш квест!");
            speciffEdit.requestFocus();
            return;
        }
        if (String.valueOf(questDiff).isEmpty()){
            diffEdit.setError("Укажите сложность!");
            diffEdit.requestFocus();
            return;
        }

        if (questDiff > 5 || questDiff < 1 ){
            diffEdit.setError("Сложность в диапазоне [1:5]!");
            diffEdit.requestFocus();
            return;
        }

        if (String.valueOf(questAge).isEmpty()){
            ageEdit.setError("Укажите рекомендованный возраст!");
            ageEdit.requestFocus();
            return;
        }

        if (String.valueOf(questInterval).isEmpty()){
            intervalEdit.setError("Укажите длительность квеста!");
            intervalEdit.requestFocus();
            return;
        }

        SQLiteDatabase database = helperQuests.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelperUsers.QUEST_NAME, questName);
        contentValues.put(DBHelperUsers.QUEST_TYPE, questType);
        contentValues.put(DBHelperUsers.QUEST_AMOUNT, questAmount);
        contentValues.put(DBHelperUsers.QUEST_COST, questCost);
        contentValues.put(DBHelperUsers.QUEST_SPECIFICATION, questSpeciff);
        contentValues.put(DBHelperUsers.QUEST_DIFF, questDiff);
        contentValues.put(DBHelperUsers.QUEST_AGE, questAge);
        contentValues.put(DBHelperUsers.QUEST_INTERVAL, questInterval);

        if (questId.equals("create")){
            database.insert(DBHelperUsers.TABLE_QUESTS, null, contentValues);

        }else{
            database.update(DBHelperUsers.TABLE_QUESTS, contentValues, DBHelperUsers.QUEST_ID + " = ?", new String[]{questId});
            database.delete(DBHelperUsers.TABLE_CATEGORIES_QUESTS, DBHelperUsers.CQ_QUEST + " = ?", new String[]{questId});
        }

        database = helperQuests.getWritableDatabase();
        Cursor cursor = database.query(DBHelperUsers.TABLE_QUESTS, null, "quest_name = ?", new String[]{questName},null,null, null);

        int questIdi = -1;

        if (cursor.moveToFirst()){
            int idIndex = cursor.getColumnIndex(DBHelperUsers.QUEST_ID);
            questIdi = cursor.getInt(idIndex);
        } else {
            Toast.makeText(CreateQuestActivity.this, "Ошибка!!!", Toast.LENGTH_LONG).show();
        }

        List<String> cateOfQuest = checkCategories();

        for (int i = 0; i < cateOfQuest.size(); i++){
            contentValues = new ContentValues();

            cursor = database.query(DBHelperUsers.TABLE_QCATEGORIES, null, "category_name = ?", new String[]{cateOfQuest.get(i)},null,null, null);

            int cateId = -1;

            if (cursor.moveToFirst()){
                int idi = cursor.getColumnIndex(DBHelperUsers.QCATEGORY_ID);
                cateId = cursor.getInt(idi);
            } else {
                Toast.makeText(CreateQuestActivity.this, "Ошибка!!!!!!!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
            }

            contentValues.put(DBHelperUsers.CQ_QUEST, questIdi);
            contentValues.put(DBHelperUsers.CQ_CATEGORY, cateId);

            database.insert(DBHelperUsers.TABLE_CATEGORIES_QUESTS, null, contentValues);

            cursor.close();

        }


        helperQuests.close();
        Intent myIntent = new Intent(this, ReadActivity.class);
        myIntent.putExtra("userEmail", "Успешно!");
        startActivity(myIntent);

    }

    private List<String> checkCategories(){
        List<String> categories = new ArrayList<>();
        if (checkBox1.isChecked())
            categories.add(checkBox1.getText().toString());
        if (checkBox2.isChecked())
            categories.add(checkBox2.getText().toString());
        if (checkBox3.isChecked())
            categories.add(checkBox3.getText().toString());
        if (checkBox4.isChecked())
            categories.add(checkBox4.getText().toString());
        if (checkBox5.isChecked())
            categories.add(checkBox5.getText().toString());
        if (checkBox6.isChecked())
            categories.add(checkBox6.getText().toString());
        if (checkBox7.isChecked())
            categories.add(checkBox7.getText().toString());
        if (checkBox8.isChecked())
            categories.add(checkBox8.getText().toString());
        if (checkBox9.isChecked())
            categories.add(checkBox9.getText().toString());
        if (checkBox10.isChecked())
            categories.add(checkBox10.getText().toString());
        if (checkBox11.isChecked())
            categories.add(checkBox11.getText().toString());
        if (checkBox12.isChecked())
            categories.add(checkBox12.getText().toString());
        if (checkBox13.isChecked())
            categories.add(checkBox13.getText().toString());
        if (checkBox14.isChecked())
            categories.add(checkBox14.getText().toString());
        if (checkBox15.isChecked())
            categories.add(checkBox15.getText().toString());
        if (checkBox16.isChecked())
            categories.add(checkBox16.getText().toString());



        return categories;
    }

    private void updateCheckbox(List<String> list){
        for (String s : list){
            if (s.equals(checkBox1.getText()))
                checkBox1.setChecked(true);
            if (s.equals(checkBox2.getText()))
                checkBox2.setChecked(true);
            if (s.equals(checkBox3.getText()))
                checkBox3.setChecked(true);
            if (s.equals(checkBox4.getText()))
                checkBox4.setChecked(true);
            if (s.equals(checkBox5.getText()))
                checkBox5.setChecked(true);
            if (s.equals(checkBox6.getText()))
                checkBox6.setChecked(true);
            if (s.equals(checkBox7.getText()))
                checkBox7.setChecked(true);
            if (s.equals(checkBox8.getText()))
                checkBox8.setChecked(true);
            if (s.equals(checkBox9.getText()))
                checkBox9.setChecked(true);
            if (s.equals(checkBox10.getText()))
                checkBox10.setChecked(true);
            if (s.equals(checkBox11.getText()))
                checkBox11.setChecked(true);
            if (s.equals(checkBox12.getText()))
                checkBox12.setChecked(true);
            if (s.equals(checkBox13.getText()))
                checkBox13.setChecked(true);
            if (s.equals(checkBox14.getText()))
                checkBox14.setChecked(true);
            if (s.equals(checkBox15.getText()))
                checkBox15.setChecked(true);
            if (s.equals(checkBox16.getText()))
                checkBox16.setChecked(true);

        }
    }
}
