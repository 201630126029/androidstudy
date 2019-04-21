package android.bignerdranch.androidhomework;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


public class NewsPageFragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragmentList;
    private FragmentManager fm;

    public NewsPageFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
        super(fm);
        this.fragmentList=fragmentList;
        this.fm=fm;
    }

    @Override
    public Fragment getItem(int idx) {
        return fragmentList.get(idx % fragmentList.size());
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
