package com.simplexservers.minecraft.commandutils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A manager for a set of commands, usually for an entire plugin.
 *
 * @author Zach Abney
 */
public class CommandManager {

	/**
	 * The list of commands associated with their base command.
	 */
	private Map<String, GroupedCommand> groupedCommands = new HashMap<>(); // <Base Command, Grouped Command>
	/**
	 * The registrant responsible for native command registration.
	 */
	private CommandRegistrant registrant;

	/**
	 * Constructs a new CommandManager without a registrant.
	 */
	public CommandManager() {
		this(new CommandRegistrantAdapter());
	}

	/**
	 * Constructs a new CommandManager with a registrant.
	 *
	 * @param registrant The registrant responsible for native command registration.
	 */
	public CommandManager(CommandRegistrant registrant) {
		this.registrant = registrant;
	}

	/**
	 * Registers the CommandHandler and the command methods it handles.
	 *
	 * @param handler The CommandHandler to register.
	 */
	public void registerHandler(CommandHandler handler) {
		for (Method method : handler.getClass().getDeclaredMethods()) {
			if (CommandMethod.validate(method)) {
				CommandMethod commandMethod = new CommandMethod(handler, method, method.getAnnotation(CommandProperties.class));
				registerCommandMethod(commandMethod);
			}
		}
	}

	/**
	 * Registers the CommandMethod in the list of grouped commands.
	 *
	 * @param method The CommandMethod to register.
	 */
	private void registerCommandMethod(CommandMethod method) {
		// The commands (main command and aliases) to register
		String[] commands = new String[method.getMeta().aliases().length + 1];
		commands[0] = method.getMeta().command();
		for (int i = 0; i < method.getMeta().aliases().length; i++) {
			commands[i + 1] = method.getMeta().aliases()[i];
		}

		for (String command : commands) { // Register the main command and each alias
			String[] commandFragments = command.split(" ");

			String baseCommand = commandFragments[0];
			StringBuilder subcommand = new StringBuilder();
			for (int i = 1; i < commandFragments.length; i++){
				if (subcommand.length() != 0) subcommand.append(' ');
				subcommand.append(commandFragments[i]);
			}

			boolean newBaseCommand = !groupedCommands.containsKey(baseCommand);

			GroupedCommand groupedCommand = getGroupedCommand(baseCommand);
			groupedCommand.attachSubcommand(subcommand.toString(), method);

			// Notify the registrant when the base command is registered.
			if (newBaseCommand)
				registrant.baseCommandRegistered(this, baseCommand);

			// Notify the registrant when the method has successfully been registered.
			registrant.commandMethodRegistered(this, method);
		}
	}

	/**
	 * Gets a new or existing GroupedCommand with the given base command.
	 *
	 * @param baseCommand The command base.
	 * @return The GroupedCommand for the given base command.
	 */
	private GroupedCommand getGroupedCommand(String baseCommand) {
		GroupedCommand groupedCommand;
		if (!groupedCommands.containsKey(baseCommand)) {
			groupedCommand = new GroupedCommand(baseCommand);
			groupedCommands.put(baseCommand, groupedCommand);
		} else {
			groupedCommand = groupedCommands.get(baseCommand);
		}

		return groupedCommand;
	}

	/**
	 * Gets the collection of command metas for each subcommand.
	 *
	 * @return The array of subcommand metas.
	 */
	public Collection<CommandProperties[]> getCommandMetas() {
		ArrayList<CommandProperties[]> groupedCommandMetas = new ArrayList<>(groupedCommands.size());
		groupedCommands.values().forEach(groupedCommand -> groupedCommandMetas.add(groupedCommand.getSubcommandMetas()));
		return groupedCommandMetas;
	}

	/**
	 * Invokes the appropriate CommandMethod based on the command
	 * entered by the user.
	 *
	 * @param sender The sender of the command.
	 * @param baseCommand The base of the command.
	 * @param args The arguments provided with the base command.
	 * @return true if the command is valid, false otherwise.
	 */
	public boolean invokeCommand(CommandInvoker sender, String baseCommand, String[] args) {
		if (!groupedCommands.containsKey(baseCommand)) return false;
		GroupedCommand groupedCommand = groupedCommands.get(baseCommand);
		MatchedCommandMethod matchedMethod;
		try {
			// Find the subcommand method that matches the arguments.
			matchedMethod = groupedCommand.matchSubcommand(args);
		} catch (ArgumentParseException e) {
			// There's an issue with the arguments provided.
			sender.sendMessage(e.getDisplayMessage());
			return false;
		}
		if (matchedMethod == null) return false; // No matching method was found.

		// We found the method, try to invoke it
		try {
			matchedMethod.getMethod().invoke(sender, matchedMethod.getArguments());
		} catch (Exception e) {
			Logger.getGlobal().log(Level.SEVERE, "An error occurred while invoking " + matchedMethod.toString(), e);
			sender.sendMessage("§cAn error occurred while executing the " + baseCommand + " command.");
		}

		return true;
	}

}