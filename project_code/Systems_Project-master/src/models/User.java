package models;

import exceptions.NoDigitInPasswordException;
import exceptions.PasswordToLongException;
import exceptions.PasswordTooShortException;
import helpers.Encryption;

import java.security.NoSuchAlgorithmException;

public class User {
	private String email;
	private String title;
	private String forenames;
	private String surname;
	private String university;
	private String password;

	public String getEmail()
	{
		return email;
	}

	public void setEmail(String email)
	{
		this.email = email;
	}

	public String getForenames()
	{
		return forenames;
	}

	public void setForenames(String forenames)
	{
		this.forenames = forenames;
	}

	public String getSurname()
	{
		return surname;
	}

	public void setSurname(String surname)
	{
		this.surname = surname;
	}

	public String getUniversity()
	{
		return university;
	}

	public void setUniversity(String university)
	{
		this.university = university;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(char[] password) throws PasswordToLongException, PasswordTooShortException, NoSuchAlgorithmException, NoDigitInPasswordException {
		this.password = Encryption.encryptPassword(String.valueOf(password));
	}

	public void setPassword(String password) throws PasswordToLongException, PasswordTooShortException, NoSuchAlgorithmException, NoDigitInPasswordException {
		this.password = Encryption.encryptPassword(password);
	}

	public String toString()
	{
		return "User " + forenames + " " + surname + " " + email + " from " + university + " with password '"
				+ password;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setPasswordNoEncryption(String string){
		this.password = string;
	}
}
