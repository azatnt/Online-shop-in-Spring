package com.example.demo.services;

import com.example.demo.entities.Categories;
import com.example.demo.entities.Roles;
import com.example.demo.entities.Users;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    Users getUserByEmail(String email);

    Roles getRolesByName(String name);

    List<Users> getAllUsers();
    Users getUser(Long id);
    Users saveUser(Users users);
    Users addUser(Users users);
    void deleteUser(Users users);


    List<Roles> getAllRoles();
    Roles getRole(Long id);
    Roles saveRole(Roles roles);
    Roles addRole(Roles roles);
    void deleteRole(Roles roles);
}
