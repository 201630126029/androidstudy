package com.example.wyc_3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AlbumActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private List<Map<String,Object>> mDataList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);

        recyclerView = (RecyclerView)findViewById(R.id.news_list);
        // 设置LinearLayoutManager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 设置ItemAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        // 设置适配器， 建立RecyclerView和数据之间的关联
        mDataList = getData();
        //recyclerView.setAdapter(new NewsListAdapter(this,mDataList));
        recyclerView.setAdapter(new NewsListAdapterME(this,mDataList));
    }

    private List<Map<String, Object>> getData(){
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("news_title","毡帽系列");
        map.put("news_info","此系列服装有点cute， 像不像小车夫。");
        map.put("news_thumb",R.drawable.i1);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("news_title","蜗牛系列");
        map.put("news_info","宝宝变成了小蜗牛， 爬啊爬啊爬啊。");
        map.put("news_thumb",R.drawable.i2);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("news_title","小蜜蜂系列");
        map.put("news_info","小蜜蜂， 嗡嗡嗡， 飞到西， 飞到东。");
        map.put("news_thumb",R.drawable.i3);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("news_title","毡帽系列");
        map.put("news_info","此系列服装有点cute， 像不像小车夫。");
        map.put("news_thumb",R.drawable.i4);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("news_title","蜗牛系列");
        map.put("news_info","宝宝变成了小蜗牛， 爬啊爬啊爬啊。");
        map.put("news_thumb",R.drawable.i5);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("news_title","小蜜蜂系列");
        map.put("news_info","小蜜蜂， 嗡嗡嗡， 飞到西， 飞到东。");
        map.put("news_thumb",R.drawable.i6);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("news_title","毡帽系列");
        map.put("news_info","此系列服装有点cute， 像不像小车夫。");
        map.put("news_thumb",R.drawable.i4);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("news_title","蜗牛系列");
        map.put("news_info","宝宝变成了小蜗牛， 爬啊爬啊爬啊。");
        map.put("news_thumb",R.drawable.i5);
        list.add(map);
        map = new HashMap<String, Object>();
        map.put("news_title","小蜜蜂系列");
        map.put("news_info","小蜜蜂， 嗡嗡嗡， 飞到西， 飞到东。");
        map.put("news_thumb",R.drawable.i6);
        list.add(map);
        return list;
    }
}
