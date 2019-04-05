package android.bignerdranch.com;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.UUID;

public class CrimePageActivity extends AppCompatActivity {
    private ViewPager mViewPager;
    private List<Crime> mCrimes;
    private Button jumpToFirst;
    private Button jumpToLast;
    private static final String EXTRA_CRIME_ID="com.bignerdranch.android.crininalintent.crime_id";
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crime_page);
        final UUID crimeId = (UUID)getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        mViewPager=(ViewPager)findViewById(R.id.crime_view_pager);
        jumpToFirst=(Button)findViewById(R.id.jump_to_first);
        jumpToLast=(Button)findViewById(R.id.jump_to_last);
        jumpToFirst.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mViewPager.setCurrentItem(0);
            }
        });
        jumpToLast.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mViewPager.setCurrentItem(mCrimes.size()-1);
            }
        });
        mCrimes=CrimeLab.get(this).getCrimes();
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int i) {
                Crime crime = mCrimes.get(i);
                return CrimeFragment.newInstance(crime.getId());  //这里调用
            }

            @Override
            public int getCount() {
                return mCrimes.size();
            }
        });
        for(int position=0; position<mCrimes.size(); position++){
            if(mCrimes.get(position).getId().equals(crimeId)){
                mViewPager.setCurrentItem(position);
                break;
            }
        }

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                jumpToFirst.setEnabled(true);
                jumpToLast.setEnabled(true);
                if(i==0){
                    jumpToFirst.setEnabled(false);
                }
                if(i == mCrimes.size()-1){
                    jumpToLast.setEnabled(false);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }
    public static Intent newIntent(Context packageContext, UUID crimeId){
        Intent intent = new Intent(packageContext, CrimePageActivity.class);
        intent.putExtra(EXTRA_CRIME_ID, crimeId);
        return intent;
    }
}
