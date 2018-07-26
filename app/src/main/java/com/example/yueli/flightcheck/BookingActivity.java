package com.example.yueli.flightcheck;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yueli.flightcheck.Bean.Booking;
import com.example.yueli.flightcheck.Bean.BookingItem;
import com.example.yueli.flightcheck.Bean.Flight;
import com.example.yueli.flightcheck.Bean.Flight_;
import com.example.yueli.flightcheck.Util.ApplicationUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.objectbox.query.QueryBuilder;

import static com.example.yueli.flightcheck.Bean.Booking_.FlightId;
import static com.example.yueli.flightcheck.Bean.Booking_.UserId;

public class BookingActivity extends AppCompatActivity implements AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener,View.OnClickListener{
    private ListView bookingList;
    private List<String>mContent=new ArrayList<>();
    private List<String>select=new ArrayList<>();
    private EditAdapter editAdapter;
    private boolean isDeleteMode=false;
    private ImageView submit;
    boolean ifDeleteSucceed=false;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);
        final Handler handler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                if(msg.obj.equals("ok")){
                    editAdapter.notifyDataSetChanged();
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                initData();
                Message message=new Message();
                message.obj="ok";
                handler.sendMessage(message);
            }
        }).start();

        bookingList=findViewById(R.id.booking_msg_list);
        submit=findViewById(R.id.edit_submit);
        submit.setOnClickListener(this);
        editAdapter=new EditAdapter(this);
        bookingList.setAdapter(editAdapter);
        bookingList.setOnItemClickListener(this);
        bookingList.setOnItemLongClickListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private void initData(){
        ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
        List<Booking>bookings=applicationUtil.bookingBox.find(UserId,applicationUtil.userId);
        for(Booking booking:bookings){
            Flight flight=applicationUtil.flightBox.query().equal(Flight_.id,booking.FlightId).build().findFirst();
            mContent.add(flight.flno);
        }
     /* if(applicationUtil.bookingList!=null) {
          for (BookingItem item : applicationUtil.bookingList)
              mContent.add(item.t_flid);
      }*/
    }
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.v("arki","item  click");
        Intent intent=new Intent(BookingActivity.this,FlightCheckDetailActivity.class);
        intent.putExtra("flno",mContent.get(position));
        intent.putExtra("time","null");
        startActivity(intent);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        isDeleteMode=true;
        select.clear();
        submit.setVisibility(View.VISIBLE);
        return true;
    }
    public void show(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View v) {
        Log.v("arki","delete click");
        final ApplicationUtil applicationUtil=(ApplicationUtil)getApplication();
       switch (v.getId()){
            case R.id.edit_submit:
                isDeleteMode=false;
               for(final String s:select){
                   QueryBuilder<Booking> bookingQueryBuilder=applicationUtil.bookingBox.query();
                   Booking booking=bookingQueryBuilder.equal(UserId,applicationUtil.userId)
                           .equal(FlightId,applicationUtil.getFlight(s).id)
                           .build().findFirst();
                   applicationUtil.bookingBox.remove(booking);
                   mContent.remove(s);
                  /* handler=new Handler(){
                       @Override
                       public void handleMessage(Message msg) {
                           if(msg.obj.equals("ok")){
                               if(ifDeleteSucceed) {
                                   mContent.remove(s);
                                   editAdapter.notifyDataSetChanged();
                               }
                           }
                       }
                   };
                   new Thread(new Runnable() {
                       @Override
                       public void run() {
                           ifDeleteSucceed=applicationUtil.deleteBooking(s);
                       }
                   }).start();*/
               }
                select.clear();
                editAdapter.notifyDataSetChanged();
                submit.setVisibility(View.GONE);
                break;
        }
    }

    class EditAdapter extends BaseAdapter{
        private Context context;
        ViewHolder holder;
        public EditAdapter(Context context){
            this.context=context;
        }

        @Override
        public int getCount() {
            return mContent.size();
        }

        @Override
        public Object getItem(int position) {
            return mContent.get(position);
        }

        @Override
        public long getItemId(int position) {
            return (long)position;
        }

        @Override
        public View getView(final int position, View convertView, final ViewGroup parent) {
          if(convertView==null){
                convertView=View.inflate(context,R.layout.edit_list_item,null);
                holder=new ViewHolder();
                holder.textView=convertView.findViewById(R.id.edit_item_text);
                holder.checkBox=convertView.findViewById(R.id.edit_item_check);
                convertView.setTag(holder);
            }else
                holder=(ViewHolder)convertView.getTag();
            holder.textView.setText(mContent.get(position));
            holder.checkBox.setChecked(false);
            if(select.contains(position))
                holder.checkBox.setChecked(true);
            if(isDeleteMode)
                holder.checkBox.setVisibility(View.VISIBLE);
            else
                holder.checkBox.setVisibility(View.INVISIBLE);
            holder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(select.contains(mContent.get(position)))
                        holder.checkBox.setChecked(false);
                    else
                        holder.checkBox.setChecked(true);
                    if(holder.checkBox.isChecked()){
                        select.add(mContent.get(position));
                        Log.v("arki","选择"+position);
                    }else{
                        select.remove(mContent.get(position));
                         Log.v("arki",position+"取消");
                    }
                }
            });
            return convertView;
        }
        class ViewHolder{
            public TextView textView;
            public CheckBox checkBox;
        }
    }
}
