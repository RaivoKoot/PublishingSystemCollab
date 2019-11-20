package exceptions;

public class IncompleteInformationException extends Exception{

	public IncompleteInformationException() {
		super("The object you passed has null for some attributes needed to perform this action");
	}
}
