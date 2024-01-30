/*
 *
 *  *  Copyright (c) "2023". rchemist.io by Rchemist
 *  *  Licensed under the Rchemist Common License, Version 1.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License under controlled by Rchemist
 *  *
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *
 */

package io.rchemist.common.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.rchemist.common.configuration.platform.DataProviderConfig;
import io.rchemist.common.configuration.platform.DataSourceConfig;
import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.BatchStrategies;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <p>
 * Project : Rchemist Commerce Platform
 * <p>
 * Created by kunner
 **/

@Configuration(CommonRedisCacheManager.CACHE_MANAGER_CONFIG)
public class CommonRedisCacheManager {

  public static final String CACHE_MANAGER_CONFIG = "rcmCommonRedisCacheManagerConfig";
  public static final String CACHE_MANAGER = "rcmCommonRedisCacheManager";

  protected final RedisConnectionFactory redisConnectionFactory;

  protected final ObjectMapper objectMapper;
  protected final DataProviderConfig dataProviderConfig;

  protected final DataSourceConfig dataSourceConfig;


  public CommonRedisCacheManager(RedisConnectionFactory redisConnectionFactory,
      ObjectMapper objectMapper, DataProviderConfig dataProviderConfig,
      DataSourceConfig dataSourceConfig) {
    this.redisConnectionFactory = redisConnectionFactory;
    this.objectMapper = objectMapper;
    this.dataProviderConfig = dataProviderConfig;
    this.dataSourceConfig = dataSourceConfig;
  }

  @Bean(CACHE_MANAGER)
  public CacheManager redisCacheManager(){
    RedisCacheConfiguration redisCacheConfiguration = getDefaultRedisCacheConfiguration();

    return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory,
        BatchStrategies.scan(1000))).cacheDefaults(redisCacheConfiguration)
        .transactionAware()
        .withCacheConfiguration(CommonCacheRegion.Common.CACHE,
            getDefaultRedisCacheConfiguration().entryTtl(Duration.ofSeconds(CommonCacheRegion.Common.TTL_ONE_HOUR)))
        .withCacheConfiguration(CommonCacheRegion.Common.METADATA_CACHE,
            getDefaultRedisCacheConfiguration().entryTtl(Duration.ofDays(CommonCacheRegion.Common.TTL_ONE_MONTH)))
        .withCacheConfiguration(CommonCacheRegion.Common.REPORT_CACHE,
            getDefaultRedisCacheConfiguration().entryTtl(Duration.ofDays(CommonCacheRegion.Common.TTL_ONE_DAY)))
        .withCacheConfiguration(CommonCacheRegion.Common.MENU_CACHE,
            getDefaultRedisCacheConfiguration().entryTtl(Duration.ofSeconds(CommonCacheRegion.Common.TTL_ONE_DAY)))
        .withCacheConfiguration(CommonCacheRegion.Common.DASHBOARD_BOARD, getDefaultRedisCacheConfiguration())
        .build();
  }

  private RedisCacheConfiguration getDefaultRedisCacheConfiguration() {

    ObjectMapper objectMapper = this.objectMapper.copy();
    objectMapper = objectMapper.activateDefaultTyping(objectMapper.getPolymorphicTypeValidator(), ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);

    return RedisCacheConfiguration.defaultCacheConfig()
    .serializeKeysWith(
        RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
    .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper)))
    .entryTtl(Duration.ofSeconds(CommonCacheRegion.Common.TTL_ONE_HOUR));
  }

}
