# TwitterSimulator
I have made the following assumptions for this simulator.
  Each user has a unique name(as per the data).
  When a user has more than one popular follower I am returning only one of them based on the sorted person_id.

I have completed both the shortest path, and popular followers for each user in the task

## Below are the endpoints and URI used in the challenge

### Read Tweets:

To get tweets of the user and user's followers tweets: http://localhost:8080/twitter/readtweets
to search for a specific tweet http://localhost:8080/twitter/readtweets?search={searchterm}

### Follow a Person:

To follow a new person http://localhost:8080/twitter/followPerson/{Personname}
this will return whether the request was success or failure and reason for failure if any(like user is already in your list or the user does not exist).

### Unfollow a Person:

To follow a new person http://localhost:8080/twitter/unfollowPerson/{Personname}
this will return whether the request was success or failure and reason for failure if any(like user is not in your follower list or the user does not exist).

### Get follower and followee list:

To get the follower and followee list use http://localhost:8080/twitter/getfolloweefollower
this will return list of names of followees and followers the user has

### Get shortest distance:

To get shortest distance of a person http://localhost:8080/twitter/getDistance/{distPersonName}
this will give the minimum hops required to reach the person.

### Get popular follower

To get popular follower for all users http://localhost:8080/twitter/getDistance/findpopular
this will return list of all users with their respective popular follower.
