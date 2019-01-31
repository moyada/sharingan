package io.moyada.sharingan.application.data;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author xueyikang
 * @create 2018-01-01 18:48
 */
public class PageData<T> implements Serializable {

    private static final long serialVersionUID = -7092253365043364716L;

    private static final PageData<?> EMPTY = new PageData<>(0, 0, 0, Collections.emptyList());

    private Integer pageIndex;

    private Integer pageSize;

    private Integer total;

    private List<T> data;

    public PageData(Integer pageIndex, Integer pageSize, Integer total, List<T> data) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.total = total;
        this.data = data;
    }

    @SuppressWarnings("unchecked")
    public static <T> PageData<T> nil() {
        return (PageData<T>) EMPTY;
    }

    public Integer getPageIndex() {
        return pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public List<T> getData() {
        return data;
    }
}
