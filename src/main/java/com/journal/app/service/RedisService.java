package com.journal.app.service;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RedisService {
	private final RedisTemplate<String, String> redisTemplate;
	private final ObjectMapper objectMapper;
	
	public RedisService(RedisTemplate<String, String> redisTemplate, ObjectMapper objectMapper) {
		this.redisTemplate = redisTemplate;
		this.objectMapper = objectMapper;
	}
	
	public <T> T get(String key, Class<T> entityClass) {
		try {			
			String jsonValue = redisTemplate.opsForValue().get(key);
			if(jsonValue == null) return null;
			return objectMapper.readValue(jsonValue, entityClass);
		}
		catch (Exception e) {
			log.error("Exception", e);
			return null;
		}
	}
	
	public void set(String key, Object obj, int timeLimit) {
		try {
			String jsonValue = objectMapper.writeValueAsString(obj);
			redisTemplate.opsForValue().set(key, jsonValue, timeLimit, TimeUnit.SECONDS); 
		}
		catch (Exception e) {
			log.error("Exception", e);
		}
	}
	
}
