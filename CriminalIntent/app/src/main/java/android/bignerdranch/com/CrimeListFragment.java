package android.bignerdranch.com;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class CrimeListFragment  extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private CrimeAdapter mAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //这里是将布局转换成具体的对象，container为父级可选视图
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView=(RecyclerView)view.findViewById(R.id.crime_recycler_view);
        //实际的摆放任务被分给了LayoutManager
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        upDateUI();  //设置Adapt
        return view;
    }
    private class CrimeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener{  //这里直接使用的实现接口
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private Crime mCrime;
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent) {
            //转换成视图,每个小view也对应一个视图，这里是list_item_crime
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
            itemView.setOnClickListener(this);  //直接为itemView 设置监听
            mTitleTextView=(TextView)itemView.findViewById(R.id.crime_title);
            mDateTextView=(TextView)itemView.findViewById(R.id.crime_date);
        }
        public void bind(Crime crime){  //每次有新的Crime显示时，都要调用一次
            mCrime=crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getDate().toString());
        }
        public void onClick(View v){
            Toast.makeText(getActivity(), mCrime.getTitle()+"click", Toast.LENGTH_SHORT).show();
        }
    }
    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder>{
        private List<Crime> mCrimes;

        public CrimeAdapter(List<Crime> crimes){
            mCrimes=crimes;
        }

        @NonNull
        @Override
        //需要新的ViewHolder来显示列表项时，调用此方法
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
            LayoutInflater layoutinflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutinflater, parent);
        }

        @Override
        //注意这里传的参数是crimeHolder，第一个应该是显示的view，第二个是处于整个List的第几个
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int i) {  //更换工作
             Crime crime = mCrimes.get(i);
             crimeHolder.bind(crime);

        }

        @Override
        public int getItemCount() {  //返回整个链表的元素个数
            return mCrimes.size();
        }
    }

    private void upDateUI(){
        CrimeLab crimeLab = CrimeLab.get(getActivity());
        List<Crime> crimes= crimeLab.getCrimes();
        mAdapter= new CrimeAdapter(crimes);
        mCrimeRecyclerView.setAdapter(mAdapter);  //为RecycleView设置Adapter
    }
}
