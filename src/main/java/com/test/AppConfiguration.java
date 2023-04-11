package com.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import com.test.common.filter.ReqFilter;

@SpringBootConfiguration
public class AppConfiguration {
	//注册一个过滤器
	@Bean
	public FilterRegistrationBean<ReqFilter> Filter(){
		FilterRegistrationBean<ReqFilter> filter=new FilterRegistrationBean<>();
		filter.setName("myFilter");
		filter.setFilter(new ReqFilter());
		filter.setOrder(-1);
		return filter;
	}

	//redis
	@Value("${spring.redis.host}")
    private String redisHost;
	@Value("${spring.redis.port}")
    private int redisPort;
	
	@Bean
    public JedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }
	
	@Bean
	public RedisTemplate<String,Object>redisTemplate(RedisConnectionFactory connectionFactory){
		RedisTemplate<String,Object>template=new RedisTemplate<>();
		template.setConnectionFactory(connectionFactory);
			
		//设置key的序列化和反序列化类型 采用StringRedisSerializer
	    template.setKeySerializer(new StringRedisSerializer());
	    template.setHashKeySerializer(new StringRedisSerializer());

	    //设置value的序列化和反序列化类型 采用GenericJackson2JsonRedisSerializer
	    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
	    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
	        
		return template;
	}
	
	@Bean
	public ServerEndpointExporter serverEndpointExporter() {
		return new ServerEndpointExporter();
	}
}
