package com.sky.assignment.engine;

import com.google.common.hash.HashCode;
import com.sky.assignment.filters.RecFilter;
import com.sky.assignment.model.Recommendation;
import com.sky.assignment.model.Recommendations;

import java.util.List;

/**
 * Created by Pradeep Muralidharan.
 */
public abstract class AbstractRecommendationEngine {

    protected abstract RecFilter[] getFilters();

    /**
     * Provide recommendations based on the paremeters
     * @param numberOfRecs
     * @param start
     * @param end
     * @return
     */
    protected abstract Recommendations recommend(long numberOfRecs, long start, long end);

    protected abstract RecommendationCacheService getRecommendationCacheService();

    /**
     * Execute filters and check if recommendations are valid
     * @param r
     * @param start
     * @param end
     * @return
     */
    protected boolean runFilters(Recommendation r, long start, long end) {
        for (RecFilter f: getFilters()) {
            if (!f.isRelevant(r, start, end)) {
                return false;
            }
        }
        return true;
    }

    /**
     *  get cached recommendations
     * @param start
     * @param end
     * @return
     */
    protected List<Recommendation> getCachedRecommendation(long start, long end){
        return getRecommendationCacheService().getRecommendation(generateKey(start,end));
    }

    /**
     * Add recommendations to cache
     * @param start
     * @param end
     * @param recs
     */
    protected void addRecommendationsToCache(long start, long end, List<Recommendation> recs) {
        getRecommendationCacheService().addRecommendation(generateKey(start,end), recs);
    }

    /**
     * Generate the cache key
     * @param start
     * @param end
     * @return
     */
    public abstract HashCode generateKey(long start, long end);

}
