package models;

import exceptions.NoDigitInPasswordException;
import exceptions.PasswordToLongException;
import exceptions.PasswordTooShortException;

import javax.swing.*;
import java.security.NoSuchAlgorithmException;

public class JournalEditor extends User{
	private boolean isChief;
	private String issn;
	
	public JournalEditor() {
		super();
	}
	
	public JournalEditor(User user) {
		super();
		super.setEmail(user.getEmail());
		super.setForenames(user.getForenames());
		super.setSurname(user.getSurname());
		super.setUniversity(user.getUniversity());



		super.setPasswordNoEncryption(user.getPassword());
	}
	
	public boolean isChief()
	{
		return isChief;
	}

	public void setChief(boolean isChief)
	{
		this.isChief = isChief;
	}

	public String getIssn() {
		return issn;
	}

	public void setIssn(String issn) {
		this.issn = issn;
	}

	public String toString() {
		return "Editor for journal '" +issn+"' with chief="+isChief+" and user info: " + super.toString();
	}
}
