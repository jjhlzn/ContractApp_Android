package com.jinjunhang.contract.controller;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.ServiceLocator;
import com.jinjunhang.contract.service.ServerResponse;
import com.jinjunhang.contract.service.ServiceConfiguration;

/**
 * Created by lzn on 16/5/7.
 */
public class ConfigurationFragment extends android.support.v4.app.Fragment  {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_configuration, container, false);

        String http = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_HTTP, ServiceLocator.DEFAULT_HTTP);
        final String serverName = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_SERVERNAME, ServiceLocator.DEFAULT_SERVERNAME);
        int port = PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_PORT, ServiceLocator.DEFAULT_PORT);

        ((TextView)v.findViewById(R.id.config_http)).setText(http);
        ((TextView)v.findViewById(R.id.config_serverName)).setText(serverName);
        ((TextView)v.findViewById(R.id.config_port)).setText(port+"");

        Button saveButton = (Button)v.findViewById(R.id.config_saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View button) {
                String newHttp = ((TextView)v.findViewById(R.id.config_http)).getText().toString();
                String newServerName = ((TextView)v.findViewById(R.id.config_serverName)).getText().toString().trim();
                String newPort = ((TextView)v.findViewById(R.id.config_port)).getText().toString();
                int port = 80;
                if (!"http".equals(newHttp) && !"https".equals(newHttp)) {
                    Utils.showMessage(getActivity(), "协议必须是http或者https");
                    return;
                }

                try {
                    port = Integer.parseInt(newPort);
                }
                catch (Exception ex) {
                    Utils.showMessage(getActivity(), "端口号必须是数字");
                    return;
                }

                if ("".equals(newServerName)) {
                    Utils.showMessage(getActivity(), "服务器名不能为空");
                    return;
                }

                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_HTTP, newHttp);
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_SERVERNAME, newServerName);
                PrefUtils.saveToPrefs(getActivity(), PrefUtils.PREFS_LOCATOR_PORT, port);
                ServiceConfiguration.LOCATOR_HTTP = newHttp;
                ServiceConfiguration.LOCATOR_SERVERNAME = newServerName;
                ServiceConfiguration.LOCATOR_PORT = port;

                Utils.showMessage(getActivity(), "保存成功", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                    }
                });
            }
        });

        return v;
    }
}
