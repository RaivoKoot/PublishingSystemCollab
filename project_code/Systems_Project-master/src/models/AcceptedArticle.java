package models;

public class AcceptedArticle extends Submission {

	private int articleID;
	private int startingPage;
	private int endingPage;

	public AcceptedArticle()
	{
		super();
	}

	public AcceptedArticle(Submission submission)
	{
		super();
		super.setTitle(submission.getTitle());
		super.setSummary(submission.getSummary());
		super.setArticleContent(submission.getArticleContent());
		super.setSubmissionID(submission.getSubmissionID());
	}

	public int getArticleID()
	{
		return articleID;
	}

	public void setArticleID(int articleID)
	{
		this.articleID = articleID;
	}

	public int getStartingPage()
	{
		return startingPage;
	}

	public void setStartingPage(int startingPage)
	{
		this.startingPage = startingPage;
	}

	public int getEndingPage()
	{
		return endingPage;
	}

	public void setEndingPage(int endingPage)
	{
		this.endingPage = endingPage;
	}

	public String toString()
	{
		return "Article starting at page " + startingPage + " and ending at page " + endingPage
				+ " with following information: " + super.toString();
	}

}
