package exceptions;

public class CantRemoveLastChiefEditorException extends Exception {

	private static final String ERROR_MESSAGE_TEMPLATE = "This user is the last chief editor of Journal with ISSN '%s' and therefore can not be deleted";

	public CantRemoveLastChiefEditorException(String ISSN)
	{
		super(String.format(ERROR_MESSAGE_TEMPLATE, ISSN));
	}
}
