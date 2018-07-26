package com.example.yueli.flightcheck.Fragment;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.example.yueli.flightcheck.FlightCheckDetailActivity;
import com.example.yueli.flightcheck.R;
import com.example.yueli.flightcheck.FlightSelectActivity;
import com.example.yueli.flightcheck.Util.ApplicationUtil;
import com.example.yueli.flightcheck.Util.REQUEST_INT;
import com.example.yueli.flightcheck.Util.showDialog;

import java.util.Date;

/**
 * Created by yueli on 2018/7/5.
 */

public class fragCheckFLNO extends Fragment implements View.OnClickListener{
    private View flnoDate;
    private TextView flnoDateText,flno;
    private Date date;
    private Button submit;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.frag_tabcon_flight,null);
        flnoDate=view.findViewById(R.id.line_date);
        flnoDateText=view.findViewById(R.id.flno_date_text);
        flno=view.findViewById(R.id.flno_no);
        ApplicationUtil applicationUtil=(ApplicationUtil)getActivity().getApplication();
        flno.setText("GS857101");
        flno.setOnClickListener(this);
        date=new Date();
        String month=""+(date.getMonth()+1);
        String day=""+(date.getDay()+1);
        if(month.length()<2)
            month=0+month;
        if(day.length()<2)
            day=0+day;
        flnoDateText.setText(date.getYear()-100+2000+"-"+month+"-"+day);
        flnoDate.setOnClickListener(this);
        submit=view.findViewById(R.id.frag_tabcon_flight_btn);
        submit.setOnClickListener(this);
        return view;
    }
    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        // 参数1： 触发事件    参数2：年    参数3：月    参数4：：日(ps:参数2、3、4是显示的时间)
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String month=""+(monthOfYear+1);
            String day=""+dayOfMonth;
            if(month.length()<2)
                month=0+month;
            if(day.length()<2)
                day=0+day;
            flnoDateText.setText(year+"-"+month+"-"+day);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQUEST_INT.FRAG_FLNO_REQUEST){
            if(resultCode==REQUEST_INT.FRAG_FLNO_REQUEST)
                flno.setText(data.getStringExtra("BookingJsonBean"));
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.line_date:
                showDialog.showDateDiaglog(getActivity(),dateSetListener);
                break;
            case R.id.flno_no:
                intent=new Intent(getActivity(), FlightSelectActivity.class);
                intent.putExtra("request", REQUEST_INT.FRAG_FLNO_REQUEST);
                startActivityForResult(intent,REQUEST_INT.FRAG_FLNO_REQUEST);
                break;
            case R.id.frag_tabcon_flight_btn:
                intent=new Intent(getActivity(),FlightCheckDetailActivity.class);
                intent.putExtra("flno",flno.getText().toString());
                intent.putExtra("time",flnoDateText.getText().toString());
                startActivity(intent);
                break;
        }
    }
}
