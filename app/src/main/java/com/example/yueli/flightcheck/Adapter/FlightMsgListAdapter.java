package com.example.yueli.flightcheck.Adapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.yueli.flightcheck.Bean.PickupDetail;
import com.example.yueli.flightcheck.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.support.v4.content.res.ResourcesCompat.getColor;

/**
 * Created by yueli on 2018/7/7.
 */

public class FlightMsgListAdapter extends BaseAdapter {
    //private static final FlightMsgListAdapter ourInstance = new FlightMsgListAdapter();
    private  List<PickupDetail> list=new ArrayList<>();
    private Context context;

  /*  public static FlightMsgListAdapter getInstance() {
        return ourInstance;
    }*/
    public FlightMsgListAdapter(Context context,List<PickupDetail> list){
        this.context=context;
        this.list=list;
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
        PickupDetail item=list.get(position);
        convertView=View.inflate(context, R.layout.fragment_recv_msg_item,null);
        ViewHolder holder=new ViewHolder();
        holder.flno=convertView.findViewById(R.id.frag_pickup_flno);
        holder.from=convertView.findViewById(R.id.frag_pickup_start);
        holder.arrive0=convertView.findViewById(R.id.frag_pickup_date0);
        holder.arrive1=convertView.findViewById(R.id.frag_pickup_date1);
        holder.state=convertView.findViewById(R.id.frag_pickup_status);
        holder.hzl=convertView.findViewById(R.id.frag_pickup_hzl);
        holder.flno.setText(item.flno);
       // holder.from.setText(item.fromA);
        holder.arrive0.setText(item.arriveTime0);
        holder.arrive1.setText(item.arriveTime1);
        if(item.state.equals("延误"))
            holder.state.setTextColor(getColor(R.color.red));
        getColor(color,)
        else
            holder.state.setTextColor(getResources().getColor(R.color.colorPrimary));
        holder.state.setText(item.state);
        holder.hzl.setText(item.toA);
        return convertView;
    }
    class ViewHolder {
        public TextView flno;
        public TextView from;
        public TextView arrive0;
        public TextView arrive1;
        public TextView state;
        public TextView hzl;
    }
}
