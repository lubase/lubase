package com.lubase.orm.service.impl;

import com.lubase.orm.exception.InvokeCommonException;
import com.lubase.orm.service.IDGenerator;
import lombok.SneakyThrows;
import org.apache.commons.lang3.RandomUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class DataIdGenerator  implements IDGenerator {

    /**
     * 开始时间：2021-01-01 00:00:00
     */
    private final long beginTs = 1577808000000L;

    private final long workerIdBits = 10;

    /**
     * 2^10 - 1 = 1023
     */
    private final long maxWorkerId = -1L ^ (-1L << workerIdBits);

    private final long sequenceBits = 12;

    /**
     * 2^12 - 1 = 4095
     */
    private final long maxSequence = -1L ^ (-1L << sequenceBits);

    /**
     * 时间戳左移22位
     */
    private final long timestampLeftOffset = workerIdBits + sequenceBits+1;

    /**
     * 业务ID左移12位
     */
    private final long workerIdLeftOffset = sequenceBits;

    /**
     * 合并了机器ID和数据标示ID，统称业务ID，10位
     */
    private Integer workerId;

    /**
     * 毫秒内序列，12位，2^12 = 4096个数字
     */
    private long sequence = 0L;

    /**
     * 上一次生成的ID的时间戳，同一个worker中
     */
    private long lastTimestamp = -1L;

    static final String PRE = "IDGenerator_";

    @Value("${spring.application:demo}")
    String application;
    static  final HashMap<String,Integer> map=new HashMap<>();
    private static Logger logger = LoggerFactory.getLogger(DataIdGenerator.class);
    @PostConstruct
    void init() {
        try {
            String machKey = PRE + application + "_" + InetAddress.getLocalHost().getHostName();
            workerId = (Integer) map.get(machKey);
            if (workerId == null) {
                String machCKey = PRE + application;
                Integer machCount = (Integer) map.get(machCKey);
                if (machCount == null) {
                    machCount = 1;
                } else {
                    machCount++;
                }
                workerId=machCount;
                if(machCount==maxWorkerId){
                    map.put(machCKey, 0);
                }else{
                    map.put(machCKey,machCount);
                }
                map.put(machKey, workerId);
            }
            logger.info("machKey:" + machKey + ",workerId:" + workerId);
        } catch (Exception ex) {
            logger.error("ID参数初始化失败",ex);
            workerId= RandomUtils.nextInt(0,(int)maxWorkerId);

        }
    }
    @Override
    @SneakyThrows
    public long nextId() {
        if (workerId > maxWorkerId || workerId < 0) {
            throw new IllegalArgumentException(String.format("WorkerId必须大于或等于0且小于或等于%d", maxWorkerId));
        }
        long ts = System.currentTimeMillis();
        if (ts < lastTimestamp) {
            throw new InvokeCommonException(String.format("系统时钟回退了%d毫秒", (lastTimestamp - ts)));
        }
        // 同一时间内，则计算序列号
        if (ts == lastTimestamp) {
            // 序列号溢出
            if (++sequence > maxSequence) {
                ts = tilNextMillis(lastTimestamp);
                sequence = 0L;
            }
        } else {
            // 时间戳改变，重置序列号
            sequence = 0L;
        }
        lastTimestamp = ts;
        // 0 - 00000000 00000000 00000000 00000000 00000000 0 - 00000000 00 - 00000000 0000
        // 左移后，低位补0，进行按位或运算相当于二进制拼接
        // 本来高位还有个0<<63，0与任何数字按位或都是本身，所以写不写效果一样
        long l0=(ts - beginTs) ;
        long l1=l0 << timestampLeftOffset;
        long l2= workerId << workerIdLeftOffset;
        long l3=sequence;
        return (ts - beginTs) << timestampLeftOffset | workerId << workerIdLeftOffset | sequence;
    }

    /**
     * 阻塞到下一个毫秒
     *
     * @param lastTimestamp
     * @return
     */
    private long tilNextMillis(long lastTimestamp) {
        long ts = System.currentTimeMillis();
        while (ts <= lastTimestamp) {
            ts = System.currentTimeMillis();
        }
        return ts;
    }

    @Override
    @SneakyThrows
    public List<Long> nextIds(Integer count){
        List<Long> ids = new ArrayList<>();
        for (int i=0;i<count;i++){
            ids.add(nextId());
        }
        return  ids;
    }
}
