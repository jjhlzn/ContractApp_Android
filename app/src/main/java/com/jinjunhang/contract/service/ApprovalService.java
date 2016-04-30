package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.Approval;
import com.jinjunhang.contract.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by lzn on 16/4/6.
 */
public class ApprovalService extends BasicService {


    public SearchApprovalResponse search(String userId, String keyword, boolean containApproved,
                                         boolean containUnapproved, String startDate, String endDate, int pageNo, int pageSize) {

        Map<String, String> params = new LinkedHashMap();
        params.put("userid", userId);
        params.put("keyword", keyword);
        params.put("containapproved", containApproved + "");
        params.put("containunapproved", containUnapproved + "");
        params.put("startdate", startDate);
        params.put("enddate", endDate);
        params.put("index", pageNo + "");
        params.put("pagesize", pageSize + "");
        return sendRequest(ServiceConfiguration.SearchApprovalUrl(), params, SearchApprovalResponse.class, new ResponseHandler() {
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                SearchApprovalResponse resp = (SearchApprovalResponse)response;

                resp.setTotalNumber(json.getInt("totalNumber"));
                JSONArray array = json.getJSONArray("approvals");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject item = array.getJSONObject(i);
                    Approval approval = new Approval();
                    approval.setId(item.getString("id"));
                    approval.setKeyword(item.getString("keyword"));
                    approval.setAmount(item.getDouble("amount"));
                    approval.setReporter(item.getString("reporter"));
                    approval.setType(item.getString("type"));
                    approval.setStatus(item.getString("status"));
                    approval.setReportDate(item.getString("reportDate"));
                    approval.setApprovalObject(item.getString("approvalObject"));
                    approval.setApprovalResult(item.getString("approvalResult"));
                    resp.addApproval(approval);
                }

                return resp;
            }
        });
    }

    public AuditApprovalResponse audit(String userId, String approvalId, String result) {
        Map<String, String> params = new LinkedHashMap();
        params.put("userid", userId);
        params.put("approvalid", approvalId);
        params.put("result", result);
        return sendRequest(ServiceConfiguration.AuditApprovalUrl(), params, AuditApprovalResponse.class, new ResponseHandler() {
            @Override
            public ServerResponse handle(ServerResponse response, JSONObject json) throws JSONException {
                AuditApprovalResponse resp = (AuditApprovalResponse)response;
                JSONObject resultJson = json.getJSONObject("auditResult");
                resp.setResult(resultJson.getBoolean("result"));
                resp.setMessage(resultJson.getString("message"));
                return resp;
            }
        });
    }

}
