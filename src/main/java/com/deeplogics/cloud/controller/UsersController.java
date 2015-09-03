package com.deeplogics.cloud.controller;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.Principal;
import java.security.SecureRandom;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.deeplogics.cloud.EmailMessages;
import com.deeplogics.cloud.client.api.UsersSvcApi;
import com.deeplogics.cloud.client.entities.EditPass;
import com.deeplogics.cloud.client.entities.EditUser;
import com.deeplogics.cloud.model.PasswordResetToken;
import com.deeplogics.cloud.model.Role;
import com.deeplogics.cloud.model.Users;
import com.deeplogics.cloud.model.VerificationToken;
import com.deeplogics.cloud.repositories.PasswordResetTokenRepository;
import com.deeplogics.cloud.repositories.UsersRepository;
import com.deeplogics.cloud.repositories.VerificationTokenRepository;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

@RestController
@Api(value="Users", position=0)
public class UsersController {

	@Autowired
	private UsersRepository userRepository;

	@Autowired
	private VerificationTokenRepository verifyTokenRepository;
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;

	@ApiOperation(value="Returns a List of Users, limit parameter is how many users you want, per page, Default is 50")
	@RequestMapping(value=UsersSvcApi.USERS_PATH, method=RequestMethod.GET)
	public @ResponseBody Page<Users> getUsers(
			@RequestParam(value=UsersSvcApi.EMAIL_PARAMETER, required=false) String email,
			@RequestParam(value=UsersSvcApi.NAME_PARAMETER, required=false) String name,
			@RequestParam(value=UsersSvcApi.PAGE_PARAMETER, required=false) Integer page,
			@RequestParam(value=UsersSvcApi.LIMIT_PARAMETER, required=false) Integer limit) {
		if(page == null )
			page = 0;
		if(limit == null)
			limit = 50;
		
		if(email != null)
			return userRepository.findByEmail(email, new PageRequest(limit, page));
		if(name != null) 
			return userRepository.findByFullNameLike(name, new PageRequest(page, limit));
			
		return userRepository.findAll(new PageRequest(page, limit));
	}
	
	@ApiOperation(value = "Gets the Currently logged in User Info.")
	@RequestMapping(value=UsersSvcApi.USERS_GET_INFO_PATH, method=RequestMethod.GET)
	public @ResponseBody Users getUserInfo(Principal user) {
		return userRepository.findOne(user.getName());
	}
	
	@ApiOperation(value = "Gets the public Info from the specified User")
	@RequestMapping(value="/users/{id}", method=RequestMethod.GET)
	public @ResponseBody Users getUser(@PathVariable("id") String id) {
		return userRepository.findOne(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value = "Registers a new User into the Database.")
	@RequestMapping(value=UsersSvcApi.USERS_REGISTRATION_PATH, method=RequestMethod.POST)
	public @ResponseBody Users addUser(@RequestBody Users user, HttpServletResponse resp) throws IOException {
		//Verifies if valid email and password
		user.setEmail(user.getEmail());
		//Search for the User
		Users uc = userRepository.findByEmail(user.getEmail());

		if(uc == null){
			//Sets the default values for the User's account
			user.setEnabled(true);
			user.setAccountNonLocked(true);
			user.setJoinedDate(new Date());
			Collection<Role> role = new HashSet<>();
			role.add(new Role("USER"));
			user.setRoles(role);

			//Hashes the password for the user
			user.setPassword(hashPassword(user.getPassword()));

			//Save the User into the database.
			Users storedUser = userRepository.save(user);

			//Generate a verifyToken TODO: Uncomment.
			//String token = generateToken();
			//VerificationToken verifyToken = new VerificationToken(token, storedUser);
			//verifyTokenRepository.save(verifyToken);

			//Sends an Email in order to verify email address and activate account.
			//EmailMessages email = new EmailMessages(storedUser, token);

			//email.sendEmail("Activate Account", storedUser.getEmail(), "Account Activation");

			return storedUser;
		}else{
			//User already exists in the database
			resp.sendError(403, "User already exists.");
			return null;
		}
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value="Verifies their email and activates their account.(Meant to be clicked in email.)")
	@RequestMapping(value=UsersSvcApi.USERS_ACTIVATEACC_PATH, method=RequestMethod.GET)
	public @ResponseBody void activateAccount(
			@RequestParam(UsersSvcApi.EMAIL_PARAMETER) String email,
			@RequestParam(UsersSvcApi.USERID_PARAMETER) String user_id,
			@RequestParam("token") String token, HttpServletResponse resp) throws IOException {

		VerificationToken userVerify = verifyTokenRepository.findByToken(token);
		Users user = userRepository.findOne(user_id);
		if(userVerify != null && user != null){
			if(userVerify.getUser().getId().equals(user_id)){
				//Enables the account
				user.setEnabled(true);
				//Updates the User
				userRepository.save(user);
				verifyTokenRepository.delete(userVerify);
				resp.sendRedirect("https://localhost:8443");
			}else {
				resp.sendError(403, "Forbidden");
			}
		}else {
			resp.sendError(404, "User does not exist");
		}

	}

	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value="Uploads the User's Profile Pic into the Database.")
	@RequestMapping(value=UsersSvcApi.USERS_UPLOAD_PHOTO_PATH, method=RequestMethod.POST)
	public @ResponseBody void uploadProfilePicture(
			@RequestParam(UsersSvcApi.FILE_PARAMETER) MultipartFile file,
			Principal user, HttpServletResponse resp) throws IOException {
		
		Users u = userRepository.findOne(user.getName());
		if(u != null) {
			//Copies File byte array into the ByteArrayOutputStream
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			FileCopyUtils.copy(file.getInputStream(), output);
			
			String destination = "src/test/resources/TEST_PIC.jpg";
			FileUtils.copyInputStreamToFile(file.getInputStream(), new File(destination));
			//Sets it to the User
			u.setProfilePic(destination);
			userRepository.save(u);
		}
	}
	
	@ApiOperation(value="Retrieves the User's photo from the url provided. (Provide the url from profilePic field)")
	@RequestMapping(value=UsersSvcApi.USERS_GET_PHOTO_PATH, method=RequestMethod.GET)
	public @ResponseBody void getPhoto(@RequestParam("fileurl") String fileurl, HttpServletResponse resp) throws IOException {
		File file = new File(fileurl);
		if(file.exists()){
			FileInputStream fileStream = new FileInputStream(file);
			byte[] filebytes = IOUtils.toByteArray(fileStream);
			FileCopyUtils.copy(filebytes, resp.getOutputStream());
		} else
			resp.sendError(404, "Image does not exist");
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value = "Edits the User's public fields.")
	@RequestMapping(value=UsersSvcApi.USERS_EDIT_PATH, method=RequestMethod.PUT)
	public @ResponseBody Void editUser(@RequestBody EditUser edit, Principal user) {
		//Retrieves the current user from the database
		Users userDB = userRepository.findOne(user.getName());

		if(edit.getEmail() != null) 
			userDB.setEmail(edit.getEmail());
		if(edit.getFirstName() != null)
			userDB.setFirstName(edit.getFirstName());
		if(edit.getLastName() != null)
			userDB.setLastName(edit.getLastName());
		if(edit.getPhone() != null)
			userDB.setPhone(edit.getPhone());
		if(edit.getAboutMe() != null)
			userDB.setAboutMe(edit.getAboutMe());
		if(edit.getCity() != null)
			userDB.setCity(edit.getCity());
		if(edit.getCountry() != null)
			userDB.setCountry(edit.getCountry());
		if(edit.getStreet1() != null)
			userDB.setStreet1(edit.getStreet1());
		if(edit.getStreet2() != null)
			userDB.setStreet2(edit.getStreet2());
		if(edit.getZip() != null)
			userDB.setZip(edit.getZip());

		//Updates it's fullName property with the firstName and lastName
		userDB.setFullName();

		//Updates the user
		userRepository.save(userDB);


		return null;
	}
	
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@ApiOperation(value="Edits the User's Password Field.")
	@RequestMapping(value=UsersSvcApi.USERS_EDIT_PASS_PATH, method=RequestMethod.PUT)
	public @ResponseBody void editPassword(@RequestBody EditPass editPass, 
			Principal user, HttpServletResponse resp) throws IOException {
		Users userDB = userRepository.findOne(user.getName());
		String password = editPass.getPassword();
		String confirmPass = editPass.getConfirmPassword();
		
		//Verifies that password matches
		if(password != null && password.equals(confirmPass)) 
			userDB.setPassword(hashPassword(password));
		else
			resp.sendError(400, "Passwords do not match");
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@ApiOperation(value="Creates a reset password token and sends an email to the user to confirm.")
	@RequestMapping(value=UsersSvcApi.USERS_FORGOTPASS_PATH, method=RequestMethod.POST)
	public @ResponseBody HttpServletResponse forgotPassword(@RequestParam("email") String email,
			HttpServletResponse resp) throws IOException {
		//Retrieves the User from Database
		Users user = userRepository.findByEmail(email);
		PasswordResetToken resetToken = passwordResetTokenRepository.findByUser(user);
		
		//Verifies if User exists
		if(user != null) {
			//Generates the token
			String token = generateToken();
			
			//Creates or updates the User's reset pass token
			if(resetToken == null)
				resetToken = new PasswordResetToken(token, user);
			else
				resetToken.setToken(token);
			
			//Sends an email to the user requesting forget password
			EmailMessages emailMsg = new EmailMessages(user, token);
			emailMsg.sendEmail("Password Recovery Tool", email, "Forgot Password");
			
			passwordResetTokenRepository.save(resetToken);
		}
		return null;
	}
	
	@ResponseStatus(HttpStatus.OK)
	@ApiOperation(value="Changes the User's password and sends it via email. (Meant to be clicked in email)")
	@RequestMapping(value=UsersSvcApi.USERS_CHANGEPASS_PATH, method=RequestMethod.GET)
	public @ResponseBody void changePassword(@RequestParam("email") String email,
			@RequestParam("uid") String user_id, 
			@RequestParam("token") String token, HttpServletResponse resp) throws IOException{
		//Checks to see if this user requested a change password
		PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);

		if(resetToken != null && resetToken.getToken().equals(token)){
			Users userDB = userRepository.findOne(user_id);
			if(userDB != null && userDB.getEmail().equals(email) && resetToken.getUser().getId().equals(user_id)){
				//TODO: Remove generatedPassword for Production Note: Temporary
				
				String password = generatePassword();
				
				//Sends the Email
				EmailMessages emailMsg = new EmailMessages(userDB, password);
				emailMsg.sendEmail("Password for SpringHibernate", email, "Generate Password");

				//Hashes the password
				userDB.setPassword(hashPassword(password));

				userRepository.save(userDB);
				passwordResetTokenRepository.delete(resetToken);
				resp.sendRedirect("https://localhost:8443");
			}
		}
	}

	/**
	 * Hashes the password with BCrypt Algorithm
	 * @param password
	 * @return
	 */
	private String hashPassword(String password){
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		return passwordEncoder.encode(password);
	}

	/**
	 * Generates a token to be validated when using forgot password
	 * @return Token String
	 */
	private String generateToken(){
		SecureRandom random = new SecureRandom();

		return new BigInteger(130, random).toString(32);
	}
	
	/**
	 * Temporary method to be used in forgot password while website
	 * is still being built.
	 * @return a Random Password String.
	 */
	private String generatePassword(){
		Random r = new Random();
		String setOfChars = "abcdefghijklmnopqrstuvwxyz0123456789";
		
		return generateString(r, setOfChars, 6);
	}
	
	/**
	 * Temporary method for generate password
	 * @param rng
	 * @param characters
	 * @param length
	 * @return
	 */
	public static String generateString(Random rng, String characters, int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}


}
