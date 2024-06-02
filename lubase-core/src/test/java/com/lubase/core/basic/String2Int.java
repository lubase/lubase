package com.lubase.core.basic;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class String2Int {

    @Test
    void test1() {
        String a = "11";
        assert Integer.parseInt(a) == 11;
    }

    @Test
    void testnullempty() {
        System.out.println("null:");
        test(null);

        System.out.println("kong:");
        test("");

        System.out.println("1空格：");
        test(" ");

        System.out.println("2空格：");
        test("  ");
    }

    void test(String val) {
        System.out.println(StringUtils.isEmpty(val));
        System.out.println(StringUtils.isWhitespace(val));
    }

    @Test
    void testequal() {
        boolean resl = "vv".equals(null);
        System.out.println(String.format("null equal val is %s", resl));
    }

}
