package org.taniwan.common.base.test;

import org.apache.commons.collections.map.HashedMap;
import org.junit.Test;
import org.taniwan.common.base.bean.UserBean;
import org.taniwan.common.base.util.BeanToMapUtil;

import java.util.Map;

/**
 * Created by LENOVO on 2017/3/8.
 */
public class BeanToMapUtilTest {

    @Test
    public void beanToMap(){
        Map<String, Object> map = new HashedMap();
        map.put("name", "张三");
        map.put("age", 18);
        map.put("cellphone", "18899856245");
        map.put("address.province", "广东省");
        map.put("address.city", "深圳市");
        map.put("address.county", "南山区");
        System.out.println(BeanToMapUtil.convertMap(UserBean.class, map));
    }
}
