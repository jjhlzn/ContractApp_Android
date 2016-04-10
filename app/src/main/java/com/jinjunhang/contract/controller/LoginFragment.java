package com.jinjunhang.contract.controller;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.service.LoginResponse;
import com.jinjunhang.contract.service.LoginService;
import com.jinjunhang.contract.service.ServerResponse;

/**
 * Created by lzn on 16/4/7.
 */
public class LoginFragment extends android.support.v4.app.Fragment {

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

        mUserNameEditText.setRawInputType(Configuration.KEYBOARD_QWERTY);

        //mUserNameEditText.setHintTextColor(0x7d7c7c);
        //mPasswordEditText.setHintTextColor(0x7d7c7c);

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
            mLoading.dismiss();
            if (loginResponse.getStatus() == ServerResponse.FAIL) {
                Utils.showServerErrorDialog(getActivity());
                return;
            }

            if (loginResponse.isSuccess()) {
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_ISLOGIN_KEY, "1");
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, mUserNameEditText.getText().toString());
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_NAME_KEY, loginResponse.getName());
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_DEPARTMENT_KEY, loginResponse.getDepartment());

                Intent i = new Intent(getActivity(), MainActivity2.class);
                startActivity(i);

            } else {
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
}
