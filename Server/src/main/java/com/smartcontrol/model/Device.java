package com.smartcontrol.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * @Author: laizc
 * @Date: Created in 9:42 2019-06-27
 */
@Getter
@Setter
public class Device {
    //主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)//自增
    private Integer id;

    //设备名
    private String name;

    //调节单位
    private String unit;

    //值/状态
    private String value;

}
