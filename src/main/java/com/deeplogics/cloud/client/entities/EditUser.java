package com.deeplogics.cloud.client.entities;


/**
 * This class is instantiated with everything in null, and is only meant
 * to be used with the method EditUser from the UsersSvcApi.editUser() for
 * UsersController.editUser(), it will only update the fields that are not
 * in null.
 * 
 * Note: This is for the Retrofit Client Side Application.
 * 
 * @author Gabriel
 *
 */
public class EditUser {
	
	private String email = null;
	
	private String firstName = null;
	
	private String lastName = null;
	
	private String phone = null;
	
	private String aboutMe = null;
	
	private String city = null;
	
	private String country = null;
	
	private String street1 = null;
	
	private String street2 = null;
	
	private String zip = null;
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getStreet1() {
		return street1;
	}

	public void setStreet1(String street1) {
		this.street1 = street1;
	}

	public String getStreet2() {
		return street2;
	}

	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

}
