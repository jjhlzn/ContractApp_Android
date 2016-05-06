package com.jinjunhang.contract.service;

import com.jinjunhang.contract.controller.PrefUtils;

/**
 * Created by lzn on 16/3/23.
 */
public class ServiceConfiguration {
        //public final static String serverName = "192.168.1.50";
        private final static boolean isUseServiceLoator = true;
        public static String LOCATOR_HTTP = "";
        public static String LOCATOR_SERVERNAME = "";
        public static int LOCATOR_PORT = 0;
        public final static String serverName1 = "jjhtest.hengdianworld.com";
        public final static int port1 = 80;

        public final static String serverName2 = "192.168.1.50";
        public final static int port2 = 3000;

        public final static String serverName3 = "oa.lloydind.com";
        public final static int port3 = 10080;
        public static String httpMethod() {
                if (isUseServiceLoator) {
                        return LOCATOR_HTTP;
                }
                return "http";
        }

        public static String serverName() {
                if (isUseServiceLoator) {
                        return LOCATOR_SERVERNAME;
                }
                return serverName1;
        }

        public static int port() {
                if (isUseServiceLoator) {
                        return LOCATOR_PORT;
                }
                return port1;
        }

        public  static String SeachOrderUrl() {
                return httpMethod() + "://"+ serverName() + ":" + port() +"/order/search.json";
        }
        public  static String GetOrderPurcaseInfoUrl() {
                return httpMethod() + "://"+ serverName() + ":" + port() +"/order/getPurchaseInfo.json";
        }
        public  static String GetBasicInfoUrl() {
                return httpMethod() + "://"+ serverName() + ":" + port() +"/order/getBasicInfo.json";
        }
        public  static String GetOrderChuyunInfoUrl() {
                return httpMethod() + "://"+ serverName() + ":" + port() +"/order/getChuyunInfo.json";
        }
        public  static String GetOrderFukuangInfoUrl() {
                return httpMethod() + "://"+ serverName() + ":" + port() +"/order/getFukuangInfo.json";
        }
        public  static String GetOrderShouhuiInfoUrl() {
                return httpMethod() + "://"+ serverName() + ":" + port() +"/order/getShouhuiInfo.json";
        }

        public  static String SearchApprovalUrl() {
                return httpMethod() + "://"+serverName()+":" + port() +"/approval/search.json";
        }
        public  static String AuditApprovalUrl() {
                return httpMethod() + "://"+serverName()+":"+port() +"/approval/audit.json";
        }

        public  static String LoginUrl() {
                return httpMethod() + "://"+serverName()+":"+port() +"/login/login.json";
        }

        public  static String GetProductUrl() {
                return httpMethod() + "://" + serverName() + ":" + port() + "/product/search.json";
        }
        public  static String GetProductImageUrl() {
                return httpMethod() + "://" + serverName() + ":" + port() + "/product/getImage.json";
        }

}


