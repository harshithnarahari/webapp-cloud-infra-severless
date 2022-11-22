package com.webapp.webapp.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.webapp.webapp.Model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findById(long id);

}
