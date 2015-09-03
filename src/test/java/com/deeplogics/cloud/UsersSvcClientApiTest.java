package com.deeplogics.cloud;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import com.deeplogics.cloud.client.SecuredRestBuilder;
import com.deeplogics.cloud.client.api.BaseUrl;
import com.deeplogics.cloud.client.api.UsersSvcApi;
import com.deeplogics.cloud.client.entities.EditUser;
import com.deeplogics.cloud.client.entities.PageClient;
import com.deeplogics.cloud.model.Users;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import retrofit.client.Response;
import retrofit.converter.GsonConverter;
import retrofit.mime.TypedFile;

/**
 * 
 * This integration test sends a POST request to the UserServlet to add a new user 
 * and then sends a second GET request to check that the User showed up in the list
 * of Users. Actual network communication using HTTP is performed with this test.
 * 
 * The test requires that the UserSvc be running first (see the directions in
 * the README.md file for how to launch the Application).
 * 
 * To run this test, right-click on it in Eclipse and select
 * "Run As"->"JUnit Test"
 * 
 * Pay attention to how this test that actually uses HTTP and the test that just
 * directly makes method calls on a UserSvc object are essentially identical.
 * All that changes is the setup of the UserService variable. Yes, this could
 * be refactored to eliminate code duplication...but the goal was to show how
 * much Retrofit simplifies interaction with our service!
 * 
 * @author Gabriel
 *
 */
public class UsersSvcClientApiTest {
	
	private final static String USERNAME = "admingabriel@springhibernate.com"; //"admin";
	private String PASSWORD = "gabriel";
	private final String CLIENT_ID = "mobile-SpringHibernate";
	
	private static Gson gson = new GsonBuilder()
		.setDateFormat("yyyy/MM/dd hh:mm:ss")
		.create();
	

	private UsersSvcApi usersService = new SecuredRestBuilder()
			.setLoginEndpoint(BaseUrl.TEST_URL + BaseUrl.TOKEN_PATH)
			.setUsername(USERNAME)
			.setPassword(PASSWORD)
			.setClientId(CLIENT_ID)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setConverter(new GsonConverter(gson))
			.setEndpoint(BaseUrl.TEST_URL).setLogLevel(LogLevel.FULL).build()
			.create(UsersSvcApi.class);

	private static UsersSvcApi usersNoTokenService = new RestAdapter.Builder()
			.setEndpoint(BaseUrl.TEST_URL)
			.setClient(new ApacheClient(UnsafeHttpsClient.createUnsafeClient()))
			.setConverter(new GsonConverter(gson))
			.setLogLevel(LogLevel.FULL).build()
			.create(UsersSvcApi.class);
	

	private Users user = TestData.randomUser();
	
	/**
	 * EXECUTE THIS METHOD FIRST! if you just started up the server
	 * so you have at least the main user.
	 */
	public static void main(String[] args) {
		Users mainUser = TestData.randomUser();
		mainUser.setEmail(USERNAME);
		Users u = usersNoTokenService.addUser(mainUser);
		
		assertTrue(u.getEmail().equals(mainUser.getEmail()));
	}
	
	@Test
	public void testGetUser(){
		Users u = usersService.getUserInfo();
		
		assertTrue(u != null);
		assertTrue(u.getPassword() == null);
	}
	
	@Test
	public void testGetUserList() {
		PageClient<Users> pageList = usersService.getUserByName(user.getFullName());
		Collection<Users> userList = pageList.getContent();
		assertTrue(userList != null);
		assertTrue(userList.size() > 0);
	}
	
	@Test
	public void testAddUserAndList() throws Exception {
		
		// Add the Users
		Users u = usersNoTokenService.addUser(user);
		
		assertTrue(u.getEmail().equals(user.getEmail()));
	}
	
	
	@Test
	public void testUploadPhotoAndRetrieve() throws IOException {
		File file = new File("src/test/resources/profile_pic.jpg");
		
		String mimeType = "image/jpeg";
		TypedFile photo = new TypedFile(mimeType, file);
        
		usersService.uploadPhoto(photo);
		
		Users u = usersService.getUserInfo();
		
		assertTrue(u != null);
		
		Response resp = usersService.getPhoto(u.getProfilePic());
		assertEquals(200, resp.getStatus());
		
		InputStream photoData = resp.getBody().in();
		byte[] originalFile = IOUtils.toByteArray(new FileInputStream(file));
		byte[] retrievedFile = IOUtils.toByteArray(photoData);
		
		assertTrue(Arrays.equals(originalFile, retrievedFile));
	}
	
	/**
	 * This test creates a User, adds the User to the UserSvc, and then
	 * checks that the User is retrieved from the database.
	 * 
	 * @throws Exception
	 */

	
	@Test
	public void testEditUserAndList() {
		EditUser edit = new EditUser();
		String firstName = "Gabriel";
		edit.setFirstName(firstName);
		String lastName = "Rivera Per-ossenkopp";
		edit.setLastName(lastName);
		
		String fullName = firstName + " " + lastName;
		usersService.editUser(edit);
		
		Users uc = usersService.getUserInfo();
		String retrievedName = uc.getFullName();
		assertTrue(retrievedName.equals(fullName));
	}
	
	@Test
	public void forgotAndChangePass(){
		Response resp = usersNoTokenService.forgotPass("admingabriel@springhibernate.com");
		assertEquals(201, resp.getStatus());
	}
	
}
