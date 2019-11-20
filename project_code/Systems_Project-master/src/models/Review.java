package models;

import java.util.ArrayList;

public class Review {

	private static final String REVIEWER_TEMPLATE = "Reviewer %d";

	private String reviewer;
	private String summary;
	private String verdict;
	private ArrayList<Critique> critiques;

	public Review()
	{
		critiques = new ArrayList<>();
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

	public String getReviewer()
	{
		return reviewer;
	}

	public void setReviewerName(int reviewerNumber)
	{
		this.reviewer = String.format(REVIEWER_TEMPLATE, reviewerNumber);
	}

	public ArrayList<Critique> getCritiques()
	{
		return critiques;
	}

	public String toString()
	{
		String string = "Review by " + reviewer + " with verdict '" + verdict + "' and summary '" + summary
				+ "' and critiques:";
		
		for(Critique critique :critiques) {
			string += "\n\t" +critique.toString();
		}
		
		return string;
	}

}
