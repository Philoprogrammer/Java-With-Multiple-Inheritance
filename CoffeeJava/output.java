//This is the expected output written in Java

public class Test {
	
	//"struct" becomes "public static class"
	public static class LivingInfo {
		//Make sure no methods and only fields
		int age;
	}
	
	public abstract class LivingBeing {
		
		LivingInfo info;
		
		public abstract void takeStep();
	}
	
	public static class Person extends LivingBeing {
		
		String name;
		
		public void greetYourself(){
			//Println is translated to System.out.println
			System.out.println("Hi, I am ", name);
		}
		
		@Override
		public void takeStep() {
			System.out.println("Yay, I just took a step!");
		}
	}

	public static class Horse extends LivingBeing {
		
		public void sayHello(){
			System.out.println("Neigh");
		}
		
		@Override
		public void takeStep() {
			System.out.println("Neigh. Clop.");
		}
		
	}
	
	public static abstract class Centaur extends Person {
		
		//Copied over from Horse class
		public void takeStep<HorseID>() {
			System.out.println("Neigh. Clop.");
		}
		
		public void sayHello<HorseID>(){
			System.out.println("Neigh");
		}
		
		//Required overriding
		@Override
		public void takeStep() {
			takeStep<HorseID>();
		}
	}

}