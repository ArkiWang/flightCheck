package com.example.yueli.flightcheck.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.yueli.flightcheck.CallBackInterface.FilterListener;
import com.example.yueli.flightcheck.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yueli on 2018/7/11.
 */

public class SearchListAdapter extends BaseAdapter implements Filterable {
    List<String> list;
    Context context;
    MyFilter filter=null;
    FilterListener listener=null;
    public SearchListAdapter(List<String> list, Context context,FilterListener listener){
        this.list=list;
        this.context=context;
        this.listener=listener;
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return (long)position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String s=list.get(position);
        TextView textView;
        if(s.length()<=1) {
            convertView = View.inflate(context, R.layout.city_letter_item, null);
            textView=convertView.findViewById(R.id.city_letter);
        }else {
            convertView = View.inflate(context, R.layout.normal_search_item, null);
            textView=convertView.findViewById(R.id.city_name);
        }
        textView.setText(s);
        return convertView;
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
            filter=new MyFilter(list);
        return filter;
    }
    class MyFilter extends Filter{
        private List<String>original;

        public MyFilter(List<String> list){original=list;}

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results=new FilterResults();
            if(TextUtils.isEmpty(constraint)){
                results.values=original;
                results.count=original.size();
            }else {
                List<String>mList=new ArrayList<>();
                for(String s:original){
                    //过滤条件
                    if(s.trim().toLowerCase().contains(constraint.toString().trim()))
                        mList.add(s);
                }
                results.values=mList;
                results.count=mList.size();
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            list=(List<String>)results.values;
            if(listener!=null)
                listener.getFilterData(list);//send call back data
            notifyDataSetChanged();
        }
    }
}
