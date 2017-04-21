package com.fiction.fiction.zw81.detail.widget;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;

import com.fiction.R;
import com.fiction.fiction.zw81.detail.model.ZWListDetailModel;
import com.fiction.fiction.zw81.detail.presenter.ZWListDetailPresenter;
import com.fiction.fiction.zw81.detail.presenter.ZWListDetailPresenterImpl;
import com.fiction.fiction.zw81.detail.view.ZWListDetailView;
import com.framework.base.BaseActivity;
import com.framework.utils.UIUtils;
import com.framework.widget.EasyWebView;

/**
 * by y on 2017/4/6.
 */

public class ZWListDetailActivity extends BaseActivity
        implements ZWListDetailView, View.OnClickListener, EasyWebView.WebViewLoadListener {
    private static final String URL = "url";

    private Toolbar toolbar;
    private ContentLoadingProgressBar progressBar;
    private ZWListDetailPresenter presenter;
    private String onUrl = null;
    private String nextUrl = null;
    private EasyWebView webView;
    private FrameLayout frameLayout;

    public static void getInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString(URL, url);
        UIUtils.startActivity(ZWListDetailActivity.class, bundle);
    }

    @Override
    protected void initCreate(Bundle savedInstanceState) {
        webView.setLoadListener(this);
        presenter = new ZWListDetailPresenterImpl(this);
        Bundle extras = getIntent().getExtras();
        toolbar.setTitle(getString(R.string.app_name));
        setSupportActionBar(toolbar);
        presenter.startDetail(extras.getString(URL));
    }

    @Override
    protected void initById() {
        webView = getView(R.id.detail_webView);
        frameLayout = getView(R.id.btn_rootView);
        toolbar = getView(R.id.toolbar);
        progressBar = getView(R.id.progress_bar);
        getView(R.id.btn_next).setOnClickListener(this);
        getView(R.id.btn_on).setOnClickListener(this);
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_zw_list_detail;
    }

    @Override
    public void netWorkSuccess(ZWListDetailModel data) {
        onUrl = data.onPage;
        nextUrl = data.nextPage;
        toolbar.setTitle(data.title);
        webView.loadDataUrl(data.message);
        webView.post(() -> webView.scrollTo(0, 0));
    }

    @Override
    public void netWorkError() {
        UIUtils.snackBar(getView(R.id.rootView), getString(R.string.network_error));
    }

    @Override
    public void showProgress() {
        frameLayout.setVisibility(View.GONE);
        progressBar.show();
    }

    @Override
    public void hideProgress() {
        progressBar.hide();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (!TextUtils.isEmpty(nextUrl)) {
                    presenter.startDetail(nextUrl);
                } else {
                    UIUtils.toast(UIUtils.getString(R.string.on_empty));
                }
                break;
            case R.id.btn_on:
                if (!TextUtils.isEmpty(onUrl)) {
                    presenter.startDetail(onUrl);
                } else {
                    UIUtils.toast(UIUtils.getString(R.string.on_empty));
                }
                break;
        }
    }

    @Override
    public void loadingSuccess() {
        frameLayout.setVisibility(View.VISIBLE);
    }
}
