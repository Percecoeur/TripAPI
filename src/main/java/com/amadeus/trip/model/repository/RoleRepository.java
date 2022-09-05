package com.amadeus.trip.model.repository;

import com.amadeus.trip.model.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role, String> {

  Role findByName(String name);
}