package com.sky.assignment.engine;

import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.sky.assignment.filters.RecFilter;
import com.sky.assignment.model.Recommendation;
import com.sky.assignment.model.Recommendations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Pradeep Muralidharan.
 */

@Service
public class RecsEngine extends AbstractRecommendationEngine {

    private RecFilter[] filters;

    private RecommendationCacheService recommendationCacheService;

    private RecsGenerator recsGenerator;


    /**
     * {@inheritDoc}
     * @param numberOfRecs
     * @param start
     * @param end
     * @return
     */
    @Override
    public Recommendations recommend(long numberOfRecs, long start, long end) {
        List<Recommendation> recs = new ArrayList<Recommendation>();
        if (isValidInput(numberOfRecs, start, end)) {
            populateValidCachedRecommendations(numberOfRecs, start, end, recs);
            generateNewRecommendationsIfRequired(numberOfRecs, start, end, recs);
            addRecommendationsToCache(start, end, recs);
        }
        return new Recommendations(recs);
    }


    /**
     * Get new reommendations
     * @param numberOfRecs
     * @param start
     * @param end
     * @param recs
     */
    protected void generateNewRecommendationsIfRequired(long numberOfRecs, long start, long end, List<Recommendation> recs) {
        while (recs.size() < numberOfRecs) {
            Recommendation r = recsGenerator.generate();
            if (runFilters(r, start, end)) {
                recs.add(r);
            }
        }

    }

    /**
     * Get recommendations from cache, if recommendations are still valid
     * populate the list of recommendations
     * @param numOfRecs
     * @param start
     * @param end
     * @param recs
     */
    protected void populateValidCachedRecommendations(long numOfRecs, long start, long end, List<Recommendation> recs) {
        List<Recommendation> cachedRecommendations = getCachedRecommendation(start, end);
        if (cachedRecommendations != null && !CollectionUtils.isEmpty(cachedRecommendations)) {
            for (Recommendation r : cachedRecommendations) {
                if (recs.size() < numOfRecs && runFilters(r, start, end)) {
                    recs.add(r);
                }
            }
        }
    }


    /**
     * Checks if input is valid
     * @param num
     * @param start
     * @param end
     * @return
     */
    private boolean isValidInput(long num, long start, long end) {
        return (num > 0 && start < end);
    }

    /**
     * Generate the cache key
     * @param start
     * @param end
     * @return
     */
    public HashCode generateKey(long start, long end){
        return Hashing.md5().hashLong(start+end);
    }

    @Autowired
    public RecsEngine(RecFilter[] filters, RecommendationCacheService recommendationCacheService, RecsGenerator recsGenerator) {
        this.filters = filters;
        this.recommendationCacheService = recommendationCacheService;
        this.recsGenerator = recsGenerator;
    }

    @Override
    public RecFilter[] getFilters() {
        return filters;
    }

    @Override
    public RecommendationCacheService getRecommendationCacheService() {
        return this.recommendationCacheService;
    }


}
