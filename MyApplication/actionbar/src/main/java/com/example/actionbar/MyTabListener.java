package com.example.actionbar;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;

public class MyTabListener implements ActionBar.TabListener {

    /**
     * 需要Fragment的Activity类
     */
    private final Activity mActivity;
    /**
     * 指定要加载的Fragment所对应的类
     */
    private final Class mClass;
    /**
     * 放进去的Fragment
     */
    private Fragment mFragment;

    public MyTabListener(Activity activity, Class aClass) {
        mActivity = activity;
        mClass = aClass;
    }

    /**
     * 被选中的时候
     * @param tab
     * @param fragmentTransaction
     */
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if(mFragment == null){
            mFragment=Fragment.instantiate(mActivity, mClass.getName());
            fragmentTransaction.add(android.R.id.content, mFragment,null);
        }
        fragmentTransaction.attach(mFragment);
    }

    /**
     * 退出选择的时候
     * @param tab
     * @param fragmentTransaction
     */
    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        if(mFragment != null){
            fragmentTransaction.detach(mFragment);
        }
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {

    }
}
