package interpretor;

import static util.DebuggableStringTokenizer.defaultDelim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import structs.Class;
import structs.Code;
import structs.Field;
import structs.Method;
import structs.Node;
import util.DebuggableStringTokenizer;

public class Main {

	//Convert to methods, fields, constructors, etc.
	
	public static void main(String[] args) throws Exception {
		Class bigWrapper = read("input.coffeejava");
		
		Class c = (Class) bigWrapper.nodes.iterator().next();
		
		boolean containsPrintln = false;
		for(Node n: c.nodes) {
			if(!(n instanceof Method)) continue;
			Method m = (Method) n;
			System.out.println("Name: " + m.name);
			if(m.name.equals("println")) {
				containsPrintln = true;
				break;
			}
		}
		
		if(!containsPrintln) {
			Method m = new Method();
			Code code = new Code("StringBuffer sb = new StringBuffer()\nfor(String s: str) {\n sb.append(s)\n}\nSystem.out.println(sb.toString())\n");
			m.code = code;
			Set<Field> fields = new LinkedHashSet<>();
			Field field = new Field();
			field.modifiers = new LinkedHashSet<>();
			field.name = "str";
			field.type = "String...";
			fields.add(field);
			m.fields = fields;
			Set<String> modifiers = new LinkedHashSet<>();
			modifiers.add("public");
			modifiers.add("static");
			modifiers.add("void");
			m.modifiers = modifiers;
			m.name = "println";
			c.nodes.add(m);
		}
		
		//System.out.println("----------------------------");
		
/*		System.out.println("Fields:");
		for(Field f: c.fields) {
			System.out.println(f);
		}
		
		System.out.println("Nodes:");
		for(Node n: c.nodes) {
			System.out.println(n);
		}
		*/
		{
			Deque<Class> q = new ArrayDeque<>();
		
			q.add(c);
		
			while(!q.isEmpty()){
				Class current = q.remove();
			
				//Ignore constructors for now
				if(current.superclasses.size() > 1) {
					int i = 0;
					//System.out.println(current.superclasses);
					for(Class superclass: current.superclasses) {
						if(i == 0) {
							i++;
							continue;
						}
						//System.out.println(superclass);
						
						for(Node n: superclass.nodes) {
							if(!(n instanceof Method)) continue;
							Method m = (Method) n;
							//Copy methods over
							//if(!current.nodes.contains(n)) 
							{
								Method newM = (Method)m.clone();
								newM.name = "super" + Math.abs(("(" + superclass.name + ")").hashCode()) + m.name;
								newM.modifiers.remove("@Override");
								current.nodes.add(newM);
								//System.out.println(current.name + " " + newM.name);
							}
							
						}
						//q.add(superclass);
						//Subclass cannot be abstract
						i++;
					}
				}
				
				for(Node n: current.nodes) {
					if(n instanceof Class) {
						q.add((Class) n);
					}
				}
			}
		}
		{
			//Handle methods
			Deque<Class> q = new ArrayDeque<>();
			
			for(Node n: c.nodes) {
				if(!(n instanceof Class)) {
					continue;
				}
				q.add((Class) n);
			}
			
			while(!q.isEmpty()) {
				Class current = q.remove();
				//System.out.println(current.name);
				
				for(Node n: current.nodes) {
					if(!(n instanceof Method)) continue;
					
					Method m = (Method) n;
					
					Code code = m.code;
					
					String[] superThings = code.total.split("(super\\()");
					
					StringBuffer newCode = new StringBuffer();
					
					if(superThings.length == 1) {
						newCode.append(superThings[0]);
					}else {
						for(int i = 0; i < superThings.length - 1; i++) {
							String token = superThings[i];
							if(i > 0) {
								token = token.substring(token.indexOf(")") + 1, token.length());
							}
							StringTokenizer tokenizer = new StringTokenizer(superThings[i + 1]);
							String thing = "super" + Math.abs(("(" + tokenizer.nextToken(")") + ")").hashCode());
							
							newCode.append(token);
							newCode.append(thing);
						}
						newCode.append(superThings[superThings.length - 1].substring(superThings[superThings.length - 1].indexOf(")") + 1, superThings[superThings.length - 1].length()));
						
					}
					
					m.code = new Code(newCode.toString());
				}
				
				for(Node n: current.nodes) {
					if(n instanceof Class) {
						q.add((Class)n);
					}
				}
			
		}
		}
		
		//Do final find and replace
		
		
		//Print out final result
		Printer.print(c);
	}
	
	//Accept only one file for now
	public static Class read(String file) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(file));
		
		Class root = new Class();
		
		String line;
		StringBuffer code = new StringBuffer();
		
		while((line = br.readLine()) != null) {
			//line = line.replace("\\s+","");
			
			//Deal with imports
			if(line.startsWith("import")) {
				root.imports.add(line);
				continue;
			}
			
			//Ignore comments
			if(line.startsWith("//")) continue;
			
			code.append(line);
			code.append('\n');
		}
		
		br.close();
		
		Class main = new Class();
		
		recurse(main, new DebuggableStringTokenizer(code.toString()));
		
		return main;
	}
	
	public static Map<String, Class> stringClassMap = new HashMap<>();
	
	public static void recurse(Class main, DebuggableStringTokenizer tokenizer) {
		String firstToken = tokenizer.nextToken(defaultDelim);
		//System.out.println("First Token: " + firstToken);
		if(firstToken.equals("}")) {
			//System.out.println("END");
			return;
		}
		
		Set<String> modifiers = new LinkedHashSet<>();
		String currentToken = null;
		
		if(!firstToken.equals("{"))
			tokenizer.addToken(firstToken);
		
		while(tokenizer.hasMoreTokens()) {
			currentToken = tokenizer.nextToken(defaultDelim);
			//System.out.println(currentToken);
			if(matchesModifiers(currentToken)) {
				if(currentToken.equals("struct")) currentToken = "class";
				modifiers.add(currentToken);
			}else{
				//System.out.println("NOT PASSED: " + currentToken);
				break;
			}
		}
		
		String name = currentToken;
		
		String nextToken = tokenizer.nextToken(defaultDelim);
		
		//System.out.println(modifiers.toString() + " " + name + " " + nextToken);
		
		//System.out.println("NextToken: " + nextToken);
		
		if(nextToken.equals("{") || modifiers.contains("abstract") || nextToken.startsWith("(") || nextToken.equals("extends")) {
			//Class or method
			if(modifiers.contains("class") || modifiers.contains("interface") || modifiers.contains("struct")) {
				Set<Class> superclasses = new LinkedHashSet<>();
				
				if(nextToken.equals("extends")) {
					String superclassString = tokenizer.nextToken("{");
				
					DebuggableStringTokenizer superclassTokenizer = new DebuggableStringTokenizer(superclassString, defaultDelim + ",");
				
					while(superclassTokenizer.hasMoreTokens()){
						//Classes must be declared in the right order
						superclasses.add(stringClassMap.get(superclassTokenizer.nextToken()));
					}
					
					//System.out.println("Superclasses: " + superclasses.toString());
				}
				
				//Class
				Class inner = new Class();
				inner.modifiers = modifiers;
				inner.name = name;
				inner.superclasses = superclasses;
				
				main.nodes.add(inner);
				stringClassMap.put(inner.name, inner);
				
				//Ignore "{"
				//tokenizer.nextToken(defaultDelim);
				
				//System.out.println("recurse: " + inner.name);
				recurse(inner, tokenizer);
			}else thing:{
				String fields = nextToken;
				fields += tokenizer.nextToken("\n");
				fields = fields.replaceAll("\t", "");
				
				//System.out.println("Fields: " + fields);
				
				Set<Field> fieldSet = new LinkedHashSet<>();
				
				DebuggableStringTokenizer fieldTokenizer = new DebuggableStringTokenizer(fields);
				
				while(fieldTokenizer.hasMoreTokens("()" + defaultDelim)) {
					//Not allowing final keyword
					
					String fieldType = fieldTokenizer.nextToken("()" + defaultDelim);
					//System.out.println("Field Type: " + fieldType);
					String fieldName = fieldTokenizer.nextToken("()" + defaultDelim);
					//System.out.println("Field Name: " + fieldName);
					
					Field f = new Field();
					
					//Blank
					f.modifiers = new LinkedHashSet<>();
					f.type = fieldType;
					f.name = fieldName;
					
					fieldSet.add(f);
				}
				
				Method m = new Method();
				
				m.modifiers = modifiers;
				m.name = name;
				m.fields = fieldSet;
				
				//Method
				if(modifiers.contains("abstract")) {
					tokenizer.addToken(nextToken);
					
					//Abstract
					m.code = new Code("");
					
					main.nodes.add(m);
					
					//System.out.println("Abstract Method");
					
					break thing;
				}
				
				StringBuffer methodCode = new StringBuffer();
				
				methodCode.append(tokenizer.nextToken("}"));
				
				methodCode = new StringBuffer(methodCode.substring(methodCode.indexOf("{") + 1, methodCode.length()));
				
				tokenizer.setPosition(tokenizer.getPosition() + 1);
				
				//tokenizer.setPosition(tokenizer.getPosition() + 1);
				
				Code actualCode = new Code(methodCode.toString());
				
				//System.out.println(methodCode.toString());
				
				m.code = actualCode;
				
				main.nodes.add(m);
			}
		}else{
			//Field
			Field f = new Field();
			
			f.type = name;
			f.modifiers = modifiers;
			f.name = nextToken;
			
			main.fields.add(f);
		}
		
		//After recursing
		//System.out.println("Recurse on same class: " + main.name);
		if(main.name == null) return;
		recurse(main, tokenizer);
		
		//Discern between methods, classes, consructors, and fields
	}
	
	public static boolean matchesModifiers(String s) {
		switch(s) {
		
		case "public":
		case "static":
		case "abstract":
		case "void":
		case "final":
		case "class":
		case "default":
		case "interface":
			
		case "@Override":
			
		case "struct":
			return true;
		default:
			return false;
		}
	}
	
}
