package com.example.Cooperwrite.models.data;


import com.example.Cooperwrite.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public interface UserDao extends CrudRepository<User, Integer> {
    User findTopByOrderByIdDesc();
    User findByName(String name);
    User findByEmail(String email);
    User findByPassword(String password);
    List<User> findByActive(boolean active);
}
