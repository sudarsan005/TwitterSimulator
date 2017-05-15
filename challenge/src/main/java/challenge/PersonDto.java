package challenge;

/**
 * Created by newuser on 5/12/17.
 */
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class PersonDto {
    private String name;
    private int personId;
    private List<String> tweets = new ArrayList<String>();
    private Set<String> followingSet = new HashSet<String>();
    private Set<String> followersSet = new HashSet<String>();
    private Map<String,List<String>> followingTweets = new HashMap<String,List<String>>();
    public Map<String, List<String>> getFollowingTweets() {
        return followingTweets;
    }

    public void setFollowingTweets(Map<String, List<String>> followingTweets) {
        this.followingTweets = followingTweets;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTweets() {
        return tweets;
    }

    public void setTweets(List<String> tweets) {
        this.tweets = tweets;
    }

    public Set<String> getFollowingSet() {
        return followingSet;
    }

    public void setFollowingSet(Set<String> followingSet) {
        this.followingSet = followingSet;
    }

    public Set<String> getFollowersSet() {
        return followersSet;
    }

    public void setFollowersSet(Set<String> followersSet) {
        this.followersSet = followersSet;
    }


}

