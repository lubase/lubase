package com.lubase.orm;

import com.alibaba.fastjson.JSON;
import com.lubase.orm.util.TypeConverterUtils;
import com.lubase.model.DbField;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class BasicTest {
    @Test
    void testDateTime() {
        String val = "2022-12-01";
        String pattern = "yyyy-MM-dd";
        String result = TypeConverterUtils.object2LocalDateTime2String(val, pattern);
        assert result.equals(val);
        result = TypeConverterUtils.object2LocalDateTime2String("2022-12-01 12:12:12", "yyyy-MM");
        assert result.equals("2022-12");
    }

    @Test
    void testDateTimeConvert() {
        String format = "";
        format = "yyyy-MM-dd";
        LocalDateTime o = LocalDateTime.now();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            simpleDateFormat.parse(o.toString());
        } catch (ParseException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            assert 1 == 2;
        }
    }

    @Test
    void testStreamMapper() {
        List<DbField> fieldList = new ArrayList<>();
        DbField field = new DbField();
        field.setTableId("123");
        field.setCode("col1");
        fieldList.add(field);
        field = new DbField();
        field.setTableId("123");
        field.setCode("col2");
        fieldList.add(field);
        field = new DbField();
        field.setTableId("1234");
        field.setCode("col3");
        fieldList.add(field);

        Object[] tableIds = fieldList.stream().map(o -> o.getTableId()).distinct().toArray();
        System.out.println(JSON.toJSON(tableIds));
        assert tableIds.length == 2;
    }


}
