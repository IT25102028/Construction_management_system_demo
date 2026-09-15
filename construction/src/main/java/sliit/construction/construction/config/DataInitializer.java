package sliit.construction.construction.config;

import sliit.construction.construction.entity.Role;
import sliit.construction.construction.entity.User;
import sliit.construction.construction.repository.UserRepository;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createStaffUsers(UserRepository userRepository) {

        return args -> {

            BCryptPasswordEncoder encoder =
                    new BCryptPasswordEncoder();


            // ==========================================
            // 1. PROJECT MANAGER
            // ==========================================

            createOrUpdateUser(
                    userRepository,
                    encoder,
                    "projectmanager",
                    "projectmanager@wbcms.com",
                    "Wbcms@123",
                    Role.PROJECT_MANAGER,
                    "Project Manager"
            );


            // ==========================================
            // 2. SITE ENGINEER
            // ==========================================

            createOrUpdateUser(
                    userRepository,
                    encoder,
                    "siteengineer",
                    "siteengineer@wbcms.com",
                    "Wbcms@123",
                    Role.SITE_ENGINEER,
                    "Site Engineer"
            );


            // ==========================================
            // 3. CONSTRUCTION SUPERVISOR
            // ==========================================

            createOrUpdateUser(
                    userRepository,
                    encoder,
                    "supervisor",
                    "supervisor@wbcms.com",
                    "Wbcms@123",
                    Role.CONSTRUCTION_SUPERVISOR,
                    "Construction Supervisor"
            );


            // ==========================================
            // 4. PROCUREMENT OFFICER
            // ==========================================

            createOrUpdateUser(
                    userRepository,
                    encoder,
                    "procurement",
                    "procurement@wbcms.com",
                    "Wbcms@123",
                    Role.PROCUREMENT_OFFICER,
                    "Procurement Officer"
            );


            // ==========================================
            // 5. SYSTEM ADMINISTRATOR
            // ==========================================

            createOrUpdateUser(
                    userRepository,
                    encoder,
                    "admin",
                    "admin@wbcms.com",
                    "Wbcms@123",
                    Role.SYSTEM_ADMINISTRATOR,
                    "System Administrator"
            );


            System.out.println();
            System.out.println("==============================================");
            System.out.println("       WBCMS STAFF ACCOUNTS READY");
            System.out.println("==============================================");
            System.out.println("Project Manager       : projectmanager / Wbcms@123");
            System.out.println("Site Engineer         : siteengineer   / Wbcms@123");
            System.out.println("Construction Supervisor: supervisor    / Wbcms@123");
            System.out.println("Procurement Officer   : procurement    / Wbcms@123");
            System.out.println("System Administrator  : admin          / Wbcms@123");
            System.out.println("==============================================");
            System.out.println();

        };
    }


    private void createOrUpdateUser(
            UserRepository userRepository,
            BCryptPasswordEncoder encoder,
            String username,
            String email,
            String password,
            Role role,
            String fullName
    ) {

        User user =
                userRepository.findByUsername(username)
                        .orElseGet(User::new);


        user.setUsername(username);
        user.setEmail(email);

        /*
         * Always create a BCrypt hash.
         * This means the plain password is never
         * stored directly in SQL Server.
         */
        user.setPasswordHash(
                encoder.encode(password)
        );

        user.setRole(role);
        user.setFullName(fullName);
        user.setPhoneNumber(null);


        userRepository.save(user);

    }
}