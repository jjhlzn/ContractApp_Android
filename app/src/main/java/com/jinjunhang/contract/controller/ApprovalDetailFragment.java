package com.jinjunhang.contract.controller;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.contract.model.Approval;
import com.jinjunhang.contract.service.ApprovalService;
import com.jinjunhang.contract.service.AuditApprovalResponse;
import com.jinjunhang.contract.service.ServerResponse;

/**
 * Created by lzn on 16/4/6.
 */
public class ApprovalDetailFragment extends android.support.v4.app.Fragment {

    public final static String EXTRA_APPROVAL = "approval";
    public final static String EXTRA_POSITON = "position";

    private Approval mApproval;
    private Button  mPassButton;
    private Button  mUnpassButton;
    private LoadingAnimation mLoading;

    private TextView mStatusTV;
    private TextView mResultTV;
    private int mPositon;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_approval, container, false);


        mLoading = new LoadingAnimation(getActivity());

        ((ApprovalDetailActivity)getActivity()).setOnBackPressedListener(new ApprovalDetailOnBackPressedListener());

        mApproval = (Approval)getActivity().getIntent().getSerializableExtra(EXTRA_APPROVAL);
        mPositon = getActivity().getIntent().getIntExtra(EXTRA_POSITON, -1);

        TextView objectTV = (TextView)v.findViewById(R.id.approval_detail_object);
        objectTV.setText(mApproval.getApprovalObject());

        TextView keywordTV = (TextView)v.findViewById(R.id.approval_detail_keyword);
        keywordTV.setText(mApproval.getKeyword());

        TextView amountTV = (TextView)v.findViewById(R.id.approval_detail_amount);
        amountTV.setText("¥" + String.format("%.2f", mApproval.getAmount()));

        TextView reporterTV = (TextView)v.findViewById(R.id.approval_detail_repoter);
        reporterTV.setText(mApproval.getReporter());

        TextView reportDateTV = (TextView)v.findViewById(R.id.approval_detail_reportDate);
        reportDateTV.setText(mApproval.getReportDate());

        mStatusTV = (TextView)v.findViewById(R.id.approval_detail_status);
        mStatusTV.setText(mApproval.getStatus());

        mResultTV = (TextView)v.findViewById(R.id.approval_detail_result);
        mResultTV.setText(mApproval.getApprovalResult());

        mPassButton = (Button)v.findViewById(R.id.approval_detail_passButton);
        mPassButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Utils.showConfirmMessage(getActivity(), "确定批准吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AuditApprovalTask().execute("0");

                    }
                });
            }
        });

        mUnpassButton = (Button)v.findViewById(R.id.approval_detail_unpassButton);
        mUnpassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.showConfirmMessage(getActivity(), "确定不批准吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new AuditApprovalTask().execute("-1");

                    }
                });
            }
        });


        if (mApproval.getStatus().equals("待批")) {
            mResultTV.setText("无");
        } else {
            hideButtons();
        }


        return v;
    }

    public class ApprovalDetailOnBackPressedListener implements SingleFragmentActivity.OnBackPressedListener{
        @Override
        public void doBack() {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_APPROVAL, mApproval);
            intent.putExtra(EXTRA_POSITON, mPositon);
            getActivity().setResult(1, intent);
            getActivity().finish();
        }
    }

    private void hideButtons() {
        mPassButton.setVisibility(View.GONE);
        mUnpassButton.setVisibility(View.GONE);
    }


    class AuditApprovalTask extends AsyncTask<String, Void, AuditApprovalResponse> {
        private boolean isPass;

        @Override
        protected AuditApprovalResponse doInBackground(String... params) {
            isPass = params[0] == "0";
            return new ApprovalService().audit(PrefUtils.getFromPrefs(getActivity(), PrefUtils.PREFS_LOGIN_USERNAME_KEY, ""),
                    mApproval.getId(), params[0]);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoading.show("");
        }

        @Override
        protected void onPostExecute(AuditApprovalResponse resp) {
            super.onPostExecute(resp);
            mLoading.dismiss();
            if (resp.getStatus() == ServerResponse.FAIL) {
                Utils.showServerErrorDialog(getActivity());
                return;
            }

            if (resp.isResult()) {
                mStatusTV.setText("已批");
                mApproval.setStatus("已批");
                hideButtons();
            } else {
                Utils.showMessage(getActivity(), "审批失败");
            }

            if (isPass) {
                mResultTV.setText("批准");
                mApproval.setApprovalResult("批准");
            } else {
                mResultTV.setText("不批准");
                mApproval.setApprovalResult("不批准");
            }
        }
    }



}
