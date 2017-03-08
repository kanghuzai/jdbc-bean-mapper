package org.taniwan.common.base.util;

import org.taniwan.common.base.emus.SetEnum;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class BeanToMapUtil {

	private static final Logger log = LoggerFactory.getLogger(BeanToMapUtil.class);
    private static final Map<String, List<Field>> fieldTypeMapCache = new ConcurrentHashMap<>();
	private static final Map<String, String> fieldNameMapCache = new ConcurrentHashMap<>();
	
    public static <T> T convertMap(Class<T> type, Map<String, Object> resultMap) {
    	T obj = null;
    	try {
    		obj = type.newInstance();
    		log.debug("### current obj type: {} ###", obj.toString());
    		if(!fieldTypeMapCache.containsKey(type.getName())){
    			fieldTypeMapCache.put(type.getName(), BeanReflectUtil.getBeanAllField(obj));
    		}
			Map<String, Object> tempFieldMaps = new HashMap<>();
			for(String key: resultMap.keySet()){
				if(key.contains(".")){
					// 自定义的类型，聚合了其他的数据实体
					String[] strs = key.split("\\.");
					for(Field field: fieldTypeMapCache.get(type.getName())){
						if(field.getName().equals(strs[0])){
							String fieldName = obj.getClass().getName() + "-" + strs[0];
							if(!fieldTypeMapCache.containsKey(fieldName)){
								fieldTypeMapCache.put(fieldName, BeanReflectUtil.getBeanAllField(field.getType().newInstance()));
							}
							if(!tempFieldMaps.containsKey(strs[0])){
								tempFieldMaps.put(strs[0], field.getType().newInstance());
								log.debug("### set new custom field name: {}, type: {} ###", field.getName(), field.getType().getName());
							}
							setNativeFieldValue(fieldName, resultMap, tempFieldMaps.get(strs[0]), key);
						}
					}
				}
				else{
					// 基本类型，直接赋值
					setNativeFieldValue(type.getName(), resultMap, obj, key);
				}
			}
			for(String key: tempFieldMaps.keySet()){
				BeanUtils.setProperty(obj, key, tempFieldMaps.get(key));
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
    	return obj;
    }

	private static <T> void setNativeFieldValue(String type, Map<String, Object> resultMap, T obj, String key){
		String tempKey = key.contains(".") ? key.split("\\.")[1] : key;
		if(tempKey.contains("_")){
			if(fieldNameMapCache.containsKey(tempKey)){
				tempKey = fieldNameMapCache.get(tempKey);
			}
			else{
				String tempKey2 = "";
				String[] temps = tempKey.split("_");
				int i = 0;
				for(String temp: temps){
					if(i == 0){
						tempKey2 += temp;
					}
					else{
						tempKey2 += temp.substring(0, 1).toUpperCase() + temp.substring(1);
					}
					i++;
				}
				fieldNameMapCache.put(tempKey, tempKey2);
				tempKey = tempKey2;
			}
		}
		for(Field field: fieldTypeMapCache.get(type)){
			try {
				if(field.getName().equals(tempKey)){
					if(resultMap.get(key) == null){
						log.debug("### current sql field name: {}, java field name: {}, value is null direct continue, type: {} ###", key, tempKey, field.getType().getName());
						continue;
					}
					if(field.getType().equals(Date.class) && resultMap.get(key) instanceof String){
						Date temp = DateUtil.parseyyyyMMddHHmmss((String) resultMap.get(key));
						BeanUtils.setProperty(obj, field.getName(), temp);
					}
					else if(field.getType().isEnum()){
						Method set = null;
						for(Method m: field.getType().getMethods()){
							if(!Modifier.isStatic(m.getModifiers())){
								continue;
							}
							boolean  isReturn = m.getReturnType().getName().equals(field.getType().getName());
							boolean isAn = AnnotationUtils.getAnnotation(m, SetEnum.class) != null;
							if(isReturn && isAn){
								set = m;
								break;
							}
						}
						if(set != null){
							if(resultMap.get(key) instanceof Integer && (Integer)resultMap.get(key) < 256){
								byte v = (byte)(int)resultMap.get(key);
								MethodUtils.invokeStaticMethod(field.getType(), set.getName(), v);
								Object enumv = set.invoke(null, v);
								BeanUtils.setProperty(obj, field.getName(), enumv);
							}
							else if(resultMap.get(key) instanceof Byte){
								Object enumv = set.invoke(obj, (byte)resultMap.get(key));
								BeanUtils.setProperty(obj, field.getName(), enumv);
							}
							else{
								throw new RuntimeException(field.getType().getName() + "equivalent sql field type is not [tinyint, int]");
							}
						}
						else{
							throw new RuntimeException(field.getType().getName() + " is not define @SetEnum static getEnums(byte type) method");
						}
					}
					else{
						BeanUtils.setProperty(obj, field.getName(), resultMap.get(key));
					}
					log.debug("### set new native sql field name: {}, java field name: {}, value: {}, type: {} ###", key, tempKey, resultMap.get(key), field.getType().getName());
				}
			}
			catch (Exception e) {
				log.error("### current sql field name: {}, java field name: {}, value: {}, type: {} ###", key, tempKey, resultMap.get(key), field.getType().getName());
				throw new RuntimeException(e);
			}
		}
	}

    public static <T> List<T> convertMap(Class<T> type, List<Map<String, Object>> maps) {
        List<T> list = new ArrayList<>();
        for (Map<String, Object> map : maps) {
            list.add(convertMap(type, map));
        }
        return list;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     */
    public static Map<String, Object> convertBean(Object bean) {
        Map<String, Object> returnMap;
        try {
            Class type = bean.getClass();
            returnMap = new HashMap<String, Object>();
            BeanInfo beanInfo = Introspector.getBeanInfo(type);

            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (int i = 0; i < propertyDescriptors.length; i++) {
                PropertyDescriptor descriptor = propertyDescriptors[i];
                String propertyName = descriptor.getName();
                if (!propertyName.equals("class")) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    } else {
                        returnMap.put(propertyName, "");
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getMessage(), e);
        }
        return returnMap;
    }
    
}