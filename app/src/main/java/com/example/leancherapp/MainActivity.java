package com.example.leancherapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Space;
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
        showAllItem();
        itemFilter.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            // при изменении текста выполняем фильтрацию
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                goodsLayout.removeAllViews();
                itemCursor = db.rawQuery("select * from " + dbHelper.TABLE + " where " +
                            dbHelper.COLUMN_NAME + " like \"%" + s + "%\"", null);
                itemCursor.moveToFirst();
                while (!itemCursor.isAfterLast()){
                    createGoodsCard();
                    itemCursor.moveToNext();
                }
            }
        });
    }

    private void showAllItem(){
        itemCursor = db.rawQuery("select * from " + dbHelper.TABLE,null);
        itemCursor.moveToFirst();
        while (!itemCursor.isAfterLast()){
            createGoodsCard();
            itemCursor.moveToNext();
        }
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

    private void createGoodsCard(){
        LinearLayout curGoodsLayout = new LinearLayout(this);
        curGoodsLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout descLayout = new LinearLayout(this);
        descLayout.setOrientation(LinearLayout.VERTICAL);
        descLayout.addView(headerItem());
        descLayout.addView(propItem());

        ImageView imgGoods = new ImageView(this);
        imgGoods.setLayoutParams(new LinearLayout.LayoutParams(225, 300));
        imgGoods.setImageResource(R.drawable.tempimg);
        Integer id = itemCursor.getInt(itemCursor.getColumnIndex(dbHelper.COLUMN_ID));
        imgGoods.setId(id);
        imgGoods.setClickable(true);
        imgGoods.setOnClickListener(this);

        Space space = new Space(this);
        space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));

        curGoodsLayout.addView(descLayout);
        curGoodsLayout.addView(imgGoods);
        goodsLayout.addView(space);
        goodsLayout.addView(curGoodsLayout);
    }

    private TextView propItem(){
        Integer id = itemCursor.getInt(itemCursor.getColumnIndex(dbHelper.COLUMN_ID));
        String invNum = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_INV_NUM));
        String dateProd = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_PROD));
        String dateReceipt = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_RECEIPT));
        String dateWriteOff = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_WRITE_OFF));
        String prop = "Инвент. номер: " + invNum + "\n" + "Произведен: " + dateProd + "\n" +
                "Поступил: " + dateReceipt + "\n" + "Списан: " + dateWriteOff;
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(850, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
        textView.setId(id);
        textView.setClickable(true);
        textView.setOnClickListener(this);
        textView.setText(prop);
        return textView;
    }

    private TextView headerItem(){
        Integer id = itemCursor.getInt(itemCursor.getColumnIndex(dbHelper.COLUMN_ID));
        String type = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_TYPE));
        String name = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_NAME));
        String header = type + " " + name;
        TextView textView = new TextView(this);
        textView.setLayoutParams(new LinearLayout.LayoutParams(850, 100));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,70);
        textView.setText(header);
        textView.setId(id);
        textView.setClickable(true);
        textView.setOnClickListener(this);
        return textView;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        itemCursor.close();
    }
}
