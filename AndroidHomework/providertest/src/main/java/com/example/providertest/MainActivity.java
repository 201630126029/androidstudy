package com.example.providertest;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String newId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button addData=findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
                ContentValues values=new ContentValues();
                values.put("name", "书1");
                values.put("author", "书1");
                values.put("pages", 1024);
                values.put("price", 22.85);
                Uri newuri = getContentResolver().insert(uri, values);
                newId=newuri.getPathSegments().get(1);
                Log.i("xuanqisnow", "添加一个数据应该是结束了");
            }
        });
        Button queryData=findViewById(R.id.query_data);
        queryData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book");
                Cursor cursor=getContentResolver().query(uri, null, null, null, null);
                if(cursor != null && cursor.moveToFirst()){
                    do{
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));

                        Log.i("xuanqisnow", "name is "+name);
                        Log.i("xuanqisnow", "author is "+author);
                        Log.i("xuanqisnow", "pages is "+pages);
                        Log.i("xuanqisnow", "price is "+price);
                    }while(cursor.moveToNext());
                }
                Log.i("xuanqisnow", "查询数据应该是结束了");
            }
        });

        Button updateData=findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book"+newId);
                ContentValues values=new ContentValues();
                values.put("name", "更新后的书");
                values.put("pages", 1216);
                values.put("price", 111);
                getContentResolver().update(uri, values, null, null);
                Log.i("xuanqisnow", "更新一个数据应该是结束了");
            }
        });
        Button deleteData=findViewById(R.id.delete_data);
        deleteData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("content://com.example.databasetest.provider/book"+newId);
                getContentResolver().delete(uri, null, null);
                Log.i("xuanqisnow", "删除一个数据应该是结束了");
            }
        });

    }
}
