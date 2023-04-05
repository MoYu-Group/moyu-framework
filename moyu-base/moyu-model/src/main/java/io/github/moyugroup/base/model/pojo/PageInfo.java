package io.github.moyugroup.base.model.pojo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 分页信息封装
 * <p>
 * Created by fanfan on 2022/05/23.
 */
@Getter
@Setter
@ToString
public class PageInfo<T> {

    /**
     * 当前页
     */
    private long currentPage;

    /**
     * 每页记录数
     */
    private long pageSize;

    /**
     * 总记录数
     */
    private long totalCount;

    /**
     * 总页数
     */
    private long totalPage;

    /**
     * 分页数据
     */
    private List<T> data;

    /**
     * 构建分页对象
     *
     * @param currentPage 当前页
     * @param pageSize    页大小
     * @param totalCount  总记录数
     * @param data        返回数据
     * @param <T>         泛型类型
     * @return PageInfo<T>
     */
    public static <T> PageInfo<T> buildPageInfo(long currentPage, long pageSize, long totalCount, List<T> data) {
        PageInfo<T> pageInfo = new PageInfo<>();
        pageInfo.setCurrentPage(currentPage);
        pageInfo.setPageSize(pageSize);
        pageInfo.setTotalCount(totalCount);
        pageInfo.setData(data);
        return pageInfo;
    }

    /**
     * 获取总页数
     */
    public long getTotalPage() {
        if (pageSize == 0 || totalCount == 0) {
            return 0;
        }
        return totalCount % pageSize == 0 ? (totalCount / pageSize) : (totalCount / pageSize) + 1;
    }

}
