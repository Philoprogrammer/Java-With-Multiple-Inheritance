package interpretor;

import java.util.Set;

import structs.Class;
import structs.Field;
import structs.Method;
import structs.Node;

public class Printer {
	
	public static void print(Class c) {
		for(String s: c.imports) {
			println(s);
		}
		
		printModifiers(c.modifiers);
		
		print(c.name);
		
		if(c.superclasses.size() > 0) {
			print(" extends " + c.superclasses.iterator().next());
			
/*			int counter = 0;
			for(Class superclass: c.superclasses) {
				if(counter == c.superclasses.size() - 1) {
					print(superclass.name + " ");
				}else{
					print(superclass.name + ", ");
				}
				counter++;
			}*/
		}
		
		println("{");
		
		printFields(c.fields);
		
		for(Node n: c.nodes) {
			if(n instanceof Class) {
				print((Class) n);
			}else if(n instanceof Method) {
				printMethod((Method) n);
			}
		}
		
		println("}");
	}
	
	public static void printMethod(Method m) {
		printModifiers(m.modifiers);
		print(m.name);
		print("(");
		int counter = 0;
		for(Field f: m.fields) {
			print(f.toString());
			
			if(counter < m.fields.size() - 1) {
				print(", ");
			}
			
			counter++;
		}
		print(")");
		
		if(m.modifiers.contains("abstract")) {
			println(";");
			return;
		}
		
		println("{");
		
		
		for(String s: m.code.code) {
			print(s);
			if(s.replace("\t", "").length() > 0 && !(s.endsWith("}") || s.endsWith("{"))) {
				print(";");
			}
			println("");
		}
		
		
		
		println("");
		println("}");
	}
	
	public static void printFields(Set<Field> fields) {
		for(Field f: fields) {
			printModifiers(f.modifiers);
			println(f.toString() + ";");
		}
	}
	
	public static void printModifiers(Set<String> modifiers) {
		for(String modifier: modifiers) {
			print(modifier + " ");
		}
	}
	
	public static void print(String str) {
		//Add in write to file support
		System.out.print(str);
	}
	
	public static void println(String str) {
		//Add in write to file support
		System.out.println(str);
	}
	
}
