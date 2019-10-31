package com.example.leancherapp;

import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import android.widget.Space;
import android.view.View.OnClickListener;


interface OnClickInterface{
    void onClickImage(View v);
}

public class MainActivity extends AppCompatActivity implements OnClickListener{
    dbHelper dbHelper;
    SQLiteDatabase db;
    Cursor itemCursor;
    LinearLayout goodsLayout;
    EditText itemFilter;
    goodsCard goodsCard;
    static final int REQUEST_TAKE_PHOTO = 1;
    static cameraHelper cameraHelper;
    static Integer imageID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goodsLayout = (LinearLayout) findViewById(R.id.goodsLayout);
        itemFilter = (EditText) findViewById(R.id.itemFilter);
        goodsCard = new goodsCard(this,this, MainActivity::onClickImage);
        dbHelper = new dbHelper(getApplicationContext());
        dbHelper.create_db();
        cameraHelper = new cameraHelper(this, this);
    }

    @Override
    public void onResume() {
        super.onResume();
        goodsLayout.removeAllViews();
        // открываем подключение
        db = dbHelper.open();
        showAllItem();
        showFilteredItem();
    }

    private void showAllItem(){
        itemCursor = db.rawQuery("select * from " + dbHelper.TABLE,null);
        getDataFromDB();
    }

    private void showFilteredItem(){
        itemFilter.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) { }
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            // при изменении текста выполняем фильтрацию
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                goodsLayout.removeAllViews();
                itemCursor = db.rawQuery("select * from " + dbHelper.TABLE + " where " +
                        dbHelper.COLUMN_NAME + " like \"%" + s + "%\"", null);
                getDataFromDB();
            }
        });
    }

    private void getDataFromDB(){
        itemCursor.moveToFirst();
        while (!itemCursor.isAfterLast()){
            showGoodsCard();
            itemCursor.moveToNext();
        }
    }

    private void showGoodsCard(){
        Space space = new Space(this);
        space.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 5));
        goodsLayout.addView(space);
        LinearLayout curGoodsLayout = goodsCard.createGoodsCard(itemCursor);
        goodsLayout.addView(curGoodsLayout);
    }

    // по нажатию на кнопку запускаем GoodsActivity для добавления данных
    public void addGoodsToDB(View view){
        Intent intent = new Intent(this, GoodsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getApplicationContext(), GoodsActivity.class);
        intent.putExtra("id", v.getId());
        startActivity(intent);
    }

    public static void onClickImage(View v){
        imageID = v.getId();
        cameraHelper.dispatchTakePictureIntent();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_PHOTO && resultCode == RESULT_OK) {
            Bitmap bitmap = cameraHelper.showPhoto();
            //Resources res = getResources();
            //Integer resID =  res.getInteger(imageID);
            //ImageView imageView = (ImageView) findViewById(resID);
            //imageView.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        itemCursor.close();
    }
}
