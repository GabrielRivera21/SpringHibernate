package com.deeplogics.cloud.model;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.GenericGenerator;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.google.common.base.Objects;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * See: 
 * http://www.mkyong.com/hibernate/hibernate-many-to-many-relationship-example-annotation/
 * for ManyToMany Tables.
 */
@Entity
@Table(name="Users", 
	uniqueConstraints ={@UniqueConstraint(columnNames = "email")})
public class Users {
	
	@Id
	@GeneratedValue(generator = "uuid")
	@GenericGenerator(name = "uuid", strategy = "uuid2")
    public String id;

    private String firstName;

    private String lastName;
    
    private String fullName;
    
    @Column(nullable=false)
    @ApiModelProperty(required=true)
    private String email;
    
    @Column(nullable=false)
    @ApiModelProperty(required=true)
    private String password;
    
    private String phone;
    
    private String aboutMe;
    
    private String country;
    
    private String city;
    
    private String zip;
    
    private String street1;
    
    private String street2;
    
    private double overAllRating;
    
    private String profilePic;
    
    @JsonIgnore 
    private boolean enabled;
    
    @JsonIgnore
    private boolean accountNonLocked;
    
    @JsonIgnore
    private boolean verifiedCredit;
    
    @DateTimeFormat(pattern = "yyyy/MM/dd hh:mm:ss")
    @JsonFormat(pattern="yyyy/MM/dd hh:mm:ss")
    private Date joinedDate;
    
    @JsonIgnore
    @ManyToMany(fetch=FetchType.EAGER, cascade=CascadeType.ALL)
    @JoinTable(name = "users_roles", 
    	joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"), 
    	inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    private Collection<Role> roles = new HashSet<Role>();
    
    protected Users() {}
    
	public Users(String email, String password, String firstName, String lastName) {
		super();
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName() {
		fullName = firstName + " " + lastName;
	}

	public String getEmail() {
		return email;
	}
	
	@ApiModelProperty(value="email", required=true)
	public void setEmail(String email) {
		this.email = email;	
	}
	
	@JsonIgnore
	public String getPassword() {
		return password;
	}
	
	@JsonProperty(value = "password")
	public void setPassword(String password) {
		this.password = password;
	}

	@JsonIgnore
	public String getPhone() {
		return phone;
	}
	
	@JsonProperty(value="phone")
	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getAboutMe() {
		return aboutMe;
	}

	public void setAboutMe(String aboutMe) {
		this.aboutMe = aboutMe;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}
	
	@JsonIgnore
	public String getZip() {
		return zip;
	}
	
	@JsonProperty("zip")
	public void setZip(String zip) {
		this.zip = zip;
	}
	
	@JsonIgnore
	public String getStreet1() {
		return street1;
	}
	
	@JsonProperty("street1")
	public void setStreet1(String street1) {
		this.street1 = street1;
	}
	
	@JsonIgnore
	public String getStreet2() {
		return street2;
	}
	
	@JsonProperty("street2")
	public void setStreet2(String street2) {
		this.street2 = street2;
	}

	public double getOverAllRating() {
		return overAllRating;
	}

	public void setOverAllRating(double overAllRating) {
		this.overAllRating = overAllRating;
	}
	
	@JsonProperty("profilePic")
	public String getProfilePic() {
		return profilePic;
	}
	
	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isAccountNonLocked() {
		return accountNonLocked;
	}

	public void setAccountNonLocked(boolean accountNonLocked) {
		this.accountNonLocked = accountNonLocked;
	}

	public boolean isVerifiedCredit() {
		return verifiedCredit;
	}

	public void setVerifiedCredit(boolean verifiedCredit) {
		this.verifiedCredit = verifiedCredit;
	}
	
	@JsonSerialize(using = DateSerializer.class)
	public Date getJoinedDate() {
		return joinedDate;
	}
	
	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}

	public Collection<Role> getRoles() {
		return roles;
	}

	public void setRoles(Collection<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Two Users will generate the same hashcode if they have exactly the same
	 * values for their email.
	 * 
	 */
	@Override
	public int hashCode() {
		// Google Guava provides great utilities for hashing
		return Objects.hashCode(email);
	}
	
	/**
	 * If both Users have the same email
	 * then they are the same user.
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Users) {
			Users other = (Users) obj;
			// Google Guava provides great utilities for equals too!
			return Objects.equal(email, other.email);
		} else {
			return false;
		}
	}
}
