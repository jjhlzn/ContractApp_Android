package com.jinjunhang.contract.service;

/**
 * Created by lzn on 16/3/23.
 */
public class ServiceConfiguration {
        //public final static String serverName = "192.168.1.50";
        public final static String serverName1 = "jjhtest.hengdianworld.com";
        public final static int port1 = 80;

        public final static String serverName2 = "www.jinjunhang.com";
        public final static int port2 = 3000;

        public final static String serverName3 = "oa.lloydind.com";
        public final static int port3 = 10080;
        public static String httpMethod() {
                return "http";
        }

        public static String serverName() {
                return serverName1;
        }

        public static int port() {
                return port1;
        }


        public final static String SeachOrderUrl =  httpMethod() + "://"+ serverName() + ":" + port() +"/order/search.json";
        public final static String GetOrderPurcaseInfoUrl = httpMethod() + "://"+ serverName() + ":" + port() +"/order/getPurchaseInfo.json";
        public final static String GetBasicInfoUrl = httpMethod() + "//"+ serverName() + ":" + port() +"/order/getBasicInfo.json";
        public final static String GetOrderChuyunInfoUrl = httpMethod() + "://"+ serverName() + ":" + port() +"/order/getChuyunInfo.json";
        public final static String GetOrderFukuangInfoUrl = httpMethod() + "://"+ serverName() + ":" + port() +"/order/getFukuangInfo.json";
        public final static String GetOrderShouhuiInfoUrl = httpMethod() + "://"+ serverName() + ":" + port() +"/order/getShouhuiInfo.json";

        public final static String SearchApprovalUrl = httpMethod() + "://"+serverName()+":" + port() +"/approval/search.json";
        public final static String AuditApprovalUrl = httpMethod() + "://"+serverName()+":"+port() +"/approval/audit.json";

        public final static String LoginUrl = httpMethod() + "://"+serverName()+":"+port() +"/login/login.json";

        public final static String GetProductUrl = httpMethod() + "://" + serverName() + ":" + port() + "/product/search.json";
        public final static String GetProductImageUrl = httpMethod() + "://" + serverName() + ":" + port() + "/product/getImage.json";

}


