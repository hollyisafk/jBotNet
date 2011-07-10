package core;

public class shell {
	public static void start_shell() {
		while (true) {
			String input;
			
			terminal.print_prompt();
			input = terminal.read_input();
			
			if (input.equalsIgnoreCase("exit") || input.equalsIgnoreCase("quit")) {
				//print new line
				break;
			}
			
			handle(input);
		}
	}
	
	private static void handle(String input) {
		String[] words = input.split(" ");
		if (words.length == 0)
			return;
	}
}
