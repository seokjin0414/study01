/*
*/

package com.company.nill.myTool.utill;


import com.lazy.admin.vo.common.CodeVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * Class Invoke 유틸
 * @author MOBUILDZ
 * @since 2021. 01
 * @version 1.0
 *
 */
public class ClassUtils {

	private static Logger LOGGER = LoggerFactory.getLogger(ClassUtils.class);

    /**
     * Method명을 가지고 온다.
     * @param clazz
     * @param methodName
     * @return
     */
    private static Method findMethodByName(Class<?> clazz, String methodName) {
        Method[] methods = clazz.getMethods();
        for(Method method : methods) {
            if(methodName.equals(method.getName()) && Modifier.isPublic(method.getModifiers())) {
                return method;
            }
        }
        return null;
    }

    private static Field[] findField(Class<?> clazz){
        return clazz.getDeclaredFields();
    }



    public static List<String> getEnumFileds(String className,String enumName){
    	List<String> rstList = new ArrayList<String>();
    	try {
			Class<?> clazz = getDefaultClassLoader().loadClass(className);
			Class<?> clazzs[] = clazz.getDeclaredClasses();
			for(Class<?> tmpCls : clazzs){
				if(tmpCls.getSimpleName().equals(enumName)){
					for(Object en : tmpCls.getEnumConstants()){
						String name = invoke(en, "name");
						rstList.add(name);
					}
					return rstList;
				}
			}
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(className+" not found");
		}
    	return rstList;
    }

    /**
     * Getter메소드를 호출하여 값을 불러 온다.
     * @param obj
     * @param filedName
     * @return
     */
    public static <T> T getterInvoke(Object obj,String filedName){
        String methodName = "get"+StringUtils.capitalize(filedName);
        return invoke(obj, methodName);
    }

    @SuppressWarnings("unchecked")
    public static <T> T invoke(Object obj,String methodName){
    	Method method = findMethodByName(obj.getClass(), methodName);
        if(method==null){
            throw new RuntimeException("Method not found : "+methodName);
        }
        try{
            return (T)ReflectionUtils.invokeMethod(method, obj);
        }catch(Exception e){
            throw new RuntimeException("invoke 실행 에러.", e);
        }
    }

    /**
     * setter 메소드를 호출한다.
     * @param obj
     * @param filedName
     * @param value
     */
    public static void setterInvoke(Object obj,String filedName,Object value){
        String methodName = "set"+StringUtils.capitalize(filedName);
        Method method = findMethodByName(obj.getClass(), methodName);
        if(method==null){
            throw new RuntimeException("Method not found : "+filedName);
        }
        try{
            ReflectionUtils.invokeMethod(method, obj,new Object[]{value});
        }catch(Exception e){
            throw new RuntimeException("setter 실행에러.", e);
        }
    }


    /**
     * @param className com.xx.xx.xx.em.codemapping.XXX
     * @return
     */
    public static List<CodeVo> getEnumCode(String className){
        List<CodeVo> rstList = new ArrayList<CodeVo>();
        try {
            Class<?> clazz = getDefaultClassLoader().loadClass(className);
            for(Object en : clazz.getEnumConstants()){
                String cd = invoke(en, "name");
                String nm = invoke(en, "getTitle");
                CodeVo cdVo = new CodeVo();
                cdVo.setCode(cd);
                cdVo.setCodeNm(nm);
                rstList.add(cdVo);
            }
            return rstList;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(className+" not found");
        }
    }

    public static List<String> getEnumFileds(String className){
        List<String> rstList = new ArrayList<String>();
        try {
            Class<?> clazz = getDefaultClassLoader().loadClass(className);
            for(Object en : clazz.getEnumConstants()){
                String name = invoke(en, "name");
                rstList.add(name);
            }
            return rstList;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(className+" not found");
        }
    }

	public static ClassLoader getDefaultClassLoader() {
		ClassLoader classLoader = null;
		try {
			classLoader = Thread.currentThread().getContextClassLoader();
		}
		catch (Throwable ex) {
			LOGGER.warn(ex.getMessage(), ex);
		}
		if(classLoader == null) {
			classLoader = ClassUtils.class.getClassLoader();
		}
		return classLoader;
	}

}
