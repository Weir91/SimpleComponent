package cn.weir.base.vlayout.base;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

/**
 * VLayout配置项
 *
 * @author Weir
 * @date 2018-03-08
 */
public class BaseVLayoutConfigBuilder {
    private int page = 0;
    private int pageSize = 20;
    private int beforeLoadMoreItemCount = 5;
    private boolean isAutoSetEmptyView = false;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayout.OnRefreshListener swipeRefreshListener;
    private List<BaseVLayoutAdapter> adapterList = new ArrayList<>();
    private BaseVLayoutAdapter loadMoreAdapter;
    private DataListCallBack loadCallback;
    private boolean showLoadMoreTipsView = true;
    private String loadingText = "加载中...";
    private String loadEndText = "~ 没有更多了 ~";

    /**
     * 实例化Builder
     *
     * @return BaseVLayoutConfigBuilder
     */
    public static BaseVLayoutConfigBuilder newBuilder() {
        return new BaseVLayoutConfigBuilder();
    }

    /**
     * 设置RecyclerView
     *
     * @param recyclerView RecyclerView
     * @return
     */
    public BaseVLayoutConfigBuilder setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
        return this;
    }

    /**
     * 设置加载更多下 刷新布局
     * 备注：框架自行实现刷新流程
     *
     * @param swipeRefreshLayout SwipeRefreshLayout
     * @return
     */
    public BaseVLayoutConfigBuilder setLoadMoreSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        return this;
    }

    /**
     * 设置加载更多下 刷新布局
     * 备注：只有在自行实现下拉刷新的情况下使用，否则请不要设置监听，框架自行实现刷新流程
     *
     * @param swipeRefreshLayout SwipeRefreshLayout
     * @param listener           刷新回调
     * @return
     */
    public BaseVLayoutConfigBuilder setLoadMoreSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout
            , SwipeRefreshLayout.OnRefreshListener listener) {
        this.swipeRefreshLayout = swipeRefreshLayout;
        this.swipeRefreshListener = listener;
        return this;
    }

    /**
     * 设置Adapter
     *
     * @param adapter BaseVLayoutAdapter
     * @return
     */
    public BaseVLayoutConfigBuilder addBaseVLayoutAdapter(BaseVLayoutAdapter adapter) {
        this.adapterList.add(adapter);
        return this;
    }

    /**
     * 设置Adapter
     *
     * @param adapter BaseVLayoutAdapter
     * @return
     */
    public BaseVLayoutConfigBuilder addBaseVLayoutAdapter(int position, BaseVLayoutAdapter adapter) {
        this.adapterList.add(position, adapter);
        return this;
    }

    /**
     * 设置Adapter
     *
     * @param adapter BaseVLayoutAdapter
     * @return
     */
    public BaseVLayoutConfigBuilder addBaseVLayoutAdapter(BaseVLayoutAdapter adapter, boolean isLoadMoreAdapter) {
        if (isLoadMoreAdapter) {
            this.loadMoreAdapter = adapter;
        } else {
            this.adapterList.add(adapter);
        }
        return this;
    }

    /**
     * 提前几项加载更多数据
     *
     * @param beforeLoadMoreItemCount item数
     * @return
     */
    public BaseVLayoutConfigBuilder setBeforeLoadMoreItemCount(int beforeLoadMoreItemCount) {
        this.beforeLoadMoreItemCount = beforeLoadMoreItemCount;
        return this;
    }

    /**
     * 设置数据加载回调监听
     *
     * @param callBack
     * @return
     */
    public BaseVLayoutConfigBuilder setLoadCallBack(DataListCallBack callBack) {
        this.loadCallback = callBack;
        return this;
    }

    /**
     * 设置是否自动显示/隐藏空白页
     *
     * @param isAutoSetEmptyView
     * @return
     */
    public BaseVLayoutConfigBuilder setAutoSetEmptyView(boolean isAutoSetEmptyView) {
        this.isAutoSetEmptyView = isAutoSetEmptyView;
        return this;
    }

    /**
     * 设置分页参数，起始页码
     *
     * @param page
     * @return
     */
    public BaseVLayoutConfigBuilder setPage(int page) {
        this.page = page;
        return this;
    }

    /**
     * 设置分页参数，页长
     *
     * @param pageSize
     * @return
     */
    public BaseVLayoutConfigBuilder setPageSize(int pageSize) {
        this.pageSize = pageSize;
        return this;
    }

    public BaseVLayoutConfigBuilder setShowLoadMoreTipsView(boolean showLoadMoreTipsView) {
        this.showLoadMoreTipsView = showLoadMoreTipsView;
        return this;
    }

    public BaseVLayoutConfigBuilder setLoadMoreTipsText(String loadingText, String loadEndText) {
        this.loadingText = loadingText;
        this.loadEndText = loadEndText;
        return this;
    }

    /**
     * 构建返回体
     *
     * @return
     */
    public VLayoutConfigBean build() {
        VLayoutConfigBean bean = new VLayoutConfigBean();
        bean.recyclerView = recyclerView;
        bean.swipeRefreshLayout = swipeRefreshLayout;
        bean.swipeRefreshListener = swipeRefreshListener;
        bean.adapterList = adapterList;
        bean.loadMoreAdapter = loadMoreAdapter;
        bean.beforeLoadMoreItemCount = beforeLoadMoreItemCount;
        bean.isAutoSetEmptyView = isAutoSetEmptyView;
        bean.loadCallback = loadCallback;
        bean.page = page;
        bean.pageSize = pageSize;
        bean.showLoadMoreTipsView = showLoadMoreTipsView;
        bean.loadingText = loadingText;
        bean.loadEndText = loadEndText;
        return bean;
    }

    public class VLayoutConfigBean {
        public int page;
        public int pageSize;
        public RecyclerView recyclerView;
        public SwipeRefreshLayout swipeRefreshLayout;
        public SwipeRefreshLayout.OnRefreshListener swipeRefreshListener;
        public List<BaseVLayoutAdapter> adapterList;
        public BaseVLayoutAdapter loadMoreAdapter;
        public int beforeLoadMoreItemCount;
        public DataListCallBack loadCallback;
        public boolean isAutoSetEmptyView;
        public boolean showLoadMoreTipsView;
        public String loadingText;
        public String loadEndText;
    }
}