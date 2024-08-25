package com.example.addressbook.securityconfig.repository;

import com.example.addressbook.securityconfig.entity.Privilege;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrivilegeRepository extends CrudRepository<Privilege,Long> {

    Privilege findByName(String name);
}
