package com.deeplogics.cloud.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.deeplogics.cloud.model.Users;
import com.deeplogics.cloud.client.api.*;

@RepositoryRestResource(path=UsersSvcApi.USERS_PATH)
public interface UsersRepository extends CrudRepository<Users, String>{
	
	//Finds the user who's email matches
	public Users findByEmail(@Param(UsersSvcApi.EMAIL_PARAMETER) String email);
	
	public Page<Users> findByEmail(@Param(UsersSvcApi.EMAIL_PARAMETER) String email, Pageable page);
	
	public Page<Users> findByFullNameLike(String fullName, Pageable page);
	
	public Page<Users> findAll(Pageable page);
}
