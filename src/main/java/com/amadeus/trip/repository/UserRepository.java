package com.amadeus.trip.repository;

import com.amadeus.trip.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

  @Query("{name:'?0'}")
  User findItemByName(String name);

  long count();

}