package models;

import java.time.Month;

public class Edition {
	private static final String NAME_TEMPLATE = "Vol %d, No %d";
	
	private int volumeNum;
	private int editionNum;
	private Month publicationMonth;
	
	public String getName()
	{
		return String.format(NAME_TEMPLATE, volumeNum, editionNum);
	}
	public Month getPublicationMonth()
	{
		return publicationMonth;
	}
	public void setPublicationMonth(int publicationMonth)
	{
		this.publicationMonth = Month.of(publicationMonth);
	}
	public int getVolumeNum()
	{
		return volumeNum;
	}
	public void setVolumeNum(int volumeNum)
	{
		this.volumeNum = volumeNum;
	}
	public int getEditionNum()
	{
		return editionNum;
	}
	public void setEditionNum(int editionNum)
	{
		this.editionNum = editionNum;
	}
	
	public String toString() {
		return getName() +" published in " + publicationMonth;
	}
	
}
