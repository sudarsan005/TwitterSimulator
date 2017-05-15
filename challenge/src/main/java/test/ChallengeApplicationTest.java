package test;

/**
 * Created by newuser on 5/14/17.
 *
 */

//import org.junit.Assert.*;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;
import challenge.PersonDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import java.net.URL;
import java.util.List;
import java.util.Map;

@RunWith(SpringRunner.class)
@EnableAutoConfiguration()

public class ChallengeApplicationTest {

    private URL base;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:8080/twitter");
    }

    @Test
    public void getTweets() throws Exception {
        final TestRestTemplate template = new TestRestTemplate();
        this.base = new URL("http://localhost:8080/twitter/readtweets");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic UmlnZWwgWW91bmc6cGFzc3dvcmQ=");
        ResponseEntity<PersonDto> response = template.exchange(base.toString(),
                HttpMethod.GET, new HttpEntity<>(headers), PersonDto.class);
        Map<String, List<String>> map = response.getBody().getFollowingTweets();
        assertEquals("Rigel Young",response.getBody().getName());
        assertNotEquals("Rigel Youngs",response.getBody().getName());
        assertEquals(22,response.getBody().getTweets().size());//check number of tweets of user
        assertEquals(19,map.get("Vanna Noel").size());//check number of tweets of user's one of the follower
    }

    @Test
    public void getTweetsSearch() throws Exception {
        final TestRestTemplate template = new TestRestTemplate();
        this.base = new URL("http://localhost:8080/twitter/readtweets?search=dolor");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic UmlnZWwgWW91bmc6cGFzc3dvcmQ=");
        ResponseEntity<PersonDto> response = template.exchange(base.toString(),
                HttpMethod.GET, new HttpEntity<>(headers), PersonDto.class);
        Map<String, List<String>> map = response.getBody().getFollowingTweets();
        assertEquals("Rigel Young",response.getBody().getName());
        assertNotEquals("Rigel Youngs",response.getBody().getName());
        assertEquals(3,response.getBody().getTweets().size());//check number of tweets of user
        assertEquals(1,map.get("Ella Mullen").size());//check number of tweets of user's one of the follower

    }

    @Test
    public void follow() throws Exception{
        final TestRestTemplate template = new TestRestTemplate();
        this.base=new URL("http://localhost:8080/twitter/followPerson/Guinevere Lindsey");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic UmlnZWwgWW91bmc6cGFzc3dvcmQ=");
        ResponseEntity<String> response = template.exchange(base.toString(), HttpMethod.POST, new HttpEntity<>(headers), String.class);
        assertEquals("{\"Code\":\"Success\"}",response.getBody().toString());
        response = template.exchange(base.toString(), HttpMethod.POST, new HttpEntity<>(headers), String.class);
        assertEquals("{\"Code\":\"Failed: You already follow the person\"}",response.getBody().toString());
        this.base=new URL("http://localhost:8080/twitter/unfollowPerson/Abc xyz");
        response = template.exchange(base.toString(), HttpMethod.POST, new HttpEntity<>(headers), String.class);
        assertEquals("{\"Code\":\"Failed: Person Does not exist\"}",response.getBody().toString());
    }

    @Test
    public void unFollow() throws Exception{
        final TestRestTemplate template = new TestRestTemplate();
        this.base=new URL("http://localhost:8080/twitter/unfollowPerson/Judith Woodard");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic UmlnZWwgWW91bmc6cGFzc3dvcmQ=");
        ResponseEntity<String> response = template.exchange(base.toString(), HttpMethod.POST, new HttpEntity<>(headers), String.class);
        assertEquals("{\"Code\":\"Success\"}",response.getBody().toString());
        response = template.exchange(base.toString(), HttpMethod.POST, new HttpEntity<>(headers), String.class);
        assertEquals("{\"Code\":\"Failed: User is not in your following list\"}",response.getBody().toString());
        this.base=new URL("http://localhost:8080/twitter/unfollowPerson/Abc xyz");
        response = template.exchange(base.toString(), HttpMethod.POST, new HttpEntity<>(headers), String.class);
        assertEquals("{\"Code\":\"Failed: Person Does not exist\"}",response.getBody().toString());
        //"{\"Code\",\"Failed: Person Does not exist\"}"
    }

    @Test
    public void getFolloweeFollower() throws  Exception{
        final TestRestTemplate template = new TestRestTemplate();
        this.base=new URL("http://localhost:8080/twitter/getfolloweefollower");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic UmlnZWwgWW91bmc6cGFzc3dvcmQ=");
        ResponseEntity<PersonDto> response = template.exchange(base.toString(), HttpMethod.GET,
                new HttpEntity<>(headers), PersonDto.class);
        assertTrue(response.getBody().getFollowingSet().contains("Ella Mullen"));
        assertTrue(response.getBody().getFollowersSet().contains("Noble Walsh"));
        assertFalse(response.getBody().getFollowingSet().contains("Emma Arnold"));//failcase
        assertFalse(response.getBody().getFollowersSet().contains("Ignatius Salinas"));//failcase
    }

    @Test
    public void getDistance() throws  Exception{
        final TestRestTemplate template = new TestRestTemplate();
        this.base=new URL("http://localhost:8080/twitter/getDistance/Ella Mullen");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic UmlnZWwgWW91bmc6cGFzc3dvcmQ=");
        ResponseEntity<String> response = template.exchange(base.toString(), HttpMethod.GET,
                new HttpEntity<>(headers),String.class);
        assertEquals("{\"Distance: \":1}",response.getBody());
        response = template.exchange(base.toString(), HttpMethod.GET,
                new HttpEntity<>(headers), String.class);
        assertEquals("{\"Distance: \":1}",response.getBody());

    }

    @Test
    public void findpopular() throws Exception{
        final TestRestTemplate template = new TestRestTemplate();
        this.base=new URL("http://localhost:8080/twitter/findpopular");
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic UmlnZWwgWW91bmc6cGFzc3dvcmQ=");
        ResponseEntity<Map<String,String>> response = template.exchange(base.toString(), HttpMethod.GET,
                new HttpEntity<>(headers), new ParameterizedTypeReference<Map<String,String>>(){});
        Map<String,String> map = response.getBody();
        String actual=map.get("Noble Walsh");
        assertEquals("Xandra Christensen",actual);
        actual=map.get("Judith Woodard");
        assertEquals("Eagan Perry",actual);
    }


}
