package util;

public class DebuggableStringTokenizer extends ExtendableStringTokenizer {

	public static final String defaultDelim = " \t\n\r\f";
	
	public DebuggableStringTokenizer(String str) {
		super(str);
	}
	
	//public static LinkedList<String> q = new LinkedList<>();
	
	public DebuggableStringTokenizer(String str, String delim) {
		super(str, delim);
	}

	public void addToken(String s) {
		//q.add(s);
		super.currentPosition -= s.length();
	}
	
	@Override
	public String nextToken(String delim) {
/*		if(!q.isEmpty()) {
			return q.remove();
		}*/
		return super.nextToken(delim);
	}

	public boolean hasMoreTokens(String string) {
		delimiters = string;

        /* delimiter string specified, so set the appropriate flag. */
        delimsChanged = true;

        setMaxDelimCodePoint();
        
        return super.hasMoreTokens();
	}
	
	public void setPosition(int position) {
		super.currentPosition = position;
	}

	public int getPosition() {
		return super.currentPosition;
	}
	
/*	@Override
	public String nextToken(String delim) {
		String token = super.nextToken(delim);
		
		//System.out.println(token);
		
		return token;
	}*/

}
