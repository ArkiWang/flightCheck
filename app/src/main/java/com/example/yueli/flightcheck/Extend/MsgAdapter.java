package com.example.yueli.flightcheck.Extend;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yueli.flightcheck.R;

import java.util.ArrayList;


public class MsgAdapter extends BaseAdapter{

    Context mContext;
    LayoutInflater mInflater;
    ArrayList<MsgInfo> mArray;

    public MsgAdapter(Context context,ArrayList<MsgInfo> arr){
        this.mContext=context;
        this.mInflater=LayoutInflater.from(context);
        this.mArray=arr;
    }

    @Override
    public int getCount() {
        if(mArray==null){
            return 0;
        }else{
            return mArray.size();
        }
    }

    @Override
    public Object getItem(int position) {
        if(mArray==null ){
            return null;
        }else{
            return mArray.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder  viewHolder;
        if(convertView==null){
            convertView=mInflater.inflate(R.layout.msg_item, null);
            viewHolder=new ViewHolder();

            viewHolder.llyMy=(LinearLayout)convertView.findViewById(R.id.ll_msg);
            viewHolder.tvTimeMy=(TextView)convertView.findViewById(R.id.tv1_time_msg);
            viewHolder.tvNameMy=(TextView)convertView.findViewById(R.id.tv2_name_msg);
            viewHolder.tvMsgMy=(TextView)convertView.findViewById(R.id.tv3_msg_question);
            viewHolder.ivHeadMy=(ImageView)convertView.findViewById(R.id.iv1_head_msg);

            viewHolder.lly=(LinearLayout)convertView.findViewById(R.id.ll2_msg);
            viewHolder.tvTime=(TextView)convertView.findViewById(R.id.tv4_time_msg);
            viewHolder.tvName=(TextView)convertView.findViewById(R.id.tv5_name_msg);
            viewHolder.tvMsg=(TextView)convertView.findViewById(R.id.tv6_msg_answer);
            viewHolder.ivHead=(ImageView)convertView.findViewById(R.id.iv2_head_msg);

            convertView.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder)convertView.getTag();
        }
        MsgInfo msgInfo=(MsgInfo)getItem(position);
        if(msgInfo.IsMyMsg()){
            //显示我的信息
            viewHolder.llyMy.setVisibility(View.VISIBLE);
            viewHolder.tvMsgMy.setText(msgInfo.mMsg);
            viewHolder.tvTimeMy.setText(msgInfo.getTime());
            viewHolder.tvNameMy.setText(msgInfo.mName);
            viewHolder.ivHeadMy.setImageDrawable(mContext.getResources().getDrawable(msgInfo.mHead));
            viewHolder.lly.setVisibility(View.GONE);

        }else{
            viewHolder.lly.setVisibility(View.VISIBLE);
            viewHolder.tvTime.setText(msgInfo.getTime());
            viewHolder.tvName.setText(msgInfo.mName);
            viewHolder.tvMsg.setText(msgInfo.mMsg);
            viewHolder.ivHead.setImageDrawable(mContext.getResources().getDrawable(msgInfo.mHead));
            viewHolder.llyMy.setVisibility(View.GONE);
        }
        return convertView;
    }
    class ViewHolder{
        /***
         * 显示我的信息的控件;
         */
        LinearLayout llyMy;
        TextView tvTimeMy;
        TextView tvNameMy;
        TextView tvMsgMy;
        ImageView ivHeadMy;

        /***
         * 显示别人的信息的控件
         */

        LinearLayout lly;
        TextView tvTime;
        TextView tvName;
        TextView tvMsg;
        ImageView ivHead;;
    }
}