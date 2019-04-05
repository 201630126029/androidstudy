package com.example.wyc_3;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Map;

public class NewsListAdapterME extends RecyclerView.Adapter<NewsListAdapterME.ViewHolder> {

    private Context mContext;
    private  List<Map<String,Object>> mDataList;
    private LayoutInflater mLayoutInflater;
    public NewsListAdapterME(Context context,List<Map<String,Object>> mDataList ) {
        mContext = context;
        this.mDataList = mDataList;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = mLayoutInflater.inflate(R.layout.cardview_bt,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.news_thumb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Map<String,Object> entity =mDataList.get(position);
//                Toast.makeText(view.getContext(),entity.get("news_title").toString()+"image",Toast.LENGTH_SHORT).show();
                Toast.makeText(view.getContext(),"相册详情",Toast.LENGTH_SHORT).show();
            }
        });
        holder.news_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Map<String,Object> entity =mDataList.get(position);
//                Toast.makeText(view.getContext(),entity.get("news_title").toString()+"view",Toast.LENGTH_SHORT).show();
                Toast.makeText(view.getContext(),"相册详情",Toast.LENGTH_SHORT).show();
            }
        });
        holder.news_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                int position = holder.getAdapterPosition();
                Toast.makeText(view.getContext(),"添加成功",Toast.LENGTH_SHORT).show();
//                showInfo(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Map<String,Object> entity = mDataList.get(position);
        if (null == entity)
            return;
        //ViewHolder viewHolder = (ViewHolder)holder;
        holder.news_title.setText(entity.get("news_title").toString());
        holder.news_info.setText(entity.get("news_info").toString());
        holder.news_thumb.setImageResource(Integer.parseInt(entity.get("news_thumb").toString()));
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void showInfo(int position){
        new AlertDialog.Builder(mContext)
                .setTitle(mDataList.get(position).get("news_title").toString())
                .setMessage(mDataList.get(position).get("news_info").toString())
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView news_title;
        ImageView news_thumb;
        TextView news_info;
        View news_container;
        ImageButton news_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            news_title = (TextView)itemView.findViewById(R.id.news_title);
            news_info = (TextView)itemView.findViewById(R.id.news_info);
            news_thumb = (ImageView)itemView.findViewById(R.id.news_thumb);
            news_container = (View)itemView.findViewById(R.id.news_container);
            news_btn = (ImageButton)itemView.findViewById(R.id.news_btn);
            itemView.findViewById(R.id.news_container).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                    //int position = getAdapterPosition();
                   // Map<String,Object> entity =mDataList.get(position);
                   // Toast.makeText(view.getContext(),entity.get("news_Title").toString(),Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
