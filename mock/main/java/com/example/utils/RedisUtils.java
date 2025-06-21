package com.example.utils;

import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis 工具类，封装了 Redis 的常用操作，提供了对 String、Map、List、Set、Geo、HyperLogLog、BitMap 等类型的支持。
 * 通过 RedisTemplate 对 Redis 进行操作，适用于需要缓存和数据持久化的场景。
 *
 * @param <V> Redis 中存储的值类型
 */
@Component
public class RedisUtils<V> {

    @Resource
    private RedisTemplate<String, V> redisTemplate;

    private final Logger logger = LoggerFactory.getLogger(RedisUtils.class);

    public RedisTemplate<String, V> getRedisTemplate() {
        return redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, V> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ======================== Common =================================

    /**
     * 获取 Redis 键的类型。
     *
     * @param key 键
     * @return Redis 键的数据类型
     */
    public DataType getType(String key) {
        return redisTemplate.type(key);
    }

    /**
     * 设置键的过期时间。
     *
     * @param key     键
     * @param timeout 过期时间，单位为秒，必须大于 0
     * @return 设置成功返回 true，失败返回 false
     */
    public Boolean setExpire(String key, long timeout) {
        try {
            if (timeout > 0) {
                redisTemplate.expire(key, timeout, TimeUnit.SECONDS);
            }
            return true;
        }
        catch (Exception e) {
            logger.error("Redis 设置过期时间失败，键: {}, 过期时间: {} 秒", key, timeout, e);
            return false;
        }
    }

    /**
     * 获取键的剩余过期时间。
     *
     * @param key 键
     * @return 剩余过期时间（秒），0 表示永久有效
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断键是否存在。
     *
     * @param key 键
     * @return 如果存在返回 true，否则返回 false
     */
    public Boolean exists(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        }
        catch (Exception e) {
            logger.error("Redis 判断键是否存在失败，键: {}", key, e);
            return false;
        }
    }

    /**
     * 删除一个或多个键。
     *
     * @param key 要删除的键，可以传入一个或多个
     */
    public void delete(String... key) {
        if (key != null && key.length > 0) {
            try {
                if (key.length == 1) {
                    redisTemplate.delete(key[0]);
                }
                else {
                    redisTemplate.delete((Collection<String>) CollectionUtils.arrayToList(key));
                }
            }
            catch (Exception e) {
                logger.error("Redis 删除键失败，键: {}", (Object) key, e);
            }
        }
    }

    // ======================== String =================================

    /**
     * 获取指定键的值。
     *
     * @param key 键
     * @return 对应的值，如果键不存在返回 null
     */
    public V get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 设置键值对。
     *
     * @param key   键
     * @param value 值
     * @return 成功返回 true，失败返回 false
     */
    public boolean set(String key, V value) {
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        }
        catch (Exception e) {
            logger.error("Redis 写入键值对失败，键: {}, 值: {}", key, value, e);
            return false;
        }
    }

    /**
     * 设置带有过期时间的键值对。
     *
     * @param key      键
     * @param value    值
     * @param timeout  过期时间
     * @param timeUnit 时间单位
     * @return 成功返回 true，失败返回 false
     */
    public boolean setWithExpire(String key, V value, long timeout, TimeUnit timeUnit) {
        try {
            if (timeout > 0) {
                redisTemplate.opsForValue().set(key, value, timeout, timeUnit);
            }
            else {
                logger.info("过期时间小于 0，跳过设置过期时间");
                set(key, value);
            }
            return true;
        }
        catch (Exception e) {
            logger.error("Redis 写入键值对失败，键: {}, 值: {}, 过期时间: {} {}", key, value, timeout, timeUnit, e);
            return false;
        }
    }

    /**
     * 对 Redis 中的整数值执行自增操作。
     *
     * @param key       Redis 键
     * @param increment 自增的值
     * @return 自增后的值，如果操作失败返回 null
     */
    public Long incr(String key, int increment) {
        try {
            return redisTemplate.opsForValue().increment(key, increment);
        }
        catch (Exception e) {
            logger.error("Redis 自增操作失败，键: {}, 自增值: {}", key, increment, e);
            return null;
        }
    }

    /**
     * 对 Redis 中的整数值执行自减操作。
     *
     * @param key       Redis 键
     * @param decrement 自减的值
     * @return 自减后的值，如果操作失败返回 null
     */
    public Long decr(String key, int decrement) {
        try {
            return redisTemplate.opsForValue().decrement(key, decrement);
        }
        catch (Exception e) {
            logger.error("Redis 自减操作失败，键: {}, 自减值: {}", key, decrement, e);
            return null;
        }
    }

    /**
     * 在 Redis 中追加值到指定键的现有值。
     *
     * @param key   Redis 键
     * @param value 要追加的值
     * @return 追加后的值长度，如果操作失败返回 null
     */
    public Integer append(String key, String value) {
        try {
            return redisTemplate.opsForValue().append(key, value);
        }
        catch (Exception e) {
            logger.error("Redis 追加操作失败，键: {}, 追加值: {}", key, value, e);
            return null;
        }
    }

    // ======================== Map =================================

    /**
     * 从 Redis 哈希表中获取指定键和哈希键的值。
     *
     * @param key     键，不能为 null
     * @param hashKey 哈希键，不能为 null
     * @return 返回与指定哈希键对应的值，如果键或哈希键为 null，返回 null
     * @throws NullPointerException 如果 key 或 hashKey 为 null，将触发警告日志记录，并返回 null
     */
    @SuppressWarnings("unchecked")
    public V hGet(String key, String hashKey) {
        // 检查输入参数是否为 null
        if (key == null || hashKey == null) {
            logger.warn("hGet 操作失败：键或哈希键为 null，key: {}, hashKey: {}", key, hashKey);
            return null;
        }
        // 从 Redis 哈希表中获取指定键的值，并进行类型转换
        return (V) redisTemplate.opsForHash().get(key, hashKey);
    }

    /**
     * 获取指定键对应的所有哈希表键值对。
     *
     * @param key 键
     * @return 包含多个键值对的 Map，若键为 null，返回 null。
     */
    public Map<Object, Object> hGetAll(String key) {
        // 检查输入参数是否为 null
        if (key == null) {
            logger.warn("hGetAll 操作失败：键为 null");
            return null;
        }
        // 获取指定键的所有哈希表键值对
        return redisTemplate.opsForHash().entries(key);
    }

    /**
     * 将指定的键值对集合存储到哈希表中。
     *
     * @param key 键
     * @param map 包含多个键值对的 Map
     * @return true 表示成功，false 表示失败
     */
    public boolean hSet(String key, Map<String, V> map) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        }
        catch (Exception e) {
            logger.error("hSet 操作失败：键为 {}，异常信息：{}", key, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 将指定的键值对集合存储到哈希表中，并设置过期时间。
     *
     * @param key     键
     * @param map     包含多个键值对的 Map
     * @param timeout 过期时间（秒），如果为负值则不设置过期时间
     * @return true 表示成功，false 表示失败
     */
    public boolean hSet(String key, Map<String, V> map, long timeout) {
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (timeout > 0) {
                setExpire(key, timeout);
            }
            return true;
        }
        catch (Exception e) {
            logger.error("hSet 操作失败：键为 {}，异常信息：{}", key, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向哈希表中放入指定的键值对，如果键不存在则创建新项。
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @return true 表示成功，false 表示失败
     */
    public boolean hSet(String key, String hashKey, Object value) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            return true;
        }
        catch (Exception e) {
            logger.error("hSet 操作失败：键为 {}，哈希键为 {}，异常信息：{}", key, hashKey, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 向哈希表中放入指定的键值对，如果键不存在则创建新项，并设置过期时间。
     *
     * @param key     键
     * @param hashKey 项
     * @param value   值
     * @param timeout 过期时间（秒），如果为负值则不设置过期时间
     * @return true 表示成功，false 表示失败
     */
    public boolean hSet(String key, String hashKey, V value, long timeout) {
        try {
            redisTemplate.opsForHash().put(key, hashKey, value);
            if (timeout > 0) {
                setExpire(key, timeout);
            }
            return true;
        }
        catch (Exception e) {
            logger.error("hSet 操作失败：键为 {}，哈希键为 {}，异常信息：{}", key, hashKey, e.getMessage(), e);
            return false;
        }
    }

    /**
     * 删除哈希表中的指定项。
     *
     * @param key     键，不能为空
     * @param hashKey 项，可以是多个，不能为空
     */
    public void hDelete(String key, Object... hashKey) {
        if (key == null || hashKey == null || hashKey.length == 0) {
            logger.warn("hDelete 操作失败：键或哈希键为 null");
            return;
        }
        redisTemplate.opsForHash().delete(key, hashKey);
    }

    /**
     * 判断哈希表中是否存在指定项的值。
     *
     * @param key  键，不能为空
     * @param item 项，不能为空
     * @return true 表示存在，false 表示不存在
     */
    public boolean hExists(String key, String item) {
        if (key == null || item == null) {
            logger.warn("hExists 操作失败：键或项为 null");
            return false;
        }
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * 对哈希表中的指定项进行递增操作，如果不存在则创建并返回新增后的值。
     *
     * @param key   键
     * @param item  项
     * @param delta 步长，必须为正数；如果为负数，则可能导致意外行为。
     * @return 增加后的值
     */
    public Double hIncr(String key, String item, double delta) {
        if (delta < 0) {
            logger.warn("当前步长被设置为负数，可能会出现意外行为");
        }
        if (key == null || item == null) {
            logger.warn("hIncr 操作失败：键或项为 null");
            return null;
        }
        return redisTemplate.opsForHash().increment(key, item, delta);
    }

    /**
     * 对哈希表中的指定项进行递减操作，等同于递增操作的负数形式。
     *
     * @param key     键
     * @param hashKey 项
     * @param delta   步长，必须为正数；如果为负数，则可能导致意外行为。
     * @return 减少后的值
     */
    public Double hDecr(String key, String hashKey, double delta) {
        if (delta < 0) {
            logger.warn("当前步长被设置为负数，可能会出现意外行为");
        }
        if (key == null || hashKey == null) {
            logger.warn("hDecr 操作失败：键或哈希键为 null");
            return null;
        }
        return redisTemplate.opsForHash().increment(key, hashKey, -delta);
    }

    // ======================== List =================================

    /**
     * 获取列表指定范围的元素。
     *
     * @param key   键
     * @param start 开始索引（包含）
     * @param end   结束索引（包含），-1 表示最后一个元素
     * @return 包含指定范围元素的列表
     */
    public List<V> lRange(String key, long start, long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    /**
     * 将值插入到列表的头部。
     *
     * @param key   键
     * @param value 值
     * @return 返回列表的长度
     */
    public Long lPush(String key, V value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    /**
     * 将值插入到列表的尾部。
     *
     * @param key   键
     * @param value 值
     * @return 返回列表的长度
     */
    public Long rPush(String key, V value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    /**
     * 从列表中移除元素。
     *
     * @param key   键
     * @param count 移除的数量，0 表示移除所有匹配的元素，正数表示从头部开始移除，负数表示从尾部开始移除
     * @param value 值
     * @return 返回移除的数量
     */
    public Long lRemove(String key, long count, V value) {
        return redisTemplate.opsForList().remove(key, count, value);
    }

    // ======================== Set =================================

    /**
     * 向集合中添加元素。
     *
     * @param key    键
     * @param values 元素
     * @return 返回成功添加的元素个数，不包括已经存在的元素
     */
    public Long sAdd(String key, V... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    /**
     * 获取集合中的所有元素。
     *
     * @param key 键
     * @return 集合中的所有元素
     */
    public Set<V> sMembers(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    /**
     * 判断集合中是否存在指定元素。
     *
     * @param key   键
     * @param value 元素
     * @return 如果存在返回 true，否则返回 false
     */
    public Boolean sIsMember(String key, V value) {
        return redisTemplate.opsForSet().isMember(key, value);
    }

    /**
     * 从集合中移除指定元素。
     *
     * @param key    键
     * @param values 元素
     * @return 返回移除的元素个数
     */
    public Long sRemove(String key, V... values) {
        return redisTemplate.opsForSet().remove(key, values);
    }

    /**
     * 获取 Redis 集合的成员数量。
     *
     * @param key 键
     * @return 集合的成员数量，如果键不存在，返回 0
     */
    public Long sCard(String key) {
        try {
            return redisTemplate.opsForSet().size(key);
        }
        catch (Exception e) {
            logger.error("Redis 获取集合成员数量失败，键: {}", key, e);
            return null;
        }
    }

    // ======================== ZSet =================================

    /**
     * 向 Redis 有序集合中添加一个或多个成员，分数可以重复。
     *
     * @param key    键
     * @param score  成员的分数
     * @param member 成员
     * @return 添加的成员数量，不包括已经存在的成员
     */
    public Long zAdd(String key, double score, V member) {
        try {
            return Boolean.TRUE.equals(redisTemplate.opsForZSet().add(key, member, score)) ? 1L : 0L;
        }
        catch (Exception e) {
            logger.error("Redis 向有序集合中添加成员失败，键: {}, 成员: {}, 分数: {}", key, member, score, e);
            return null;
        }
    }

    /**
     * 获取 Redis 有序集合的成员列表，按分数从小到大排序。
     *
     * @param key   键
     * @param start 起始索引（0 为第一个成员）
     * @param end   结束索引（-1 为最后一个成员）
     * @return 有序集合的成员列表
     */
    public Set<V> zRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().range(key, start, end);
        }
        catch (Exception e) {
            logger.error("Redis 获取有序集合成员失败，键: {}", key, e);
            return null;
        }
    }

    /**
     * 获取 Redis 有序集合的成员列表，按分数从大到小排序。
     *
     * @param key   键
     * @param start 起始索引（0 为第一个成员）
     * @param end   结束索引（-1 为最后一个成员）
     * @return 有序集合的成员列表
     */
    public Set<V> zRevRange(String key, long start, long end) {
        try {
            return redisTemplate.opsForZSet().reverseRange(key, start, end);
        }
        catch (Exception e) {
            logger.error("Redis 获取有序集合成员（逆序）失败，键: {}", key, e);
            return null;
        }
    }

    /**
     * 获取 Redis 有序集合的成员数量。
     *
     * @param key 键
     * @return 有序集合的成员数量
     */
    public Long zSize(String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        }
        catch (Exception e) {
            logger.error("Redis 获取有序集合成员数量失败，键: {}", key, e);
            return null;
        }
    }

    /**
     * 从 Redis 有序集合中删除一个或多个成员。
     *
     * @param key     键
     * @param members 要删除的成员，可以是一个或多个
     * @return 被删除的成员数量，不包括不存在的成员
     */
    public Long zRemove(String key, Object... members) {
        try {
            return redisTemplate.opsForZSet().remove(key, members);
        }
        catch (Exception e) {
            logger.error("Redis 从有序集合中删除成员失败，键: {}, 成员: {}", key, members, e);
            return null;
        }
    }

    /**
     * 获取指定成员在 Redis 有序集合中的排名（按分数从小到大排序）。
     *
     * @param key    键
     * @param member 成员
     * @return 成员的排名，排名从 0 开始，如果成员不存在，返回 null
     */
    public Long zRank(String key, V member) {
        try {
            return redisTemplate.opsForZSet().rank(key, member);
        }
        catch (Exception e) {
            logger.error("Redis 获取有序集合成员排名失败，键: {}, 成员: {}", key, member, e);
            return null;
        }
    }

    /**
     * 获取指定成员在 Redis 有序集合中的排名（按分数从大到小排序）。
     *
     * @param key    键
     * @param member 成员
     * @return 成员的排名，排名从 0 开始，如果成员不存在，返回 null
     */
    public Long zRevRank(String key, V member) {
        try {
            return redisTemplate.opsForZSet().reverseRank(key, member);
        }
        catch (Exception e) {
            logger.error("Redis 获取有序集合成员逆排名失败，键: {}, 成员: {}", key, member, e);
            return null;
        }
    }

    /**
     * 获取指定成员在 Redis 有序集合中的分数。
     *
     * @param key    键
     * @param member 成员
     * @return 成员的分数，如果成员不存在，返回 null
     */
    public Double zScore(String key, V member) {
        try {
            return redisTemplate.opsForZSet().score(key, member);
        }
        catch (Exception e) {
            logger.error("Redis 获取有序集合成员分数失败，键: {}, 成员: {}", key, member, e);
            return null;
        }
    }

    // ======================== Geo =================================

    /**
     * 将地理位置添加到 Redis。
     *
     * @param key       键
     * @param longitude 经度
     * @param latitude  纬度
     * @param member    成员
     * @return 返回成功添加的成员数量
     */
    public Long geoAdd(String key, double longitude, double latitude, V member) {
        return redisTemplate.opsForGeo().add(key, new Point(longitude, latitude), member);
    }

    /**
     * 获取指定成员的地理位置。
     *
     * @param key    键
     * @param member 成员
     * @return 返回指定成员的地理位置
     */
    public Point geoPos(String key, V member) {
        List<Point> points = redisTemplate.opsForGeo().position(key, member);
        return points == null || points.isEmpty() ? null : points.get(0);
    }

    // ======================== HyperLogLog =================================

    /**
     * 添加元素到 HyperLogLog
     *
     * @param key   键
     * @param value 要添加的元素
     * @return 返回添加后 HyperLogLog 的基数估计值
     */
    public Long pfAdd(String key, V value) {
        Long result = redisTemplate.opsForHyperLogLog().add(key, value);
        return result;
    }

    /**
     * 获取 HyperLogLog 的基数估计值
     *
     * @param key 键
     * @return HyperLogLog 中唯一元素的估计数量
     */
    public Long pfCount(String key) {
        Long count = redisTemplate.opsForHyperLogLog().size(key);
        return count;
    }

    /**
     * 合并多个 HyperLogLog 的基数
     *
     * @param destination 目标键
     * @param sources     源键列表
     */
    public void pfMerge(String destination, String... sources) {
        redisTemplate.opsForHyperLogLog().union(destination, sources);
    }

    // ======================== BitMap =================================

    /**
     * 设置 BitMap 的指定位置为 1
     *
     * @param key    键
     * @param offset 位位置
     * @return 返回设置前该位的值
     */
    public Boolean setBit(String key, long offset) {
        return redisTemplate.opsForValue().setBit(key, offset, true);
    }

    /**
     * 获取 BitMap 的指定位置的值
     *
     * @param key    键
     * @param offset 位位置
     * @return 返回该位的值，true 表示 1，false 表示 0
     */
    public Boolean getBit(String key, long offset) {
        return redisTemplate.opsForValue().getBit(key, offset);
    }

    /**
     * 统计 BitMap 中值为 1 的位的数量
     *
     * @param key 键
     * @return 返回值为 1 的位的数量
     */
    public Long bitCount(String key) {
        return redisTemplate.execute((RedisCallback<Long>) connection -> connection.bitCount(key.getBytes(), 0, -1));
    }

    /**
     * 执行 BitMap 的位与操作
     *
     * @param destKey 目标键
     * @param keys    源键列表
     */
    public void bitAnd(String destKey, String... keys) {
        if (keys.length < 2) {
            throw new IllegalArgumentException("至少需要两个源键来进行位与操作");
        }
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            byte[][] keyBytes = new byte[keys.length][];
            for (int i = 0; i < keys.length; i++) {
                keyBytes[i] = keys[i].getBytes();
            }
            connection.bitOp(RedisStringCommands.BitOperation.AND, destKey.getBytes(), keyBytes);
            return null;
        });
    }

    /**
     * 执行 BitMap 的位或操作
     *
     * @param destKey 目标键
     * @param keys    源键列表
     */
    public void bitOr(String destKey, String... keys) {
        if (keys.length < 2) {
            throw new IllegalArgumentException("至少需要两个源键来进行位或操作");
        }
        redisTemplate.execute((RedisCallback<Void>) connection -> {
            byte[][] keyBytes = new byte[keys.length][];
            for (int i = 0; i < keys.length; i++) {
                keyBytes[i] = keys[i].getBytes();
            }
            connection.bitOp(RedisStringCommands.BitOperation.OR, destKey.getBytes(), keyBytes);
            return null;
        });
    }

    // ======================== Utility =================================

    /**
     * 清空 Redis 数据库。
     *
     * @return true 表示成功，false 表示失败
     */
    public boolean flushDb() {
        try {
            RedisConnection connection = Objects.requireNonNull(redisTemplate.getConnectionFactory()).getConnection();
            connection.serverCommands().flushDb();
            return true;
        }
        catch (Exception e) {
            logger.error("Redis 清空数据库失败", e);
            return false;
        }
    }
}
