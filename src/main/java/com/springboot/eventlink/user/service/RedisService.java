package com.springboot.eventlink.user.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 만료 시간 없는 키 값 지정
     */
    @Transactional
    public void setValues(String key, String value){
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 만료시간 설정 -> 자동삭제
     */
    @Transactional
    public void setValuesWithTimeout(String key, String value, long timeout){
        redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MILLISECONDS);
    }

    /**
     * 키를 이용한 값 확인
     */
    public String getValues(String key){
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * 키 삭제
     */
    @Transactional
    public void deleteValues(String key) {
        redisTemplate.delete(key);
    }
}
