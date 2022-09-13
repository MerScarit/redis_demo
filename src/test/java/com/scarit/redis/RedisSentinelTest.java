package com.scarit.redis;


import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisSentinelPool;

import java.util.HashSet;
import java.util.Set;

public class RedisSentinelTest {

    JedisSentinelPool jedisSentinelPool;

    @BeforeTest
    public  void beforeTest(){
        //创建jedis的连接池
        JedisPoolConfig config = new JedisPoolConfig();
        //最大、最小空闲连接
        config.setMaxIdle(10);
        config.setMinIdle(5);

        //最大空闲时间
        config.setMaxWaitMillis(3000);

        config.setMaxTotal(50);


        Set<String> sentinels = new HashSet<>();
        sentinels.add("192.168.180.130:26379");
        sentinels.add("192.168.180.130:26380");
        sentinels.add("192.168.180.130:26381");

        jedisSentinelPool = new JedisSentinelPool("mymaster", sentinels,config);
    }

    @Test
    public void keysTest(){
        Jedis resource = jedisSentinelPool.getResource();

        Set<String> keys = resource.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

    }



    @AfterTest
    public void afterTest(){
        jedisSentinelPool.close();
    }
}
