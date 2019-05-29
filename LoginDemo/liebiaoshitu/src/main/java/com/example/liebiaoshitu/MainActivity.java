package com.example.liebiaoshitu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.listView);
        int imageId[] = new int[]{
                R.mipmap.img01,R.mipmap.img02,R.mipmap.img03,R.mipmap.img04,R.mipmap.img05,
                R.mipmap.img06,R.mipmap.img07,R.mipmap.img08,R.mipmap.img09
        };
        String title[] = new String[]{
                "1游泳", "2方法", "3放到", "4搜索", "5让人", "6娃娃", "7vv", "8谷歌", "9啊啊"
        };
        List<Map<String, Object>> listItems = new ArrayList<>();
        for (int i = 0; i < imageId.length; i++) {
            Map<String, Object> map = new HashMap<>();
            map.put("image", imageId[i]);
            map.put("name", title[i]);
            listItems.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this,listItems, R.layout.main, new String[]{
                "name", "image"
        },new int[]{
               R.id.title,  R.id.image
        });
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Map<String, Object> map = (Map<String, Object>)parent.getItemAtPosition(position);
                Toast.makeText(MainActivity.this, map.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
