package com.deeplogics.cloud.client.api;


import com.deeplogics.cloud.client.entities.EditPass;
import com.deeplogics.cloud.client.entities.EditUser;
import com.deeplogics.cloud.client.entities.PageClient;
import com.deeplogics.cloud.model.Users;

import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Query;
import retrofit.http.Streaming;
import retrofit.mime.TypedFile;

/**
 * This interface defines an API for a UserSvc. The
 * interface is used to provide a contract for client/server
 * interactions. The interface is annotated with Retrofit
 * annotations so that clients can automatically convert the
 * 
 * 
 * @author Gabriel
 *
 */
public interface UsersSvcApi {

	// The path where we expect the Users to live
	public static final String USERS_PATH = BaseUrl.API_PATH + "/users";

	/** Path to add Users to the database **/
	public static final String USERS_REGISTRATION_PATH = USERS_PATH + "/register";
	
	public static final String USERS_GET_INFO_PATH = USERS_PATH + "/me";
	
	/** The path to edit a user's public field **/
	public static final String USERS_EDIT_PATH = USERS_PATH + "/edit/me";
	
	/** The path to edit a user's password **/
	public static final String USERS_EDIT_PASS_PATH = USERS_EDIT_PATH + "/pass";

	/** The path to upload User's profile photo **/
	public static final String USERS_UPLOAD_PHOTO_PATH = USERS_PATH + "/upload/photo";
	
	public static final String USERS_GET_PHOTO_PATH = USERS_PATH + "/photo";

	/** The path to request a new password **/
	public static final String USERS_FORGOTPASS_PATH = USERS_PATH + "/forgotpass";
	
	/** The path to reset your password **/
	public static final String USERS_CHANGEPASS_PATH = USERS_PATH + "/resetpass";

	public static final String USERS_ACTIVATEACC_PATH = USERS_PATH + "/activate";

	public static final String NAME_PARAMETER = "name";

	public static final String EMAIL_PARAMETER = "email";

	public static final String FILE_PARAMETER = "fileContent";

	public static final String USERID_PARAMETER = "uid";

	public static final String PAGE_PARAMETER = "page";

	public static final String LIMIT_PARAMETER = "limit";

	@GET(USERS_PATH)
	public PageClient<Users> getUserByEmail(@Query(EMAIL_PARAMETER) String email);
	
	@GET(USERS_PATH)
	public PageClient<Users> getUserByName(@Query(NAME_PARAMETER) String name);
	
	@GET(USERS_GET_INFO_PATH)
	public Users getUserInfo();

	@POST(USERS_REGISTRATION_PATH)
	public Users addUser(@Body Users u);

	@Multipart
	@POST(USERS_UPLOAD_PHOTO_PATH)
	public Response uploadPhoto(@Part(FILE_PARAMETER) TypedFile photo);

	@PUT(USERS_EDIT_PATH)
	public Void editUser(@Body EditUser u);
	
	public Void editPass(@Body EditPass passBody);

	@POST(USERS_FORGOTPASS_PATH)
	public Response forgotPass(@Query(EMAIL_PARAMETER) String email);
	
	@Streaming
	@GET(USERS_GET_PHOTO_PATH)
	public Response getPhoto(@Query("fileurl") String profilePic);

}
