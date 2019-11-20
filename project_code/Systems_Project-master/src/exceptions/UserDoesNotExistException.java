package exceptions;

public class UserDoesNotExistException extends Exception{
	
	private static final String ERROR_MESSAGE_TEMPLATE = "User with email %s does not exist";

	public UserDoesNotExistException(String email) {
		super(String.format(ERROR_MESSAGE_TEMPLATE, email));
	}
}
