package cn.weir.base.vlayout.base;


public interface DataListCallBack<T> {
    void onSuccess(ListData<T> data);

    void onError(int code, String errorMessage);

    void onComplete();
}
