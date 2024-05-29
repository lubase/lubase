package com.lubase.core.util;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StringUtil {
    /**
     * 字符串分隔
     * @param data
     * @param regex
     * @return
     */
    public static String[] splitStr(String data, String regex,Boolean ignoreEmpty) {
        if(data==null || data.isEmpty()){
            return  new String[]{};
        }
        String[] strData = data.split(regex);
        if(!ignoreEmpty){
            return  strData;
        }
        List<String> collect = Arrays.stream(strData).filter(m -> !m.isEmpty()).collect(Collectors.toList());
        String[] strData2 = collect.toArray(new String[collect.size()]);
        return strData2;
    }

    /**
     * 字符串分隔
     * @param data
     * @param regex
     * @return
     */
    public static String[] splitStr(String data, String regex) {
        return  splitStr( data,  regex,true);
    }
}
