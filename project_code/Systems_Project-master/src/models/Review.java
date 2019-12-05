package models;

import java.util.ArrayList;

public class Review {

	private static final String REVIEWER_TEMPLATE = "Reviewer %d";

	private int reviewID;
	private int submissionArticleID;
	private int articleOfReviwerID;
	private String reviewerPseudonym;

	private String summary;
	private String verdict;
	private boolean isFinal;
	private String articleName;
	private ArrayList<Critique> critiques;

	private String typos;

	public String getTypos() {
		return typos;
	}

	public void setTypos(String typos) {
		this.typos = typos;
	}

	public Review()
	{
		critiques = new ArrayList<>();
	}

	public String getArticleName() {
		return articleName;
	}

	public void setArticleName(String articleName) {
		this.articleName = articleName;
	}

	public void addCritique(Critique critique)
	{
		critiques.add(critique);
	}

	public String getSummary()
	{
		return summary;
	}

	public void setSummary(String summary)
	{
		this.summary = summary;
	}

	public String getVerdict()
	{
		return verdict;
	}

	public void setVerdict(String verdict)
	{
		this.verdict = verdict;
	}

	public String getReviewerPseudonym()
	{
		return reviewerPseudonym;
	}

	public void setReviewerName(int reviewerNumber)
	{
		this.reviewerPseudonym = String.format(REVIEWER_TEMPLATE, reviewerNumber);
	}

	public ArrayList<Critique> getCritiques()
	{
		return critiques;
	}

	public int getReviewID() {
		return reviewID;
	}

	public void setReviewID(int reviewID) {
		this.reviewID = reviewID;
	}

	public int getSubmissionArticleID() {
		return submissionArticleID;
	}

	public void setSubmissionArticleID(int submissionArticleID) {
		this.submissionArticleID = submissionArticleID;
	}

	public int getArticleOfReviwerID() {
		return articleOfReviwerID;
	}

	public void setArticleOfReviwerID(int articleOfReviwerID) {
		this.articleOfReviwerID = articleOfReviwerID;
	}

	public boolean isFinal() {
		return isFinal;
	}

	public void setFinal(boolean aFinal) {
		isFinal = aFinal;
	}

	public String toString()
	{
		String string = "Review by " + reviewerPseudonym + " with verdict '" + verdict + "' and summary '" + summary
				+ "' and critiques:";
		
		for(Critique critique :critiques) {
			string += "\n\t" +critique.toString();
		}
		
		return string;
	}


}
