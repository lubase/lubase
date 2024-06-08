package com.lubase.orm;


import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
public class DataKeyFactoryTest {


    @Test
    public void createKeyTest() {

        String a = "848744839303847758585";
        String[] split = a.split("\\.");

        //String currentKey = DataKeyFactory.instance.createCurrentKey();
       // System.out.println(currentKey);
       // String[] currentKey1 = DataKeyFactory.instance.CreateCurrentKeys(10);
       // Assert.isFalse(currentKey.isEmpty(), "null");
    }


}