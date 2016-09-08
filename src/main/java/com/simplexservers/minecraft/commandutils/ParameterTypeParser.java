package com.simplexservers.minecraft.commandutils;

/**
 * A parser to convert from user input to the given parameter type.
 *
 * @author Zach Abney
 */
public interface ParameterTypeParser<T> {

	/**
	 * Attempts to parse from the given input to the parameter type.
	 *
	 * @param input The String input to parse.
	 * @return The parsed value of the input in the form of the parameter type.
	 * @throws ArgumentParseException If there is a format issue parsing the argument to the class type.
	 */
	T parse(String input) throws ArgumentParseException;

}
