package com.jinjunhang.contract.service;

import com.jinjunhang.contract.model.Approval;
import com.jinjunhang.contract.model.Order;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by lzn on 16/4/6.
 */
public class ApprovalService extends BasicService {


    public SearchApprovalResponse search(String userId, String keyword, boolean containApproved,
                                         boolean containUnapproved, String startDate, String endDate, int pageNo, int pageSize) {
        String url = makeSearchApprovalUrl(userId, keyword, containApproved, containUnapproved, startDate, endDate, pageNo, pageSize);
        return sendRequest(url, SearchApprovalResponse.class, new ResponseHandler() {
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

    private String makeSearchApprovalUrl(String userId, String keyword, boolean containApproved,
                                         boolean containUnapproved, String startDate, String endDate, int pageNo, int pageSize) {
        String queryStr = "";
        try {
            queryStr = String.format("userid=%s&keyword=%s&containapproved=%s&containunapproved=%s&startdate=%s&enddate=%s&index=%d&pagesize=%d",
                    userId, URLEncoder.encode(keyword, "UTF-8"), containApproved, containUnapproved, startDate, endDate, pageNo, pageSize);
        }
        catch (UnsupportedEncodingException ex){

        }
        return ServiceConfiguration.SearchApprovalUrl + String.format("?%s", queryStr);
    }

    public AuditApprovalResponse audit(String userId, String approvalId, String result) {
        String url = makeAuditApprovalUrl(userId, approvalId, result);
        return sendRequest(url, AuditApprovalResponse.class, new ResponseHandler() {
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

    private String makeAuditApprovalUrl(String userId, String approvalId, String result) {
        return String.format(ServiceConfiguration.AuditApprovalUrl + "?userid=%s&approvalid=%s&result=%s",
                userId, approvalId, result);
    }
}
