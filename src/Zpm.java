
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * Class: Zpm
 * 
 * @author Yiding Wang
 * @version 1.0 Course: CSE465, Spring 2024 Written: February 08, 2024
 * 
 *          Purpose: This class serves as an interpreter for the Z+- programming
 *          language. It processes and executes Z+- program code read from a
 *          file with a .zpm extension. The class supports basic operations like
 *          variable assignments, arithmetic operations, and conditional loops
 *          specific to the Z+- language.
 * 
 *          [NOTICE]Part of JavaDoc for methods are generated by ChatGPT4 with
 *          permission by professor.
 */
public class Zpm {

	// Map to store the variables and their values
	private static final Map<String, String> variablesMap = new HashMap<>();

	/**
	 * The main method that is the entry point of the program.
	 * 
	 * @param args Command line arguments, expects a single argument that is the
	 *             path to the .zpm file
	 */
	public static void main(String[] args) {
		// Check if a file name is provided
		if (args.length != 1) {
			System.exit(1); // Exit with an error code
		}

		// Get the file name from command line argument
		String fileName = args[0];
		File file = new File(fileName);

		try {
			// Initialize a scanner to read the Z+- program file
			Scanner scan = new Scanner(file);
			while (scan.hasNextLine()) {
				// Read and process each line of the Z+- program
				String line = scan.nextLine().trim();
				if (!line.isEmpty()) {
					performCode(line);
				}
			}
			scan.close();
		} catch (FileNotFoundException e) {
			System.err.println("Sorry, file not found for" + fileName);
			System.exit(1); // Exit with an error code
		}
	}

	/**
	 * Handles assignments and compound assignments.
	 * 
	 * [NOTICE]: Debugging approach suggested by ChatGPT
	 * 
	 * @param operator     The assignment operator (=, +=, -=, *=).
	 * @param variableName The name of the variable being assigned.
	 * @param valueOperate The value or variable name to assign or use in the
	 *                     operation.
	 */
	private static void dealOperator(String operator, String variableName, String valueOperate) {
		// Check if the value is a variable name and get its value
		if (variablesMap.containsKey(valueOperate)) {
			valueOperate = variablesMap.get(valueOperate);
		} else if (valueOperate.length() > 1 && valueOperate.startsWith("\"") && valueOperate.endsWith("\"")) {
			// It's a string literal, strip the quotes
			valueOperate = valueOperate.substring(1, valueOperate.length() - 1);
		}

		// Perform the assignment operation based on the operator
		switch (operator) {
		case "=":
			// Assigns the value to the variable named variableName
			variablesMap.put(variableName, valueOperate);
			break;
		case "+=":
			String currentValue = variablesMap.getOrDefault(variableName, "");
			// If true, performs integer addition and updates the variable's value.
			if (checkInt(currentValue) && checkInt(valueOperate)) {
				int result = Integer.parseInt(currentValue) + Integer.parseInt(valueOperate);
				variablesMap.put(variableName, Integer.toString(result));
				// If false, performs string concatenation
			} else { // If false, performs string concatenation
				variablesMap.put(variableName, currentValue + valueOperate);
			}
			break;
		case "-=":
			// Subtraction just for integers
			if (checkInt(valueOperate) && checkInt(variablesMap.get(variableName))) {
				int result = Integer.parseInt(variablesMap.get(variableName)) 
						- Integer.parseInt(valueOperate);
				variablesMap.put(variableName, Integer.toString(result));
			} else {
				dealError("Invalid operation for non-integer values.");
			}
			break;
		case "*=":
			// Multiplication just for integers
			if (checkInt(valueOperate) && checkInt(variablesMap.get(variableName))) {
				int result = Integer.parseInt(variablesMap.get(variableName)) 
						* Integer.parseInt(valueOperate);
				variablesMap.put(variableName, Integer.toString(result));
			} else {
				dealError("Invalid operation for non-integer values.");
			}
			break;
		default:
			dealError("Unknown operator: " + operator);
		}
	}

	/**
	 * Checks if the provided string can be parsed as an integer. This method
	 * attempts to parse a string as an integer, and returns true if the parsing is
	 * successful, indicating that the string represents a valid integer. If the
	 * string cannot be parsed into an integer, it catches a NumberFormatException
	 * and returns false, indicating that the string is not a valid integer.
	 * 
	 * @param value The string to be checked for being an integer.
	 * @return boolean True if the string can be parsed as an integer, false
	 *         otherwise.
	 */
	private static boolean checkInt(String value) {
		try {
			// Attempt to parse the string as an integer.
			Integer.parseInt(value);
			// If successful, the string is an integer, return true.
			return true;
		} catch (NumberFormatException e) {
			// If parsing throws a NumberFormatException, the string is not an integer.
			// In this case, return false.
			return false;
		}
	}

	/**
	 * Executes a single statement of the Z+- program.
	 * 
	 * @param line The statement line to be executed.
	 */
	private static void performCode(String line) {
		// Trim any leading and trailing whitespace
		line = line.trim();
		if (line.endsWith(";")) {
			line = line.substring(0, line.length() - 1).trim(); // Remove the semicolon
		}

		String[] segment = line.split("\\s+");
		// Ignore empty statements
		if (segment.length == 0) {
			return;
		}

		if (segment[0].equals("PRINT")) {
			printOutput(segment[1]);
		} else if (segment[0].equals("FOR")) {
			executeNormalForLoop(line);
			// Debugging approach suggested by ChatGPT
		} else if (segment.length >= 3 && (segment[1].equals("=") || 
				segment[1].equals("+=") || segment[1].equals("-=")
				|| segment[1].equals("*="))) {
			dealOperator(segment[1], segment[0], segment[2]);
		} else {
			System.err.println("Runtime Error: Unknown command '" + segment[0] + "'");
			System.exit(1);
		}
	}

	/**
	 * Executes a FOR loop statement.
	 * 
	 * [NOTICE]: Debugging approach suggested by ChatGPT
	 * 
	 * @param loopHeader The header of the loop containing control variables and
	 *                   limits.
	 * @param loopBody   The body of the loop to be executed.
	 */
	private static void executeNormalForLoop(String line) {
		// Extract the number of iterations and loop body
		String[] segments = line.split("\\s+", 3);
		if (segments.length < 3) {
			dealError("Oops! Encounter syntax Error for [Invalid FOR loop syntax]");
			return;
		}
		int cycleNum;
		try {
			cycleNum = Integer.parseInt(segments[1]);
		} catch (NumberFormatException e) {
			dealError("Oops! Encounter syntax Error for [Invalid number of iterations]");
			return;
		}
		String loopBody = segments[2].substring(0, segments[2].lastIndexOf("ENDFOR")).trim();
		String[] loopCommands = loopBody.split(";");

		// Execute loop body for specified number of iterations
		for (int i = 0; i < cycleNum; i++) {
			for (String command : loopCommands) {
				if (!command.trim().isEmpty()) {
					performCode(command.trim());
				}
			}
		}
	}

	/**
	 * Handles the PRINT command by printing the value of the variable.
	 * 
	 * @param variableName The name of the variable to print.
	 */
	private static void printOutput(String variableName) {
		if (variablesMap.containsKey(variableName)) {
			System.out.println(variableName + "=" + variablesMap.get(variableName));
		} else {
			System.err.println("Runtime Error: Variable '" + variableName + "' not initialized.");
			System.exit(1); // Exit with an error code if variable is not initialized
		}
	}

	/**
	 * Handles and reports runtime errors. This method prints the specified error
	 * message and terminates the program.
	 *
	 * @param errorMessage The error message to be displayed.
	 */
	private static void dealError(String errorMessage) {
		System.err.println("Runtime Error: " + errorMessage);
		System.exit(1);
	}

}