package com.example.copro.global.redis.application;

import com.example.copro.global.config.RedisConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Service
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;
    private final RedisConfig redisConfig;

    public RedisService(RedisTemplate<String, String> redisTemplate, RedisConfig redisConfig) {
        this.redisTemplate = redisTemplate;
        this.redisConfig = redisConfig;
    }

    public boolean checkAndAddViewByMember(Long memberId, Long boardId) {
        String namespace = redisConfig.getNamespace();
        String redisKey = namespace + ":boardView:" + boardId;
        String redisMemberKey = namespace + ":memberView:" + memberId;

        List<String> memberViewList = getValuesList(redisMemberKey);

        if (!memberViewList.contains(redisKey)) {
            setValuesListWithExpire(redisMemberKey, redisKey, Duration.ofHours(24));
            return true;
        }
        return false;
    }

    public void setValuesListWithExpire(String key, String data, Duration duration) {
        redisTemplate.opsForList().rightPush(key, data);
        redisTemplate.expire(key, duration);
    }

    public List<String> getValuesList(String key) {
        Long len = redisTemplate.opsForList().size(key);
        return len == 0 ? new ArrayList<>() : redisTemplate.opsForList().range(key, 0, len-1);
    }

}
