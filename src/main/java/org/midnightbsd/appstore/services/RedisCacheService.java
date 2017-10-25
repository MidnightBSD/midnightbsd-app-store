package org.midnightbsd.appstore.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author Lucas Holt
 */
@Slf4j
@Service
public class RedisCacheService implements CacheService<Object, Object> {
    private RedisTemplate<Object, Object> client;

    @Autowired
    public RedisCacheService(RedisTemplate<Object, Object> client) {
        this.client = client;
    }

    public Boolean keyExists(@NotNull Object key) {
        return this.client.hasKey(key);
    }

    public Object get(@NotNull Object key) {
        return this.client.opsForValue().get(key);
    }

    public List<String> list() throws ServiceException {
        try {
            Set<Object> redisKeys = this.client.keys("*");
            return (List) redisKeys.stream().map(Object::toString).collect(Collectors.toList());
        } catch (Exception var2) {
            log.error(var2.getMessage(), var2);
            throw new ServiceException("Cache list could not be loaded");
        }
    }

    public void set(@NotNull Object key, Object value) {
        this.client.opsForValue().set(key, value);
    }

    public void set(@NotNull Object key, Object value, long timeout, TimeUnit unit) {
        this.client.opsForValue().set(key, value, timeout, unit);
    }

    public void delete(@NotNull Object key) throws ServiceException {
        try {
            this.client.delete(key);
        } catch (Exception var3) {
            log.error(var3.getMessage(), var3);
            throw new ServiceException("Could not delete " + key.toString());
        }
    }

    public void deleteAllFromCurrentDb() throws ServiceException {
        if (this.client == null) {
            log.error("Client is null.");
            throw new ServiceException("Could not delete all key/value pairs from current redis database, null client.");
        } else {
            RedisConnectionFactory factory = this.client.getConnectionFactory();
            if (factory == null) {
                log.error("Factory is null.");
                throw new ServiceException("Could not delete all key/value pairs from current redis database, unable to establish a factory");
            } else {
                RedisConnection connection = factory.getConnection();
                if (connection == null) {
                    log.error("Connection is null.");
                    throw new ServiceException("Could not delete all key/value pairs from current redis database, unable to establish a connection");
                } else {
                    try {
                        connection.flushDb();
                        log.trace("flushed cache for current db %n", ((JedisConnectionFactory) this.client.getConnectionFactory()).getDatabase());
                    } catch (Exception var4) {
                        log.error(var4.getMessage(), var4);
                        throw new ServiceException("Unable to delete all key/value pairs from current redis database");
                    }
                }
            }
        }
    }

    public void deleteAllFromInstance() throws ServiceException {
        try {
            this.client.getConnectionFactory().getConnection().flushAll();
            log.trace("flushed caches from instance %s", ((JedisConnectionFactory) this.client.getConnectionFactory()).getHostName());
        } catch (Exception var2) {
            log.error(var2.getMessage(), var2);
            throw new ServiceException("Unable to clear all databases in redis instance");
        }
    }
}
