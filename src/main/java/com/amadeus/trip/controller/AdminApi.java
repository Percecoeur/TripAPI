package com.amadeus.trip.controller;

import com.amadeus.trip.model.Role;
import com.amadeus.trip.model.User;
import com.amadeus.trip.model.request.UserDTO;
import com.amadeus.trip.model.repository.RoleRepository;
import com.amadeus.trip.model.repository.UserRepository;
import com.amadeus.trip.utils.Constants;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/v0")
@Log4j2
public class AdminApi {

  private static final String OK = "Message sent !";
  @Autowired
  PasswordEncoder bCryptPasswordEncoder;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;


//  @PostMapping("/token")
//  public ResponseEntity<?> token(@Valid @RequestBody ) {}
//
//  @PostMapping("/register")
//  public ResponseEntity<?> register(@Valid @RequestBody ) {
//  }
//
  @ApiOperation(value = "Will create a new user with role")
  @RolesAllowed({ Constants.ADMIN })
  @PostMapping("/adduser")
  public ResponseEntity<User> adduser(@RequestBody UserDTO userDTO) {

    //  we need to reference the DB objects so we have to find them first (can we avoid this?)
    Set<Role> userRoles = userDTO.getRoles();
    Set<Role> dbRoles = new HashSet<>();
    userRoles.forEach(r -> dbRoles.add(roleRepository.findByName(r.getName())));

    User newUser = new User();
    newUser.setRoles(dbRoles);
    newUser.setPassword(bCryptPasswordEncoder.encode(userDTO.getPassword()));
    newUser.setUsername(userDTO.getUsername());
    newUser = userRepository.save(newUser);
    log.info("User has been added" + newUser);

    return new ResponseEntity<User>(newUser, HttpStatus.OK);
  }

  @GetMapping("/listHeaders")
  public ResponseEntity<String> listAllHeaders(
      @RequestHeader Map<String, String> headers) {
    headers.forEach((key, value) -> {
      log.info(String.format("Header '%s' = %s", key, value));
    });

    return new ResponseEntity<String>(
        String.format("Listed %d headers", headers.size()), HttpStatus.OK);
  }

  @ApiOperation(value = "List users")
  @RolesAllowed({ Constants.ADMIN })
  @GetMapping("/listuser")
  public ResponseEntity<List<User>> listuser() {
    return new ResponseEntity<List<User>>(userRepository.findAll(), HttpStatus.OK);
  }

//  @ApiOperation(value = "Register a new trip for notification")
//  @RolesAllowed({ Constants.USER, Constants.ADMIN })
//  @PostMapping("/register")
//  public ResponseEntity<String> register(@RequestHeader("authorization") String authorization, String body) {
//
//
//    log.info("Received API CALL");
//    return new ResponseEntity<String>(authorization, HttpStatus.OK);
//  }
//
//  @ApiOperation(value = "Only allowed to registered user")
//  @RolesAllowed({ Constants.USER })
//  @PostMapping("/user")
//  public ResponseEntity<String> user(String body) {
//
//    log.info("Received API CALL");
//    return new ResponseEntity<String>("Received", HttpStatus.OK);
//  }
}
