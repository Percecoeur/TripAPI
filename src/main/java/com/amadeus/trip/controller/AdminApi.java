package com.amadeus.trip.controller;

import com.amadeus.trip.config.JwtConfig;
import com.amadeus.trip.model.Role;
import com.amadeus.trip.model.User;
import com.amadeus.trip.model.dto.ConnectDTO;
import com.amadeus.trip.model.dto.UserDTO;
import com.amadeus.trip.model.exception.RoleException;
import com.amadeus.trip.model.repository.RoleRepository;
import com.amadeus.trip.model.repository.UserRepository;
import com.amadeus.trip.service.jwt.JwtWrapper;
import com.amadeus.trip.utils.Constants;
import com.amadeus.trip.utils.Utils;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/admin")
@Log4j2
public class AdminApi {

  @Autowired
  JwtConfig jwtConfig;
  @Autowired
  PasswordEncoder bCryptPasswordEncoder;
  @Autowired
  AuthenticationManager authenticationManager;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private RoleRepository roleRepository;

  @PostMapping("/token")
  public ResponseEntity<?> token(@Valid @RequestBody ConnectDTO connectDTO) {

    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(connectDTO.getUsername(), connectDTO.getPassword()));

    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = Utils.createToken(authentication, jwtConfig.getExpiration(), jwtConfig.getSecret());

    return ResponseEntity.ok(JwtWrapper.builder().token(jwt).build());
  }

  @ApiOperation(value = "Will create a new user with role",
      notes = "You do not need to be logged.")
  @PostMapping("/register")
  public ResponseEntity<?> register(@Valid @RequestBody UserDTO userDTO) {

    // Check first that this user does not exist
    if (userRepository.existsByUsername(userDTO.getUsername())) {
      return new ResponseEntity<>("User already exists : " + userDTO.getUsername(), HttpStatus.BAD_REQUEST);
    }

    Set<Role> userRoles = userDTO.getRoles();
    if (userRoles.isEmpty()) {
      // Default value to USER role
      userRoles.add(Role.builder().name(Constants.USER).build());
    }
    Set<Role> dbRoles = new HashSet<>();
    //  we need to reference the DB objects so we have to find them first (can we avoid this?)
    try {
      userRoles.forEach(r -> {
        Role role = roleRepository.findByName(r.getName())
            .orElseThrow(() -> new RoleException("Following role is not found " + r.getName()));
        dbRoles.add(role);
      });

    } catch (RoleException re) {
      return new ResponseEntity<>(re.getMessage(), HttpStatus.BAD_REQUEST);
    }

    User newUser = User.builder()
        .password(bCryptPasswordEncoder.encode(userDTO.getPassword()))
        .email(userDTO.getEmail())
        .username(userDTO.getUsername())
        .roles(dbRoles)
        .build();

    newUser = userRepository.save(newUser);
    log.debug("User has been added" + newUser);

    return new ResponseEntity<>("User has been created successfully.", HttpStatus.OK);
  }

  @ApiOperation(value = "List users")
  @RolesAllowed({ Constants.ADMIN })
  @GetMapping("/list/users")
  public ResponseEntity<?> listUsers() {

    return new ResponseEntity<>(userRepository.findAll(), HttpStatus.OK);
  }

  @ApiOperation(value = "List roles")
  @RolesAllowed({ Constants.ADMIN })
  @GetMapping("/list/roles")
  public ResponseEntity<?> listRoles() {

    return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
  }

}
