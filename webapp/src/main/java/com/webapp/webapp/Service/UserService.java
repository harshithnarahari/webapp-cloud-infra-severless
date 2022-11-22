package com.webapp.webapp.Service;

import com.webapp.webapp.UserD;
import com.webapp.webapp.Model.Input.UserI;

public interface UserService {

    UserI createUser(UserD userdto);

    UserI findUser(long id);

    UserI findUserByMailId(UserD userdto);

    UserI updateUser(UserD userdto);

    UserI authenticateUser(UserD userdto);
}
