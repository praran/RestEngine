package com.sky.assignment.engine;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.sky.assignment.config.CacheConfig;
import com.sky.assignment.model.Recommendation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.SimpleValueWrapper;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

/**
 * Created by Pradeep Muralidharan.
 */

@RunWith(MockitoJUnitRunner.class)
public class RecommendationCacheServiceImplTest {

    @InjectMocks
    private RecommendationCacheServiceImpl rCacheService = new RecommendationCacheServiceImpl();

    @Mock
    private CacheManager cacheManager;

    @Mock
    private Cache cache;

    @Mock
    private Recommendation recommendation;

    private HashCode hash = Hashing.md5().hashLong(10);

    @Test
    public void test_get_recommendation_when_key_null()  {
        when(cache.get(null)).thenReturn(null);
        when(cacheManager.getCache(CacheConfig.RECOMMENDATION_CACHE_NAME)).thenReturn(cache);
        List<Recommendation> recommendation = rCacheService.getRecommendation(null);
        assertNull(recommendation);
    }

    @Test
    public void test_get_recommendation_when_key_notnull_object_not_exist()  {
        when(cache.get(hash)).thenReturn(null);
        when(cacheManager.getCache(CacheConfig.RECOMMENDATION_CACHE_NAME)).thenReturn(cache);
        List<Recommendation> recommendation = rCacheService.getRecommendation(hash);
        assertNull(recommendation);
    }

    @Test
    public void test_get_recommendation_when_key_notnull_object__exist()  {
        List<Recommendation> recs = Arrays.asList(recommendation);
        when(cacheManager.getCache(CacheConfig.RECOMMENDATION_CACHE_NAME)).thenReturn(cache);
        when(cache.get(hash)).thenReturn(new SimpleValueWrapper(recs));
        List<Recommendation> recommendation = rCacheService.getRecommendation(hash);
        assertNotNull(recommendation);
    }

}