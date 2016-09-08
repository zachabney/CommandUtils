package com.simplexservers.minecraft.commandutils;

/**
 * Represents a CommandMethod that has been matched from user input.
 *
 * @author Zach Abney
 */
public class MatchedCommandMethod {

	/**
	 * The CommandMethod that was matched.
	 */
	private CommandMethod method;
	/**
	 * The arguments, parsed from the user command, to be passed to the CommandMethod.
	 */
	private Object[] arguments;

	/**
	 * Constructs a new MatchedCommandMethod that represents a CommandMethod
	 * found and the arguments to be passed to it from the command entered by the CommandSender.
	 *
	 * @param method The CommandMethod matched from the user command.
	 * @param arguments The arguments to be passed to the matched CommandMethod.
	 */
	protected MatchedCommandMethod(CommandMethod method, Object[] arguments) {
		this.method = method;
		this.arguments = arguments;
	}

	/**
	 * Gets the CommandMethod that was matched to the user command.
	 *
	 * @return The matched CommandMethod.
	 */
	public CommandMethod getMethod() {
		return method;
	}

	/**
	 * Gets the arguments that should be passed to the matched CommandMethod
	 * to represent the arguments entered by the CommandSender.
	 *
	 * @return The arguments to be passed to the CommandMethod.
	 */
	public Object[] getArguments() {
		return arguments;
	}

}
