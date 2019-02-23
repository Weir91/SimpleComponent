package cn.weir.base.vlayout.base;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ListData<T> {
    private Class<T> clazz;

    @SerializedName("content")
    private List<T> list;
    /**
     * 当前请求页数
     */
    @SerializedName("number")
    private int number;
    /**
     * 当前页返回的记录数
     */
    @SerializedName("numberOfElements")
    private int numberOfElements;
    /**
     * 总条数
     */
    @SerializedName("totalElements")
    private int totalElements;
    /**
     * 请求每页长度
     */
    @SerializedName("size")
    private int size;
    /**
     * 总页数
     */
    @SerializedName("totalPages")
    private int totalPages;

    /**
     * 是否第一页
     */
    private boolean first;
    /**
     * 是否最后一页
     */
    private boolean last;

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public List<T> getList() {
        if (list == null) {
            return list;
        }
        if (clazz != null) {
            List newList = new ArrayList();
            for (T o : list) {
                newList.add(new Gson().fromJson(o.toString(), clazz));
            }
            return newList;
        } else {
            return list;
        }
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getNumberOfElements() {
        return numberOfElements;
    }

    public void setNumberOfElements(int numberOfElements) {
        this.numberOfElements = numberOfElements;
    }

    public int getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(int totalElements) {
        this.totalElements = totalElements;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }

    public boolean isHasNext() {
        return isLast() == false;
    }
}