package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static java.lang.System.currentTimeMillis;

/**
 * Created by Pradeep Muralidharan.
 */

@Component
public class PercentCompleteFilter implements RecFilter {

    public static final int ELAPSED_TIME_PERCENTAGE = 60;

    /**
     * {@inheritDoc}
     *  this filter should discard recommendations that running time is past 60%
     *  for example if recommendation start time is 8:00 and end time is 9:00 then
     *  this recommendation should be discarded if current time is past 8:36, which is 60% of total show time
     *
     * @param r
     * @param start
     * @param end
     * @return
     */
    @Override
    public boolean isRelevant(Recommendation r, long start, long end) {

        boolean isRelevant = false;
        if (isValidRecommendation(r)) {
            BigDecimal timeValue = new BigDecimal((r.end - r.start) * ELAPSED_TIME_PERCENTAGE).divide(new BigDecimal(100));
            long thresholdTime = r.start + timeValue.setScale(0, RoundingMode.HALF_UP).longValueExact();
            isRelevant = (currentTimeMillis() < thresholdTime);
        }
        return isRelevant;
    }

    /**
     * Basic validation
     * assumption: Detailed validation is already present
     *
     * @param r
     * @return
     */
    private boolean isValidRecommendation(Recommendation r) {
        return (r != null && r.start != null && r.end != null && r.uuid != null && r.start < r.end);
    }

}
