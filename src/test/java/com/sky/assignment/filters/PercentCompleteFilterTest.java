package com.sky.assignment.filters;

import com.sky.assignment.model.Recommendation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import utils.TestUtils;

import java.util.List;

import static java.lang.System.currentTimeMillis;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by Pradeep Muralidharan.
 */

@RunWith(MockitoJUnitRunner.class)
public class PercentCompleteFilterTest {
    // SUT
    private PercentCompleteFilter percentCompleteFilter = new PercentCompleteFilter();

    @Mock
    private Recommendation recommendation;

    @Test
    public void should_be_irrelevant_when_recommendation_is_null(){
        boolean isRelevant = percentCompleteFilter.isRelevant(null, 0l,0l);
        assertFalse("Null recommendation should be irrelevant",isRelevant);
    }


    @Test
    public void should_be_irrelevant_when_recommendation_is_incomplete(){
        boolean isRelevant = percentCompleteFilter.isRelevant(recommendation, 1l,10l);
        assertFalse("recommendation should be irrelevant when starttime is greater than end time ",isRelevant);
    }

    @Test
    public void should_be_irrelevant_when_starttime_is_greater_than_end_time(){
        boolean isRelevant = percentCompleteFilter.isRelevant(recommendation, 10l,0l);
        assertFalse("recommendation should be irrelevant when starttime is greater than end time ",isRelevant);
    }

    @Test
    public void should_be_irrelevant_when_recommendation_run_past_60_percent_of_play_time(){
        long start = currentTimeMillis();
        long end   = start +( 3*30*60*1000);
        List<Recommendation> recs = TestUtils.getRecommendationsGreaterThan60PercentPlaytime(5, start);
        for(Recommendation r : recs){
            assertFalse(percentCompleteFilter.isRelevant(r, start, end));
        }
    }

    @Test
    public void should_be_relevant_when_recommendation_run_less_than_60_percent_of_play_time(){
        long start = currentTimeMillis();
        long end   = start +( 3*30*60*1000);
        List<Recommendation> recs = TestUtils.getRecommendationsLessThan60PercentPlaytime(5, start);
        for(Recommendation r : recs){
            assertTrue(String.format("Recommendation %s is irrelevant ", r.toString()),percentCompleteFilter.isRelevant(r, start, end));
        }
    }


}
