package cn.moyada.dubbo.faker.web.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author xueyikang
 * @create 2018-01-01 18:48
 */
public class PageVO<T> implements Serializable {

    private static final long serialVersionUID = -7092253365043364716L;

    private Integer pageIndex;

    private Integer pageSize;

    private Integer total;

    private List<T> data;

    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}
