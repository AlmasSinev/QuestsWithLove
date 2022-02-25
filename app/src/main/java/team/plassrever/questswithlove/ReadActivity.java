package team.plassrever.questswithlove;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import team.plassrever.questswithlove.ui.DBHelperQuests;

public class ReadActivity extends AppCompatActivity implements View.OnClickListener{

    private ListView questsList;
    private ArrayAdapter<String> adapter;
    private List<String> list;
    private Button createQuestButton;

    private RadioGroup sortRadioGroup;
    private RadioButton byAgeRadio;
    private RadioButton byDiffRadio;
    private RadioButton byCostRadio;

    private EditText searchEdit;
    private Button searchBtn;

    private String orderBy;

    DBHelperUsers helperQuests;

    List<String> titles;
    List<String> types;
    List<Integer> intervals;
    List<Integer> diffs;

    int[] imgs = {R.drawable.quest_image};

    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        helperQuests = new DBHelperUsers(this);

        sortRadioGroup = findViewById(R.id.sort_radio_group);
        byAgeRadio = findViewById(R.id.radio_by_age);
        byDiffRadio = findViewById(R.id.radio_by_diff);
        byCostRadio = findViewById(R.id.radio_by_cost);

        byAgeRadio.setOnClickListener(radioListener);
        byCostRadio.setOnClickListener(radioListener);
        byDiffRadio.setOnClickListener(radioListener);

        searchEdit = findViewById(R.id.read_search_edit_text);
        searchBtn = findViewById(R.id.read_search_btn);

        SQLiteDatabase database = helperQuests.getWritableDatabase();

        questsList = findViewById(R.id.quests_list);
        createQuestButton = findViewById(R.id.create_quest_btn);
        createQuestButton.setOnClickListener(this);
        list = new ArrayList<>();

        titles = new ArrayList<>();
        types = new ArrayList<>();
        intervals = new ArrayList<>();
        diffs = new ArrayList<>();

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchEdit.getText().toString().length() > 0) {
                    List<Quest> questsByName =  helperQuests.getQuestsLikeName(searchEdit.getText().toString());
                    StringBuilder sb = new StringBuilder("");
                    for (Quest q : questsByName){
                        sb.append(q.name + " \n");
                    }
                    Toast.makeText(ReadActivity.this, sb.toString(), Toast.LENGTH_SHORT).show();
//                    List<String> titlesByName = new ArrayList<>();
//                    List<String> typesByName = new ArrayList<>();
//                    List<Integer> intervalsByName = new ArrayList<>();
//                    List<Integer> diffsByName = new ArrayList<>();
//
//                    for (Quest q : questsByName) {
//                        titlesByName.add(q.name);
//                        typesByName.add(q.type);
//                        intervalsByName.add(q.interval);
//                        diffsByName.add(q.difficulty);
//                    }
//
//                    myAdapter = new MyAdapter(ReadActivity.this, titlesByName, typesByName, intervalsByName, diffsByName, imgs);
//                    questsList.setAdapter(myAdapter);

                } else {
                    Toast.makeText(ReadActivity.this, "Сначала введите название квеста." , Toast.LENGTH_LONG).show();
                }
            }
        });

        Intent myIntent = getIntent(); // gets the previously created intent
        String userEmail = myIntent.getStringExtra("userEmail");
        String[] selectionArgs = new String[]{userEmail};
        Cursor cursor = database.query(DBHelperUsers.TABLE_USERS, null, "user_email = ?", selectionArgs,null,null, null);
        if (cursor.moveToFirst()){
            int idFName = cursor.getColumnIndex(DBHelperUsers.USER_FNAME);
            int idLName = cursor.getColumnIndex(DBHelperUsers.USER_LNAME);
            Toast.makeText(ReadActivity.this, "Добро пожаловать, " + cursor.getString(idFName) + " " + cursor.getString(idLName) , Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(ReadActivity.this, userEmail , Toast.LENGTH_LONG).show();
        }

        cursor = database.query(DBHelperUsers.TABLE_QUESTS, null, null,null,null,null, null);

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

            do {
                StringBuilder sb = new StringBuilder("");
                sb.append(cursor.getInt(idIndex) + ". " + cursor.getString(idName) + "\n");
                sb.append(cursor.getString(idSpeciff)+ "\n");
                sb.append("TYPE: " + cursor.getString(idType) + "\n");
                sb.append("DIFF: " + cursor.getInt(idDiff));

                titles.add(cursor.getString(idName));
                types.add(cursor.getString(idType));
                intervals.add(cursor.getInt(idInterval));
                diffs.add(cursor.getInt(idDiff));

                list.add(sb.toString());
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(ReadActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
        }
        cursor.close();
        helperQuests.close();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        myAdapter = new MyAdapter(this, titles, types, intervals, diffs, imgs);
        questsList.setAdapter(myAdapter);

        questsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView title = view.findViewById(R.id.row_name_text);
                String titleString = title.getText().toString();

                Intent myIntent = new Intent(ReadActivity.this, QuestActivity.class);
                myIntent.putExtra("titleQuest", titleString);
                startActivity(myIntent);
            }
        });
    }

    View.OnClickListener radioListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RadioButton rb = (RadioButton)v;
            switch (rb.getId()){
                case R.id.radio_by_age:
                    orderBy = DBHelperUsers.QUEST_AGE;
                    updateList();
                    break;
                case R.id.radio_by_cost:
                    orderBy = DBHelperUsers.QUEST_COST;
                    updateList();
                    break;
                case R.id.radio_by_diff:
                    orderBy = DBHelperUsers.QUEST_DIFF;
                    updateList();
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.create_quest_btn:
                Intent myIntent = new Intent(this, CreateQuestActivity.class);
                myIntent.putExtra("questId", "create");
                startActivity(myIntent);
                break;
        }
    }

    public void updateList(){
        list = new ArrayList<>();

        titles = new ArrayList<>();
        types = new ArrayList<>();
        intervals = new ArrayList<>();
        diffs = new ArrayList<>();

        SQLiteDatabase database = helperQuests.getWritableDatabase();
        Cursor cursor = database.query(DBHelperUsers.TABLE_QUESTS, null, null,null,null,null, orderBy);

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

            do {
                StringBuilder sb = new StringBuilder("");
                sb.append(cursor.getInt(idIndex) + ". " + cursor.getString(idName) + "\n");
                sb.append(cursor.getString(idSpeciff)+ "\n");
                sb.append("TYPE: " + cursor.getString(idType) + "\n");
                sb.append("DIFF: " + cursor.getInt(idDiff));
                list.add(sb.toString());

                titles.add(cursor.getString(idName));
                types.add(cursor.getString(idType));
                intervals.add(cursor.getInt(idInterval));
                diffs.add(cursor.getInt(idDiff));

            } while (cursor.moveToNext());
        } else {
            Toast.makeText(ReadActivity.this, "Ошибка", Toast.LENGTH_LONG).show();
        }
        cursor.close();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        myAdapter = new MyAdapter(this, titles, types, intervals, diffs, imgs);
        questsList.setAdapter(myAdapter);
    }


    class MyAdapter extends ArrayAdapter<String> {

        Context context;
        List<String> rTitle;
        List<String> rType;
        List<Integer> rInterval;
        int[] rImg;
        List<Integer> rDiffs;


        public MyAdapter(@NonNull Context context, List<String> title, List<String> type, List<Integer> interval, List<Integer> diffs, int[] imgs) {
            super(context, R.layout.row, title);
            this.context = context;
            rTitle = title;
            rType = type;
            rInterval = interval;
            rDiffs = diffs;
            rImg = imgs;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = layoutInflater.inflate(R.layout.row, parent, false);
            ImageView image = row.findViewById(R.id.row_quest_image);
            TextView titleText = row.findViewById(R.id.row_name_text);
            TextView typeText = row.findViewById(R.id.row_type_text);
            TextView intervalText = row.findViewById(R.id.row_interval_text);

            image.setImageResource(rImg[0]);
            titleText.setText(rTitle.get(position));
            typeText.setText(rType.get(position));

            String intervalStr = "Длиельность: " + String.valueOf(rInterval.get(position)) + " мин";
            intervalText.setText(intervalStr);

            ImageView image1 = row.findViewById(R.id.diff_img_1);
            ImageView image2 = row.findViewById(R.id.diff_img_2);
            ImageView image3 = row.findViewById(R.id.diff_img_3);
            ImageView image4 = row.findViewById(R.id.diff_img_4);
            ImageView image5 = row.findViewById(R.id.diff_img_5);

            ImageView[] diffImages = { image5, image4, image3, image2, image1};

            for (int i=0; i< diffs.get(position); i++){
                diffImages[i].setImageResource(R.mipmap.ic_locked);
            }

            return row;
        }
    }
}
