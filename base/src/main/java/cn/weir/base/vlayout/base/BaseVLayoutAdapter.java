package cn.weir.base.vlayout.base;

import android.view.ViewGroup;

import com.alibaba.android.vlayout.DelegateAdapter;
import com.alibaba.android.vlayout.LayoutHelper;
import com.alibaba.android.vlayout.layout.LinearLayoutHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Weir.
 * @date 2018/3/8.
 */
public abstract class BaseVLayoutAdapter<T, VH extends RecyclerView.ViewHolder> extends DelegateAdapter.Adapter<VH>
        implements BaseVLayoutInterface<T> {

    private List<T> entities = new ArrayList<>();

    public List<T> getEntities() {
        return entities;
    }

    @Deprecated
    @Override
    public LayoutHelper onCreateLayoutHelper() {
        return onMyCreateLayoutHelper();
    }

    @Deprecated
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onMyCreateViewHolder(parent, viewType);
    }

    @Deprecated
    @Override
    public void onBindViewHolder(VH holder, int position) {
        onMyBindViewHolder(holder, position);
    }

    @Deprecated
    @Override
    public int getItemViewType(int position) {
        return getMyItemViewType(position);
    }

    public LayoutHelper onMyCreateLayoutHelper() {
        return new LinearLayoutHelper();
    }

    public abstract VH onMyCreateViewHolder(ViewGroup parent, int viewType);

    public abstract void onMyBindViewHolder(VH holder, int position);

    public abstract int getMyItemViewType(int position);

    @Override
    public int getItemCount() {
        return entities.size();
    }

    @Override
    public void setData(List<T> dataList) {
        if (dataList != null) {
            entities = dataList;
            notifyDataSetChanged();
        } else {
            entities.clear();
            notifyDataSetChanged();
        }
    }

    @Override
    public void addData(List<T> dataList) {
        if (dataList != null) {
            entities.addAll(dataList);
            notifyDataSetChanged();
        }
    }
}
