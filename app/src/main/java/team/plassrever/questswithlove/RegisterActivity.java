package team.plassrever.questswithlove;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText nameEdit;
    private EditText lastNameEdit;
    private EditText emailEdit;
    private EditText phoneNumberEdit;
    private EditText passwordEdit;

    DBHelperUsers helperUsers;

    private ProgressBar progressBar;

    private TextView bannerText;

    private Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        bannerText = findViewById(R.id.banner);
        bannerText.setOnClickListener(this);

        registerBtn = findViewById(R.id.register_btn);
        registerBtn.setOnClickListener(this);

        nameEdit = findViewById(R.id.name_edit);
        lastNameEdit = findViewById(R.id.last_name_edit);
        emailEdit = findViewById(R.id.email_edit);
        phoneNumberEdit = findViewById(R.id.phone_edit);
        passwordEdit = findViewById(R.id.password_edit);

        progressBar = findViewById(R.id.progressbar);

        helperUsers = new DBHelperUsers(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.banner:
                break;
            case R.id.register_btn:
                registerUser();
                break;
        }
    }

    private void registerUser() {

        final String email = emailEdit.getText().toString().trim();
        final String firstName = nameEdit.getText().toString().trim();
        final String lastName = lastNameEdit.getText().toString().trim();
        final String phoneNumber = phoneNumberEdit.getText().toString().trim();
        String password = passwordEdit.getText().toString().trim();

        if (firstName.isEmpty()){
            nameEdit.setError("Имя должно быть заполнено!");
            nameEdit.requestFocus();
            return;
        }

        if (lastName.isEmpty()){
            lastNameEdit.setError("Имя должно быть заполнено!");
            lastNameEdit.requestFocus();
            return;
        }

        if (phoneNumber.isEmpty()){
            phoneNumberEdit.setError("Номер телефона не может быть пустым!");
            phoneNumberEdit.requestFocus();
            return;
        }

        if (phoneNumber.length() != 10){
            phoneNumberEdit.setError("Неккоректно введет номер!");
            phoneNumberEdit.requestFocus();
            return;
        }

        if (email.isEmpty()){
            emailEdit.setError("Email не может быть пустым!");
            emailEdit.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEdit.setError("Введите правильный Email!");
            emailEdit.requestFocus();
            return;
        }

        if (password.isEmpty()){
            passwordEdit.setError("Введите пароль!");
            passwordEdit.requestFocus();
            return;
        }

        if (password.length() < 6){
            passwordEdit.setError("Слишком легкий пароль!");
            passwordEdit.requestFocus();
            return;
        }

        SQLiteDatabase database = helperUsers.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(DBHelperUsers.USER_FNAME, firstName);
        contentValues.put(DBHelperUsers.USER_LNAME, lastName);
        contentValues.put(DBHelperUsers.USER_EMAIL, email);
        contentValues.put(DBHelperUsers.USER_NUMBER, phoneNumber);
        contentValues.put(DBHelperUsers.USER_PASSWORD, password);

        database.insert(DBHelperUsers.TABLE_USERS, null, contentValues);

        Toast.makeText(RegisterActivity.this, "Успешно!", Toast.LENGTH_LONG).show();
        helperUsers.close();

        Intent myIntent = new Intent(this, ReadActivity.class);
        myIntent.putExtra("userEmail", email);
        startActivity(myIntent);

//        progressBar.setVisibility(View.VISIBLE);
//        mAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()){
//                            User user = new User(firstName, lastName, phoneNumber, email);
//
//                            FirebaseDatabase.getInstance().getReference("Users")
//                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
//                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
//                                @Override
//                                public void onComplete(@NonNull Task<Void> task) {
//                                    if (task.isSuccessful()){
//                                        Toast.makeText(RegisterActivity.this, "Пользователь успешно создан!", Toast.LENGTH_LONG).show();
//                                        progressBar.setVisibility(View.GONE);
//                                    } else {
//                                        Toast.makeText(RegisterActivity.this, "Ошибка! Попробуйте снова.", Toast.LENGTH_LONG).show();
//                                        progressBar.setVisibility(View.GONE);
//                                    }
//                                }
//                            });
//                        } else {
//                            Toast.makeText(RegisterActivity.this, "_error_  ", Toast.LENGTH_LONG).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//                    }
//                });

    }
}
