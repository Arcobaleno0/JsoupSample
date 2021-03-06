package com.magnetic.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;

import com.afollestad.materialdialogs.MaterialDialog;
import com.framework.base.BaseFragment;
import com.framework.utils.ApkUtils;
import com.framework.utils.UIUtils;
import com.framework.widget.LoadMoreRecyclerView;
import com.framework.widget.StatusLayout;
import com.magnetic.R;
import com.magnetic.manager.DBManager;
import com.magnetic.mvp.model.MagneticModel;
import com.magnetic.mvp.presenter.MagneticListPresenterImpl;
import com.magnetic.mvp.view.ViewManager;
import com.xadapter.adapter.XRecyclerViewAdapter;

import java.util.List;

import io.reactivex.jsoup.network.bus.RxBus;

/**
 * by y on 2017/6/6.
 */

public class MagneticListFragment extends BaseFragment<MagneticListPresenterImpl> implements
        ViewManager.MagneticListView,
        SwipeRefreshLayout.OnRefreshListener,
        LoadMoreRecyclerView.LoadMoreListener {

    public static final String ZHIZHU_TAG = "zhizhu";
    protected int page = 0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LoadMoreRecyclerView recyclerView;
    private XRecyclerViewAdapter<MagneticModel> mAdapter;

    public static MagneticListFragment newInstance(String search, int position) {
        MagneticListFragment magneticListFragment = new MagneticListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(FRAGMENT_INDEX, position);
        bundle.putString(FRAGMENT_TYPE, search);
        magneticListFragment.setArguments(bundle);
        return magneticListFragment;
    }

    @Override
    protected void initBundle() {
        super.initBundle();
        tabPosition = bundle.getInt(FRAGMENT_INDEX);
        type = bundle.getString(FRAGMENT_TYPE);
    }

    @Override
    protected void initById() {
        swipeRefreshLayout = getView(R.id.refresh_layout);
        recyclerView = getView(R.id.recyclerView);
    }

    @Override
    protected void initActivityCreated() {
        if (!isPrepared || !isVisible || isLoad) {
            return;
        }
        initRecyclerView();
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.post(this::onRefresh);
        setLoad();
    }

    private void initRecyclerView() {
        mAdapter = new XRecyclerViewAdapter<>();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLoadingMore(this);
        recyclerView.setAdapter(
                mAdapter
                        .setLayoutId(R.layout.item_magnetic_list)
                        .onXBind((holder, position, magneticModel) -> holder.setTextView(R.id.tv_magnetic_name, magneticModel.title))
                        .setOnItemClickListener((view, position, info) -> {
                            switch (tabPosition) {
                                case 3:
                                    mPresenter.netWorkZhiZhuMagnetic(info.url);
                                    break;
                                default:
                                    onStartMagnetic(info.title, info.url);
                                    break;
                            }
                        })
        );
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_magnetic_list;
    }

    @Override
    protected MagneticListPresenterImpl initPresenter() {
        return new MagneticListPresenterImpl(this);
    }

    @Override
    protected void clickNetWork() {
        super.clickNetWork();
        if (!swipeRefreshLayout.isRefreshing()) {
            onRefresh();
        }
    }

    @Override
    public void onRefresh() {
        setStatusViewStatus(StatusLayout.SUCCESS);
        mPresenter.netWorkRequest(type, page = 0);
    }

    @Override
    public void onLoadMore() {
        if (swipeRefreshLayout.isRefreshing()) {
            return;
        }
        mPresenter.netWorkRequest(type, page);
    }

    @Override
    public void netWorkSuccess(List<MagneticModel> data) {
        if (isStatusViewNoNull()) {
            if (page == 0) {
                mAdapter.removeAll();
            }
            ++page;
            mAdapter.addAllData(data);
        }
    }

    @Override
    public void netWorkError() {
        if (isStatusViewNoNull()) {
            if (page != 0) {
                UIUtils.snackBar(coordinatorLayout, R.string.net_error);
            } else {
                mAdapter.removeAll();
                setStatusViewStatus(StatusLayout.ERROR);
            }
        }
    }

    @Override
    public void showProgress() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    public void hideProgress() {
        if (swipeRefreshLayout != null)
            swipeRefreshLayout.setRefreshing(false);
    }


    @Override
    public void noMore() {
        if (isStatusViewNoNull()) {
            if (page != 0) {
                UIUtils.snackBar(coordinatorLayout, R.string.data_empty);
            } else {
                mAdapter.removeAll();
                setStatusViewStatus(StatusLayout.EMPTY);
            }
        }
    }

    @Override
    public int getTabPosition() {
        return tabPosition;
    }

    @Override
    public void zhizhuMagnetic(MagneticModel magneticModel) {
        onStartMagnetic(magneticModel.title, magneticModel.url);
    }

    @Override
    public void onDestroyView() {
        if (mPresenter != null)
            mPresenter.cancelZhiZhuDetailNetWork();
        super.onDestroyView();
    }

    private void onStartMagnetic(String name, String url) {
        if (TextUtils.isEmpty(url)) {
            UIUtils.snackBar(coordinatorLayout, R.string.url_null);
            return;
        }
        boolean collectionEmpty = DBManager.isCollectionEmpty(url);
        int negativeRes = collectionEmpty ? R.string.collection : R.string.collection_no;
        new MaterialDialog
                .Builder(getActivity())
                .title(R.string.magnetic_title)
                .content(url)
                .positiveText(R.string.xl)
                .negativeText(R.string.copy)
                .neutralText(negativeRes)
                .onPositive((dialog, which) -> {
                    Intent xlIntent = ApkUtils.getXLIntent();
                    if (xlIntent == null) {
                        UIUtils.toast(R.string.xl_null);
                        return;
                    }
                    UIUtils.copy(url);
                    startActivity(xlIntent);
                })
                .onNegative((dialog, which) -> {
                    UIUtils.copy(url);
                    UIUtils.snackBar(coordinatorLayout, R.string.copy_success);
                })
                .onNeutral((dialog, which) -> {
                    if (collectionEmpty) {
                        DBManager.insertCollection(name, url);
                        UIUtils.snackBar(coordinatorLayout, R.string.collection_success);
                    } else {
                        DBManager.clearCollection(url);
                        UIUtils.snackBar(coordinatorLayout, R.string.collection_no_success);
                    }
                    RxBus.getInstance().post(CollectionFragment.class.getSimpleName(), url);
                })
                .show();
    }
}
