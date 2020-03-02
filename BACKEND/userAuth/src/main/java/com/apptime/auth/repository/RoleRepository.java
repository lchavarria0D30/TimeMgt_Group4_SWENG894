package com.apptime.auth.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.apptime.auth.model.Roles;

/**
 * @author Bashiir Mohamed
 * this class represent jpa Role repository
 */
public interface RoleRepository extends JpaRepository<Roles, Integer>{

}