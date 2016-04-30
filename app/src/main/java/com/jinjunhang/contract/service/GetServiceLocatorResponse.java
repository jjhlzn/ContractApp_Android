package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.ServiceLocator;

/**
 * Created by lzn on 16/4/30.
 */
public class GetServiceLocatorResponse extends  ServerResponse {

    private ServiceLocator mServiceLocator;

    public ServiceLocator getServiceLocator() {
        return mServiceLocator;
    }

    public void setServiceLocator(ServiceLocator serviceLocator) {
        mServiceLocator = serviceLocator;
    }
}
