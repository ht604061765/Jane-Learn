//package com.tenfine.napoleon.framework.util;
//
//import java.util.*;
//
//import javax.annotation.PostConstruct;
//import javax.annotation.PreDestroy;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import redis.clients.jedis.*;
//
///**
// * redis 工具类
// * 进阶：哨兵模式
// */
//@Component
//public class RedisUtil {
//
//    private final static Logger logger = LoggerFactory.getLogger(RedisUtil.class);
//
//    @Value(value = "${spring.redis.host}")
//    private String host;
//
//    @Value(value = "${spring.redis.port}")
//    private String port;
//
//    @Value(value = "${spring.redis.password}")
//    private String password;
//
//    @Value(value = "${spring.redis.lettuce.pool.max-active}")
//    private String maxActive;
//
//    @Value(value = "${spring.redis.jedis.pool.max-idle}")
//    private String maxIdle;
//
//    @Value(value = "${spring.redis.jedis.pool.max-wait}")
//    private String maxWait;
//
//    @Value(value = "${spring.redis.timeout}")
//    private String timeout;
//
//    private static JedisPool jedisPool = null;
//
//    /**
//     * 被@PostConstruct修饰的方法会在服务器加载Servlet的时候运行，并且只会被服务器调用一次，类似于Servlet的init()方法。
//     * 被@PostConstruct修饰的方法会在构造函数之后，init()方法之前运行。
//     */
//    @PostConstruct
//    private void initJedisPool() {
//        try {
//            JedisPoolConfig config = new JedisPoolConfig();
//            config.setMaxTotal(Integer.parseInt(maxActive));
//            config.setMaxIdle(Integer.parseInt(maxIdle));
//            config.setMaxWaitMillis(Integer.parseInt(maxWait));
//            // 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的
//            config.setTestOnBorrow(true);
//            if(StringUtil.isNotEmpty(password)) {
//                jedisPool = new JedisPool(config, host, Integer.parseInt(port), Integer.parseInt(timeout), password);
//            } else {
//                jedisPool = new JedisPool(config, host, Integer.parseInt(port), Integer.parseInt(timeout));
//            }
//        } catch (Exception e) {
//            if (logger.isInfoEnabled()) {
//                logger.info(e.getMessage());
//            }
//            e.printStackTrace();
//        }finally {
//        	 logger.info("JedisPool注入完毕！！");
//             logger.info("redis地址：" + host + ":" + port);
//		}
//    }
//
//    /**
//     * 被@PreDestroy修饰的方法会在服务器卸载Servlet的时候运行，并且只会被服务器调用一次，类似于Servlet的destroy()方法。
//     * 被@PreDestroy修饰的方法会在destroy()方法之后运行，在Servlet被彻底卸载之前。
//     */
//    @PreDestroy
//    private void destroyJedisPool() {
//        if (jedisPool != null) {
//            jedisPool.destroy();
//        }
//    }
//
//    /**
//     * 获取Jedis实例
//     *
//     * @return Jedis
//     */
//    public synchronized static Jedis getJedis() {
//        if (jedisPool != null) {
//            return jedisPool.getResource();
//        } else {
//            return null;
//        }
//    }
//
//    /**
//     * 释放jedis资源
//     *
//     * @param jedis jedis实例
//     */
//    public static void returnResource(final Jedis jedis) {
//        if (jedis != null) {
//            jedis.close();
//        }
//    }
//
//    public static void set(String key, String value) {
//        Jedis jedis = null;
//        try {
//            jedis = getJedis();
//            if (jedis != null) {
//                jedis.set(key, value);
//            }
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//        } finally {
//            returnResource(jedis);
//        }
//    }
//
//    public static void append(String key, String value) {
//        Jedis jedis = null;
//        try {
//            jedis = getJedis();
//            if (jedis != null) {
//                jedis.append(key, value);
//            }
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//        } finally {
//            returnResource(jedis);
//        }
//    }
//
//    public static Long del(String key) {
//        Jedis jedis = null;
//        try {
//            jedis = getJedis();
//            if (jedis != null) {
//                return jedis.del(key);
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            return null;
//        } finally {
//            returnResource(jedis);
//        }
//
//    }
//
//    public static String get(String key) {
//        Jedis jedis = null;
//        try {
//            jedis = getJedis();
//            if (jedis != null) {
//                return jedis.get(key);
//            } else {
//                return null;
//            }
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//            return "";
//        } finally {
//            returnResource(jedis);
//        }
//    }
//
//    /**
//     * 设置值 并加上过期
//     *
//     * @param key     唯一标识
//     * @param value   值
//     * @param seconds 过期事件（秒）
//     */
//    public static void setAndExpire(String key, String value, int seconds) {
//        Jedis jedis = null;
//        try {
//            jedis = getJedis();
//            if (jedis != null) {
//                jedis.set(key, value);
//                jedis.expire(key, seconds);
//            }
//        } catch (Exception e) {
//            logger.info(e.getMessage());
//        } finally {
//            returnResource(jedis);
//        }
//    }
//
//    /**
//     * 移除锁
//     *
//     * @param jedis      Jedis 客户端
//     * @param lockName   锁名字，会加上前缀“lock:”
//     * @param identifier 锁的标识
//     * @return 是否移除成功
//     */
//    public static boolean releaseLock(Jedis jedis, String lockName,
//                                      String identifier) {
//        lockName = String.format("lock:%s", lockName);
//        while (true) {
//            jedis.watch(lockName);
//            if (identifier.equals(jedis.get(lockName))) {
//                Transaction tx = jedis.multi();
//                tx.del(lockName);
//                List<Object> list = tx.exec();
//                jedis.unwatch();
//                if (list == null) {
//                    continue;
//                }
//                return true;
//            } else {
//                jedis.unwatch();
//                break;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 移除锁
//     *
//     * @param lockName   锁名字，会加上前缀“lock:”
//     * @param identifier 锁的标识
//     * @return 是否移除成功
//     */
//    public static boolean releaseLock(String lockName, String identifier) {
//        Jedis jedis = RedisUtil.getJedis();
//        boolean re;
//        try {
//            re = RedisUtil.releaseLock(jedis, lockName, identifier);
//        } finally {
//            RedisUtil.returnResource(jedis);
//        }
//        return re;
//    }
//
//    /**
//     * 获取锁
//     *
//     * @param jedis          Jedis 客户端
//     * @param lockName       锁名字，会加上前缀“lock:”
//     * @param acquireTimeout 获取锁，重试时间（毫秒）
//     * @param lockTimeout    锁的自动过期时间（秒）
//     * @return 锁的标识
//     */
//    public static String acquireLockWithTimeout(Jedis jedis, String lockName,
//                                                int acquireTimeout, int lockTimeout) {
//        String identifier = UUID.randomUUID().toString();
//        lockName = String.format("lock:%s", lockName);
//        long end = System.currentTimeMillis() + acquireTimeout;
//        do {
//            if (jedis.setnx(lockName, identifier) == 1) {
//                jedis.expire(lockName, lockTimeout);
//                return identifier;
//            } else if (jedis.ttl(lockName) == -1) {
//                jedis.expire(lockName, lockTimeout);
//            }
//            try {
//                Thread.sleep(1);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        } while (System.currentTimeMillis() < end);
//        return null;
//    }
//
//    /**
//     * 获取锁
//     *
//     * @param lockName       锁名字，会加上前缀“lock:”
//     * @param acquireTimeout 获取锁，重试时间（毫秒）
//     * @param lockTimeout    锁的自动过期时间（秒）
//     * @return 锁的标识
//     */
//    public static String acquireLockWithTimeout(String lockName,int acquireTimeout, int lockTimeout) {
//        Jedis jedis = RedisUtil.getJedis();
//        String identifier;
//        try {
//            identifier = RedisUtil.acquireLockWithTimeout(jedis, lockName,
//                    acquireTimeout, lockTimeout);
//        } finally {
//            RedisUtil.returnResource(jedis);
//        }
//        return identifier;
//    }
//
//
//}
