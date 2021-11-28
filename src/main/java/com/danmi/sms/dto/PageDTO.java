package com.danmi.sms.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wanghuinan
 * 自定义分页
 */
@Data
@NoArgsConstructor
public class PageDTO<T> {

    /**
     * 数据列表
     */
    private List<T> records;
    /**
     * 数据总条数
     */
    private Long total;
    /**
     * 一页多少条数据  默认10
     */
    private Long pageSize = 10L;
    /**
     * 页码 默认第一页
     */
    private Long pageNum = 1L;


    public PageDTO(IPage<T> iPage) {
        this.records = iPage.getRecords();
        this.total = iPage.getTotal();
        this.pageNum = iPage.getCurrent();
        this.pageSize = iPage.getSize();
    }


}
