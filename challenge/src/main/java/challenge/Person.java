package challenge;

import java.util.*;

/**
 * Created by newuser on 5/10/17.
 */
public class Person {


    private int id;
    private List<String> tweets = new ArrayList<String>();
    private String name;
    private Set<Integer> followingSet = new HashSet<Integer>();
    private Set<Integer> followersSet = new HashSet<Integer>();

    Person()
    {

    }

    Person(int id, String name)
    {
        this.id=id;
        this.name=name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFollowingSet(Set<Integer> followingSet) {
        this.followingSet = followingSet;
    }

    public void setFollowersSet(Set<Integer> followersSet) {
        this.followersSet = followersSet;
    }
    public List<String> getTweets() {
        return tweets;
    }

    public void setTweets(List<String> tweets) {
        this.tweets = tweets;
    }


    public Set<Integer> getFollowingSet() {
        return followingSet;
    }

    public Set<Integer> getFollowersSet() {
        return followersSet;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }




}
