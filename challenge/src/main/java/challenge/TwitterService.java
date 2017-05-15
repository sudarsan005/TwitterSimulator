package challenge;

/**
 * Created by newuser on 5/11/17.
 */
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import org.assertj.core.util.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@SpringBootApplication
public class TwitterService implements RowMapper {

    @Autowired
    JdbcTemplate h2 = new JdbcTemplate();

    public List<Person> findAllPerson() {
        List<Person> pList = new ArrayList<>();
        String sql = "SELECT id,name from PERSON";
    try{
        h2.query(
                sql,
                (rs, rowNum) -> new Person(rs.getInt("id"), rs.getString("name"))
        ).forEach(person -> pList.add(person));
    } catch (EmptyResultDataAccessException e) {
        return null;
    }
        return pList;
    }

    @Override
    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
        Person person = new Person(rs.getInt("id"), rs.getString("name"));
        return person;
    }

    //for authentication
    public int findUserExists(String name)
    {

        try{
            String sql = "SELECT count(1) FROM PERSON WHERE name = ?";
            int total = h2.queryForObject(sql, new Object[]{name.toLowerCase()},
                    Integer.class);
            return total;
        } catch (Exception e) {
            return -1;
        }
    }

    public Person findPersonDetail(String name) {
        String sql = "SELECT count(1) FROM PERSON WHERE LOWER(name) = ?";
        Person p;
        int total = h2.queryForObject(sql, new Object[]{name.toLowerCase()},
                Integer.class);
        if (total > 1)
            return new Person();
        else {
        try{
            sql = "SELECT id, name FROM PERSON WHERE LOWER(name) = ?";
            p = (Person) h2.queryForObject(
                    sql, new Object[]{name.toLowerCase()},
                    new TwitterService());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        }
        p.setFollowersSet(findFollowers(p.getId()));
        p.setFollowingSet(findFollowing(p.getId()));
        p.setTweets(getTweets(p.getId()));
        return p;
    }

    public Person findPersonDetail(int id) {
        String sql = "SELECT count(1) FROM PERSON WHERE id = ?";
        Person p;
        int total = h2.queryForObject(sql, new Object[]{id},
                Integer.class);
        if (total > 1)
            return new Person();
        else {
            sql = "SELECT id, name FROM PERSON WHERE id = ?";
         try{
            p = (Person) h2.queryForObject(
                    sql, new Object[]{id},
                    new TwitterService());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        }
        p.setFollowersSet(findFollowers(p.getId()));
        p.setFollowingSet(findFollowing(p.getId()));
        p.setTweets(getTweets(p.getId()));
        return p;
    }


    //find followers
    public Set<Integer> findFollowers(int id) {

        Set<Integer> fSet = new HashSet<Integer>();
        String sql = "SELECT follower_person_id from followers where person_id=?";
        h2.query(sql, new Object[]{id},
                (rs, rowNum) -> fSet.add(rs.getInt("follower_person_id")));
        return fSet;
    }

    //find following
    public Set<Integer> findFollowing(int id) {
        Set<Integer> fSet = new HashSet<Integer>();
        String sql = "SELECT person_id from followers where follower_person_id=?";
        h2.query(sql, new Object[]{id},
                (rs, rowNum) -> fSet.add(rs.getInt("person_id")));
        return fSet;
    }

    //to follow someone new
    public int follow(String followerName, String followeeName)  {
        Person follower = findPersonDetail(followerName);
        Person followee = findPersonDetail(followeeName);
        if(followee==null)
            return -2; //person does not exist
        if (!follower.getFollowingSet().contains(followee.getId())) {
            String sql = "INSERT INTO followers (person_id, follower_person_id) VALUES (?, ?)";
            h2.update(sql, new Object[]{followee.getId(), follower.getId()});
            follower.getFollowingSet().add(followee.getId());
            return 0;
        }
        return -1;//already followed
    }

    //unfollow
    public int unfollow(String followerName, String followeeName) {
        Person follower = findPersonDetail(followerName);
        Person followee = findPersonDetail(followeeName);
        if(followee==null)
            return -2;
        if (follower.getFollowingSet().contains(followee.getId())) {
            String sql = "DELETE followers WHERE person_id=? and follower_person_id=?";
            h2.update(sql, new Object[]{followee.getId(), follower.getId()});
            follower.getFollowingSet().remove(followee.getId());
            return 0;
        }
        return -1;
    }

    //findpopularfollower
    public Person findPopularFollower(int id) {

        try {
            String sql = "SELECT ID,NAME FROM PERSON WHERE ID IN\n" +
                    "(SELECT PERSON_ID FROM\n" +
                    "(SELECT (COUNT(1)),PERSON_ID FROM FOLLOWERS WHERE \n" +
                    "PERSON_ID IN (SELECT FOLLOWER_PERSON_ID FROM FOLLOWERS where PERSON_ID=?)\n" +
                    "GROUP BY PERSON_ID ORDER BY COUNT(1) DESC,PERSON_ID LIMIT 1))";
            Person p = (Person) h2.queryForObject(
                    sql, new Object[]{id},
                    new TwitterService());
            return p;

        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public List<String> getTweets(int id) {
        List<String> tweetList = new ArrayList<String>();
        String sql = "SELECT CONTENT FROM TWEET WHERE PERSON_ID=?";
        try{
            h2.query(sql, new Object[]{id},
                    (rs, rowNum) -> tweetList.add(rs.getString("CONTENT")));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
        return tweetList;
    }

    //findallpopular
    public Map<String, String> findAllPopular() {
        String sql = "SELECT ID,NAME FROM PERSON";
        List<Person> pList = new ArrayList<Person>();

        h2.query(
                sql,
                (rs, rowNum) -> new Person(rs.getInt("id"), rs.getString("name"))
        ).forEach(person -> pList.add(person));

        Map<String, String> popularMap = new HashMap<String, String>();

        for (Person p : pList) {
            Person popularFollower = findPopularFollower(p.getId());
            if (popularFollower == null)
                continue;
            popularMap.put(p.getName(), popularFollower.getName());
        }
        return popularMap;

    }

    public Map<String, List<String>> getFollowingTweets(Set<Integer> followingSet)
    {
        Map<String, List<String>> followingTweets = new HashMap<String, List<String>>();
        for(int id : followingSet)
        {
            String name = findPersonNameById(id);
            if(name!=null)
                followingTweets.put(name,getTweets(id));
        }
        return followingTweets;
    }

    public Map<String,List<String>> getFilteredTweets(int id,Set<Integer> followingSet,String searchTerm)
    {
        MapSqlParameterSource parameters = new MapSqlParameterSource();
        followingSet.add(id);
        parameters.addValue("ids", Lists.newArrayList(followingSet));
        parameters.addValue("searchTerm",searchTerm);


        Map<String,List<String>> filteredTweetMap = new LinkedHashMap<String,List<String>>();

        for(int pid : followingSet)
        {
            filteredTweetMap.put(findPersonNameById(pid),new ArrayList<String>());
            filteredTweetMap.put(findPersonNameById(id),new ArrayList<String>());
        }
        NamedParameterJdbcTemplate np= new NamedParameterJdbcTemplate(h2);
        String sql ="SELECT NAME,CONTENT FROM TWEET JOIN PERSON ON TWEET.PERSON_iD = PERSON.iD WHERE PERSON_ID in (:ids) and CONTENT LIKE :searchTerm";

        try{

            np.query(sql,parameters,
                    (rs, rowNum) -> filteredTweetMap.get(rs.getString("NAME")).add(rs.getString("CONTENT")));

        } catch (Exception e)

        {
            e.printStackTrace();
            return null;
        }
        return filteredTweetMap;
    }

    public String findPersonNameById(int id)
    {
        String sql = "SELECT NAME FROM PERSON WHERE ID=?";
        try {
            String name = h2.queryForObject(sql, new Object[]{id},
                    String.class);
            return name;
        } catch (EmptyResultDataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Set<String> findAllPersonName(Set<Integer> idSet)
    {
        Set<String> personName = new HashSet<String>();
        for(int id: idSet) {
            String name=findPersonNameById(id);
            if(name!=null)
                personName.add(name);
        }
        return personName;
    }


    //find shortest distance- using a Breath First Search approach
    public int findDistance(Person p1, Person p2)
    {
        int count=0;
        if(p1.getId()==p2.getId())
            return count;
        Set<Integer> visted = new HashSet<Integer>();
        Queue<Person> queue = new LinkedList<Person>();
        queue.add(p1);
        visted.add(p1.getId());
        while(!queue.isEmpty())
        {
            p1=queue.poll();
            Person temp;
            Set<Integer> following=p1.getFollowingSet();
            if(following.contains(p2.getId())) {
                count++;
                return count;
            }
            count++;
            for(int i : following)
            {
                if(!visted.contains(i))
                {
                    visted.add(i);
                    Person child=findPersonDetail(i);
                    if(child.getFollowingSet().contains(p2.getId())) {
                        count++;
                        return count;
                    }
                    queue.add(child);
                }
            }

        }
        return 0;
    }
}
