package challenge;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by newuser on 5/13/17.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {


    @Autowired
    TwitterService twitterService;

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        int userExists= twitterService.findUserExists(username);
        if (userExists == -1)
            throw new UsernameNotFoundException("username " + username
                    + " not found");

        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        UserDetails userDetails = new User(username, "password", true,
                true, true, true,
                authList);
        return userDetails;
    }

}
