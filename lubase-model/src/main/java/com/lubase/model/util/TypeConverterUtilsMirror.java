package com.lubase.model.util;

import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * object类型和常用类型转换。此类仅用于DBEntity
 *
 * @author A
 */
public class TypeConverterUtilsMirror {
    public static String object2String(Object obj) {
        if (obj == null || StringUtils.isEmpty(obj)) {
            return null;
        } else {
            return obj.toString();
        }
    }

    public static String object2String(Object obj, String defaultValue) {
        if (obj == null || StringUtils.isEmpty(obj)) {
            return defaultValue;
        } else {
            return obj.toString();
        }
    }

    public static Integer object2Integer(Object obj) {
        return object2Integer(obj, null);
    }

    public static Integer object2Integer(Object obj, Integer defaultValue) {
        if (obj == null || StringUtils.isEmpty(obj)) {
            return defaultValue;
        } else if (obj instanceof Boolean) {
            return Boolean.parseBoolean(obj.toString()) ? 1 : 0;
        } else {
            return Integer.parseInt(obj.toString());
        }
    }

    public static Long object2Long(Object obj) {
        if (obj == null || StringUtils.isEmpty(obj)) {
            return null;
        } else {
            return Long.parseLong(obj.toString());
        }
    }

    public static Double object2Double(Object obj) {
        if (obj == null) {
            return null;
        } else {
            return Double.parseDouble(obj.toString());
        }
    }

    public static Boolean object2Boolean(Object obj) {
        if (obj == null || StringUtils.isEmpty(obj)) {
            return null;
        } else if (obj instanceof Integer) {
            return Integer.parseInt(obj.toString()) == 1;
        } else {
            return Boolean.parseBoolean(obj.toString());
        }
    }

    public static LocalDateTime object2LocalDateTime(Object obj) {
        if (obj == null || StringUtils.isEmpty(obj)) {
            return null;
        }
        LocalDateTime localDateTime;
        if (obj instanceof Date) {
            Date date = (Date) obj;
            localDateTime = LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
        } else {
            String time = obj.toString().replace(' ', 'T');
            if (time.contains("T")) {
                localDateTime = LocalDateTime.parse(time);
            } else {
                localDateTime = LocalDate.parse(time).atStartOfDay();
            }
        }
        return localDateTime;
    }

    public static String object2LocalDateTime2String(Object obj, String pattern) {
        LocalDateTime localDateTime = object2LocalDateTime(obj);
        if (localDateTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }
}

