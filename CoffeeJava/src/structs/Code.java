package structs;

import java.util.StringTokenizer;

public class Code {

	//Code must be separated line by line
	public String[] code;
	public String total;
	
	public Code(String text) {
		StringTokenizer tokenizer = new StringTokenizer(text, "\n");
		
		code = new String[tokenizer.countTokens()];
		
		int i = 0;
		while(tokenizer.hasMoreTokens()) {
			code[i] = tokenizer.nextToken();
			i++;
		}
		
		total = text;
	}
	
}
