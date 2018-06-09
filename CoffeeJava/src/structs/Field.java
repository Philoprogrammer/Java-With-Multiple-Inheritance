package structs;

public class Field extends Node {

	public String type;
	
	@Override
	public String toString() {
		return type + " " + name;
	}
	
	@Override
	public int hashCode() {
		return (type + " " + name).hashCode();
	}
	
}
