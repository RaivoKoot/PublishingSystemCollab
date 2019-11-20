package models;

public class User {
	private String email;
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

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String toString()
	{
		return "User " + forenames + " " + surname + " " + email + " from " + university + " with password '"
				+ password;
	}
}
