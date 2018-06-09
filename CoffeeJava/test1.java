public class Test {
	class LivingInfo {
		int age;
	}

	public abstract class LivingBeing {
		LivingInfo info;

		public abstract void takeStep();
	}

	public static class Person extends LivingBeing {
		String name;

		public void greetYourself() {
			println("Hi, I am ", name);

		}

		@Override
		public void takeStep() {
			println("Yay, I just took a step!");

		}
	}

	public static class Horse extends LivingBeing {
		public void sayHello() {
			println("Neigh");

		}

		@Override
		public void takeStep() {
			println("Neigh. Clop.");

		}
	}

	public static abstract class Centaur extends Person {
		@Override
		public void takeStep() {
			super987238860takeStep();

		}

		public void super987238860sayHello() {
			println("Neigh");

		}

		@Override
		public void super987238860takeStep() {
			println("Neigh. Clop.");

		}
	}

	public static void println(String str) {
		System.out.println(str);
	}
}