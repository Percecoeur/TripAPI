package com.amadeus.trip.model.repository;

import com.amadeus.trip.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {

  Optional<Role> findByName(String name);

}