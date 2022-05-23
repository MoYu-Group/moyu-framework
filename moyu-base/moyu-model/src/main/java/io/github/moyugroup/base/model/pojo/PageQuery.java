package io.github.moyugroup.base.model.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * 分页查询基类
 * <p>
 * Created by fanfan on 2022/05/23.
 */
@Getter
@Setter
public class PageQuery {

    /**
     * 默认当前页码
     */
    private static final int DEFAULT_PAGE_NUM = 1;
    /**
     * 默认每页记录数量
     */
    private static final int DEFAULT_PAGE_SIZE = 10;

    /**
     * 当前页数，第一页值为1
     */
    private Integer currentPage = DEFAULT_PAGE_NUM;

    /**
     * 每页的条数
     */
    private Integer pageSize = DEFAULT_PAGE_SIZE;

}
