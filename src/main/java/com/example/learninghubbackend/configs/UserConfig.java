package com.example.learninghubbackend.configs;

import com.example.learninghubbackend.models.User;
import com.example.learninghubbackend.services.user.Gender;
import com.example.learninghubbackend.services.user.Role;
import com.example.learninghubbackend.services.user.UserService;
import com.example.learninghubbackend.utils.HashUtil;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {

    @Bean
    CommandLineRunner runner(UserService userService) {
        return args -> {
            User owner = new User("product_owner", HashUtil.hash("Product_Owner@123"), "Product Owner", "", "", Role.OWNER, Gender.OTHER);
            if (!userService.query().existByUsername(owner.getUsername())) {
                userService.query().save(owner);
            }
        };
    }
}
