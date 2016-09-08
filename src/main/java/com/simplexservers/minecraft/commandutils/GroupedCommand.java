package com.simplexservers.minecraft.commandutils;

import java.util.Comparator;
import java.util.Map.Entry;
import java.util.logging.Level;

import java.util.TreeMap;
import java.util.logging.Logger;

/**
 * Represents a base command and it's subcommands.
 *
 * @author Zach Abney
 */
public class GroupedCommand {

	/**
	 * The comparator to sort the subcommands in order of precision (length)
	 */
	private static Comparator<String> commandComparator = (subcommand1, subcommand2) -> {
		int compare = subcommand2.length() - subcommand1.length();
		return compare == 0 ? 1 : compare;
	};

	/**
	 * The base of the command and subcommands.
	 */
	private String baseCommand;
	/**
	 * The list of subcommands and their corresponding CommandMethods.
	 */
	private TreeMap<String, CommandMethod> subcommands = new TreeMap<>(commandComparator); // <Subcommand, CommandMethod>

	/**
	 * Constructs a new GroupedCommand with the given command base.
	 *
	 * @param baseCommand The base command.
	 */
	protected GroupedCommand(String baseCommand) {
		this.baseCommand = baseCommand;
	}

	/**
	 * Gets the base command.
	 *
	 * @return The command base.
	 */
	public String getBaseCommand() {
		return baseCommand;
	}

	/**
	 * Gets the array of metas for the subcommands.
	 *
	 * @return The CommandProperties for each subcommand.
	 */
	public CommandProperties[] getSubcommandMetas() {
		CommandProperties[] metas = new CommandProperties[subcommands.size()];
		int index = 0;
		for (CommandMethod method : subcommands.values()) {
			metas[index++] = method.getMeta();
		}

		return metas;
	}

	/**
	 * Attaches the subcommand and CommandMethod to the grouped command.
	 *
	 * @param subcommand The subcommand to attach.
	 * @param commandMethod The corresponding CommandMethod for the subcommand.
	 */
	public void attachSubcommand(String subcommand, CommandMethod commandMethod) {
		subcommands.put(subcommand, commandMethod);
	}

	/**
	 * Matches a subcommand based on the arguments provided.
	 *
	 * @param args The arguments entered by the CommandSender.
	 * @return The MatchedCommandMethod that corresponds to the subcommand.
	 * @throws ArgumentParseException If there was an issue parsing the arguments for the subcommand.
	 */
	public MatchedCommandMethod matchSubcommand(String[] args) throws ArgumentParseException {
		// Build the args as one string
		StringBuilder argsStringBuilder = new StringBuilder();
		for (String arg : args) {
			if (argsStringBuilder.length() != 0) argsStringBuilder.append(' ');
			argsStringBuilder.append(arg);
		}
		String argsString = argsStringBuilder.toString();

		for (Entry<String, CommandMethod> subcommandEntry : subcommands.entrySet()) {
			if (!argsString.startsWith(subcommandEntry.getKey())) continue;
			CommandMethod subcommandMethod = subcommandEntry.getValue();

			// Check to ensure the command is allowed for the current environment.
			int allowedEnvironments = subcommandMethod.getMeta().assertEnvironment();
			if (!EnvironmentFlags.hasFlag(EnvironmentFlags.CURRENT_FLAG, allowedEnvironments)) {
				continue;
			}

			String commandArgsString = argsString.substring(subcommandEntry.getKey().length()).trim();
			// .split() always has a length of 1 so use an empty String array if the args are empty
			String[] commandArgs = commandArgsString.length() == 0 ? new String[0] : commandArgsString.split(" ");
			// Gets the array of arguments, built from the arguments provided, that match the method parameters.
			Object[] parameters;
			try {
				parameters = getArguments(commandArgs, subcommandMethod.getMethod().getParameterTypes());
			} catch (ArgumentParseException e) {
				// There was an issue parsing the arguments
				String usage = "§cUsage: " + subcommandMethod.getMeta().usage();
				throw new ArgumentParseException(e.getMessage(), e.getDisplayMessage() + "\n" + usage);
			}
			return new MatchedCommandMethod(subcommandMethod, parameters);
		}

		// A subcommand could not be found
		return null;
	}

	/**
	 * Gets the array of arguments to be passed to the method based on the
	 * arguments entered by the CommandSender and the parameters
	 * required for the CommandMethod.
	 *
	 * @param args The arguments entered by the CommandSender.
	 * @param parameterTypes The parameter types required for the CommandMethod.
	 * @return The array of arguments to be passed to the CommandMethod.
	 * @throws ArgumentParseException If there is an issue parsing the arguments to the required parameter types.
	 */
	private static Object[] getArguments(String[] args, Class<?>[] parameterTypes) throws ArgumentParseException {
		if (parameterTypes.length == 0) return null; // Method doesn't even take a CommandSender. It shouldn't even be registered
		if (parameterTypes.length == 1) return new Object[0]; // Method doesn't take additional parameters
		if (parameterTypes.length == 2 && parameterTypes[1] == String[].class) return new Object[] {args}; // Method takes arguments as parameter
		if (args.length >= parameterTypes.length - 1) { // Good match, parse each argument
			Object[] arguments = new Object[parameterTypes.length - 1];
			for (int i = 0; i < arguments.length; i++) {
				try {
					arguments[i] = ParameterType.parseArgument(args[i], parameterTypes[i + 1]); // Throws exception if invalid
				} catch (IllegalArgumentException e) {
					// Some other issue occurred trying to parse the command. Probably an unsupported parameter type.
					Logger.getGlobal().log(Level.SEVERE, "Error handling command.", e);
					return null;
				}
			}

			return arguments;
		}

		throw new ArgumentParseException("Not enough arguments provided to satisfy method requirements.", "Not enough arguments provided.");
	}

}
