package com.scarit.redis;

import org.testng.annotations.Test;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.List;
import java.util.Set;

public class Apitest {

    JedisPool jedisPool;

    @BeforeTest
    public void beforeTest() {

        //创建jedis的连接池
        JedisPoolConfig config = new JedisPoolConfig();
        //最大、最小空闲连接
        config.setMaxIdle(10);
        config.setMinIdle(5);

        //最大空闲时间
        config.setMaxWaitMillis(3000);

        config.setMaxTotal(50);

        jedisPool = new JedisPool(config, "192.168.180.130", 6379);

    }



    @Test
    public void testKeys(){
        //从池子里拿一个连接
        Jedis resource = jedisPool.getResource();
        //操作
        Set<String> keys = resource.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }
    }

    @AfterTest
    public void closeConnection(){
        //关闭连接
        jedisPool.close();
    }


    @Test
    public void tesrString(){
        //从池子里拿一个连接
        Jedis jedis = jedisPool.getResource();

        String pv = jedis.set("pv", "0");

        String pv1 = jedis.get("pv");
        System.out.println(pv1);

        jedis.set("pv", "1000");
        String pv2 = jedis.get("pv");
        System.out.println(pv2);

        Long incr = jedis.incr("pv");
        System.out.println(incr);

        Long incr1 = jedis.incrBy("pv", 1000);
        System.out.println(incr1);

        jedisPool.close();
    }

    @Test
    public void testHash(){
        Jedis jedis = jedisPool.getResource();

        jedis.hset("goods", "iPhone14", "7999");
        jedis.hset("goods", "MacBook", "19999");

        Set<String> goods = jedis.hkeys("goods");
        for (String good : goods) {
            System.out.println(good);
        }

        jedis.hincrBy("goods", "MacBook", 3000);
        String hget = jedis.hget("goods", "MacBook");
        System.out.println(hget);

        jedis.del("goods");

        jedisPool.close();
    }
    @Test
    public void testList(){
        Jedis jedis = jedisPool.getResource();

        jedis.lpush("phoneNumber", "18529347130", "14715905222", "12514715244");

        String phoneNum = jedis.rpop("phoneNumber");
        System.out.println(phoneNum);

        List<String> lrange = jedis.lrange("phoneNumber", 0, 1);
        for (String s : lrange) {
            System.out.println(s);
        }
        jedisPool.close();
    }

    @Test
    public void testSet(){

        Jedis jedis = jedisPool.getResource();

        jedis.sadd("uv", "user1");
        jedis.sadd("uv", "user2");
        jedis.sadd("uv", "user1");

        Long uv = jedis.scard("uv");
        System.out.println(uv);

        jedis.close();
    }

    @Test
    public void testHyperLogLog(){
        Jedis jedis = jedisPool.getResource();

        for (int i = 1 ; i <= 100000 ; i++){
            jedis.pfadd("str", i+"");
        }
        System.out.println(jedis.pfcount("str"));


        jedis.close();
    }

}
