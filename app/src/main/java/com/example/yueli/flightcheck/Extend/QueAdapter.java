package com.example.yueli.flightcheck.Extend;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yueli.flightcheck.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/15 0015.
 */

public class QueAdapter extends BaseAdapter {
    private Activity act;
    private List<Map<String,Object>> data;
    private int layout;
    private TextView tv;

    public QueAdapter(Activity act, List<Map<String, Object>> data, int layout) {
        this.act = act;
        this.data = data;
        this.layout = layout;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater lf=LayoutInflater.from(act);
        View view= lf.inflate(layout,null);
        tv=(TextView) view.findViewById(R.id.service_que_tv1);
        final Map<String,Object>item=data.get(position);
        tv.setText(item.get("tv1").toString());
        return view;
    }
}
