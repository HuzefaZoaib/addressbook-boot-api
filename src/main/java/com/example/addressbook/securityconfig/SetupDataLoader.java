package com.example.addressbook.securityconfig;

import com.example.addressbook.securityconfig.entity.Privilege;
import com.example.addressbook.securityconfig.entity.Role;
import com.example.addressbook.securityconfig.entity.User;
import com.example.addressbook.securityconfig.repository.PrivilegeRepository;
import com.example.addressbook.securityconfig.repository.RoleRepository;
import com.example.addressbook.securityconfig.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class SetupDataLoader implements
        ApplicationListener<ContextRefreshedEvent> {

    boolean alreadySetup = false;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PrivilegeRepository privilegeRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {

        if (alreadySetup)
            return;

        Privilege readPrivilege
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilege writePrivilege
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilege> adminPrivileges = Arrays.asList(
                readPrivilege, writePrivilege);
        createRoleIfNotFound("ROLE_ADMIN", adminPrivileges);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(readPrivilege));

        Optional<Role> adminRole = roleRepository.findByName("ROLE_ADMIN");
        User user = new User();
        user.setFirstName("I am ");
        user.setLastName("Someone");
        user.setPassword(passwordEncoder.encode("someone"));
        user.setEmail("iamsomeone@test.com");
        user.setRoles(adminRole.isPresent() ? Arrays.asList(adminRole.get()) : Collections.emptyList() );
        user.setEnabled(true);
        userRepository.save(user);

        alreadySetup = true;
    }

    @Transactional
    Privilege createPrivilegeIfNotFound(String name) {

        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }
        return privilege;
    }

    @Transactional
    Role createRoleIfNotFound(
            String name, Collection<Privilege> privileges) {

        Optional<Role> role = roleRepository.findByName(name);
        Role _role = null;    // default value
        if (role.isEmpty()) {
            _role = new Role(name);
            _role.setPrivileges(privileges);
            roleRepository.save(_role);
        }

        return _role;
    }
}
