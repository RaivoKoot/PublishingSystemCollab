package exceptions;

public class UniqueColumnValueAlreadyExists extends Exception{

	public UniqueColumnValueAlreadyExists(){
		super("A row in that table already has the same value for one of the unique columns");
	}
}
