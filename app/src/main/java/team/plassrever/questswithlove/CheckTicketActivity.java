package team.plassrever.questswithlove;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CheckTicketActivity extends AppCompatActivity {

    EditText checkEdit;
    TextView resultText;
    Button checkButton;
    DBHelperUsers helperUsers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_ticket);

        checkEdit = findViewById(R.id.ticket_number_edit);
        resultText = findViewById(R.id.ticket_result_text);
        checkButton = findViewById(R.id.check_btn);
        helperUsers = new DBHelperUsers(this);

        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tNumber = checkEdit.getText().toString();
                SQLiteDatabase database = helperUsers.getWritableDatabase();

                Cursor cursor = database.query(DBHelperUsers.TABLE_TICKETS, null, "ticket_code = ?", new String[]{tNumber},null,null, null);

                if (cursor.moveToFirst()){
                    int idCode = cursor.getColumnIndex(DBHelperUsers.TICKET_CODE);
                    int idQuest = cursor.getColumnIndex(DBHelperUsers.TICKET_QUEST);
                    int idDate = cursor.getColumnIndex(DBHelperUsers.TICKET_DATE);
                    int idCost = cursor.getColumnIndex(DBHelperUsers.TICKET_COST);

                    String idQuestSTR = String.valueOf(cursor.getInt(idQuest));

                    Cursor cursor2 = database.query(DBHelperUsers.TABLE_QUESTS, null, "quest_id = ?", new String[]{idQuestSTR},null,null, null);

                    String questName = "";

                    if (cursor2.moveToFirst()){
                        int idName = cursor2.getColumnIndex(DBHelperUsers.QUEST_NAME);
                        questName = cursor2.getString(idName);
                    } else {
                        Toast.makeText(CheckTicketActivity.this, "Ошибка!", Toast.LENGTH_LONG).show();
                    }


                    StringBuilder sb = new StringBuilder("");
                    sb.append("CODE: " + cursor.getString(idCode) + "\n");
                    sb.append("DATE: " + cursor.getString(idDate) + "\n");
                    sb.append("COST: " + cursor.getInt(idCost) + "\n");
                    sb.append("QUEST: " + questName);

                    resultText.setText(sb.toString());
                } else {
                    Toast.makeText(CheckTicketActivity.this, "Билет не найден!", Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
