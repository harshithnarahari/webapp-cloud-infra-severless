package com.webapp.webapp.Service;

import java.util.Date;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.webapp.webapp.Model.User;
import com.webapp.webapp.Exception.MyExceptions;
import com.webapp.webapp.UserD;
import com.webapp.webapp.Repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.webapp.webapp.Model.Input.UserI;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    BCryptPasswordEncoder bcryptPasswordEncoder;

    public UserServiceImpl(UserRepository userRepository, BCryptPasswordEncoder bcryptPasswordEncoder) {
        super();
        this.userRepository = userRepository;
        this.bcryptPasswordEncoder = bcryptPasswordEncoder;
    }

    @Override
    public UserI createUser(UserD userdto) {
        // try {

        //     User userEmail = userRepository.findByEmail(userdto.getEmail());
        //     if (userEmail != null) {
        //         throw new MyExceptions("601", "Email Address already in use.");
        //     }
        // } catch (Exception e) {
        //     throw new MyExceptions("400", "Email Address already in use.");
        // }

        User user = new User();
        String encodedpass = bcryptPasswordEncoder.encode(userdto.getPassword());
        BeanUtils.copyProperties(userdto, user);
        user.setEncryptedPassword(encodedpass);
        User storedUser = userRepository.save(user);

        UserI returnUser = new UserI();
        BeanUtils.copyProperties(storedUser, returnUser);
        return returnUser;
    }

    @Override
    public UserI findUser(long id) {
        try {
            User user = new User();
            UserI returnUser = new UserI();
            user = userRepository.findById(id);
            BeanUtils.copyProperties(user, returnUser);
            return returnUser;

        } catch (Exception e) {
            throw new MyExceptions("403", "User ID not present.");
        }

    }

    @Override
    public UserI findUserByMailId(UserD userDTO) {
        try {
            User user = userRepository.findByEmail(userDTO.getEmail());
            if (user == null) {
                throw new MyExceptions("404", "User not found.");
            }
            UserI returnUser = new UserI();
            BeanUtils.copyProperties(user, returnUser);
            return returnUser;
        } catch (Exception e) {
            throw new MyExceptions("404", "User not found.");
        }
    }

    @Override
    public UserI updateUser(UserD userdto) {
        User user = userRepository.findByEmail(userdto.getEmail());
        String encodedpass = bcryptPasswordEncoder.encode(userdto.getPassword());
        user.setEncryptedPassword(encodedpass);
        user.setFirstName(userdto.getFirstName());
        user.setLastName(userdto.getLastName());
        user.setAccount_updated(new Date(System.currentTimeMillis()));
        User storedUser = userRepository.save(user);
        UserI returnUser = new UserI();
        BeanUtils.copyProperties(storedUser, returnUser);
        return returnUser;
    }

    @Override
    public UserI authenticateUser(UserD userdto) {
        User user = userRepository.findByEmail(userdto.getEmail());
        String encodedpass = bcryptPasswordEncoder.encode(userdto.getPassword());
        if (user != null && encodedpass == user.getEncryptedPassword()) {
            UserI returnUser = new UserI();
            BeanUtils.copyProperties(user, returnUser);
            return returnUser;
        }
        return null;
    }
}
