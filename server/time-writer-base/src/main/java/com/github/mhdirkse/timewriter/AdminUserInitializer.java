package com.github.mhdirkse.timewriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.github.mhdirkse.timewriter.model.UserInfo;

@Component
public class AdminUserInitializer implements CommandLineRunner {
    private UserInfoRepository userRepository;

    @Autowired
    public void setUserRepository(UserInfoRepository userRepository) {
        this.userRepository = userRepository;
    }

    private UserController userController;

    @Autowired
    public void setUserController(UserController userController) {
        this.userController = userController;
    }

    @Override
    public void run(String... args) throws Exception {
        if(userRepository.count() == 0) {
            addAdminUser();
        }
    }

    private void addAdminUser() {
        UserInfo adminUser = new UserInfo();
        adminUser.setUsername("admin");
        adminUser.setPassword("admin");
        userController.addUser(adminUser);
    }
}
