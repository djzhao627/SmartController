package com.smartcontrol.service;

import com.github.pagehelper.PageHelper;
import com.smartcontrol.dao.DeviceDao;
import com.smartcontrol.model.Device;
import com.smartcontrol.util.PageBean;
import com.smartcontrol.util.TableData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: laizc
 * @Date: Created in 10:05 2019-06-27
 */
@Service
public class DeviceServiceImpl implements DeviceService {
    @Resource
    private DeviceDao deviceDao;

    @Override
    public List<Device> list() {
        return deviceDao.selectAll();
    }

    @Override
    public TableData<Device> getTableData(PageBean pageBean) {
        int count = deviceDao.selectCount(null);
        if (count > 0) {
            PageHelper.startPage((pageBean.getOffset() / pageBean.getLimit()) + 1, pageBean.getLimit());
            Example example = new Example(Device.class);
            if (StringUtils.isNotBlank(pageBean.getOrder())) {
                example.setOrderByClause(pageBean.getSort() + " " + pageBean.getOrder());
            }
            Example.Criteria criteria = example.createCriteria();
            if (StringUtils.isNotBlank(pageBean.getSearch())) {
                criteria.andLike("name", "%" + pageBean.getSearch() + "%");
            }
            List<Device> list = deviceDao.selectByExample(example);
            return TableData.bulid(count, list);
        }
        return TableData.empty();
    }

    @Override
    public void deleteBatch(String ids) {
        deviceDao.deleteByIds(ids);
    }

    @Override
    public void add(String name, String unit, String value) {
        Example example = new Example(Device.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("name", name);
        List<Device> list = deviceDao.selectByExample(example);
        if (list.size() == 0) {
            Device device = new Device();
            device.setName(name);
            device.setUnit(unit);
            device.setValue(value);
            deviceDao.insertSelective(device);
        }
    }

    @Override
    public void update(int id, String name, String unit, String value) {
        Example example = new Example(Device.class);
        Example.Criteria criteria = example.createCriteria();
        Device device = new Device();
        device.setId(id);
        device.setName(name);
        device.setUnit(unit);
        device.setValue(value);
        deviceDao.updateByPrimaryKey(device);
    }

    @Override
    public List<Device> getDevices() {
        return deviceDao.selectAll();
    }

}
