package com.example.yueli.flightcheck.Fragment;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.yueli.flightcheck.R;

/**
 * Created by yueli on 2018/7/4.
 */

public class fragmentCheck extends Fragment implements RadioGroup.OnCheckedChangeListener {

    RadioGroup group;
    RadioButton flight,city;
    FragmentManager fm;
    FragmentTransaction ft;
    fragCheckFLNO flno;
    fragCheckAirport fcity;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_check,null);
        group=(RadioGroup)view.findViewById(R.id.check_radio_group);
        flight=(RadioButton)view.findViewById(R.id.check_flight_no);
        city=(RadioButton)view.findViewById(R.id.check_city);
        flno=new fragCheckFLNO();
        fcity=new fragCheckAirport();
        fm=getFragmentManager();
        change(fcity);
        group.setOnCheckedChangeListener(this);
        return view;
    }
    private void change(Fragment f){
        ft=fm.beginTransaction();
        ft.replace(android.R.id.tabcontent,f);
        ft.commit();
    }
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case R.id.check_flight_no:
                change(flno);
                break;
            case R.id.check_city:
                change(fcity);
                break;
            default:
                break;
        }
    }
}
