package exceptions;

public class VolumeFullException extends Exception{
	
	public VolumeFullException() {
		super("The volume you are trying to add a new edition to already has 6 editions");
	}
}
