package com.sky.assignment.engine;

import com.google.common.hash.HashCode;
import com.sky.assignment.config.CacheConfig;
import com.sky.assignment.model.Recommendation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by Pradeep Muralidharan.
 */

@Service
@Qualifier("recommendationCacheService")
public class RecommendationCacheServiceImpl implements  RecommendationCacheService {

    @Autowired
    private CacheManager cacheManager;

    /**
     * {@inheritDoc}
     * @param key
     * @return
     */
    @Override
    public List<Recommendation> getRecommendation(HashCode key){
        Cache cache = cacheManager.getCache(CacheConfig.RECOMMENDATION_CACHE_NAME);
        Cache.ValueWrapper value = cache.get(key);
        return (value == null)?null : (List < Recommendation>) cache.get(key).get();
    }

    /**
     * {@inheritDoc}
     * @param key
     * @param recommendations
     */
    @Override
    public void addRecommendation(HashCode key, List<Recommendation> recommendations){
        Cache cache = cacheManager.getCache(CacheConfig.RECOMMENDATION_CACHE_NAME);
        cache.put(key, recommendations);
    }

}
