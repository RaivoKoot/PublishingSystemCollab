package models;

public class Critique {
	private String description;
	private String reply;

	public String getDescription()
	{
		return description;
	}

	public void setDescription(String description)
	{
		this.description = description;
	}

	public String getReply()
	{
		return reply;
	}

	public void setReply(String reply)
	{
		this.reply = reply;
	}

	public String toString()
	{
		String string = "Critique with description '" + description + "' and ";

		if (null == reply)
			string += " no reply yet";
		else
			string += " reply '" + reply + "'";

		return string;
	}
}
