package com.smartcontrol.service;

import com.smartcontrol.util.PageBean;
import com.smartcontrol.util.TableData;

import java.util.List;

/**
 * @Author: laizc
 * @Date: Created in 9:46 2019-06-27
 */
public interface BaseService<T> {
    /**
     * 查询所有
     *
     * @return 返回所有数据
     */
    List<T> selectAll();

    /**
     * 添加
     *
     * @param t   实体
     *
     * @return
     */
    int save(T t);

    /**
     * 修改
     *
     * @param t
     *            实体
     * @return
     */
    int updateByPrimaryKey(T t);

    /**
     * 根据主键删除
     *
     * @param t   主键
     *
     * @return
     */
    int deleteByPrimaryKey(int t);

    /**
     * 查询表格列表
     * @param
     * @return
     */
    TableData<T> getTableData(PageBean pageBean);
}
