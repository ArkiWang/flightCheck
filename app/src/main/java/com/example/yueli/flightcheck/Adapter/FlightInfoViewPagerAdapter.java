package com.example.yueli.flightcheck.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by yueli on 2018/7/10.
 */

public class FlightInfoViewPagerAdapter extends PagerAdapter {
    private List<View> mViewList;

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(mViewList.get(position));
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View v=mViewList.get(position);
        ViewGroup parent = (ViewGroup) v.getParent();
        if (parent != null) {
            parent.removeAllViews();
        }
        container.addView(v);
        //container.addView(mViewList.get(position));
        return mViewList.get(position);
    }

    public FlightInfoViewPagerAdapter(List<View> mViewList){
        this.mViewList=mViewList;
    }
    @Override
    public int getCount() {
        return mViewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }
}
