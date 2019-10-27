package com.example.leancherapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;
import android.view.View.OnClickListener;

public class MainActivity extends AppCompatActivity implements OnClickListener{
    com.example.leancherapp.dbHelper dbHelper;
    SQLiteDatabase db;
    Cursor itemCursor;
    LinearLayout goodsLayout;
    EditText itemFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goodsLayout = (LinearLayout) findViewById(R.id.goodsLayout);
        itemFilter = (EditText) findViewById(R.id.itemFilter);

        dbHelper = new dbHelper(getApplicationContext());
        dbHelper.create_db();
    }

    @Override
    public void onResume() {
        super.onResume();
        goodsLayout.removeAllViews();
        // открываем подключение
        db = dbHelper.open();
        itemCursor = db.rawQuery("select * from "+  dbHelper.TABLE, null);
        //String[] goodsList = getGoodsList();
        itemCursor.moveToFirst();
        while (!itemCursor.isAfterLast()){
            showData();
            itemCursor.moveToNext();
        }

    }

    private String[] getGoodsList(){
        int itemsCount = itemCursor.getCount();
        int colCount = itemCursor.getColumnCount();
        String[] goodsList = new String[itemsCount];
        return goodsList;
    }

    // по нажатию на кнопку запускаем GoodsActivity для добавления данных
    public void add(View view){
        Intent intent = new Intent(this, GoodsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        String id = String.valueOf(v.getId());
        Intent intent = new Intent(getApplicationContext(), GoodsActivity.class);
        intent.putExtra("id", v.getId());
        startActivity(intent);
    }

    public void showData(){
        Integer id = itemCursor.getInt(itemCursor.getColumnIndex(dbHelper.COLUMN_ID));
        String type = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_TYPE));
        String name = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_NAME));
        String header = type + " " + name;
        showHeader(header, id);
        String invNum = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_INV_NUM));
        String dateProd = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_PROD));
        String dateReceipt = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_RECEIPT));
        String dateWriteOff = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_WRITE_OFF));
        String prop = "Инвент. номер: " + invNum + "\n" + "Произведен: " + dateProd + "\n" +
                "Поступил: " + dateReceipt + "\n" + "Списан: " + dateWriteOff;
        showProp(prop, id);
    }

    public void showProp(String prop, Integer id){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
        textView.setId(id);
        textView.setClickable(true);
        textView.setOnClickListener(this);
        textView.setText(prop);
        goodsLayout.addView(textView);
    }

    public void showHeader(String header,  Integer id){
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 100));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,80);
        //textView.setTypeface(null, Typeface.BOLD);
        textView.setText(header);
        textView.setId(id);
        textView.setClickable(true);
        textView.setOnClickListener(this);
        goodsLayout.addView(textView);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        itemCursor.close();
    }
}
