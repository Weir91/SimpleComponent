package cn.weir.base.vlayout;

import android.content.Context;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.VirtualLayoutManager;
import com.blankj.utilcode.util.ToastUtils;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import cn.weir.base.R;
import cn.weir.base.vlayout.base.BaseFootTipsAdapter;
import cn.weir.base.vlayout.base.BaseVLayoutAdapter;
import cn.weir.base.vlayout.base.BaseVLayoutConfigBuilder;
import cn.weir.base.vlayout.base.BaseVLayoutListener;
import cn.weir.base.vlayout.base.DataListCallBack;
import cn.weir.base.vlayout.base.ListData;

/**
 * @author Weir.
 * @date 2018/3/7.
 */
public class SimpleVLayoutLoadmore<T> {

    private int pageLength = 20;
    private int pageNumber = 0;

    private boolean mHasMore = true;
    private boolean isLoading = false;
    private int beforeLoadMoreItemCount = 5;

    private boolean isAutoSetEmptyView = true;

    private DataListCallBack<T> loadCallback;

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView mRecyclerView;

    private DelegateAdapter mDelegateAdapter;
    private BaseVLayoutAdapter mMLoadMoreAdapter;
    private BaseFootTipsAdapter mFootTipsAdapter;
    private Context mContext;

    private BaseVLayoutConfigBuilder.VLayoutConfigBean mConfigBean;
    private BaseVLayoutListener mVListener;


    /**
     * 初始化VLayout 及 添加Adapter
     */
    public void initVLayout(Context context, BaseVLayoutConfigBuilder.VLayoutConfigBean config) {
        mContext = context;
        if (mContext == null) {
            throw new NullPointerException("Context 不能为空");
        }
        mConfigBean = config;
        if (mConfigBean == null) {
            throw new NullPointerException("VLayoutConfig未初始化");
        }
        mRecyclerView = mConfigBean.recyclerView;
        if (mRecyclerView == null) {
            throw new NullPointerException("RecyclerView未初始化");
        }

        this.pageNumber = mConfigBean.page;
        this.pageLength = mConfigBean.pageSize;
        this.isAutoSetEmptyView = mConfigBean.isAutoSetEmptyView;
        this.beforeLoadMoreItemCount = mConfigBean.beforeLoadMoreItemCount;
        this.loadCallback = mConfigBean.loadCallback;

        getVLayoutAdapter();
        setRefreshLayout();
        List<BaseVLayoutAdapter> adapterList = mConfigBean.adapterList;
        if (adapterList != null) {
            for (BaseVLayoutAdapter adapter : adapterList) {
                mDelegateAdapter.addAdapter(adapter);
            }
        }
        setLoadMoreAdapter();
        setFootViewAdapter();
    }

    public void setLoadmoreListener(BaseVLayoutListener listener) {
        mVListener = listener;
    }

    public void start() {
        if (mVListener != null) {
            mVListener.firstLoad();
        }
    }

    /**
     * 初始化VLayout适配器
     *
     * @return VLayout Adapter
     */
    private DelegateAdapter getVLayoutAdapter() {
        VirtualLayoutManager layoutManager = new VirtualLayoutManager(mContext, VirtualLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mDelegateAdapter = new DelegateAdapter(layoutManager, true);
        mRecyclerView.setAdapter(mDelegateAdapter);
        return mDelegateAdapter;
    }

    /**
     * 初始化SwipeRefreshLayout
     */
    private void setRefreshLayout() {
        this.refreshLayout = mConfigBean.swipeRefreshLayout;
        if (refreshLayout != null) {
            refreshLayout.setColorSchemeColors(ContextCompat.getColor(mContext,
                    R.color.colorPrimary));
            refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    if (mConfigBean.swipeRefreshListener != null) {
                        preFirstLoad(false);
                        mConfigBean.swipeRefreshListener.onRefresh();
                    } else {
                        preFirstLoad(true);
                    }
                }
            });
        }
    }

    /**
     * 初始化自动加载更多监听
     */
    private void setLoadMoreAdapter() {
        mMLoadMoreAdapter = mConfigBean.loadMoreAdapter;
        if (mMLoadMoreAdapter != null) {
            mDelegateAdapter.addAdapter(mMLoadMoreAdapter);
            mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    //hasMore: status of current mPage, means if there's more data, you have to maintain this status
                    VirtualLayoutManager lm = (VirtualLayoutManager) recyclerView.getLayoutManager();
                    int first = 0, last = 0, total = 0;
                    first = ((LinearLayoutManager) lm).findFirstVisibleItemPosition();
                    last = ((LinearLayoutManager) lm).findLastVisibleItemPosition();
                    total = recyclerView.getAdapter().getItemCount();
                    if (last > 0 && last >= total - beforeLoadMoreItemCount) {
                        //beforeLoadMoreItemCount: help to trigger load more listener earlier
                        if (mHasMore && !isLoading) {
                            pageNumber++;
                            preLoadMore();
                        }
                    }
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                }
            });
        }
    }

    /**
     * 首次加载 预操作
     */
    private void preFirstLoad(boolean needFirstLoad) {
        this.pageNumber = mConfigBean.page;
        isLoading = true;
        if (needFirstLoad) {
            if (mVListener != null) {
                mVListener.firstLoad();
            }
        }
    }

    /**
     * 加载更多 预操作
     */
    private void preLoadMore() {
        isLoading = true;
        if (mVListener != null) {
            mVListener.loadMore();
        }
    }


    /**
     * 分页
     *
     * @return 页码
     */
    public int getPageNumber(boolean isFirstLoad) {
        if (isFirstLoad) {
            isLoading = true;
            pageNumber = mConfigBean.page;
        }
        return pageNumber;
    }

    /**
     * 分页
     *
     * @return 页长（单页长度）
     */
    public int getPageLength() {
        return pageLength;
    }

    /**
     * 提前预加载Item数量
     *
     * @return 提前数量
     */
    private int getBeforeLoadMoreItemCount() {
        return beforeLoadMoreItemCount;
    }

    /**
     * 底部加载更多 状态
     */
    private void setFootViewAdapter() {
        mFootTipsAdapter = new BaseFootTipsAdapter(mContext);
        mDelegateAdapter.addAdapter(mFootTipsAdapter);
        mFootTipsAdapter.setTipContent(mConfigBean.loadingText);
        mFootTipsAdapter.setShow(mConfigBean.showLoadMoreTipsView);
    }

    /**
     * 首次加载回调
     *
     * @return 首次加载回调
     */
    public DataListCallBack getFirstLoadCallback() {
        DataListCallBack<T> callBack = new DataListCallBack<T>() {
            @Override
            public void onSuccess(ListData<T> data) {
                if (isAutoSetEmptyView && data.getList().size() == 0) {
                    if (mVListener != null) {
                        mVListener.showEmptyView();
                    }
                } else {
                    if (mVListener != null) {
                        mVListener.hideEmptyView();
                    }
                }
                mHasMore = data.isHasNext();
                if (loadCallback != null) {
                    loadCallback.onSuccess(data);
                }
                mMLoadMoreAdapter.setData(data.getList());

                if (mConfigBean.showLoadMoreTipsView) {
                    mFootTipsAdapter.setTipContent(mHasMore ? mConfigBean.loadingText : mConfigBean.loadEndText);
                }
            }

            @Override
            public void onError(int code, String errorMessage) {
                ToastUtils.showShort(errorMessage);
                mMLoadMoreAdapter.setData(null);
                if (loadCallback != null) {
                    loadCallback.onError(code, errorMessage);
                }
                if (mVListener != null) {
                    mVListener.showEmptyView();
                }
            }

            @Override
            public void onComplete() {
                if (refreshLayout != null) {
                    refreshLayout.setRefreshing(false);
                }
                if (loadCallback != null) {
                    loadCallback.onComplete();
                }
                isLoading = false;
            }
        };
        return callBack;
    }

    /**
     * 加载更多回调
     *
     * @return 加载更多回调
     */
    public DataListCallBack getLoadMoreCallback() {
        DataListCallBack<T> callBack = new DataListCallBack<T>() {
            @Override
            public void onSuccess(ListData<T> data) {
                mHasMore = data.isHasNext();
                if (loadCallback != null) {
                    loadCallback.onSuccess(data);
                }
                mMLoadMoreAdapter.addData(data.getList());

                if (mConfigBean.showLoadMoreTipsView) {
                    mFootTipsAdapter.setTipContent(mHasMore ? mConfigBean.loadingText : mConfigBean.loadEndText);
                }
            }

            @Override
            public void onError(int code, String errorMessage) {
                ToastUtils.showShort(errorMessage);
                if (loadCallback != null) {
                    loadCallback.onError(code, errorMessage);
                }
            }

            @Override
            public void onComplete() {
                isLoading = false;
                if (loadCallback != null) {
                    loadCallback.onComplete();
                }
            }
        };
        return callBack;
    }
}
