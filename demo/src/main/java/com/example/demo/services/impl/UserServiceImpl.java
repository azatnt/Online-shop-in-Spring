package com.example.demo.services.impl;

import com.example.demo.entities.Roles;
import com.example.demo.entities.Users;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.management.relation.Role;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;


    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Users myUser = userRepository.findByEmail(s);


        if(myUser!=null){
            User secUser = new User(myUser.getEmail(), myUser.getPassword(), myUser.getRoles());
            return secUser;
        }

        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public Roles getRolesByName(String name) {
        return roleRepository.getRolesByNameIsLike(name);
    }

    @Override
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Users getUser(Long id) {
        return userRepository.getOne(id);
    }

    @Override
    public Users saveUser(Users users) {
        return userRepository.save(users);
    }

    @Override
    public Users addUser(Users users) {
        return userRepository.save(users);
    }

    @Override
    public void deleteUser(Users users) {
        userRepository.delete(users);
    }

    @Override
    public List<Roles> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Roles getRole(Long id) {
        return roleRepository.getOne(id);
    }

    @Override
    public Roles saveRole(Roles roles) {
        return roleRepository.save(roles);
    }

    @Override
    public Roles addRole(Roles roles) {
        return roleRepository.save(roles);
    }

    @Override
    public void deleteRole(Roles roles) {
        roleRepository.delete(roles);
    }


}
