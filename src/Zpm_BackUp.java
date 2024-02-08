package HomeWork1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 * This class is an interpreter for the Z+- programming language. It reads a Z+-
 * program from a file with a .zpm extension and executes it.
 */
public class Zpm_BackUp {

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
			System.out.println("java Zpm <filename.zpm>");
			System.exit(1); // Exit with an error code
		}

		// Get the file name from command line argument
		String fileName = args[0];
		File file = new File(fileName);

		try {
			// Initialize a scanner to read the Z+- program file
			Scanner scanner = new Scanner(file);
			while (scanner.hasNextLine()) {
				// Read and process each line of the Z+- program
				String line = scanner.nextLine().trim();
				if (!line.isEmpty()) {
					executeStatement(line);
				}
			}
			scanner.close();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + fileName);
			System.exit(1); // Exit with an error code
		}
	}
	
	/**
	 * Handles assignments and compound assignments.
	 * 
	 * @param operator     The assignment operator (=, +=, -=, *=).
	 * @param variableName The name of the variable being assigned.
	 * @param valueOperate        The value or variable name to assign or use in the
	 *                     operation.
	 */
	private static void handleAssignment(String operator, String variableName, String valueOperate) {
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
	            if (isInteger(currentValue) && isInteger(valueOperate)) {
	                int result = Integer.parseInt(currentValue) + Integer.parseInt(valueOperate);
	                variablesMap.put(variableName, Integer.toString(result));
	             // If false, performs string concatenation
	            } else { // If false, performs string concatenation
	                 variablesMap.put(variableName, currentValue + valueOperate);
	            }
	            /*
	            } else if ( isStringLiteral(currentValue) && isStringLiteral(value)){ 
	                 variables.put(variableName, currentValue + value);
	            } else {
	            	handleRuntimeError("Invalid operation for non-integer values.");
	            }
	            */
	            break;
	        case "-=":
	            // Subtraction, only for integers
	            if (isInteger(valueOperate) && isInteger(variablesMap.get(variableName))) {
	                int result = Integer.parseInt(variablesMap.get(variableName)) - Integer.parseInt(valueOperate);
	                variablesMap.put(variableName, Integer.toString(result));
	            } else {
	                handleRuntimeError("Invalid operation for non-integer values.");
	            }
	            break;
	        case "*=":
	            // Multiplication, only for integers
	            if (isInteger(valueOperate) && isInteger(variablesMap.get(variableName))) {
	                int result = Integer.parseInt(variablesMap.get(variableName)) * Integer.parseInt(valueOperate);
	                variablesMap.put(variableName, Integer.toString(result));
	            } else {
	                handleRuntimeError("Invalid operation for non-integer values.");
	            }
	            break;
	        default:
	            handleRuntimeError("Unknown operator: " + operator);
	    }
	}
	
	private static boolean isInteger(String value) {
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
	
	private static boolean isString(String value) {
	    // Check if the value starts and ends with quotes
	    return value.startsWith("\"") && value.endsWith("\"");
	}


	/**
	 * Executes a single statement of the Z+- program.
	 * 
	 * @param line The statement line to be executed.
	 */
	private static void executeStatement(String line) {
	    line = line.trim();  // Trim any leading and trailing whitespace
	    if (line.endsWith(";")) {
	        line = line.substring(0, line.length() - 1).trim(); // Remove the semicolon
	    }

	    String[] parts = line.split("\\s+");

	    if (parts.length == 0) {
	        return; // Ignore empty statements
	    }

	    if (parts[0].equals("PRINT")) {
	        printOutput(parts[1]);
	    } else if (parts[0].equals("FOR")) {
	        executeNormalForLoop(line);
	    } else if (parts.length >= 3 && (parts[1].equals("=") || parts[1].equals("+=") || parts[1].equals("-=") || parts[1].equals("*="))) {
	        handleAssignment(parts[1], parts[0], parts[2]);
	    } else {
	        System.err.println("Runtime Error: Unknown command '" + parts[0] + "'");
	        System.exit(1);
	    }
	}
	
	/**
	 * Executes a FOR loop statement.
	 * 
	 * @param loopHeader The header of the loop containing control variables and
	 *                   limits.
	 * @param loopBody   The body of the loop to be executed.
	 */
	private static void executeNormalForLoop(String line) {
	    // Extract the number of iterations and loop body
	    String[] parts = line.split("\\s+", 3);
	    if (parts.length < 3) {
	        handleRuntimeError("Syntax Error: Invalid FOR loop syntax.");
	        return;
	    }
	    int iterations;
	    try {
	        iterations = Integer.parseInt(parts[1]);
	    } catch (NumberFormatException e) {
	        handleRuntimeError("Syntax Error: Invalid number of iterations.");
	        return;
	    }
	    String loopBody = parts[2].substring(0, parts[2].lastIndexOf("ENDFOR")).trim();
	    String[] loopCommands = loopBody.split(";");

	    // Execute the loop body for the specified number of iterations
	    for (int i = 0; i < iterations; i++) {
	        for (String command : loopCommands) {
	            if (!command.trim().isEmpty()) {
	                executeStatement(command.trim());
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
	 * Adds two values, which may be integers or strings.
	 * 
	 * @param varValue   The current value of the variable.
	 * @param valueToAdd The value to add to the variable's value.
	 * @return The result of the addition.
	 */
	private static String addValues(String varValue, String valueToAdd) {
		try {
			// Try to treat both values as integers and add them
			int result = Integer.parseInt(varValue) + Integer.parseInt(valueToAdd);
			return String.valueOf(result);
		} catch (NumberFormatException e) {
			// If either value is not an integer, concatenate them as strings
			return varValue + valueToAdd;
		}
	}

	// The subtraction and multiplication operations assume that the values are
	// integers.
	// If the value is not an integer, the operation will result in a runtime error.

	/**
	 * Subtracts the second value from the first, assuming both values are integers.
	 * 
	 * @param varValue        The current value of the variable.
	 * @param valueToSubtract The value to subtract from the variable's value.
	 * @return The result of the subtraction as a string.
	 */
	private static String subtract(String varValue, String valueToSubtract) {
		try {
			// If both values are integers, subtract them
			int result = Integer.parseInt(varValue) - Integer.parseInt(valueToSubtract);
			return String.valueOf(result);
		} catch (NumberFormatException e) {
			// If either value is not an integer, throw a runtime error
			System.err.println("Runtime Error: Attempt to use non-integer values in subtraction.");
			System.exit(1);
			return ""; // This return statement won't be reached; it's only here to satisfy the
						// compiler
		}
	}

	/**
	 * Multiplies two values, assuming both are integers.
	 * 
	 * @param varValue        The current value of the variable.
	 * @param valueToMultiply The value to multiply with the variable's value.
	 * @return The result of the multiplication as a string.
	 */
	private static String multiplyValues(String varValue, String valueToMultiply) {
		try {
			// If both values are integers, multiply them
			int result = Integer.parseInt(varValue) * Integer.parseInt(valueToMultiply);
			return String.valueOf(result);
		} catch (NumberFormatException e) {
			// If either value is not an integer, throw a runtime error
			System.err.println("Runtime Error: Attempt to use non-integer values in multiplication.");
			System.exit(1);
			return ""; // This return statement won't be reached; it's only here to satisfy the
						// compiler
		}
	}	

    // add a method to handle runtime errors in a unified way
	private static void handleRuntimeError(String errorMessage) {
		System.err.println("Runtime Error: " + errorMessage);
		System.exit(1);
	}

}