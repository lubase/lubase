package com.lubase.core.service;

import java.util.List;

/**
 * id 生成器
 *
 * @author zhulanzhou
 **/
public interface IDGenerator {

     /**
      *  生成表的主键id ,返回一个id
      * @return  long 类型 id
      */
     long  nextId();

     /**
      *  生成表的主键id ,返回指定个数的id，小于1001个
      * @return  long 类型 id
      */
     List<Long> nextIds(Integer count);
}
