package cn.xiaochi.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.Resource;

/**
 *  ===== redis ======
 *  交给spring管理
 */
@Service("redisPool")
@Slf4j
public class RedisPool {

    // 获取 redis.xml 文件中定义的 shardedJedisPool
    @Resource(name = "shardedJedisPool")
    private ShardedJedisPool shardedJedisPool;

    /**
     * 获取 redis实例
     * @return
     */
    public ShardedJedis instance(){
        return shardedJedisPool.getResource();
    }

    /**
     * 关闭 redis
     * @param shardedJedis
     */
    public void safeClose(ShardedJedis shardedJedis){
        try{
            if (shardedJedis != null){
                shardedJedis.close();
            }
        }catch (Exception e){
            log.error("return redis resources exception",e);
        }
    }
}
