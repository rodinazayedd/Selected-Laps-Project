package org.example.demo1001.repository;

import org.example.demo1001.model.Admin;
import org.example.demo1001.model.User;
import org.example.demo1001.model.UserBuilder;

public abstract class SessionRepo {
    //= new Admin("","","admin","")
    private static User currentUser;
    public static void login(User user){
        currentUser=user;
    }
    public static void  logout()
    {
        currentUser=null;
        System.out.println("logout called!");
    }
    public static User getCurrentUser(){
        return currentUser;
    }
}
