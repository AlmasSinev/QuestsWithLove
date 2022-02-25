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
                    Toast.makeText(this, "Открывается окно проверки билетов.", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(this, CheckTicketActivity.class);
                    startActivity(myIntent);
                }
                break;
            case R.id.forgot_password_text:
                Toast.makeText(this, "Не реализовано", Toast.LENGTH_SHORT).show();
                break;
            case R.id.sign_in_btn:
                final String email = emailEdit.getText().toString().trim();
                final String password = passwordEdit.getText().toString().trim();

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
                    Toast.makeText(MainActivity.this, "Пользователь не найден!", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(MainActivity.this, "Успешно!", Toast.LENGTH_LONG).show();
                    Intent myIntent = new Intent(this, ReadActivity.class);
                    myIntent.putExtra("userEmail", userEmail);
                    startActivity(myIntent);
                } else{
                    Toast.makeText(MainActivity.this, "Возможно, вы неправильно ввели данные", Toast.LENGTH_LONG).show();
                }
        }
    }
}

/**
 *
 * public User[] users1 = {
 *             new User(1, "Ильдар", "Синев", "iildarado@gmail.com", "9600860729", "123456"),
 *             new User(2, "Рената", "Шаймухаметова", "nataride@gmail.com", "9036667389", "000000"),
 *             new User(3, "Катя", "Ижеметьева","rina@mail.ru", "9067652938", "202020"),
 *             new User(4, "Ильназ", "Лукманов", "lycka@gmail.com", "9631234567", "81378321"),
 *             new User(5, "Рамиль", "Камалов", "ramakama@gmail.com", "9600664567", "31684901")
 *     };
 *
 *
 *                      String[] types = { "Стандартный", "VR-квест", "Одиночный", "На двоих", "Перформанс", "Перформанс на двоих", "Одиночный перформанс" };
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
 *                     Toast.makeText(this, "Успешный успех.", Toast.LENGTH_SHORT).show();
 *                     String[] categories = {"Для взрослых", "Страшное", "Бросающие в дрожь", "Детское", "С актерами", "Для большой компании", "Топ 10", "Мистические", "Будущее", "Высокий IQ", "На скорость", "Историческое", "Атмосферное", "Хороший сюжет", "Веселое", "Юрский период", "Логическое" };
 *                     for (int i = 0; i < categories.length; i++) {
 *                         ContentValues contentValues = new ContentValues();
 *
 *                         contentValues.put(DBHelperUsers.QCATEGORY_ID, i);
 *                         contentValues.put(DBHelperUsers.QCATEGORY_NAME, categories[i]);
 *
 *                         database.insert(DBHelperUsers.TABLE_QCATEGORIES, null, contentValues);
 *                     }
 *
 *                     Toast.makeText(this, "Успешный успех2.", Toast.LENGTH_SHORT).show();
 *                     String[] methods = {"YooMoney", "Сбербанк", "Тинькофф", "WebMoney", "Qiwi", "Альфа-Банк", "Visa", "Mastercard", "Яндекс.Деньги", "PayPal"};
 *                     for (int i = 0; i < methods.length; i++) {
 *                         ContentValues contentValues = new ContentValues();
 *
 *                         contentValues.put(DBHelperUsers.PMETHOD_ID, i);
 *                         contentValues.put(DBHelperUsers.PMETHOD_NAME, methods[i]);
 *
 *                         database.insert(DBHelperUsers.TABLE_PAYMENT_METHODS, null, contentValues);
 *                     }
 *                     Toast.makeText(this, "Успешный успех3.", Toast.LENGTH_SHORT).show();
 *
 *                     String[][] discounts = {
 *                             {"Любитель загадок", "5", "Скидка 5% для игроков, учавствующих в наших квестах более 5 раз."},
 *                             {"Настоящий детектив", "10", "Скидка 10% для игроков, учавствующих в наших квестах более 10 раз."},
 *                             {"День рождения", "15", "Скидка 15% для именинников. Акция действует  3 дня до и  3 дня после дня рождения по паспорту."},
 *                             {"Черная пятница", "20", "Скидка 20% с 23 по 29 ноября."},
 *                             {"Наш Юбилей", "25", "Скидка 25% в честь нашего праздника."}};
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
 *                             new Quest(1, "Зловещие мертвецы", types[4], 4, 2750, "Вам вскоре предстоит важная покупка в жизни. Вы покупаете дом своей мечты, и вот невероятное везение – нашлось жилище, которое гораздо больше и просторнее того, что вы бы могли себе позволить на свои сбережения. По описанию и фотографиям показалось, что это отличный вариант. Тогда вы решаете посмотреть этот особняк вместе с риэлтором, и, возможно, именно вы – его новые владельцы...\n" +
 *                                                  "Но у этого мрачного дома на окраине города в непроходимой глуши своя легенда, он хранит в себе что-то потустороннее, многие называют его колыбелью смертей. У него своя история, которую вам стоило бы изучить еще до покупки. Все, кто хоть недолго жил в этой развалине, полной легенд, стремятся навсегда ее покинуть. Однако это невозможно, так как в самом ее сердце спрятана страшная тайна, из-за которой в темных углах обители до сих пор ютится неведомое зло, нуждающееся в убийствах, чтобы жить...", 3, 16, 60),
 *                             new Quest(2, "Проклятие монастыря", types[0], 4, 2000, "В давние времена она отреклась от обета и согрешила с монахом. Они не смогли это скрыть, они не успели сбежать. Любовь завела их в могилу. Никто до сих пор не знает его имени, зато ее имя знают все...", 4, 14, 45),
 *                             new Quest(3, "Нечто", types[2], 1, 1500, "«Нечто» – это шоу-перформанс, где каждый сантиметр пространства, в которое попадет ваша команда, пропитается вашими страхами. На протяжении всей игры вас будет сопровождать потустороннее присутствие. Вы станете заложниками гнилого подвала до тех пор, пока не найдете способ изгнания нечто из демонов, живущих здесь, которыми руководит страшный кукловод. Боитесь ли вы демонов? Готовы встретиться с неведомым лицом к лицу?! В таком случае вам придется играть по их правилам...", 5, 18, 60),
 *                             new Quest(4, "Черное золото", types[3], 2, 1500, "Где-то в недрах лабораторных коридоров слышно, как приходят в движение липкие ядовитые челюсти и острые конечности, возвещая о пробуждении черной вдовы…\n" +
 *                                     "Гениальный биолог в попытке спасти умирающую от неизлечимой болезни жену привил ей гены ядовитого паука черная вдова. Это было роковой ошибкой. Надежды на выздоровление обернулись кошмаром: женщина превратилась в кровожадную паучиху, потеряв человеческий облик и разум.\n" +
 *                                     "Опасаясь быть съеденным супругой, ученый отправляет в недра лаборатории своих студентов, рассчитывая на их помощь в устранении последствий жуткого эксперимента или планируя хотя бы утолить голод черной вдовы.\n" +
 *                                     "И теперь вам придется исправлять ошибки вашего профессора. Обратного пути нет.", 3, 14, 45),
 *                             new Quest(5, "Код да Винчи", types[0], 4, 2000, "В одном из заброшенных замков на юге Франции группа исследователей обнаружила комнату Леонардо да Винчи. В средневековой атмосфере, среди потайных комнат, незаконченных изобретений и рукописей гения вам предстоит решить множество загадок. Священный Грааль находится в одной из потайных комнат замка, но чтобы обнаружить его, вам потребуются смекалка и острый ум. Двери закрываются за вами, и приключение начинается. Сможете ли вы стать победителем в этом приключении, покажет только время...", 5, 14, 90),
 *                             new Quest(6, "Kepler-68", types[1], 4, 2500, "2098 год, на Земле бушует ужасный вирус, способный уничтожить все человечество.\n" +
 *                                     "В спешном порядке была организована научная группа, оснащенная по последнему слову техники, с целью разработать вакцину на основе ДНК микроорганизмов даргопутов, обитающих на экзопланете Kepler-186.\n" +
 *                                     "Экспедиция была рассчитана на 120 дней, но вскоре был получен сигнал SOS из лагеря, и связь оборвалась. По данным, полученным в результате анализа бортовых журналов ученых, мы пришли к выводу, что вакцина была изобретена, но что-то неизвестное атаковало людей и превратило их в монстров.\n" +
 *                                     "На орбите экзопланеты Kepler-186 находится научный крейсер «Союз 4», на котором хранится резервный ключ для доступа в хранилище защищенного уровня 5, где, по версии разведки, и находится готовая вакцина.\n" +
 *                                     "Вы – межгалактический спецназ, призванный добыть разработанную вакцину любыми способами. Именно от вас зависит судьба всего человечества...", 5, 18, 30),
 *                             new Quest(7, "Тьма", types[1], 4, 2000, "Ужас больше не прячется под кроватью или в шкафу, он выбирает более изощренный способ запугать свою жертву. Ужас приглашает вас на спиритический сеанс в старом доме, в недрах которого таится древнее и бесконечное зло. Когда колдун призовет духов, врата загробного мира распахнутся и нечисть хлынет из темных углов – тогда начнется жуткое приключение, вселяющее трепет и страх своей непредсказуемостью, обостряя чувства тревожной, гротескной атмосферой потустороннего.\n" +
 *                                     "В 1919 году во время эпидемии на этом месте мой муж построил большой отель для всех нуждающихся и умирающих от чумы и голода людей. Двери его были открыты всем. Здесь они доживали свои последние дни и умирали семьями, так как чума не щадила никого. И вот уже прошло более полувека, как муж ушел на войну и оставил меня с двумя детьми, и мне кажется, я начала медленно сходить с ума…", 3, 16, 60),
 *                             new Quest(8, "Отель", types[0], 6, 3000, "В 1919 году во время эпидемии на этом месте мой муж построил большой отель для всех нуждающихся и умирающих от чумы и голода людей. Двери его были открыты всем. Здесь они доживали свои последние дни и умирали семьями, так как чума не щадила никого. И вот уже прошло более полувека, как муж ушел на войну и оставил меня с двумя детьми, и мне кажется, я начала медленно сходить с ума…", 5, 18, 80),
 *                             new Quest(9, "Десять негритят", types[5], 2, 1500, "1934 год. Англия.\n" +
 *                                     "Загадочное убийство на Негритянском острове. Найдено десять трупов.\n" +
 *                                     "В старинном особняке произошла серия таинственных убийств. Прибывшие на остров инспектор Мейн и сэр Томас Лэгг обнаружили 10 бездыханных тел. Это были восемь прежде незнакомых между собой людей и одна супружеская пара. Все они были приглашены сюда мистером и миссис Оним. Большего они узнать не смогли, ведь все возможные версии противоречат фактам. Вы – группа энтузиастов, решили взять дело в свои руки и, во что бы то ни стало, решили восстановить хронологию событий, разобраться, кто и как был убит, а также вычислить убийцу.", 3, 18, 60),
 *                             new Quest(10, "Инферно", types[6], 1, 3000, "Вас с друзьями объединяет одно хобби, связанное с исследованием аномальных зон, неопознанных явлений и даже НЛО.\n" +
 *                                     "В один день человек из вашей команды пропадает. В руках у вас его незаконченное исследование новой аномальной зоны. Это место сильно отличается от всех подобных. Время там преломляется, и один час равен пяти суткам. Более того, согласно добытой информации, там есть разумная жизнь нечеловеческого характера. Он назвал это место «Инферно».\n" +
 *                                     "Он писал в своем дневнике об артефактах, которые помогут выбраться. Но бедолага исчез и, видимо, именно там.\n" +
 *                                     "Недолго думая, вы собираетесь и отправляетесь по карте к тому месту, где и было найдено злополучное Инферно.", 4, 16, 60)
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
