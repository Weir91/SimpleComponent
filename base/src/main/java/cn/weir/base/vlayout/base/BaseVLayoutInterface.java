package cn.weir.base.vlayout.base;

import java.util.List;

/**
 * @author Weir.
 * @date 2018/3/8.
 */
public interface BaseVLayoutInterface<T> {

    void setData(List<T> dataList);

    void addData(List<T> dataList);
}
