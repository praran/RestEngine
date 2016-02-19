package com.sky.assignment.config;

import net.sf.ehcache.config.CacheConfiguration;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.interceptor.SimpleKeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Pradeep Muralidharan.
 */


@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer{

   // @TODO  Externalize cache configuration parameters
   public static final String RECOMMENDATION_CACHE_NAME = "RecommendationCache";
   public static final int MAX_ENTRIES_IN_HEAP=10000;
   public static final int EVICTION_TIME = 5 *60;

    @Bean(destroyMethod="shutdown")
    public net.sf.ehcache.CacheManager ehCacheManager() {
        net.sf.ehcache.config.Configuration config = new net.sf.ehcache.config.Configuration();
        config.addCache(getCacheConfiguration());
        return net.sf.ehcache.CacheManager.newInstance(config);
    }

    @Bean
    @Qualifier("cacheManager")
    @Override
    public CacheManager cacheManager() {
        return new EhCacheCacheManager(ehCacheManager());
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return new SimpleKeyGenerator();
    }

    private CacheConfiguration getCacheConfiguration() {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setName(RECOMMENDATION_CACHE_NAME);
        cacheConfiguration.setMemoryStoreEvictionPolicy("LRU");
        cacheConfiguration.setMaxEntriesLocalHeap(MAX_ENTRIES_IN_HEAP);
        cacheConfiguration.setTimeToLiveSeconds(EVICTION_TIME);
        cacheConfiguration.setTimeToIdleSeconds(EVICTION_TIME);
        return cacheConfiguration;
    }
}
