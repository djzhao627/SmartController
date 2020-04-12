package cn.djzhao.smartcontroller.entity;

/**
 * 设备实体类
 *
 * @Author: djzhao
 * @Date: 2020/04/12 19:31
 */
public class Device {
    //主键
    private Integer id;

    //设备名
    private String name;

    //调节单位
    private String unit;

    //值/状态
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
