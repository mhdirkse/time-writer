package com.github.mhdirkse.timewriter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.mhdirkse.timewriter.h2.DataInitializerForH2;
import com.github.mhdirkse.timewriter.model.UserInfo;

@Component
public class AdminUserInitializer implements CommandLineRunner {
    private UserInfoRepository userRepository;
    private UserController userController;
    private DataInitializerForH2 dataInitializerForH2;

    private Logger logger = LoggerFactory.getLogger(AdminUserInitializer.class);

    AdminUserInitializer(
            UserInfoRepository userRepository,
            UserController userController,
            DataInitializerForH2 dataInitializerForH2) {
        this.userRepository = userRepository;
        this.userController = userController;
        this.dataInitializerForH2 = dataInitializerForH2;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        logger.debug("Putting initial data if userRepository empty");
        if(userRepository.count() == 0) {
            addAdminUser();
            dataInitializerForH2.addData(userController);
        }
    }

    private void addAdminUser() {
        UserInfo adminUser = new UserInfo();
        adminUser.setUsername(UserPrincipal.ADMIN);
        adminUser.setPassword(UserPrincipal.ADMIN);
        userController.addUser(adminUser);
    }
}
