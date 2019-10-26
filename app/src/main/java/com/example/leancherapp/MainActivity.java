package com.example.leancherapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{
    com.example.leancherapp.dbHelper dbHelper;
    SQLiteDatabase db;
    Cursor itemCursor;
    LinearLayout rootLayot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rootLayot = (LinearLayout) findViewById(R.id.root_layout);
        dbHelper = new dbHelper(getApplicationContext());
        // создаем базу данных
        dbHelper.create_db();
    }
    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = dbHelper.open();
        itemCursor = db.rawQuery("select * from "+  dbHelper.TABLE, null);
        itemCursor.moveToFirst();
        while (!itemCursor.isAfterLast()){
            showData();
            itemCursor.moveToNext();
        }
    }
    // по нажатию на кнопку запускаем GoodsActivity для добавления данных
    public void add(View view){
        Intent intent = new Intent(this, GoodsActivity.class);
        startActivity(intent);
    }

    public void showData(){
        Integer id = itemCursor.getInt(itemCursor.getColumnIndex(dbHelper.COLUMN_ID));
        String type = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_TYPE));
        String name = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_NAME));
        String header = type + " " + name;
        showHeader(header, id);
        String dateProd = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_PROD));
        String dateReceipt = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_RECEIPT));
        String dateWriteOff = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_WRITE_OFF));
        showProp("Произведен: " + dateProd, id);
        showProp("Поступил: " + dateReceipt, id);
        showProp("Списан: " + dateWriteOff, id);
    }

    public void showProp(String prop, Integer id){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 50));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
        textView.setText(prop);
        textView.setId(id);
        rootLayot.addView(textView);
    }

    public void showHeader(String header,  Integer id){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,80);
        //textView.setTypeface(null, Typeface.BOLD);
        textView.setText(header);
        textView.setId(id);
        //textView.setOnClickListener(onTextViewClick(this.get));
        rootLayot.addView(textView);
    }

    public void onTextViewClick(Integer id) {
        Intent intent = new Intent(getApplicationContext(), GoodsActivity.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        itemCursor.close();
    }
}
