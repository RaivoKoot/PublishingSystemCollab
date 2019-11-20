package models;

import java.util.ArrayList;

public class Journal {
	private String ISSN;
	private String name;
	
	public String getISSN()
	{
		return ISSN;
	}
	public void setISSN(String iSSN)
	{
		ISSN = iSSN;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	
	public String toString() {
		return "Journal '" + name + "' with ISSN '" + ISSN + "'";
	}
}
