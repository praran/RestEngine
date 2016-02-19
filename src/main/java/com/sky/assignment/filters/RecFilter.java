package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;

public interface RecFilter {

    /**
     * Checks if the given recommendation is relevant
     * @param r
     * @param start
     * @param end
     * @return
     */
    public boolean isRelevant(Recommendation r, long start, long end);
}
