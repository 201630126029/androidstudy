package android.bignerdranch.androidhomework;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WechatFragment extends Fragment implements ViewPager.OnPageChangeListener{
    //以下声明fragment_wechat.xml对应的控件实例
    private View view=null;
    private RadioGroup rgChannel=null;
    private ViewPager viewPager;
    private HorizontalScrollView hvChannel=null;

    private List<Fragment> newsChannelList=new ArrayList<Fragment>();
    private NewsPageFragmentAdapter adapter;

    public WechatFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(view==null){
            view=inflater.inflate(R.layout.fragment_wechat, null);

            rgChannel= view.findViewById(R.id.rgChannel);
            viewPager= view.findViewById(R.id.vpNewsList);
            hvChannel= view.findViewById(R.id.hvChannel);
            rgChannel.setOnCheckedChangeListener( //单选按钮的监听事件响应
                    new RadioGroup.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(RadioGroup group,int checkedId) {
                            viewPager.setCurrentItem(checkedId);
                        }
                    });
            initTab(inflater); //初始化内导航标签
            initViewPager();
        }
        ViewGroup parent=(ViewGroup)view.getParent();
        if(parent!=null){
            parent.removeView(view);
        }
        return view;
    }
    //初始化内导航标签
    private void initTab(LayoutInflater inflater){
        List<String> channelList=new ArrayList<String>();
        channelList.add("新闻");
        channelList.add("财经");
        channelList.add("科技");
        channelList.add("体育");
        channelList.add("娱乐");
        channelList.add("汽车");
        channelList.add("博客");
        channelList.add("读书");
        for(int i=0;i<channelList.size();i++){//以下添加单选按钮的实例到内导航
            RadioButton rb=(RadioButton)inflater.inflate(R.layout.tab_rb, null);
            rb.setId(i);
            rb.setText(channelList.get(i));
            RadioGroup.LayoutParams params=new
                    RadioGroup.LayoutParams(RadioGroup.LayoutParams.WRAP_CONTENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);

            rgChannel.addView(rb,params);
        }
        rgChannel.check(0); //第一个选项
    }

    private void initViewPager(){
        List<String> channelList=new ArrayList<String>();
        channelList.add("新闻");
        channelList.add("财经");
        channelList.add("科技");
        channelList.add("体育");
        channelList.add("娱乐");
        channelList.add("汽车");
        channelList.add("博客");
        channelList.add("读书");
        for(int i=0;i<channelList.size();i++){
            NewsChannelFragment fragment=new NewsChannelFragment();
            Bundle bundle=new Bundle();
            bundle.putString("cname", channelList.get(i));
            fragment.setArguments(bundle);
            newsChannelList.add(fragment);
        }
        adapter=new NewsPageFragmentAdapter(super.getActivity().getSupportFragmentManager(), newsChannelList);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(2);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    //某个碎片被选中时的动作
    public void onPageSelected(int i) {
        setTab(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
    //设置选定某个碎片时水平滚动视图的显示HorizontalScrollView
    private void setTab(int idx){
        RadioButton rb=(RadioButton)rgChannel.getChildAt(idx);
        rb.setChecked(true);
        int left=rb.getLeft();
        int width=rb.getMeasuredWidth();
        DisplayMetrics metrics=new DisplayMetrics();
        super.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int screenWidth=metrics.widthPixels;
        int len=left+width/2-screenWidth/2;
        hvChannel.smoothScrollTo(len, 0);
    }
}
