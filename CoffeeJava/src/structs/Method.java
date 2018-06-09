package structs;

import java.util.Set;

public class Method extends Node implements Cloneable {

	public Code code;
	public Set<Field> fields;
	
	@Override
	public Object clone() {
		Method m = new Method();
		m.code = this.code;
		m.fields = this.fields;
		m.modifiers = this.modifiers;
		m.name = this.name;
		return m;
	}
	
}
