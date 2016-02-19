package utils;

import com.sky.assignment.model.Recommendation;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static java.lang.System.currentTimeMillis;

/**
 * Created by Pradeep Muralidharan.
 */
public class TestUtils {
    private static Random random = new Random(currentTimeMillis());

    private static final String BASE_URL = "http://localhost:8181/recs/personalised";


    /**
     * Get reommendations less than 60 percent of playtime from start time
     * @param noOfRecommendations
     * @param startTime
     * @return
     */
    public static List<Recommendation> getRecommendationsLessThan60PercentPlaytime(int noOfRecommendations, long startTime) {
        List<Recommendation> recommendations = new ArrayList<Recommendation>();
        long minute = (60 * 1000);
        long rStart = startTime - (30 * minute);
        long rPast = rStart + (55 * minute);
        do {
            recommendations.add(new Recommendation(UUID.randomUUID().toString(), rStart, rPast + random.nextInt(10 * 60 * 1000)));

        } while (recommendations.size() < noOfRecommendations);
        return recommendations;
    }

    /**
     * Get reommendations greater than 60 percent of playtime from start time
     * @param noOfRecommendations
     * @param startTime
     * @return
     */
    public static List<Recommendation> getRecommendationsGreaterThan60PercentPlaytime(int noOfRecommendations, long startTime) {
        List<Recommendation> recommendations = new ArrayList<Recommendation>();
        long minute = (60 * 1000);
        long rStart = startTime - (30 * minute);
        do {
            recommendations.add(new Recommendation(UUID.randomUUID().toString(), rStart, startTime + random.nextInt(10 * 60 * 1000)));

        } while (recommendations.size() < noOfRecommendations);
        return recommendations;
    }

    /**
     * Get http entity with the provided MediaType
     * @param mediaType
     * @return
     */
    public static HttpEntity getHttpEntity(MediaType mediaType) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        return new HttpEntity(headers);
    }

    /**
     * Build uri with parameters
     * @param num
     * @param start
     * @param end
     * @param sub
     * @return
     */
    public static URI buildURI(long num, long start, long end, String sub) {
        return UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("num", num)
                .queryParam("start", start)
                .queryParam("end", end)
                .queryParam("subscriber", sub)
                .build().toUri();
    }

    public static boolean isRecommendationValidForPercentComplete(Recommendation r, long start){
        boolean isValid = false;
         if(r != null){
            long val = r.start +  new BigDecimal(r.start+r.end).multiply(new BigDecimal("0.6")).longValue();
            isValid = (start < val);
         }

        return isValid;

    }
}
