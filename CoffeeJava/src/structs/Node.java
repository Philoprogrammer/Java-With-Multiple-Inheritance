package structs;

import java.util.Set;

public class Node {

	public String name;
	public Set<String> modifiers;
	
	@Override
	public int hashCode() {
		return name.hashCode();
	}
	
	@Override
	public boolean equals(Object other) {
		if(!(other instanceof Node)) return false;
		
		return name.equals(((Node)other).name);
	}
	
	@Override
	public String toString() {
		return name;
	}
	
}
