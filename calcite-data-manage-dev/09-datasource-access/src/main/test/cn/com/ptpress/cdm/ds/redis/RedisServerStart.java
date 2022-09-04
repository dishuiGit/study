package cn.com.ptpress.cdm.ds.redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.embedded.RedisServer;

public class RedisServerStart {
    private RedisServer redisServer;
    private static JedisPool pool;
    private static final String HOST = "127.0.0.1";
    private static final int PORT = 6379;
    private static RedisServerStart redisServerStart = null;

    private RedisServerStart() {
    }

    public static RedisServerStart getInstance() {
        if (redisServerStart == null)
            redisServerStart = new RedisServerStart();
        return redisServerStart;
    }

    /**
     * 启动redis
     *
     * @throws Exception
     */
    public void startServer() throws Exception {
        try {
            redisServer = new RedisServer(PORT);
            redisServer.start();
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxTotal(10);
            pool = new JedisPool(jedisPoolConfig, HOST, PORT);
            makeData();
            System.out.println("redis 已经启动完成");
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 停止 redis
     */
    public void stopServer(){
        redisServer.stop();
        System.out.println("redis 已停止");
    }



    /**
     * 制造相关数据
     */
    private void makeData() {
        pool.getResource().lpush("stu_01", "{\"name\":\"小明\",\"score\":90}","{\"name\":\"小红\",\"score\":80}");
    }

}
