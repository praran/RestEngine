package com.sky.assignment.engine;

import com.google.common.hash.HashCode;
import com.sky.assignment.filters.RecFilter;
import com.sky.assignment.model.Recommendation;
import com.sky.assignment.model.Recommendations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * Created by Pradeep Muralidharan.
 */

@RunWith(MockitoJUnitRunner.class)
public class RecsEngineTest  {

    private RecsEngine recsEngine;

    @Mock
    private RecommendationCacheService recommendationCacheService;

    @Mock
    private RecFilter filter;

    @Mock
    private RecsGenerator recsGenerator;

    @Mock
    private Recommendation recommendation;

    @Mock
    private Recommendation recommendation2;


    @Before
    public void before(){
        this.recsEngine = new RecsEngine(new RecFilter[]{filter}, recommendationCacheService, recsGenerator);
    }

    @Test
    public void test_recommend_when_invalid_input(){
        Recommendations recommend = recsEngine.recommend(0, 1L, 0L);
        assertTrue("Recommendation is empty", isEmpty(recommend.recommendations));
    }

    @Test
    public void test_recommend_when_element_not_in_cache(){
        HashCode hashCode = recsEngine.generateKey(1l, 10l);

        when(recommendationCacheService.getRecommendation(hashCode)).thenReturn(null);
        when(recsGenerator.generate()).thenReturn(recommendation);
        when(filter.isRelevant(recommendation, 1l, 10l)).thenReturn(true);
        Recommendations recommend = recsEngine.recommend(1, 1l, 10l);

        assertFalse("recommendation is not empty", isEmpty(recommend.recommendations));
        assertTrue("recommendation size is 1",recommend.recommendations.size()==1);
        verify(recsGenerator).generate();

    }

    @Test
    public void test_recommend_when_element_in_cache(){
        HashCode hashCode = recsEngine.generateKey(1l, 10l);

        when(recommendationCacheService.getRecommendation(hashCode)).thenReturn(Arrays.asList(recommendation));
        when(filter.isRelevant(recommendation, 1l, 10l)).thenReturn(true);
        Recommendations recommend = recsEngine.recommend(1, 1l, 10l);

        assertFalse("recommendation is not empty", isEmpty(recommend.recommendations));
        assertTrue("recommendation size is 1",recommend.recommendations.size()==1);

    }

    @Test
    public void test_recommend_when_element_is_partially_present_in_cache(){
        HashCode hashCode = recsEngine.generateKey(1l, 10l);

        when(recommendationCacheService.getRecommendation(hashCode)).thenReturn(Arrays.asList(recommendation));
        when(recsGenerator.generate()).thenReturn(recommendation2);
        when(filter.isRelevant(recommendation, 1l, 10l)).thenReturn(true);
        when(filter.isRelevant(recommendation2, 1l, 10l)).thenReturn(true);
        Recommendations recommend = recsEngine.recommend(2, 1l, 10l);

        assertFalse("recommendation is not empty", isEmpty(recommend.recommendations));
        assertTrue("recommendation size is 1",recommend.recommendations.size()==2);

        verify(recsGenerator, times(1)).generate();
    }

}