package com.example.leancherapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.content.Context;
import android.widget.TextView;



public class goodsCard{
    Activity activity;
    Context context;
    Cursor itemCursor;
    OnClickInterface onClickInterface;

    public goodsCard(Context context, Activity activity, OnClickInterface onClickInterface){
        this.activity = activity;
        this.context = context;
        this.onClickInterface = onClickInterface;
    }
    public LinearLayout createGoodsCard(Cursor cursor){
        itemCursor = cursor;
        LinearLayout curGoodsLayout = new LinearLayout(context);
        curGoodsLayout.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout descLayout = new LinearLayout(context);
        descLayout.setOrientation(LinearLayout.VERTICAL);
        descLayout.addView(headerItem());
        descLayout.addView(propItem());

        ImageView imgGoods = new ImageView(context);
        imgGoods.setLayoutParams(new LinearLayout.LayoutParams(225, 300));
        imgGoods.setImageResource(R.drawable.tempimg);
        Integer id = itemCursor.getInt(itemCursor.getColumnIndex(dbHelper.COLUMN_ID));
        imgGoods.setId(id);
        imgGoods.setTag(id);
        imgGoods.setClickable(true);

        imgGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickInterface.onClickImage(v);
            }
        });
        curGoodsLayout.addView(descLayout);
        curGoodsLayout.addView(imgGoods);
        return curGoodsLayout;
    }


    private TextView propItem(){
        Integer id = itemCursor.getInt(itemCursor.getColumnIndex(dbHelper.COLUMN_ID));
        String invNum = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_INV_NUM));
        String dateProd = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_PROD));
        String dateReceipt = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_RECEIPT));
        String dateWriteOff = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_DATE_WRITE_OFF));
        String prop = "Инвент. номер: " + invNum + "\n" + "Произведен: " + dateProd + "\n" +
                "Поступил: " + dateReceipt + "\n" + "Списан: " + dateWriteOff;
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(850, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 40);
        textView.setId(id);
        textView.setClickable(true);
        textView.setOnClickListener((View.OnClickListener) context);
        textView.setText(prop);
        return textView;
    }

    private TextView headerItem(){
        Integer id = itemCursor.getInt(itemCursor.getColumnIndex(dbHelper.COLUMN_ID));
        String type = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_TYPE));
        String name = itemCursor.getString(itemCursor.getColumnIndex(dbHelper.COLUMN_NAME));
        String header = type + " " + name;
        TextView textView = new TextView(context);
        textView.setLayoutParams(new LinearLayout.LayoutParams(850, 100));
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,70);
        textView.setText(header);
        textView.setId(id);
        textView.setClickable(true);
        textView.setOnClickListener((View.OnClickListener) context);
        return textView;
    }
}
