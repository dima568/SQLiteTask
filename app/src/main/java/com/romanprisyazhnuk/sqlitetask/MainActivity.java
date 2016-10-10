package com.romanprisyazhnuk.sqlitetask;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    final String LOG_TAG = "myLogs";

    Button buttonPut, buttonSelect, buttonDelete;
    EditText etName, etLastName, etAge;
    TextView textView;
    ListView listView;
    DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        buttonPut = (Button) findViewById(R.id.buttonPut);
        buttonPut.setOnClickListener(this);

        buttonSelect = (Button) findViewById(R.id.buttonSelect);
        buttonSelect.setOnClickListener(this);

        buttonDelete = (Button) findViewById(R.id.buttonDelete);
        buttonDelete.setOnClickListener(this);

        etName = (EditText) findViewById(R.id.editTextName);
        etLastName = (EditText) findViewById(R.id.editTextLastName);
        etAge = (EditText) findViewById(R.id.editTextAge);

        textView =(TextView) findViewById(R.id.textView);
        // создаем объект для создания и управления версиями БД
        dbHelper = new DBHelper(this);

    }


    @Override
    public void onClick(View v) {

        ContentValues cv = new ContentValues();

        String name = etName.getText().toString();
        String lastname =  etLastName.getText().toString();
        String  age = etAge.getText().toString();
        // подключаемся к БД
        SQLiteDatabase db = dbHelper.getWritableDatabase();


        switch (v.getId()) {
            case R.id.buttonPut:
                Log.d(LOG_TAG, "--- Insert in newtable: ---");

                cv.put("name", name);
                cv.put("lastname", lastname);
                cv.put("age",age);
                // вставляем запись и получаем ее ID
                long rowID = db.insert("newtable", null, cv);
                Log.d(LOG_TAG, "row inserted, ID = " + rowID);
                break;
            case R.id.buttonSelect:
                Log.d(LOG_TAG, "--- Rows in newtable: ---");
                // делаем запрос всех данных из таблицы newtable, получаем Cursor
                Cursor c = db.query("newtable", null, null, null, null, null, null);

                // ставим позицию курсора на первую строку выборки
                // если в выборке нет строк, вернется false
                if (c.moveToFirst()) {

                    // определяем номера столбцов по имени в выборке
                    int idColIndex = c.getColumnIndex("id");
                    int nameColIndex = c.getColumnIndex("name");
                    int lastnameColIndex = c.getColumnIndex("lastname");
                    int ageColIndex = c.getColumnIndex("age");
                    String textString="";
                    do {
                        // получаем значения по номерам столбцов и пишем все в лог
                        String s ="ID = " + c.getInt(idColIndex) +
                                ", name = " + c.getString(nameColIndex) +
                                ", lastname = " + c.getString(lastnameColIndex)+
                                ", age = " + c.getString(ageColIndex);
                        Log.d(LOG_TAG, s);
                      textString+=s;
                                                                              ;
                        // переход на следующую строку
                        // а если следующей нет (текущая - последняя), то false - выходим из цикла
                    } while (c.moveToNext());
                    textView.setText(textString);
                } else
                    Log.d(LOG_TAG, "0 rows");
                c.close();
                break;
            case R.id.buttonDelete:
                Log.d(LOG_TAG, "--- Clear newtable: ---");
                // удаляем все записи
                int clearCount = db.delete("newtable", null, null);
                Log.d(LOG_TAG, "deleted rows count = " + clearCount);
                break;
        }
        dbHelper.close();
    }





}
