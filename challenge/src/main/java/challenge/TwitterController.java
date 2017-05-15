package challenge;

/**
 * Created by newuser on 5/11/17.
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(value = "/twitter")
public class TwitterController {



    @Autowired
    private TwitterService twitterservice = new TwitterService();


    @RequestMapping(value = "/getfolloweefollower", method = RequestMethod.GET, produces = "application/json")
    public @ResponseBody
    PersonDto getFolloweeFollower() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Person p = twitterservice.findPersonDetail(name);
        Set<String> followingName = twitterservice.findAllPersonName(p.getFollowingSet());
        Set<String> followerName = twitterservice.findAllPersonName(p.getFollowersSet());
        PersonDto pd = new  PersonDto();
        pd.setName(name);
        pd.setFollowersSet(followerName);
        pd.setFollowingSet(followingName);
        pd.setTweets(null);
        pd.setFollowingTweets(null);
        return pd;
    }


    @RequestMapping(value = {"/readtweets"}, method = RequestMethod.GET, produces = "application/json")
    public PersonDto readTweets(@RequestParam("search") Optional<String> search)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Person p = twitterservice.findPersonDetail(name);
        PersonDto pd = new  PersonDto();
        pd.setName(p.getName());
        if(!search.isPresent()) {
            pd.setTweets(p.getTweets());
            Map<String, List<String>> followingTweetMap = twitterservice.getFollowingTweets(p.getFollowingSet());
            pd.setTweets(pd.getTweets());
            pd.setFollowingTweets(followingTweetMap);
        }
        else
        {
            String searchTerm="%" + search.get() + "%";
            Map<String, List<String>> followingTweetMap=twitterservice.getFilteredTweets(p.getId(),p.getFollowingSet(),searchTerm);
            pd.setTweets(followingTweetMap.get(pd.getName()));
            followingTweetMap.remove(pd.getName());
            pd.setFollowingTweets(followingTweetMap);
        }
        return pd;
    }

    @RequestMapping(value = {"/followPerson/{followname}"}, method = RequestMethod.POST)
    public Map<String,String> followPerson(@PathVariable(value="followname") String followName)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        int code =twitterservice.follow(name,followName);
        if(code==0)
            return Collections.singletonMap("Code","Success");
        else if(code==-2)
            return Collections.singletonMap("Code","Failed: Person Does not exist");
        else
            return Collections.singletonMap("Code","Failed: You already follow the person");
    }

    @RequestMapping(value = {"/unfollowPerson/{followname}"}, method = RequestMethod.POST)
    public Map<String,String> unfollowPerson(@PathVariable(value="followname") String followName)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        int code =twitterservice.unfollow(name,followName);
        if(code==0)
            return Collections.singletonMap("Code","Success");
        else if(code==-2)
            return Collections.singletonMap("Code","Failed: Person Does not exist");
        else
            return Collections.singletonMap("Code","Failed: User is not in your following list");
    }

    @RequestMapping(value = {"/getDistance/{distPersonName}"}, method = RequestMethod.GET, produces = "application/json")
    public Map<String,Integer> getDistance(@PathVariable String distPersonName)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        Person pCur = twitterservice.findPersonDetail(name);
        Person pDist=twitterservice.findPersonDetail(distPersonName);
        int dist=twitterservice.findDistance(pCur,pDist);
        return Collections.singletonMap("Distance: ",dist);
    }

    @RequestMapping(value = {"/findpopular"}, method = RequestMethod.GET, produces = "application/json")
    public Map<String,String> getDistance()
    {

        return twitterservice.findAllPopular();
    }

}
