package com.example.Cooperwrite.models.data;


import com.example.Cooperwrite.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

/**
 * Created by LaunchCode
 */
@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Integer> {
}
