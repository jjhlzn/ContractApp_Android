package com.jinjunhang.framework.controller;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jinjunhang.contract.R;
import com.jinjunhang.framework.lib.Utils;
import com.jinjunhang.framework.service.PagedServerResponse;
import com.jinjunhang.framework.service.ServerResponse;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lzn on 16/6/10.
 */
public class PagableController implements SwipeRefreshLayout.OnRefreshListener, Serializable {

    private final static String TAG = "PagableController";

    private Activity mActivity;
    private ListView mListView;
    private PagableArrayAdapter mPagableArrayAdapter;
    private PagableRequestHandler mPagableRequestHandler;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //用于load more
    private View mFooterView;
    private boolean mIsLoading;
    private boolean mIsRefreshing;
    private boolean mMoreDataAvailable;
    private int mPageIndex;

    public PagableController() {
        mIsLoading = false;
        mMoreDataAvailable = true;
        mPageIndex = 0;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void setListView(ListView listView) {
        mListView = listView;
    }

    public void setPagableArrayAdapter(PagableArrayAdapter pagableArrayAdapter) {
        mPagableArrayAdapter = pagableArrayAdapter;
        mListView.setAdapter(mPagableArrayAdapter);
        setFootView();
    }

    public void setPagableRequestHandler(PagableRequestHandler pagableRequestHandler) {
        mPagableRequestHandler = pagableRequestHandler;
    }

    public void setSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        mSwipeRefreshLayout = swipeRefreshLayout;
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    public void setOnScrollListener(AbsListView.OnScrollListener listener) {
        mListView.setOnScrollListener(listener);
    }

    public PagableArrayAdapter getPagableArrayAdapter() {
        return mPagableArrayAdapter;
    }

    public int getPageIndex() {
        return mPageIndex;
    }

    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //Log.d(TAG, "mMoreDataAvailable = " + mMoreDataAvailable);
        if (!mMoreDataAvailable)
            return;

        int lastInScreen = firstVisibleItem + visibleItemCount;

        if (!mIsLoading) {
            //Log.d(TAG, "lastInScreen = " + lastInScreen + ", totalItemCount = " + totalItemCount);
            if (lastInScreen == totalItemCount) {

                //Log.d(TAG, "start loading more");
                new PagableTask(this, mPagableRequestHandler).execute();
            }
        }
    }

    @Override
    public void onRefresh() {
        //Log.d(TAG, "onRefresh");
        if (!mIsLoading) {
            mIsRefreshing = true;
            mMoreDataAvailable = true;
            mPageIndex = 0;
            new PagableTask(this, mPagableRequestHandler).execute();
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    private void setFootView() {
        mFooterView = LayoutInflater.from(mActivity).inflate(R.layout.loading_view, null);

        resetFootView();
        mListView.addFooterView(mFooterView, null, false);

    }

    private void resetFootView() {
        if (!mMoreDataAvailable) {
            mFooterView.findViewById(R.id.loading_progressbar).setVisibility(View.GONE);
            if (mPagableArrayAdapter.getCount() > 0)
                ((TextView)mFooterView.findViewById(R.id.loading_message)).setText("已加载全部数据");
            else {
                ((TextView) mFooterView.findViewById(R.id.loading_message)).setText("没有找到任何数据");
                ((TextView) mFooterView.findViewById(R.id.loading_message)).setTextSize(15);
            }
        }
    }

    public static abstract class PagableArrayAdapter<T> extends ArrayAdapter {
        private List<T> mDataSet;



        public PagableArrayAdapter(PagableController pagableController, List<T> dataSet) {
            super(pagableController.mActivity, 0, dataSet);
            this.mDataSet = dataSet;
        }

        public void addMoreData(List<T> moreData) {
            if (moreData == null)
                return;
            mDataSet.addAll(moreData);
            notifyDataSetChanged();
        }

        public void setDataSet(List<T> dataSet) {
            mDataSet = dataSet;
            notifyDataSetChanged();
        }

        public List<T> getDataSet() {
            return mDataSet;
        }

        @Override
        public int getCount() {
            return mDataSet.size();
        }

        @Override
        public T getItem(int position) {
            return mDataSet.get(position);
        }

    }

    public interface PagableRequestHandler {
        public PagedServerResponse handle();
    }

    public static class PagableTask extends AsyncTask<Void, Void, PagedServerResponse> {
        private final static String TAG = "PagableTask";
        private PagableController mPagableController;
        private PagableRequestHandler mPagableHandler;

        public PagableTask(PagableController pagableController, PagableRequestHandler pagableHandler) {
            mPagableController = pagableController;
            mPagableHandler = pagableHandler;
        }

        @Override
        protected PagedServerResponse doInBackground(Void... params) {
            Log.d(TAG, "doInBackground");
            return mPagableHandler.handle();
            //return execute();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mPagableController.mIsLoading = true;
            if (mPagableController.mIsRefreshing) {
                mPagableController.mSwipeRefreshLayout.setRefreshing(true);
                Log.d(TAG, "start refresh");
            }
        }

        @Override
        protected void onPostExecute(PagedServerResponse resp) {
            super.onPostExecute(resp);

            if (mPagableController.mIsRefreshing) {
                mPagableController.mSwipeRefreshLayout.setRefreshing(false);
            }

            if (resp.getStatus() != ServerResponse.SUCCESS) {
                Log.e(TAG, "resp return error, status = " + resp.getStatus() + ", errorMessage = " + resp.getErrorMessage());
                Utils.showMessage(mPagableController.mActivity, resp.getErrorMessage());
                mPagableController.mIsRefreshing = false;
                mPagableController.mIsLoading = false;
                return;
            }

            if (mPagableController.mIsRefreshing) {
                //Log.d(TAG, "reset dataset");
                mPagableController.mPagableArrayAdapter.setDataSet(resp.getResultSet());
            } else{
                //Log.d(TAG, "add more data");
                mPagableController.mPagableArrayAdapter.addMoreData(resp.getResultSet());
            }

            if (resp.getTotalNumber() <= mPagableController.mPagableArrayAdapter.getCount()) {
                mPagableController.mMoreDataAvailable = false;
            } else {
                mPagableController.mMoreDataAvailable = true;
            }

            mPagableController.mIsRefreshing = false;

            mPagableController.mIsLoading = false;
            mPagableController.mPageIndex += 1;
            Log.d(TAG, "loading complete");

            mPagableController.resetFootView();

        }
    }


}
