package com.example.home_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class NewsContentFragment extends Fragment {
    /**
     * 内容对应的那部分的View
     */
    private View mView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_news_content, container, false);
        return mView;
    }

    /**
     * 设置平板的右边的布局可见，并且对标题和内容进行设置
     * @param newsTitle 新闻标题
     * @param newsContent 新闻内容
     */
    public void refresh(String newsTitle, String newsContent){
        View visibilityLayout = mView.findViewById(R.id.visibility_layout);
        //设置右边的content可见， 布局中设置的是不可见
        visibilityLayout.setVisibility(View.VISIBLE);

        TextView newsTitleText = mView.findViewById(R.id.news_title);
        TextView newsContentText = mView.findViewById(R.id.news_content);

        newsTitleText.setText(newsTitle);
        newsContentText.setText(newsContent);

    }
}