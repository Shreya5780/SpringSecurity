package com.example.security.springsecurity.repo;

import com.example.security.springsecurity.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface USerRepo extends MongoRepository<UserModel, String> {
    UserModel findByUsername(String username);
}
