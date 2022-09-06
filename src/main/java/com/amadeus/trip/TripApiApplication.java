package com.amadeus.trip;

import com.amadeus.trip.model.Role;
import com.amadeus.trip.model.User;
import com.amadeus.trip.model.repository.RoleRepository;
import com.amadeus.trip.model.repository.UserRepository;
import com.amadeus.trip.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.security.crypto.password.PasswordEncoder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.HashSet;

@SpringBootApplication
@EnableMongoRepositories
@Log4j2
public class TripApiApplication { //implements CommandLineRunner {

  @Autowired
  PasswordEncoder bCryptPasswordEncoder;

  public static void main(String[] args) {

    SpringApplication.run(TripApiApplication.class, args);

  }

  @Bean
  CommandLineRunner init(RoleRepository roleRepository, UserRepository userRepository) {

    log.info("INITIALIZING ROLES");

    return args -> {

      if (roleRepository.findByName(Constants.ADMIN) == null) {
        roleRepository.save(new Role(Constants.ADMIN));
      }

      if (roleRepository.findByName(Constants.USER) == null) {
        roleRepository.save(new Role(Constants.USER));
      }

      if (userRepository.findByUsername("user") == null) {
        User user = new User();
        user.setUsername("user");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName(Constants.USER).get())));
        userRepository.save(user);
      }

      if (userRepository.findByUsername("admin") == null) {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setRoles(new HashSet<>(Collections.singletonList(roleRepository.findByName(Constants.ADMIN).get())));
        userRepository.save(user);
      }
    };

  }

  //  @Override
  //  public void run(String... args) {
  //
  ////    ts.creationNotifier("Paris CDG");
  //
  //    User u = new User("bob");
  //    userRepo.save(u);
  //
  //    System.out.println("Customers found with findAll():");
  //    System.out.println("-------------------------------");
  //    for (User user : userRepo.findAll()) {
  //      System.out.println(user);
  //    }
  //    System.out.println();
  //
  //  }

}
