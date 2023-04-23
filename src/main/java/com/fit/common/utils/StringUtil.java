package com.fit.common.utils;

import org.apache.commons.lang.text.StrBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串相关方法
 */
public class StringUtil {

    private static final String EMPTY = "";

    /**
     * 验证邮箱
     */
    public static boolean checkEmail(String email) {
        return regexStr(email, "^([a-z0-9A-Z]+[-|_|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$");
    }

    /**
     * 验证手机号码
     */
    public static boolean checkMobileNumber(String mobileNumber) {
        return regexStr(mobileNumber, "^(((13[0-9])|(15([0-3]|[5-9]))|(18[0,5-9]))\\d{8})|(0\\d{2}-\\d{8})|(0\\d{3}-\\d{7})$");
    }

    private static boolean regexStr(String str, String reg) {
        boolean flag = false;
        try {
            Pattern regex = Pattern.compile(reg);
            Matcher matcher = regex.matcher(str);
            flag = matcher.matches();
        } catch (Exception e) {
        }
        return flag;
    }

    /**
     * 用默认的分隔符(,)将字符串转换为字符串数组
     *
     * @param str 字符串
     */
    public static String[] str2StrArray(String str) {
        return str2StrArray(str, ",\\s*");
    }

    /**
     * 字符串转换为字符串数组
     *
     * @param str        字符串
     * @param splitRegex 分隔符
     */
    public static String[] str2StrArray(String str, String splitRegex) {
        if (isEmpty(str)) {
            return null;
        }
        return str.split(splitRegex);
    }

    /**
     * 获取字符串编码
     */
    public static String getEncoding(String str) {
        String[] ENCODES = {"GB2312", "ISO-8859-1", "GBK", "UTF-8"};
        for (String encode : ENCODES) {
            try {
                if (str.equals(new String(str.getBytes(encode), encode))) {
                    return encode;
                }
            } catch (Exception e) {
                continue;
            }
        }

        return "";
    }

    /**
     * 将集合转化为字符串
     *
     * @param separator 分隔符
     * @param list      参数集合
     */
    public static String join(String separator, List<?> list) {
        Object[] objs = list.toArray();
        return join(separator, objs);
    }

    /**
     * 将数组转化为字符串
     *
     * @param array     参数数组
     * @param separator 分隔符
     */
    public static String join(String separator, Object[] array) {
        if (array == null) {
            return null;
        } else if (array.length <= 0) {
            return EMPTY;
        } else if (array.length == 1) {
            return String.valueOf(array[0]);
        } else {
            StringBuilder sb = new StringBuilder(array.length * 16);
            for (int i = 0; i < array.length; ++i) {
                if (i > 0) {
                    sb.append(separator);
                }
                sb.append(array[i]);
            }
            return sb.toString();
        }
    }

    /**
     * 判断某字符串是否为空或长度为0或由空白符构成
     */
    public static boolean isBlank(CharSequence cs) {
        int strLen;
        if (cs == null || (strLen = cs.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(cs.charAt(i)) == false) {
                return false;
            }
        }
        return true;
    }

    public static boolean isNotBlank(CharSequence cs) {
        return !isBlank(cs);
    }

    /**
     * 判断是否为空
     *
     * @param cs
     * @return
     */
    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    public static boolean isNotEmpty(CharSequence cs) {
        return !isEmpty(cs);
    }

    /**
     * 字符串首字母大写
     *
     * @param name
     * @return
     */
    public static String capitalize(String name) {
        if (isEmpty(name)) {
            return name;
        }
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static String substringAfter(final String str, final String separator) {
        if (isEmpty(str)) {
            return str;
        }
        if (separator == null) {
            return EMPTY;
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return EMPTY;
        }
        return str.substring(pos + separator.length());
    }

    public static String substringBefore(final String str, final String separator) {
        if (isEmpty(str) || separator == null) {
            return str;
        }
        if (separator.isEmpty()) {
            return EMPTY;
        }
        final int pos = str.indexOf(separator);
        if (pos == -1) {
            return str;
        }
        return str.substring(0, pos);
    }

    /**
     * 去掉指定后缀
     *
     * @param str    字符串
     * @param suffix 后缀
     * @return 切掉后的字符串，若后缀不是 suffix， 返回原字符串
     */
    public static String removeSuffix(CharSequence str, CharSequence suffix) {
        String s = str.toString();
        if (!isEmpty(suffix)) {
            if (s.endsWith(suffix.toString())) {
                return s.substring(0, s.length() - suffix.length());
            }
        }
        return s;
    }

    /**
     * @param source    需要处理的字符串
     * @param oldString 需要被替换的字符
     * @param newString 替换后的字符
     * @return 比如 需要替换字符串中的? String str = "Who am I ?"; replace(str,"?","%3F");
     */
    public static String replace(String source, String oldString, String newString) {
        StringBuffer output = new StringBuffer();
        int lengthOfSource = source.length();
        int lengthOfOld = oldString.length();
        int posStart = 0;
        int pos; //
        while ((pos = source.indexOf(oldString, posStart)) >= 0) {
            output.append(source.substring(posStart, pos));
            output.append(newString);
            posStart = pos + lengthOfOld;
        }
        if (posStart < lengthOfSource) {
            output.append(source.substring(posStart));
        }
        return output.toString();
    }

    /**
     * @param resource 需要处理的字符串`1-2/ 3. 4 5_6,7<8>9+10%11*12(13)14=15\16|17!18@19#20$21^22&23[24]25{26}27;28:29'30"31?`
     * @return 返回替换后的字符串
     */
    public static String changesc(String resource) {
        String changeString = "";
        changeString = resource;
        if (changeString.indexOf("-") > -1) {// 1-替换 破折号 反斜杠 点好 空格 下划线 逗号 分号
            changeString = replace(changeString, "-", "_1");
        }
        if (changeString.indexOf("/") > -1) {// 2 /
            changeString = replace(changeString, "/", "_2");
        }
        if (changeString.indexOf(".") > -1) {// 3 .
            changeString = replace(changeString, ".", "_3");
        }
        if (changeString.indexOf(" ") > -1) {// 4
            changeString = replace(changeString, " ", "_4");
        }
        if (changeString.indexOf("_") > -1) {// 5 _
            changeString = replace(changeString, "_", "_5");
        }
        if (changeString.indexOf(",") > -1) {// 6,
            changeString = replace(changeString, ",", "_6");
        }
        if (changeString.indexOf("<") > -1) {// 7<
            changeString = replace(changeString, "<", "_7");
        }
        if (changeString.indexOf(">") > -1) {// 8>
            changeString = replace(changeString, ">", "_8");
        }
        if (changeString.indexOf("+") > -1) {// 9+
            changeString = replace(changeString, "+", "_9");
        }
        if (changeString.indexOf("%") > -1) {// 10%
            changeString = replace(changeString, "%", "_10");
        }
        if (changeString.indexOf("*") > -1) {// 11*
            changeString = replace(changeString, "*", "_11");
        }
        if (changeString.indexOf("(") > -1) {// 12(
            changeString = replace(changeString, "(", "_12");
        }
        if (changeString.indexOf(")") > -1) {// 13)
            changeString = replace(changeString, ")", "_13");
        }
        if (changeString.indexOf("=") > -1) {// 14=
            changeString = replace(changeString, "=", "_14");
        }
        if (changeString.indexOf("\\") > -1) {// 15\
            changeString = replace(changeString, "\\", "_15");
        }
        if (changeString.indexOf("|") > -1) {// 16|
            changeString = replace(changeString, "|", "_16");
        }
        if (changeString.indexOf("!") > -1) {// 17!
            changeString = replace(changeString, "!", "_17");
        }
        if (changeString.indexOf("@") > -1) {// 18@
            changeString = replace(changeString, "@", "_18");
        }
        if (changeString.indexOf("#") > -1) {// 19#
            changeString = replace(changeString, "#", "_19");
        }
        if (changeString.indexOf("$") > -1) {// 20$
            changeString = replace(changeString, "$", "_20");
        }
        if (changeString.indexOf("^") > -1) {// 21^
            changeString = replace(changeString, "^", "_21");
        }
        if (changeString.indexOf("&") > -1) {// 22&
            changeString = replace(changeString, "&", "_22");
        }
        if (changeString.indexOf("[") > -1) {// 23[
            changeString = replace(changeString, "[", "_23");
        }
        if (changeString.indexOf("]") > -1) {// 24]
            changeString = replace(changeString, "]", "_24");
        }
        if (changeString.indexOf("{") > -1) {// 25{
            changeString = replace(changeString, "{", "_25");
        }
        if (changeString.indexOf("}") > -1) {// 26}
            changeString = replace(changeString, "}", "_26");
        }
        if (changeString.indexOf(";") > -1) {// 27;
            changeString = replace(changeString, ";", "_27");
        }
        if (changeString.indexOf(":") > -1) {// 28:
            changeString = replace(changeString, ":", "_28");
        }
        if (changeString.indexOf("'") > -1) {// 29'
            changeString = replace(changeString, "'", "_29");
        }
        if (changeString.indexOf("\"") > -1) {// 30"
            changeString = replace(changeString, "\"", "_30");
        }
        if (changeString.indexOf("?") > -1) {// 31?
            changeString = replace(changeString, "?", "_31");
        }

        return changeString;

    }

    public static boolean isNumberic(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 将传入的字符串转换为十六进制字符串
     *
     * @param str String , 待转换的字符串
     * @return String , 转换完成的十六进制字符串
     */
    public static String toHex(String str) {
        StringBuffer buff = new StringBuffer();
        for (int i = 0; i < str.length(); i++) {
            if (Character.isLetter(str.charAt(i)) || Character.isDigit(str.charAt(i))) {
                buff.append(str.charAt(i));
            } else {
                buff.append(Integer.toHexString(str.charAt(i)));
            }
        }

        return buff.toString();
    }

    public static List<String> getImg(String s) {
        String regex;
        List<String> list = new ArrayList<String>();
        regex = "src=\"(.*?)\"";
        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        Matcher ma = pa.matcher(s);
        while (ma.find()) {
            list.add(ma.group());
        }
        return list;
    }

    /**
     * 返回存有图片地址的数组
     *
     * @param tar
     * @return
     */
    public static String[] getImgaddress(String tar) {
        List<String> imgList = getImg(tar);

        String res[] = new String[imgList.size()];

        if (imgList.size() > 0) {
            for (int i = 0; i < imgList.size(); i++) {
                int begin = imgList.get(i).indexOf("\"") + 1;
                int end = imgList.get(i).lastIndexOf("\"");
                String url[] = imgList.get(i).substring(begin, end).split("/");
                res[i] = url[url.length - 1];
            }
        } else {
        }
        return res;
    }

    /**
     * 字符串数组转字符串
     *
     * @param array     数组
     * @param separator 分隔符
     */
    public static String join(Object[] array, String separator) {
        if (array == null) {
            return null;
        }
        return join(array, separator, 0, array.length);
    }

    /**
     * 字符串数组转字符串
     *
     * @param array      数组
     * @param separator  分隔符
     * @param startIndex 起始游标
     * @param endIndex   结束游标
     */
    public static String join(Object[] array, String separator, int startIndex, int endIndex) {
        if (array == null) {
            return null;
        }
        if (separator == null) {
            separator = "";
        }

        int bufSize = endIndex - startIndex;
        if (bufSize <= 0) {
            return "";
        }

        bufSize *= ((array[startIndex] == null ? 16 : array[startIndex].toString().length()) + separator.length());

        StrBuilder buf = new StrBuilder(bufSize);

        for (int i = startIndex; i < endIndex; i++) {
            if (i > startIndex) {
                buf.append(separator);
            }
            if (array[i] != null) {
                buf.append(array[i]);
            }
        }
        return buf.toString();
    }
}
