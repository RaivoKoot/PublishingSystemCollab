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


		try {
			super.setPassword(user.getPassword());
		} catch (PasswordToLongException e1) {
			JOptionPane.showMessageDialog(null,"The password you entered is too long!");
			e1.printStackTrace();
			return;
		} catch (PasswordTooShortException e1) {
			JOptionPane.showMessageDialog(null,"The password you entered is too short!");
			e1.printStackTrace();
			return;
		} catch (NoSuchAlgorithmException e1) {
			JOptionPane.showMessageDialog(null,"Something went wrong. Please contact an administrator.");
			e1.printStackTrace();
			return;
		} catch (NoDigitInPasswordException e1) {
			JOptionPane.showMessageDialog(null,"The password you entered needs to contain at least one digit!");
			e1.printStackTrace();
			return;
		}
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
