package org.taniwan.common.base.util;

import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by LENOVO on 2017/3/2.
 */
public class CollectionUtil {

    public static <T> T getFirst(List<T> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(0);
    }

    public static <T> T getLast(List<T> list){
        if(CollectionUtils.isEmpty(list)){
            return null;
        }
        return list.get(list.size()-1);
    }
}
