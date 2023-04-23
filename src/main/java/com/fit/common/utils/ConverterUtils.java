package com.fit.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据类型转换工具类
 */

public class ConverterUtils {

    private static Logger logger = LoggerFactory.getLogger(ConverterUtils.class);

    /**
     * URL解码
     */
    public static String decode(String s) {
        String ret = s;
        try {
            ret = URLDecoder.decode(s.trim(), "UTF-8");
        } catch (Exception localException) {
        }
        return ret;
    }

    /**
     * URL编码
     */
    public static String encode(String s) {
        String ret = s;
        try {
            ret = URLEncoder.encode(s.trim(), "UTF-8");
        } catch (Exception localException) {
        }
        return ret;
    }

    /**
     * 请求参数URL转MAP对象
     */
    public static Map<String, String> reqStr2Map(String s) {
        Map<String, String> map = new HashMap<String, String>();
        try {
            String[] splitByte = s.split("&");
            for (String str : splitByte) {
                map.put(str.substring(0, str.indexOf("=")), str.substring(str.indexOf("=") + 1));
            }
        } catch (Exception e) {
            throw new RuntimeException("系统异常");
        }
        return map;
    }

    /**
     * 将MAP转换成URL
     */
    public static String reqMap2Str(Map<String, Object> map) {
        try {
            if (map == null) {
                return "";
            }
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                sb.append(entry.getKey() + "=" + entry.getValue());
                sb.append("&");
            }
            if (sb.toString().endsWith("&")) {
                sb.deleteCharAt(sb.length() - 1);
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("系统异常");
        }
    }

    /**
     * <将obj转换为string，如果obj为null则返回defaultVal>
     *
     * @param obj        需要转换为string的对象
     * @param defaultVal 默认值
     */
    public static String toString(Object obj, String defaultVal) {
        return (obj != null) ? obj.toString().trim() : defaultVal;
    }

    /**
     * <将obj转换为string，默认为空>
     *
     * @param obj 需要转换为string的对象
     */
    public static String toString(Object obj) {
        return toString(obj, "");
    }

    /**
     * <将对象转换为int>
     *
     * @param obj        需要转换为int的对象
     * @param defaultVal 默认值
     */
    public static Integer toInt(Object obj, Integer defaultVal) {
        try {
            return (obj != null) ? Integer.parseInt(toString(obj, "0")) : defaultVal;
        } catch (Exception e) {
        }
        return defaultVal;
    }

    /**
     * <将对象转换为int>
     *
     * @param obj 需要转换为int的对象
     */
    public static Integer toInt(Object obj) {
        return toInt(obj, 0);
    }

    /**
     * <将对象转换为Integer>
     *
     * @param obj 需要转换为Integer的对象
     * @return obj转换成的Integer值
     */
    public static Integer toInteger(Object obj) {
        return toInt(obj, null);
    }

    /**
     * <将对象转换为int>
     *
     * @param obj        需要转换为int的对象
     * @param defaultVal 默认值
     */
    public static Float toFloat(Object obj, float defaultVal) {
        return (obj != null) ? Float.parseFloat(toString(obj, "0")) : defaultVal;
    }

    /**
     * <将对象转换为Float>
     *
     * @param obj 需要转换为Float的对象
     */
    public static Float toFloat(Object obj) {
        return toFloat(obj, 0);
    }

    /**
     * <将obj转换为long>
     *
     * @param obj        需要转换的对象
     * @param defaultVal 默认值
     */
    public static Long toLong(Object obj, long defaultVal) {
        return (obj != null) ? Long.parseLong(toString(obj)) : defaultVal;
    }

    /**
     * <将obj转换为long>
     *
     * @param obj 需要转换的对象
     */
    public static Long toLong(Object obj) {
        return toLong(obj, 0l);
    }

    /**
     * 将object转换为double类型，如果出错则返回 defaultVal
     *
     * @param obj        需要转换的对象
     * @param defaultVal 默认值
     */
    public static Double toDouble(Object obj, Double defaultVal) {
        try {
            return Double.parseDouble(obj.toString());
        } catch (Exception e) {
            return defaultVal;
        }
    }

    /**
     * 将object转换为double类型，如果出错则返回 0d
     *
     * @param obj 需要转换的对象
     */
    public static double toDouble(Object obj) {
        return toDouble(obj, 0d);
    }

    /**
     * 任意类型转换成Map
     */
    public static Map<String, Object> obj2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Field[] declaredFields = obj.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (field.get(obj) != null) {
                    map.put(field.getName(), field.get(obj));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("任意类型转换成Map异常");
        }
        return map;
    }

    /**
     * 任意类型转换成List<Object>
     */
    public static List<Object> obj2List(Object obj) {
        if (obj == null) {
            return null;
        }
        List<Object> list = new ArrayList<Object>();
        try {
            if (obj.getClass().isArray()) {
                for (int i = 0; i < Array.getLength(obj); i++) {
                    Object object = Array.get(obj, i);
                    list.add(object);
                }
            }
        } catch (ArrayIndexOutOfBoundsException | IllegalArgumentException e) {
            throw new RuntimeException("任意类型转换成Map异常");
        }
        return list;
    }

    /**
     * <将List<Object>转换为List<Map<String, Object>>>
     *
     * @param list 需要转换的list
     */
    public static List<Map<String, Object>> converterForMapList(List<Object> list) throws Exception {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        for (Object tempObj : list) {
            result.add(obj2Map(tempObj));
        }
        return result;
    }

    public static Object[] list2Array(List<?> list) {
        Object[] objs = new Object[list.size()];
        if (list.size() > 0) {
            list.toArray(objs);
        }
        return objs;
    }

    /**
     * 将 Map对象转化为JavaBean
     *
     * @param clazz 要转化的类型
     * @param map
     */
    public static <T> T convertMap2Bean(Map<String, ? extends Object> map, Class<T> clazz) {
        if (map == null || map.size() == 0) {
            return null;
        }
        try {
            converter2Map(map, clazz);
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将 List<Map>对象转化为List<JavaBean>
     *
     * @param clazz 要转化的类型
     */
    public static <T> List<T> convertListMap2ListBean(List<Map<String, Object>> listMap, Class<T> clazz) {
        List<T> beanList = new ArrayList<T>();
        for (int i = 0, n = listMap.size(); i < n; i++) {
            Map<String, Object> map = listMap.get(i);
            T bean = convertMap2Bean(map, clazz);
            beanList.add(bean);
        }
        return beanList;
    }

    /**
     * 将 JavaBean对象转化为 Map
     *
     * @param bean 要转化的类型
     * @return Map对象
     */
    public static Map<String, Object> convertBean2Map(Object bean) throws Exception {
        if (bean == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                // 过滤class属性
                if (!key.equals("class")) {
                    // 得到property对应的getter方法
                    Method getter = property.getReadMethod();
                    Object value = getter.invoke(bean);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("JavaBean对象转化为 Map异常");
        }

        return map;
    }

    /**
     * Map转Bean
     *
     * @param map
     * @param clz
     * @return
     */
    public static <T> T converter2Map(Map<?, ?> map, Class<T> clz) {
        T obj = null;
        try {
            obj = clz.newInstance();
            BeanInfo beanInfo = Introspector.getBeanInfo(clz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor property : propertyDescriptors) {
                String key = property.getName();
                if (map.containsKey(key)) {
                    Object value = map.get(key);
                    if (null != value) {
                        String v = value.toString();// 值
                        // property对应的setter方法
                        Method setter = property.getWriteMethod();
                        Class<?>[] clazz = setter.getParameterTypes();
                        String type = clazz[0].getName();
                        if ("java.lang.Byte".equals(type) || "byte".equals(type)) {
                            setter.invoke(obj, Byte.parseByte(v));
                        } else if ("java.lang.Short".equals(type) || "short".equals(type)) {
                            setter.invoke(obj, Short.parseShort(v));
                        } else if ("java.lang.Integer".equals(type) || "int".equals(type)) {
                            setter.invoke(obj, Integer.parseInt(v));
                        } else if ("java.lang.Long".equals(type) || "long".equals(type)) {
                            setter.invoke(obj, Long.parseLong(v));
                        } else if ("java.lang.Float".equals(type) || "float".equals(type)) {
                            setter.invoke(obj, Float.parseFloat(v));
                        } else if ("java.lang.Double".equals(type) || "double".equals(type)) {
                            setter.invoke(obj, Double.parseDouble(v));
                        } else if ("java.lang.String".equals(type)) {
                            setter.invoke(obj, v.toString());
                        } else if ("java.lang.Character".equals(type) || "char".equals(type)) {
                            setter.invoke(obj, (Character) value);
                        } else if ("java.util.Date".equals(type)) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            setter.invoke(obj, sdf.parse(v));
                        } else if ("java.lang.Date".equals(type)) {
                            setter.invoke(obj, new Date(((java.sql.Date) value).getTime()));
                        } else if ("java.lang.Timer".equals(type)) {
                            setter.invoke(obj, new Time(((Time) value).getTime()));
                        } else if ("java.sql.Timestamp".equals(type)) {
                            setter.invoke(obj, (java.sql.Timestamp) value);
                        } else {
                            setter.invoke(obj, value);
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("map转{}异常", clz.getName(), e);
        }
        return obj;
    }

    /**
     * 将 List<JavaBean>对象转化为List<Map>
     */
    public static List<Map<String, Object>> convertListBean2ListMap(List<?> beanList) {
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        try {
            for (int i = 0, n = beanList.size(); i < n; i++) {
                Object bean = beanList.get(i);
                Map<String, Object> map = convertBean2Map(bean);
                mapList.add(map);
            }
        } catch (Exception e) {
            throw new RuntimeException("List<JavaBean>对象转化为List<Map>异常");
        }
        return mapList;
    }

    /**
     * 对象转换为字符串
     *
     * @param obj     转换对象
     * @param charset 字符集
     */
    public static String convertToString(Object obj, String charset) {
        try {
            ByteArrayOutputStream bo = new ByteArrayOutputStream();
            ObjectOutputStream oo = new ObjectOutputStream(bo);
            oo.writeObject(obj);
            byte[] data = bo.toByteArray(); // 取内存中保存的数据
            String str = new String(data, charset);
            bo.close();
            oo.close();
            return str;
        } catch (IOException e) {
            throw new RuntimeException("任意类型转换成字符串异常");
        }
    }

    /**
     * 字节转换为对象
     *
     * @param bytes 字节数组
     */
    public static Object convertToByte(byte[] bytes) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(in);
            in.close();
            ois.close();
            Object obj = ois.readObject();
            return obj;
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("字节转换为对象异常");
        }
    }

    /**
     * @param collection 集合
     * @param separator  拼接分隔符
     */
    public static String join(Collection<?> collection, String separator) {
        if (collection == null) {
            return null;
        } else {
            Iterator<?> iterator = collection.iterator();
            if (iterator == null) {
                return null;
            } else if (!iterator.hasNext()) {
                return "";
            } else {
                Object first = iterator.next();
                if (!iterator.hasNext()) {
                    String result = toString(first);
                    return result;
                } else {
                    StringBuilder buf = new StringBuilder(256);
                    if (first != null) {
                        buf.append(first);
                    }
                    while (iterator.hasNext()) {
                        if (separator != null) {
                            buf.append(separator);
                        }
                        Object obj = iterator.next();
                        if (obj != null) {
                            buf.append(obj);
                        }
                    }
                    return buf.toString();
                }
            }
        }
    }

    /**
     * uniCode编码转换中文
     */
    public static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");
        Matcher matcher = pattern.matcher(str);
        char ch;
        while (matcher.find()) {
            // group 6728
            String group = matcher.group(2);
            // ch:'木' 26408
            ch = (char) Integer.parseInt(group, 16);
            // group1 \u6728
            String group1 = matcher.group(1);
            str = str.replace(group1, ch + "");
        }
        return str;
    }
}
