package com.deeplogics.cloud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.deeplogics.cloud.model.Role;


public interface RoleRepository extends CrudRepository<Role, Long>{
	
	public Role findByRoleName(String name);
	
    public void delete(Role role);
}
