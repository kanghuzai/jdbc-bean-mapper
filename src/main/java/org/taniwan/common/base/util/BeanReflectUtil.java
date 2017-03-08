package org.taniwan.common.base.util;

import org.springframework.util.CollectionUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class BeanReflectUtil{

	/**
     * 判断实体对象的属性是否有值，即至少有一个字段不为null
	 * 
	 * @param entryObj
	 * @param clazz
	 * @return
	 * @author 张超
	 * @date 2016年3月23日-下午2:49:54
	 */
	public static Boolean hasObjFieldNullValue(Object entryObj, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			try {
				if(field.get(entryObj) != null && !Modifier.isStatic(field.getModifiers())){
					try {
						if(field.get(entryObj).toString().matches("\\s*|t|r|n")){
							return false;
						}
					} catch (Exception e) {}
					return true;
				}
			}
			catch (Exception e) {
				// ingore
			}
		}
		return false;
	}
	
	public static <T> List<Field> getBeanAllField(T bean){
		List<Field> fieldList = new ArrayList<>();
		Class<?> clazz = bean.getClass();
		getCurClazzAllField(fieldList, clazz);
		// 循环向上获取父类的属性
		for(; clazz.getSuperclass() != Object.class; clazz.getSuperclass()){
			clazz = clazz.getSuperclass();
			getCurClazzAllField(fieldList, clazz);
		}
		return fieldList;
	}

	private static void getCurClazzAllField(List<Field> fieldList, Class<?> clazz) {
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			field.setAccessible(true);
			if(!Modifier.isStatic(field.getModifiers())){
				fieldList.add(field);
			}
		}
	}

	/**
	 * 提取list元素的字段值(重复的元素会排除掉)
	 * @author 张超
	 * @date 2016年7月18日-下午6:25:57
	 * @param elementList
	 * @param elementFieldName
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <E, B> List<E> extractElementFied(List<B> elementList, String elementFieldName){
		List<E> fieldList = new ArrayList<>();
		if(CollectionUtils.isEmpty(elementList)){
			return fieldList;
		}
		for(B element: elementList){
			Field field = ReflectionUtils.findField(element.getClass(), elementFieldName);
			if(field == null){
				throw new RuntimeException("elementFieldName: [" + elementFieldName + "] is not exist...");
			}
			field.setAccessible(true);
			E temp = (E) ReflectionUtils.getField(field, element);
			if(!fieldList.contains(temp) && temp != null){
				fieldList.add(temp);
			}
		}
		return fieldList;
		
	}
	
	private static final List<Class<?>> baseType = new ArrayList<>();
	static{
		baseType.add(boolean.class);
		baseType.add(Boolean.class);
		baseType.add(int.class);
		baseType.add(Integer.class);
		baseType.add(char.class);
		baseType.add(Character.class);
		baseType.add(short.class);
		baseType.add(Short.class);
		baseType.add(long.class);
		baseType.add(Long.class);
		baseType.add(float.class);
		baseType.add(Float.class);
		baseType.add(double.class);
		baseType.add(Double.class);
		baseType.add(byte.class);
		baseType.add(Byte.class);
		baseType.add(String.class);
		baseType.add(void.class);
		baseType.add(Void.class);
	}
	
	public static <T> boolean isBaseType(Class<T> clazz){
		return baseType.contains(clazz) ? true : false;
	}
	
	public static <T> List<String> getFieldNames(Class<T> clazz){
		List<String> fields = new ArrayList<>();
		try {
			for(Field f: getBeanAllField(clazz.newInstance())){
				fields.add(f.getName());
			}
		}
		catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return fields;
	}
	
}