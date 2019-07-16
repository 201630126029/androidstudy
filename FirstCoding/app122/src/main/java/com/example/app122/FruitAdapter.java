package com.example.app122;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * RecycleView的适配器
 * @author xuanqis
 */
public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    /**
     * 上下文和数据源
     */
    private Context mContext;
    private List<Fruit> mFruits;

    class ViewHolder extends RecyclerView.ViewHolder {
        CardView mCardView;
        ImageView fruitImage;
        TextView fruitName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mCardView= (CardView) itemView;
            fruitImage=itemView.findViewById(R.id.fruit_image);
            fruitName=itemView.findViewById(R.id.fruit_name);
        }
    }

    /**
     * 数据一定要传过来
     * @param fruits fruit的list
     */
    public FruitAdapter(List<Fruit> fruits) {
        mFruits = fruits;
    }

    @NonNull
    @Override
    public FruitAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //context只需要从父布局中得到即可
        if(mContext == null){
            mContext=viewGroup.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fruit_item, viewGroup, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //可以直接得到当前的View Holder是第几个ViewHolder，原先我还以为需要在绑定中设置
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruits.get(position);
                Intent intent = new Intent(mContext, FruitActivity.class);
                intent.putExtra(FruitActivity.FRUIT_NAME, fruit.getName());
                intent.putExtra(FruitActivity.FRUIT_IMAGE_ID, fruit.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull FruitAdapter.ViewHolder viewHolder, int i) {
        //绑定应该理解为将ViewHolder和对应的数据进行绑定
        Fruit fruit = mFruits.get(i);
        viewHolder.fruitName.setText(fruit.getName());
        Glide.with(mContext).load(fruit.getImageId()).into(viewHolder.fruitImage);
    }

    @Override
    public int getItemCount() {
        return mFruits.size();
    }
}
