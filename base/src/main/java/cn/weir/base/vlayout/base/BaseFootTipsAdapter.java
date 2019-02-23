package cn.weir.base.vlayout.base;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author Weir.
 * @date 2018/3/9.
 */
public class BaseFootTipsAdapter extends BaseVLayoutAdapter<Object, BaseFootTipsAdapter.ViewHolder> {

    private Context mContext;
    private boolean isShow = true;
    private String tipContent = "加载中...";

    public BaseFootTipsAdapter(Context context) {
        mContext = context;
    }

    public void setShow(boolean show) {
        isShow = show;
        notifyDataSetChanged();
    }

    public void setTipContent(String tipContent) {
        this.tipContent = tipContent;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onMyCreateViewHolder(ViewGroup parent, int viewType) {
        TextView footView = new TextView(mContext);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 100);
        footView.setLayoutParams(layoutParams);
        footView.setGravity(Gravity.CENTER);
        footView.setText("加载中...");
        footView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        footView.setTextColor(Color.parseColor("#666666"));
        return new ViewHolder(footView);
    }

    @Override
    public void onMyBindViewHolder(ViewHolder holder, int position) {
        ((TextView) holder.itemView).setText(tipContent);
    }

    @Override
    public int getItemCount() {
        return isShow ? 1 : 0;
    }

    @Override
    public int getMyItemViewType(int position) {
        return 666;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
