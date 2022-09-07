package com.amadeus.trip;

import com.amadeus.trip.job.QrtzJob;
import com.amadeus.trip.model.Role;
import com.amadeus.trip.model.User;
import com.amadeus.trip.model.repository.RoleRepository;
import com.amadeus.trip.model.repository.UserRepository;
import com.amadeus.trip.utils.Constants;
import lombok.extern.log4j.Log4j2;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
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

    /** This is only for initializtion purpose and to avoid to load data directly in DB
     * This should be removed if moved to prod of course
     */
    log.info("--- First Initialization ---");
    log.info("name=user, password=password, role=ROLE_USER");
    log.info("name=admin, password=password, role=ROLE_ADMIN");
    log.info("----------------------------");

    return args -> {

      if (roleRepository.findByName(Constants.ADMIN).isEmpty()) {
        roleRepository.save(new Role(Constants.ADMIN));
      }

      if (roleRepository.findByName(Constants.USER).isEmpty()) {
        roleRepository.save(new Role(Constants.USER));
      }

      if (userRepository.findByUsername("user") == null) {
        User user = new User();
        user.setUsername("user");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setRoles(roleRepository.findByName(Constants.USER).map(Collections::singleton).get());
        userRepository.save(user);
      }

      if (userRepository.findByUsername("admin") == null) {
        User user = new User();
        user.setUsername("admin");
        user.setPassword(bCryptPasswordEncoder.encode("password"));
        user.setRoles(roleRepository.findByName(Constants.ADMIN).map(Collections::singleton).get());
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
