package com.journal.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.journal.app.api.response.WeatherResponse;
import com.journal.app.cache.AppCache;

@Service
public class WeatherService {
	@Value("${weather.api.key}")
	private String API_KEY;

	@Autowired
	private AppCache appCache;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private RedisService redisService;

	public WeatherResponse getWeather(String city) {
		WeatherResponse cachedValue = redisService.get("weather_of_"+city, WeatherResponse.class);
		if(cachedValue != null) {
			return cachedValue;
		} else {
			String finalAPI = appCache.APP_CACHE.get("weather_api").replace("<API_KEY>", API_KEY).replace("<CITY>", city);
			ResponseEntity<WeatherResponse> response = restTemplate.exchange(finalAPI, HttpMethod.GET, null, WeatherResponse.class);
			WeatherResponse body = response.getBody();
			if(body != null) {
				redisService.set("weather_of_"+city, body, 300);
			}
			return body;

		}
	}

}
