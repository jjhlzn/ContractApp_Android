package com.jinjunhang.contract.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jinjunhang.contract.R;

/**
 * Created by lzn on 16/4/3.
 */
public class MyInfoFragment extends android.support.v4.app.Fragment {


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_myinfo, container, false);

        final TextView userNameTV = (TextView)v.findViewById(R.id.myinfo_username);
        userNameTV.setText(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, ""));

        TextView nameTV = (TextView)v.findViewById(R.id.myinfo_name);
        nameTV.setText(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_NAME_KEY, ""));

        TextView departmentTV = (TextView)v.findViewById(R.id.myinfo_department);
        departmentTV.setText(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_DEPARTMENT_KEY, ""));

        Button logoutButton = (Button)v.findViewById(R.id.myinfo_logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Utils.showConfirmMessage(getActivity(), "确定退出登录吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_ISLOGIN_KEY, "0");
                        Intent i = new Intent(getActivity(), LoginActivity.class);
                        startActivity(i);
                    }
                });

            }
        });


        return  v;
    }
}
