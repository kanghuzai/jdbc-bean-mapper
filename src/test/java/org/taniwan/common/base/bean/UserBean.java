package org.taniwan.common.base.bean;

/**
 * Created by LENOVO on 2017/3/8.
 */
public class UserBean {

    private String name;
    private int age;
    private String cellphone;
    private RegionBean address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public RegionBean getAddress() {
        return address;
    }

    public void setAddress(RegionBean address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "UserBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", cellphone='" + cellphone + '\'' +
                ", address=" + address +
                '}';
    }
}
