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
        public final static String GetOrderPurcaseInfoUrl = "http://"+ serverName + ":" + port +"/order/getPurchaseInfo.json";
        public final static String GetBasicInfoUrl = "http://"+ serverName + ":" + port +"/order/getBasicInfo.json";
        public final static String GetOrderChuyunInfoUrl = "http://"+ serverName + ":" + port +"/order/getChuyunInfo.json";
        public final static String GetOrderFukuangInfoUrl = "http://"+ serverName + ":" + port +"/order/getFukuangInfo.json";
        public final static String GetOrderShouhuiInfoUrl = "http://"+ serverName2 + ":" + port2 +"/order/getShouhuiInfo.json";

        public final static String SearchApprovalUrl = "http://"+serverName2+":" + port2+"/approval/search.json";
        public final static String AuditApprovalUrl = "http://"+serverName2+":"+port2+"/approval/audit.json";

        public final static String LoginUrl = "http://"+serverName2+":"+port2+"/login/login.json";

        public final static String GetProductUrl = "http://" + serverName + ":" + port + "/product/search.json";

}


