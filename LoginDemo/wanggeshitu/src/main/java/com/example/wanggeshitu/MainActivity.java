package com.example.wanggeshitu;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private int[] picture = new int[]{
            R.drawable.img01, R.drawable.img02, R.drawable.img03, R.drawable.img04, R.drawable.img05,
            R.drawable.img06, R.drawable.img07, R.drawable.img08, R.drawable.img09, R.drawable.img10,
            R.drawable.img11, R.drawable.img12

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = findViewById(R.id.gridView);
        gridView.setAdapter(new ImageAdapter(this));

    }

    public class ImageAdapter extends BaseAdapter{
        private Context mcontext;
        public ImageAdapter(Context c){
            this.mcontext=c;
        }
        @Override
        public int getCount() {
            return picture.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView image ;
            if(convertView ==null){
                image=new ImageView(mcontext);
                image.setLayoutParams(new GridView.LayoutParams(200, 100));
                image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
            else {
                image=(ImageView)convertView;
            }
            image.setImageResource(picture[position]);
            return image;
        }
    }
}
