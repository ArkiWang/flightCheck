package com.example.yueli.flightcheck.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yueli.flightcheck.R;
import com.example.yueli.flightcheck.model.FlightInfo;

import java.util.List;

/**
 * Created by yueli on 2018/7/10.
 */

public class FlightInfoListViewAdapter extends BaseAdapter {
    private List<FlightInfo> mDatas;
    private LayoutInflater inflater;
    private int curIndex;
    private int pageSize;

    public FlightInfoListViewAdapter(Context context,List<FlightInfo>mDatas,int curIndex,int pageSize){
        inflater=LayoutInflater.from(context);
        this.mDatas=mDatas;
        this.curIndex=curIndex;
        this.pageSize=pageSize;
    }
    //每一页显示的条数
    @Override
    public int getCount() {
        return mDatas.size()>(curIndex+1)*pageSize? pageSize:
                (mDatas.size()-curIndex*pageSize);
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position+curIndex*pageSize);
    }

    @Override
    public long getItemId(int position) {
        return position+curIndex*pageSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.flight_info_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.flno = (TextView) convertView.findViewById(R.id.item_flight_no);
            viewHolder.takeOff = (TextView) convertView.findViewById(R.id.item_flight_start0);
            viewHolder.arrive = (TextView) convertView.findViewById(R.id.item_flight_end0);
            viewHolder.state = (TextView) convertView.findViewById(R.id.item_flight_state);
            viewHolder.boarding = (TextView) convertView.findViewById(R.id.item_flight_boarding);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        /**
         * 在给View绑定显示的数据时，计算正确的position = position + curIndex * pageSize
         */
        int pos = position + curIndex * pageSize;
        viewHolder.flno.setText(mDatas.get(pos).flno);
        viewHolder.takeOff.setText(mDatas.get(pos).takeOff);
        viewHolder.arrive.setText(mDatas.get(pos).arrive);
        viewHolder.state.setText(mDatas.get(pos).state);
        viewHolder.boarding.setText(mDatas.get(pos).boarding);
        return convertView;
    }


    class ViewHolder {
        public TextView flno;
        public TextView takeOff;
        public TextView arrive;
        public TextView state;
        public TextView boarding;
    }
}
