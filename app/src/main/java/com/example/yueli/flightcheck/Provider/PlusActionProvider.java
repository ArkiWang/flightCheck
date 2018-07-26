package com.example.yueli.flightcheck.Provider;

import android.content.Context;

import android.content.Intent;
import android.support.v4.view.ActionProvider;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

import com.example.yueli.flightcheck.Extend.AboutActivity;
import com.example.yueli.flightcheck.Extend.HelpActivity;
import com.example.yueli.flightcheck.R;
import com.example.yueli.flightcheck.FlightSelectActivity;

/**
 * Created by yueli on 2018/7/11.
 */

public class PlusActionProvider extends ActionProvider {

    private Context context;

    public PlusActionProvider(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public View onCreateActionView() {
        return null;
    }

    @Override
    public void onPrepareSubMenu(SubMenu subMenu) {
        subMenu.clear();
        subMenu.add(0,0,0,context.getString(R.string.plus_add_flight))
                .setIcon(R.drawable.ic_action_plane)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent=new Intent(context, FlightSelectActivity.class);
                        context.startActivity(intent);
                        return true;
                    }
                });
        subMenu.add(0,1,1,context.getString(R.string.plus_config))
                .setIcon(R.drawable.ic_action_gear)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent=new Intent(context, AboutActivity.class);
                        context.startActivity(intent);
                        return true;
                    }
                });
        subMenu.add(context.getString(R.string.plus_help))
                .setIcon(R.drawable.ic_action_help)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent=new Intent(context, HelpActivity.class);
                        context.startActivity(intent);
                        return true;
                    }
                });

    }
    @Override
    public boolean hasSubMenu() {
        return true;
    }

}


