package com.sky.assignment.engine;

import com.google.common.hash.HashCode;
import com.sky.assignment.model.Recommendation;

import java.util.List;

/**
 * Created by Pradeep Muralidharan.
 */

public interface RecommendationCacheService {

    /**
     * Get recommendations for cache
     * @param key
     * @return
     */
    public List<Recommendation> getRecommendation(HashCode key);

    /**
     * Add recommendations to cache
     * @param key
     * @param recommendations
     */
    public void addRecommendation(HashCode key, List<Recommendation> recommendations);

}
