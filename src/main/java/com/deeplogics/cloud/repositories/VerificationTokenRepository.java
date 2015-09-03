package com.deeplogics.cloud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.deeplogics.cloud.model.Users;
import com.deeplogics.cloud.model.VerificationToken;


public interface VerificationTokenRepository extends CrudRepository<VerificationToken, Long>{
	public VerificationToken findByToken(String token);

    public VerificationToken findByUser(Users user);
}
