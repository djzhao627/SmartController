package com.smartcontrol.dao;

import com.smartcontrol.model.Device;
import tk.mybatis.mapper.common.IdsMapper;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Author: laizc
 * @Date: Created in 10:54 2019-06-27
 */
public interface DeviceDao extends Mapper<Device>,IdsMapper<Device> {
}
