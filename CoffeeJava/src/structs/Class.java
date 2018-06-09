package structs;

import java.util.LinkedHashSet;
import java.util.Set;

public class Class extends Node {
	
	public Set<String> imports = new LinkedHashSet<>();
	
	public Set<Field> fields = new LinkedHashSet<>();
	
	public Set<Node> nodes = new LinkedHashSet<>();
	
	public Set<Class> superclasses = new LinkedHashSet<>();
	
}
