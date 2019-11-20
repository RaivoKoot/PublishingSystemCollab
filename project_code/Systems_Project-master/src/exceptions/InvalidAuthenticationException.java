package exceptions;

public class InvalidAuthenticationException extends Exception {

	public InvalidAuthenticationException()
	{
		super("The provided credentials for this action are not valid");
	}
}
