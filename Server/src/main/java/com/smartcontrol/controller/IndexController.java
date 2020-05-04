package com.smartcontrol.controller;

import com.smartcontrol.model.Device;
import com.smartcontrol.service.DeviceService;
import com.smartcontrol.util.PageBean;
import com.smartcontrol.util.TableData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author: laizc
 * @Date: Created in 9:41 2019-06-27
 */
@CrossOrigin
@Controller
@RequestMapping("/index")
public class IndexController {
    @Autowired
    private DeviceService deviceService;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @GetMapping({"index.html", "/"})
    public ModelAndView index() {
        List<Device> list = deviceService.list();
        System.out.println(list);
        ModelAndView mv = new ModelAndView("index");
        return mv;
    }

    @GetMapping("/getTableData")
    @ResponseBody
    public TableData<Device> getTableData(PageBean pageBean) {
        if (pageBean.getLimit() == null) {
            pageBean.setLimit(10);
        }
        if (pageBean.getOffset() == null) {
            pageBean.setLimit(0);
        }
        TableData<Device> tableData = deviceService.getTableData(pageBean);
        return tableData;
    }

    @PostMapping("/delete")
    @ResponseBody
    public void delete(String ids) {
        deviceService.deleteBatch(ids);
    }

    @PostMapping("/add")
    @ResponseBody
    public void add(String name, String unit, String value) {
        deviceService.add(name, unit, value);
    }

    @PostMapping("/update")
    @ResponseBody
    public void update(int deviceId, String name, String unit, String value) {
        deviceService.update(deviceId, name, unit, value);
    }

    @PostMapping("/getEnv")
    @ResponseBody
    public String getEnv() {
        return jdbcTemplate.queryForObject("select `value` from sys_config where `key` = 'enviro'", String.class);
    }

    @PostMapping("/updateEnv")
    @ResponseBody
    public int updateEnv(String value) {
        return jdbcTemplate.update("update sys_config set `value` = ? where `key` = 'enviro'", value);
    }

    // ==========API==========
    @GetMapping("/getDevices")
    @ResponseBody
    public List<Device> getDevices() {
        return deviceService.getDevices();
    }

    @GetMapping("/updateTemperature")
    @ResponseBody
    public String updateTemperature(String temp) {
        // 嘈杂|明亮哦|29|78
        if (StringUtils.isEmpty(temp)) {
            return "ERROR, Need parameter: temp";
        }
        String values = jdbcTemplate.queryForObject("select `value` from sys_config where `key` = 'enviro'", String.class);
        String pattern = "\\|\\d+\\|";
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(values);
        String result = "";
        if (matcher.find()) {
            result = matcher.group();
        } else {
            return "ERROR";
        }
        values = values.replace(result, String.format("|%s|", temp));
        jdbcTemplate.update("update sys_config set `value` = ? where `key` = 'enviro'", values);
        return "OK";
    }

    @GetMapping("/getTemperature")
    @ResponseBody
    public String getTemperature() {
        // 嘈杂|明亮哦|29|78
        String values = jdbcTemplate.queryForObject("select `value` from sys_config where `key` = 'enviro'", String.class);
        String pattern = "\\|\\d+\\|";
        Pattern p = Pattern.compile(pattern);
        assert values != null;
        Matcher matcher = p.matcher(values);
        String result = "";
        if (matcher.find()) {
            result = matcher.group();
            return result.substring(1, result.length() - 1);
        }
        // 空表示错误
        return "";
    }



}
