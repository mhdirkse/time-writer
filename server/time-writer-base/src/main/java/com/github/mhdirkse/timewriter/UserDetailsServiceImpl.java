package com.github.mhdirkse.timewriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.github.mhdirkse.timewriter.model.UserInfo;

public class UserDetailsServiceImpl implements UserDetailsService {
    private Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug(String.format("Login requested with username %s", username));
        UserInfo user = userInfoRepository.findByUsername(username);
        if(user == null) {
            logger.debug("User not found in database");
            throw new UsernameNotFoundException(username);
        }
        logger.debug("Obtained user %s from database.", user.getUsername());
        return new UserPrincipal(user);
    }

}
