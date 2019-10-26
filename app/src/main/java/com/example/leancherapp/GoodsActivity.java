package com.example.leancherapp;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class GoodsActivity extends AppCompatActivity {

    EditText nameBox;
    EditText typeBox;
    EditText dateProdBox;
    EditText dateReceiptBox;
    EditText dateWriteOffBox;
    Button delButton;
    Button saveButton;

    dbHelper sqlHelper;
    SQLiteDatabase db;
    Cursor itemCursor;
    long itemId =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods);

        nameBox = (EditText) findViewById(R.id.name);
        typeBox = (EditText) findViewById(R.id.type);
        dateProdBox = (EditText) findViewById(R.id.dateProd);
        dateReceiptBox = (EditText) findViewById(R.id.dateReceipt);
        dateWriteOffBox = (EditText) findViewById(R.id.dateWriteOff);

        delButton = (Button) findViewById(R.id.deleteButton);
        saveButton = (Button) findViewById(R.id.saveButton);

        sqlHelper = new dbHelper(this);
        db = sqlHelper.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            itemId = extras.getLong("id");
        }
        // если 0, то добавление
        if (itemId > 0) {
            // получаем элемент по id из бд
            itemCursor = db.rawQuery("select * from " + dbHelper.TABLE + " where " +
                    dbHelper.COLUMN_ID + "=?", new String[]{String.valueOf(itemId)});
            itemCursor.moveToFirst();
            nameBox.setText(itemCursor.getString(1));
            typeBox.setText(itemCursor.getString(2));
            dateProdBox.setText(itemCursor.getString(3));
            dateReceiptBox.setText(itemCursor.getString(4));
            dateWriteOffBox.setText(itemCursor.getString(5));
            itemCursor.close();
        } else {
            // скрываем кнопку удаления
            delButton.setVisibility(View.GONE);
        }
    }
    public void saveItem(View view){
        ContentValues cv = new ContentValues();
        cv.put(dbHelper.COLUMN_NAME, nameBox.getText().toString());
        cv.put(dbHelper.COLUMN_TYPE, typeBox.getText().toString());
        cv.put(dbHelper.COLUMN_DATE_PROD, dateProdBox.getText().toString());
        cv.put(dbHelper.COLUMN_DATE_RECEIPT, dateReceiptBox.getText().toString());
        cv.put(dbHelper.COLUMN_DATE_WRITE_OFF, dateWriteOffBox.getText().toString());

        if (itemId > 0) {
            db.update(dbHelper.TABLE, cv, dbHelper.COLUMN_ID + "=" + String.valueOf(itemId), null);
        } else {
            db.insert(dbHelper.TABLE, null, cv);
        }
        goMain();
    }

    public void delete(View view){
        db.delete(dbHelper.TABLE, "_id = ?", new String[]{String.valueOf(itemId)});
        goMain();
    }

    private void goMain(){
        // закрываем подключение
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }
}
