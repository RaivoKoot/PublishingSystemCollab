package models;

public class JournalEditor extends User{
	private boolean isChief;
	private Journal journal;
	
	public JournalEditor() {
		super();
	}
	
	public JournalEditor(User user) {
		super();
		super.setEmail(user.getEmail());
		super.setForenames(user.getForenames());
		super.setSurname(user.getSurname());
		super.setUniversity(user.getUniversity());
	}
	
	public boolean isChief()
	{
		return isChief;
	}

	public void setChief(boolean isChief)
	{
		this.isChief = isChief;
	}

	public Journal getJournal()
	{
		return journal;
	}

	public void setJournal(Journal journal)
	{
		this.journal = journal;
	}
	
	public String toString() {
		return "Editor for journal '" +journal.getName()+"' with chief="+isChief+" and user info: " + super.toString();
	}
}
