package Integration.test.recommend;

import com.sky.assignment.Application;
import com.sky.assignment.model.Recommendation;
import com.sky.assignment.model.Recommendations;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;
import utils.TestUtils;

import java.util.UUID;

import static java.lang.String.format;
import static junit.framework.Assert.*;
import static utils.TestUtils.buildURI;
import static utils.TestUtils.getHttpEntity;
import static utils.TestUtils.isRecommendationValidForPercentComplete;

/**
 * Created by Pradeep Muralidharan.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = Application.class)
@WebAppConfiguration
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class})
@IntegrationTest("server.port:8181")
public class RecommendationIntegrationTest {


    private RestTemplate restTemplate = new RestTemplate();

    @Before
    public void before() {
    }

    @Test
    public void test_when_request_is_invalid_num_is_0_then_0_recommendations() {
        long start = System.currentTimeMillis();
        long end = start + (4 * 30 * 60 * 1000);
        long num = 0;
        String sub = UUID.randomUUID().toString();

        HttpEntity<Recommendations> recommendations = restTemplate.exchange(
                buildURI(num, start, end, sub),
                HttpMethod.GET,
                getHttpEntity(MediaType.APPLICATION_XML),
                Recommendations.class);

        assertNotNull(recommendations.getBody());
        assertNull(recommendations.getBody().recommendations);
    }

    @Test
    public void test_when_request_is_invalid_start_end_times_then_return_0_recommendations() {
        long start = System.currentTimeMillis();
        long end = start - (4 * 30 * 60 * 1000);
        long num = 5;
        String sub = UUID.randomUUID().toString();

        HttpEntity<Recommendations> recommendations = restTemplate.exchange(
                buildURI(num, start, end, sub),
                HttpMethod.GET,
                getHttpEntity(MediaType.APPLICATION_XML),
                Recommendations.class);

        assertNotNull(recommendations.getBody());
        assertNull(recommendations.getBody().recommendations);
    }


    @Test
    public void test_when_request_is_valid_and_5_num_return_5_recommendations() {
        long start = System.currentTimeMillis();
        long end = start + (4 * 30 * 60 * 1000);
        long num = 5;
        String sub = UUID.randomUUID().toString();

        HttpEntity<Recommendations> recoEntity = restTemplate.exchange(
                buildURI(num, start, end, sub),
                HttpMethod.GET,
                getHttpEntity(MediaType.APPLICATION_XML),
                Recommendations.class);
        Recommendations recommendations = recoEntity.getBody();

        assertNotNull(recommendations);
        assertNotNull(recommendations.recommendations);
        assertTrue(format("Should get %d%n reommendations back", num), recommendations.recommendations.size() == num);
        for(Recommendation r : recommendations.recommendations)
            assertTrue(format("Recommendation %s should be relevant", r.toString()), isRecommendationValidForPercentComplete(r, start));

    }

    @Test
    public void test_when_request_is_valid_and_10_num_return_10_recommendations() {
        long start = System.currentTimeMillis();
        long end = start + (4 * 30 * 60 * 1000);
        long num = 5;
        String sub = UUID.randomUUID().toString();

        HttpEntity<Recommendations> recoEntity = restTemplate.exchange(
                buildURI(num, start, end, sub),
                HttpMethod.GET,
                getHttpEntity(MediaType.APPLICATION_XML),
                Recommendations.class);
        Recommendations recommendations = recoEntity.getBody();

        assertNotNull(recommendations);
        assertNotNull(recommendations.recommendations);
        assertTrue(format("Should get %d%n reommendations back", num), recommendations.recommendations.size() == num);
        for(Recommendation r : recommendations.recommendations)
            assertTrue(format("Recommendation %s should be relevant", r.toString()), isRecommendationValidForPercentComplete(r, start));

    }




}
