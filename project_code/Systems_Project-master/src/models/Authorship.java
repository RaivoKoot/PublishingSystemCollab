package models;

public class Authorship extends User {

	private boolean isMain;
	private Article article;

	public Authorship()
	{
		super();
	}

	public Authorship(User user)
	{
		super();
		super.setEmail(user.getEmail());
		super.setForenames(user.getForenames());
		super.setSurname(user.getSurname());
		super.setUniversity(user.getUniversity());
	}

	public boolean isMain()
	{
		return isMain;
	}

	public void setMain(boolean isMain)
	{
		this.isMain = isMain;
	}

	public Article getArticle()
	{
		return article;
	}

	public void setArticle(Article article)
	{
		this.article = article;
	}

	public String toString()
	{
		return "Author of article or submission with title '" + article.getTitle() + "' and isMainAuthor=" + isMain
				+ " and user info: " + super.toString();
	}

}
