package com.jinjunhang.contract.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jinjunhang.contract.BuildConfig;
import com.jinjunhang.contract.R;
import com.jinjunhang.contract.service.LoginService;
import com.jinjunhang.contract.service.RegisterDeviceResponse;
import com.jinjunhang.contract.service.ServerResponse;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

/**
 * Created by lzn on 16/4/3.
 */
public class MyInfoFragment extends android.support.v4.app.Fragment {

    private final static String TAG = "MyInfoFragment";
    private LoadingAnimation mLoading;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_myinfo, container, false);

        mLoading = new LoadingAnimation(getActivity());

        final TextView userNameTV = (TextView)v.findViewById(R.id.myinfo_userName);
        userNameTV.setText(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, ""));

        TextView nameTV = (TextView)v.findViewById(R.id.myinfo_name);
        nameTV.setText(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_NAME_KEY, ""));

        TextView departmentTV = (TextView)v.findViewById(R.id.myinfo_department);
        departmentTV.setText(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_DEPARTMENT_KEY, ""));

        TextView versionTV = (TextView)v.findViewById(R.id.myinfo_version);
        versionTV.setText(BuildConfig.VERSION_NAME + "." + BuildConfig.VERSION_CODE);

        Button logoutButton = (Button)v.findViewById(R.id.myinfo_logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Utils.showConfirmMessage(getActivity(), "确定退出登录吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mLoading.show("");
                        XGPushManager.unregisterPush(getActivity(), new XGIOperateCallback() {
                            @Override
                            public void onSuccess(Object o, int i) {
                                Log.d(TAG, "反注册成功, 设备devicetoken为: " + o);
                                //将Devicetoken注册到服务器
                                new RegisterDeviceTask().execute(
                                        PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, ""), "");
                            }

                            @Override
                            public void onFail(Object o, int i, String s) {
                                Log.d(TAG, "反注册失败, 无法获得devicetoken, 错误码：" + i + ",  错误信息：" + s);
                                Utils.showMessage(getActivity(), "消息注销失败，重新退出登录");
                            }
                        });


                    }
                });

            }
        });


        return  v;
    }

    class RegisterDeviceTask extends AsyncTask<String, Void, RegisterDeviceResponse> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected RegisterDeviceResponse doInBackground(String... params) {
            String userName = params[0];
            String deviceToken = params[1];
            return new LoginService().registerDevice(userName, deviceToken);
        }

        @Override
        protected void onPostExecute(RegisterDeviceResponse response) {
            super.onPostExecute(response);
            mLoading.dismiss();

            if (response.getStatus() == ServerResponse.SUCCESS) {
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_ISLOGIN_KEY, "0");
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
            } else {
                Utils.showMessage(getActivity(), "消息注销失败，重新退出登录");
            }

        }
    }
}
