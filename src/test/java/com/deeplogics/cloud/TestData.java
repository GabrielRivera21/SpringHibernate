package com.deeplogics.cloud;

import java.util.Random;

import com.deeplogics.cloud.model.Users;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This is a utility class to aid in the construction of
 * User objects with names, emails, phone and password.
 * The class also provides a facility to convert objects
 * into JSON using Jackson, which is the format that the
 * UserSvc controller is going to expect data in for
 * integration testing.
 * 
 * @author Gabriel
 *
 */
public class TestData {

	private static final ObjectMapper objectMapper = new ObjectMapper();
	private static Random r = new Random();
	private static String setOfChars = "abcdefghijklmnopqrstuvwxyz";
	//private static String setOfNums = "0123456789";
	
	public static String[] category = new String[] {"electronics", "home improvements",
		"vehicles", "professional services", "pets", "sports", "music", "other" };
	
	
	public static String[] emailAddress = new String[] {
		"gaby211992@gmail.com"
	};
	/**
	 * Construct and return a User object with a
	 * random email, name, phone and password.
	 * 
	 * @return
	 */
	public static Users randomUser() {
		// Information about the User
		// Construct a random identifier using Java's UUID class
		
		String email = generateString(r ,setOfChars, 8) + "@example.com"; //emailAddress[r.nextInt(1)];
		String firstName = generateString(r, setOfChars, 5);
//		String phone = generateString(r, setOfNums, 10);
		String password = "gabriel";
		
		return new Users(email, password, firstName, "Rivera");
	}
	
	public static String generateString(Random rng, String characters, int length)
	{
	    char[] text = new char[length];
	    for (int i = 0; i < length; i++)
	    {
	        text[i] = characters.charAt(rng.nextInt(characters.length()));
	    }
	    return new String(text);
	}
	
	/**
	 *  Convert an object to JSON using Jackson's ObjectMapper
	 *  
	 * @param o
	 * @return
	 * @throws Exception
	 */
	public static String toJson(Object o) throws Exception{
		return objectMapper.writeValueAsString(o);
	}
}
