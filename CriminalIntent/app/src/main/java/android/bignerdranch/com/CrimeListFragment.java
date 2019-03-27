package android.bignerdranch.com;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class CrimeListFragment  extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        //这里是将布局转换成具体的对象，container为父级可选视图
        View view = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecyclerView=(RecyclerView)view.findViewById(R.id.crime_recycler_view);
        //实际的摆放任务被分给了LayoutManager
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }
    private class CrimeHolder extends RecyclerView.ViewHolder{
        public CrimeHolder(LayoutInflater inflater, ViewGroup parent){
            //转换成视图
            super(inflater.inflate(R.layout.list_item_crime, parent, false));
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
        public CrimeHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater layoutinflater = LayoutInflater.from(getActivity());
            return new CrimeHolder(layoutinflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull CrimeHolder crimeHolder, int i) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }
    }
}
