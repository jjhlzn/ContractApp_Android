package com.jinjunhang.contract.controller;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.service.LoginResponse;
import com.jinjunhang.contract.service.LoginService;
import com.jinjunhang.contract.service.RegisterDeviceResponse;
import com.jinjunhang.contract.service.ServerResponse;
import com.jinjunhang.contract.service.ServiceConfiguration;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushManager;

/**
 * Created by lzn on 16/4/7.
 */
public class LoginFragment extends android.support.v4.app.Fragment {

    private static final String TAG = "LoginFragment";
    private EditText mUserNameEditText;
    private EditText mPasswordEditText;
    private LoadingAnimation mLoading;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_login, container, false);

        mLoading = new LoadingAnimation(getActivity());

        mUserNameEditText = (EditText)v.findViewById(R.id.login_userName);
        mPasswordEditText = (EditText)v.findViewById(R.id.login_password);



        Button loginButton = (Button)v.findViewById(R.id.loign_loginButton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userName = mUserNameEditText.getText().toString();
                String password = mPasswordEditText.getText().toString();
                if (userName.isEmpty()) {
                    Utils.showMessage(getActivity(), "必须输入用户名");
                    return;
                }

                if (password.isEmpty()) {
                    Utils.showMessage(getActivity(), "必须输入密码");
                    return;
                }
                new LoginTask().execute(userName, password);
            }
        });

        Button configButton = (Button)v.findViewById(R.id.login_configButton);
        configButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), ConfigurationActivity.class);
                startActivity(i);
            }
        });

        return v;
    }



    class LoginTask extends AsyncTask<String, Void, LoginResponse> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("");
        }

        @Override
        protected void onPostExecute(LoginResponse loginResponse) {
            super.onPostExecute(loginResponse);

            if (loginResponse.getStatus() == ServerResponse.FAIL) {
                mLoading.dismiss();
                Utils.showServerErrorDialog(getActivity());
                return;
            }



            if (loginResponse.isSuccess()) {

                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, mUserNameEditText.getText().toString());
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_NAME_KEY, loginResponse.getName());
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_DEPARTMENT_KEY, loginResponse.getDepartment());

                XGPushManager.registerPush(getActivity(), new XGIOperateCallback() {
                    @Override
                    public void onSuccess(Object o, int i) {
                        Log.d(TAG, "注册成功, 设备devicetoken为: " + o);
                        //将Devicetoken注册到服务器
                        new RegisterDeviceTask().execute(mUserNameEditText.getText().toString(), o.toString());
                    }

                    @Override
                    public void onFail(Object o, int i, String s) {
                        Log.d(TAG, "注册失败, 无法获得devicetoken, 错误码：" + i + ",  错误信息：" + s);
                        Utils.showMessage(getActivity(), "消息注册失败，重新登录");
                    }
                });

            } else {
                mLoading.dismiss();
                String errorMessage = loginResponse.getErrMessage();
                if (errorMessage == null || errorMessage.isEmpty()) {
                    errorMessage = "用户名或密码错误！";
                }
                Utils.showMessage(getActivity(), errorMessage);
            }
        }

        @Override
        protected LoginResponse doInBackground(String... params) {
            String userName = params[0];
            String password = params[1];
            return new LoginService().login(userName, password);
        }
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
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_ISLOGIN_KEY, "1");
                Intent intent = new Intent(getActivity(), MainActivity2.class);
                startActivity(intent);
            } else {
                Utils.showMessage(getActivity(), "消息注册失败，重新登录");
            }

        }
    }
}
