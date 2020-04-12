package com.smartcontrol.service;

import com.smartcontrol.model.Device;
import com.smartcontrol.util.PageBean;
import com.smartcontrol.util.TableData;

import java.util.List;

/**
 * @Author: laizc
 * @Date: Created in 10:04 2019-06-27
 */
public interface DeviceService {
    /**
     * 获取全部数据
     *
     * @return
     */
    List<Device> list();

    /**
     * 获取表单数据
     *
     * @param pageBean
     * @return
     */
    TableData<Device> getTableData(PageBean pageBean);

    /**
     * 批量删除
     *
     * @param ids id集合 用,隔开
     */
    void deleteBatch(String ids);

    /**
     * 添加
     *
     * @param name  设备名
     * @param unit  调节单位
     * @param value 调节值
     */
    void add(String name, String unit, String value);

    /**
     * 更新
     *
     * @param id    主键
     * @param name  设备名
     * @param unit  调节单位
     * @param value 调节值
     */
    void update(int id, String name, String unit, String value);

    /**
     * 获取设备列表
     * @return 设备列表
     */
    List<Device> getDevices();
}
