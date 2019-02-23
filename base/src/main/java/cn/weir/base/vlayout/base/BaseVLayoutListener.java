package cn.weir.base.vlayout.base;

/**
 * @author Weir.
 * @date 2019/2/22.
 */
public interface BaseVLayoutListener {

    /**
     * 首次加载
     */
    void firstLoad();

    /**
     * 加载更多
     */
    void loadMore();

    /**
     * 展示列表空白页
     */
    void showEmptyView();

    /**
     * 隐藏列表空白页
     */
    void hideEmptyView();
}
