package org.example.demo1001.factory;

import org.example.demo1001.model.*;

public class UserFactory {
    private static UserFactory instance;


    private UserFactory() {}


    public static UserFactory getInstance() {
        if (instance == null) {
            instance = new UserFactory();
        }
        return instance;
    }

        public User createUser(String userName, String password, String role, String status) {
            return new UserBuilder(userName,password,role).status(status).build();
        }

    public User createUser(int id, String userName, String password, String role, String status) {
        return new UserBuilder(userName,password,role).status(status).id(id).build();
    }


}
