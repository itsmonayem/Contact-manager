package com.spring.contactmanager.dao;

import com.spring.contactmanager.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Integer> {
}
