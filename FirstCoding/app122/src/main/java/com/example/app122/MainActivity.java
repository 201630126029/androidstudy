package com.example.app122;

import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity {
    /**
     * 滑动窗口、刷新工具
     */
    private DrawerLayout mDrawerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private Fruit[] mFruits = {
            new Fruit("Apple", R.drawable.apple),
            new Fruit("Banana", R.drawable.banana),
            new Fruit("Pear", R.drawable.pear),
            new Fruit("Pineapple", R.drawable.pineapple),
            new Fruit("Cherry", R.drawable.cherry),
            new Fruit("Watermelon", R.drawable.watermelon),
            new Fruit("Grape", R.drawable.grape),
            new Fruit("Strawberry", R.drawable.strawberry),
            new Fruit("Mango", R.drawable.mango),
            new Fruit("Orange", R.drawable.orange)
    };
    private List<Fruit> mFruitsList = new ArrayList<>();
    private FruitAdapter mFruitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //这里一定要将ActionBar换成Toolbar才能达到Material design的效果
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawerLayout = findViewById(R.id.drawerlayout);
        final NavigationView navView = findViewById(R.id.nav_view);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            //左上的按钮叫做HomeAsUp按钮，设置其可见
            actionBar.setDisplayHomeAsUpEnabled(true);
//            设置按钮的图标
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //悬浮按钮
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * snackbar跟Toast差不多，多了一个按钮功能
                 */
                Snackbar.make(v, "Data delete", Snackbar.LENGTH_LONG)
                        .setAction("Undo", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(MainActivity.this, "Data restored",
                                        Toast.LENGTH_LONG).show();
                            }
                        })
                        .show();
            }
        });
        //设置call为默认选定的
        navView.setCheckedItem(R.id.nav_call);
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //初始化数据并设置RecycleView
        initFruit();
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(layoutManager);
        mFruitAdapter = new FruitAdapter(mFruitsList);
        recyclerView.setAdapter(mFruitAdapter);

        //设置下拉刷新
        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshFruit();
            }
        });
    }

    /**
     * 进行下拉刷新的具体操作
     */
    private void refreshFruit() {
        MyApplication.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initFruit();
                        mFruitAdapter.notifyDataSetChanged();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /**
         * 将编写的菜单设置为系统的菜单
         */
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /**
         * 菜单的处理方法
         */
        switch (item.getItemId()) {
            //系统又默认的id
            case android.R.id.home:
                //直接start滑动
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.backup:
                Toast.makeText(MainActivity.this, "点击了backup", Toast.LENGTH_LONG).show();
                break;
            case R.id.delete:
                Toast.makeText(MainActivity.this, "点击了delete", Toast.LENGTH_LONG).show();
                break;
            case R.id.settings:
                Toast.makeText(MainActivity.this, "点击了settings", Toast.LENGTH_LONG).show();
                break;

            default:
                break;
        }
        return true;
    }


    private void initFruit() {
        mFruitsList.clear();
        for (int i = 0; i < 50; i++) {
            Random random = new Random();
            int index = random.nextInt(mFruits.length);
            mFruitsList.add(mFruits[index]);
        }
    }
}
