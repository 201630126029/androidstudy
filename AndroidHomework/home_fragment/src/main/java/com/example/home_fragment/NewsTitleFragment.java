package com.example.home_fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class NewsTitleFragment extends Fragment {

    /**
     * 为什么放在这个类里，回过头来看一下
     * 当前所在的这个Fragment，如果是一版的话，就直接渲染一版，如果是两版，就需要调用对右版的刷新
     * 实际上以左边的新闻列表为基本的，对一版和两版分别处理
     */
    private boolean isTwoPane;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_title, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.news_title_recycleView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        NewsAdapter newsAdapter = new NewsAdapter(getNews());
        recyclerView.setAdapter(newsAdapter);
        return view;
    }

    /**
     * 在Activity的视图创建完成后要判断一下是一版还是两版
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity().findViewById(R.id.news_content_layout) != null) {
            Log.i("xuanqisnow", "istwo");
            isTwoPane = true;
        } else {
            Log.i("xuanqisnow", "notwo");
            isTwoPane = false;
        }
    }


    private List getNews(){
        List<News> newsList = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            News news = new News();
            news.setTitle("This is newsTitle "+(i+1));
            news.setContent(getRandonLengthContent("This is newsTitle "+(i+1)+". "));
            newsList.add(news);
        }
        return newsList;
    }

    private String getRandonLengthContent(String content){
        Random random = new Random();
        int length = random.nextInt(20)+1;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(content);
        }
        return sb.toString();
    }
    class NewsAdapter extends RecyclerView.Adapter {
        private List<News> mNewsList;

        class ViewHolder extends RecyclerView.ViewHolder {
            /**
             * 每个选项Title的文字的View
             */
            TextView newsTitleText;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                newsTitleText = itemView.findViewById(R.id.news_title);
            }
        }

        public NewsAdapter(List<News> newsList) {
            mNewsList = newsList;
        }

        /**
         * 这个是创建ViewHolder的方法，在方法中创建view和对应的holder，然后将该view的监听器进行设置
         * @param viewGroup 父布局
         * @param i 这个是view的类型，而不是对应的位置， 位置可以通过holder.getAdapterPosition()来得到
         * @return ViewHolder对象
         */
        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.news_item,viewGroup, false);
            final ViewHolder holder= new ViewHolder(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    News news = mNewsList.get(holder.getAdapterPosition());
                    if(isTwoPane){
                        NewsContentFragment newsContentFragment = (NewsContentFragment) getFragmentManager().findFragmentById(R.id.news_content_fragment);
                        newsContentFragment.refresh(news.getTitle(), news.getContent());
                    }
                    else {
                        NewsContentActivity.actionStart(getActivity(), news.getTitle(), news.getContent());

                    }
                }
            });
            return holder;
        }

        /**
         * 将Holder与对应位置的数据进行绑定
         * @param viewHolder 处理的Holder对象
         * @param i 这个的i才是位置
         */
        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
            News news = mNewsList.get(i);
            ((ViewHolder)viewHolder).newsTitleText.setText(news.getTitle());
        }

        @Override
        public int getItemCount() {
            return mNewsList.size();
        }
    }
}