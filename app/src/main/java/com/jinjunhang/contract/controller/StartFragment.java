package com.jinjunhang.contract.controller;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.ServiceLocator;
import com.jinjunhang.contract.service.GetServiceLocatorResponse;
import com.jinjunhang.contract.service.LocatorService;
import com.jinjunhang.contract.service.SearchApprovalResponse;
import com.jinjunhang.contract.service.ServerResponse;
import com.jinjunhang.contract.service.ServiceConfiguration;

/**
 * Created by lzn on 16/4/7.
 */
public class StartFragment extends android.support.v4.app.Fragment {
    private final static String TAG = "StartFragment";

    private final int SPLASH_DELAY_MILLIS = 1500;
    private LocatorService mLocatorService = new LocatorService();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start, container, false);

        Intent i = new Intent(getActivity(), MainActivity2.class);
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //getServiceLocator();
                setServiceLocator();
                goHome();
            }
        }, SPLASH_DELAY_MILLIS);

        return v;
    }

    private void setServiceLocator() {
        String http = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_HTTP, ServiceLocator.DEFAULT_HTTP);
        String serverName = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_SERVERNAME, ServiceLocator.DEFAULT_SERVERNAME);
        int port = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_PORT, ServiceLocator.DEFAULT_PORT);
        ServiceConfiguration.LOCATOR_HTTP = http;
        ServiceConfiguration.LOCATOR_SERVERNAME = serverName;
        ServiceConfiguration.LOCATOR_PORT = port;
    }

    private void goHome() {
        String isLoginValue = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_ISLOGIN_KEY, "0");
        Log.d(TAG, "isLoginValue = " + isLoginValue);
        boolean isLogin =  isLoginValue.equals("1");
        Intent intent = null;
        if (isLogin) {
            intent = new Intent(getActivity(), MainActivity2.class);
        } else {
            intent = new Intent(getActivity(), LoginActivity.class);
        }

        startActivity(intent);
    }

    /*
    private void getServiceLocator() {
        new GetServiceLocatorTask().execute();
    }

    private class GetServiceLocatorTask extends AsyncTask<Void, Void, GetServiceLocatorResponse> {
        @Override
        protected void onPostExecute(GetServiceLocatorResponse resp) {
            super.onPostExecute(resp);
            if (resp.getStatus() != ServerResponse.SUCCESS) {
                Utils.showMessage(getActivity(), "获取ServiceLocator失败");
                return;
            }

            String oldHttp = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_HTTP, "");
            String oldServerName = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_SERVERNAME, "");
            int oldPort = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_PORT, 0);

            if (!oldHttp.equals(resp.getServiceLocator().getHttp()) ||
                    !oldServerName.equals(resp.getServiceLocator().getServerName()) ||
                    oldPort != resp.getServiceLocator().getPort() ) {
                Log.d(TAG, "oldHttp = " + oldHttp);
                Log.d(TAG, "oldServerName = " + oldServerName);
                Log.d(TAG, "oldPort = " + oldPort);
                Log.d(TAG, "Http = " + resp.getServiceLocator().getHttp());
                Log.d(TAG, "ServerName = " + resp.getServiceLocator().getServerName());
                Log.d(TAG, "Port = " + resp.getServiceLocator().getPort());
                Log.d(TAG, "servicelocator has changed, make login invalid");
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOGIN_ISLOGIN_KEY, "0");
            }

            //更新ServiceLocator
            PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_HTTP, resp.getServiceLocator().getHttp());
            PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_SERVERNAME, resp.getServiceLocator().getServerName());
            PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_PORT, resp.getServiceLocator().getPort());

            ServiceConfiguration.LOCATOR_HTTP = resp.getServiceLocator().getHttp();
            ServiceConfiguration.LOCATOR_PORT = resp.getServiceLocator().getPort();
            ServiceConfiguration.LOCATOR_SERVERNAME = resp.getServiceLocator().getServerName();

            Log.d(TAG, "onPostExecute: serverName = " +resp.getServiceLocator().getServerName() );

            goHome();
        }

        @Override
        protected GetServiceLocatorResponse doInBackground(Void... params) {
            return mLocatorService.getServiceLocator();

        }
    }
    */
}
