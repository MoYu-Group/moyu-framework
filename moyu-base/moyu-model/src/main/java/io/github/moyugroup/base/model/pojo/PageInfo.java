package io.github.moyugroup.base.model.pojo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 分页信息封装
 * <p>
 * Created by fanfan on 2022/05/23.
 */
@Getter
@Setter
public class PageInfo<T> {

    /**
     * 当前页
     */
    private int currentPage;

    /**
     * 每页记录数
     */
    private int pageSize;

    /**
     * 总记录数
     */
    private long total;

    /**
     * 分页数据
     */
    private List<T> list;

    /**
     * 获取总页数
     */
    public long getTotalPage() {
        if (pageSize == 0 || total == 0) {
            return 0;
        }
        return total % pageSize == 0 ? (total / pageSize) : (total / pageSize) + 1;
    }
}
