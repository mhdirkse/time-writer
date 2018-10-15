package com.github.mhdirkse.timewriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.github.mhdirkse.timewriter.model.UserInfo;

public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userInfoRepository.findByUsername(username);
        if(user == null) {
            throw new UserNotFoundException(username);
        }
        return new UserPrincipal(user);
    }

}
