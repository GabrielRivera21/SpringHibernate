package com.deeplogics.cloud.repositories;

import org.springframework.data.repository.CrudRepository;

import com.deeplogics.cloud.model.PasswordResetToken;
import com.deeplogics.cloud.model.Users;


public interface PasswordResetTokenRepository extends CrudRepository<PasswordResetToken, Long>{
	
	public PasswordResetToken findByToken(String token);

    public PasswordResetToken findByUser(Users user);
}
