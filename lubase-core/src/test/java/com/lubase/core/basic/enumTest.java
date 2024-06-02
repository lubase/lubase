package com.lubase.core.basic;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class enumTest {
    @Test
    void test1() {
        testEnum test = testEnum.red;
        System.out.println(test);

        testEnum t1 = testEnum.valueOf("red");
        System.out.println(String.format("red ordinal is %s,getIndex is %s", t1.ordinal(), t1.getIndex()));
        testEnum t2 = testEnum.values()[1];
        System.out.println(String.format("t2 is %s", t2));
        System.out.println(String.format("t2 ordinal is %s", t2.ordinal()));

        testEnum t3 = testEnum.fromIndex(4);
        assert t3 == testEnum.blue;
    }
}

enum testEnum {
    red(1), green(2), blue(4);
    private int index;

    testEnum(int index) {
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public static testEnum fromIndex(int i) {
        if (i == 1) {
            return testEnum.red;
        } else if (i == 2) {
            return testEnum.green;
        } else if (i == 4) {
            return testEnum.blue;
        }
        return null;
    }

}
