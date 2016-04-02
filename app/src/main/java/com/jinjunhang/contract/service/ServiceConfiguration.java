package com.jinjunhang.contract.service;

/**
 * Created by lzn on 16/3/23.
 */
public class ServiceConfiguration {
        //public final static String serverName = "localhost"
        public final static String serverName = "jjhtest.hengdianworld.com";

        public final static int port = 80;
        public final static String serverName2 = "www.jinjunhang.com";
        public final static int port2 = 3000;
        public final static String SeachOrderUrl = "http://"+ serverName + ":" + port +"/order/search.json";
        public final static String GetOrderPurcaseInfoUrl = "http://"+ serverName2 + ":" + port2 +"/order/getPurchaseInfo.json";
        public final static String GetBasicInfoUrl = "http://"+ serverName2 + ":" + port2 +"/order/getBasicInfo.json";
        public final static String GetOrderChuyunInfoUrl = "http://"+ serverName2 + ":" + port2 +"/order/getChuyunInfo.json";
        public final static String GetOrderFukuangInfoUrl = "http://"+ serverName2 + ":" + port2 +"/order/getFukuangInfo.json";
        public final static String GetOrderShouhuiInfoUrl = "http://"+ serverName2 + ":" + port2 +"/order/getShouhuiInfo.json";

        public final static String SeachApprovalUrl = "http://"+serverName+":" + port+"/approval/search.json";
        public final static String AuditApprovalUrl = "http://"+serverName+":"+port+"/approval/audit.json";

        public final static String LoginUrl = "http://"+serverName+":"+port+"/login/login.json";
}


