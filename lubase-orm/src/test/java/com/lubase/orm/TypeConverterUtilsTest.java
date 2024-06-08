package com.lubase.orm;

import com.lubase.orm.util.TypeConverterUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TypeConverterUtilsTest {

    @Test
    void testDateTime() {
        String val = "2022-12-01";
        String result = TypeConverterUtils.object2LocalDateTime2String(val, "yyyy-MM-dd");
        assert result.equals(val);
    }

    @Test
    void testStr2Int() {
        String str = "02";
        assert 2 == TypeConverterUtils.object2Integer(str);
    }
}
