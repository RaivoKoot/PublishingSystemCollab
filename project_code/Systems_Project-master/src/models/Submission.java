package models;

public class Submission {
	
	private int submissionID;
	private String title;
	private String summary;
	private String articleContent;
	
	
	public int getSubmissionID()
	{
		return submissionID;
	}
	public void setSubmissionID(int submissionID)
	{
		this.submissionID = submissionID;
	}
	public String getTitle()
	{
		return title;
	}
	public void setTitle(String title)
	{
		this.title = title;
	}
	public String getSummary()
	{
		return summary;
	}
	public void setSummary(String summary)
	{
		this.summary = summary;
	}
	public String getArticleContent()
	{
		return articleContent;
	}
	public void setArticleContent(String articleContent)
	{
		this.articleContent = articleContent;
	}
	
	public String toString() {
		return "Submission with title '"+title+"' and summary '" +summary+"' and text '" + articleContent+"'";
	}
	
	
}
