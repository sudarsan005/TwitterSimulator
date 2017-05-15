# TwitterSimulator
I have made the following assumptions for this simulator.
  The problem statement mentions "An endpoint to read the message list for the current user". Hence, I've assumed each user name to be unique.
  When a user has more than one popular follower I am returning only one of them based on the sorted person_id.

For login Authentication User Name will be from the database and default password for all users is 'password' (eg. Rigel Young/password). Note that in order to configure passwords for each user, the database schema would have to be changed to incorporate the salted hash of the passwords retrived from a login form. This would be an enhancement that could be considered.

I have completed both the shortest path and popular followers for each user tasks.

## Below are the endpoints and URI used in the challenge

### Read Tweets:

To get tweets of the user and the user's followers tweets: GET http://localhost:8080/twitter/readtweets
To search for a specific tweet GET http://localhost:8080/twitter/readtweets?search={searchterm}

### Follow a Person:

To follow a new person: POST http://localhost:8080/twitter/followPerson/{personName}
This will return whether the request was successful or failed and the reason for failure if any(eg. the user is already in your list or the user does not exist).

### Unfollow a Person:

To follow a new person: POST http://localhost:8080/twitter/unfollowPerson/{persoName}
This will return whether the request was successful or failed and the reason for failure if any(eg. the user is already in your list or the user does not exist).

### Get follower and followee list:

To get the follower and followee list: GET http://localhost:8080/twitter/getfolloweefollower
This will return a list of names of followees and followers corresponding to the user.

### Get shortest distance:

To get shortest distance of a person GET http://localhost:8080/twitter/getDistance/{distPersonName}
This will give the minimum hops required to reach the person.

### Get popular follower

To get popular follower for all users GET http://localhost:8080/twitter/getDistance/findpopular
This will return list of all users with their respective popular follower.
