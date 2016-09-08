package com.simplexservers.minecraft.commandutils;

import java.util.ArrayList;

/**
 * A utility class to assist in generating command help pages.
 */
public class HelpCommandUtil {

	/**
	 * Generates a detailed help for all the commands registered with the CommandManager.
	 *
	 * @param cmdManager The CommandManager to get the commands from.
	 * @param invoker The person invoking the help command.
	 * @param showPermission Whether or not to show the required permission node.
	 * @return The generated help page.
	 */
	public static String generateHelp(CommandManager cmdManager, CommandInvoker invoker, boolean showPermission) {
		StringBuilder help = new StringBuilder();

		// Keeps a record of added commands to remove duplicates (with different args)
		ArrayList<String> addedCommands = new ArrayList<>();

		cmdManager.getCommandMetas().forEach(subcmdMetas -> {
			if (help.length() != 0) {
				help.append('\n');
			}

			help.append("§e-------------------------");
			for (CommandProperties meta : subcmdMetas) {
				// Skip if the command has already been added
				if (addedCommands.contains(meta.command())) {
					continue;
				} else {
					addedCommands.add(meta.command());
				}

				help.append("\n§9Command: §6/" + meta.command());
				help.append("\n§9Description: §f" + meta.description());
				help.append("\n§9Usage: §f" + meta.usage());
				if (showPermission) {
					String permission;
					if (meta.permission().length() == 0) {
						permission = "§fnone";
					} else {
						permission = (invoker.hasPermission(meta.permission()) ? "§a" : "§c") + meta.permission();
					}

					help.append("\n§9Required Permission: " + permission);
				}
				help.append("\n§e-------------------------");
			}
		});

		return help.toString();
	}

	/**
	 * Generates a simple help for for the commands an invoker has access to.
	 *
	 * @param cmdManager The CommandManager to get the commands from.
	 * @param invoker The person invoking the help command.
	 * @return The generated help page.
	 */
	public static String generateSimpleHelp(CommandManager cmdManager, CommandInvoker invoker) {
		StringBuilder help = new StringBuilder();

		// Keeps a record of added commands to remove duplicates (with different args)
		ArrayList<String> addedCommands = new ArrayList<>();

		cmdManager.getCommandMetas().forEach(subcmdMetas -> {
			for (CommandProperties meta : subcmdMetas) {
				if (invoker.hasPermission(meta.permission())) {
					// Skip if the command has already been added
					if (addedCommands.contains(meta.command())) {
						continue;
					} else {
						addedCommands.add(meta.command());
					}

					if (help.length() != 0) {
						help.append('\n');
					}

					help.append("§9/" + meta.command() + "§f - §e" + meta.description());
				}
			}
		});

		return help.toString();
	}

}
