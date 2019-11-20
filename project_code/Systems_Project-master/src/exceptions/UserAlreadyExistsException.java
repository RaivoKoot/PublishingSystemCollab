package exceptions;

public class UserAlreadyExistsException extends Exception{

	private static final String ERROR_MESSAGE_TEMPLATE = "A user with the email '%s' already exists in system";

	public UserAlreadyExistsException(String email)
	{
		super(String.format(ERROR_MESSAGE_TEMPLATE, email));
	}
}
