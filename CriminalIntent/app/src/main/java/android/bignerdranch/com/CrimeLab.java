package android.bignerdranch.com;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;


/* 单例，把数据给全部放在一个另外的里面，数据不随fragment的变化而变化
 *

 */
public class CrimeLab {
    private static CrimeLab sCrimeLab;
    private List<Crime> mCrimes;

    public static CrimeLab get(Context context){  //返回Crime数组
        if(sCrimeLab == null){
            sCrimeLab=new CrimeLab(context);
        }
        return sCrimeLab;
    }
    private CrimeLab(Context context){//构造函数，生成100个Crime对象的数组
        mCrimes=new ArrayList<>();
        for(int i=1; i<=100; i++){
            Crime crime = new Crime();
            crime.setTitle("Crime #"+i);
            crime.setSolved(i%2 == 0);
            if(i % 2 == 0)
                crime.setRequiresPolice(true);
            else
                crime.setRequiresPolice(false);
            mCrimes.add(crime);  //加入进去
        }
    }

    public List<Crime> getCrimes(){
        return mCrimes;
    }
    public Crime getCrime(UUID id){  //找对应UUID的Crime对象，没有则返回null
        for(Crime crime:mCrimes){
            if(crime.getId() == id){
                return crime;
            }
        }
        return null;
    }

}
