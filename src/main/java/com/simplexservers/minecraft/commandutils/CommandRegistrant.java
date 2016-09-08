package com.simplexservers.minecraft.commandutils;

/**
 * A handler that's responsible for registering a command natively.
 *
 * @author Zach Abney
 */
public interface CommandRegistrant {

	/**
	 * Called when a new base command is registered with the CommandManager.
	 * <p>
	 * Note: This will only be called once per base command.
	 * </p>
	 *
	 * @param manager The CommandManager the base command was registered with.
	 * @param baseCommand The new base command that was registered.
	 */
	void baseCommandRegistered(CommandManager manager, String baseCommand);

	/**
	 * Called when a command method is registered with the CommandManager.
	 *
	 * @param manager The CommandManager the method is registered to.
	 * @param method The CommandMethod that was registered with the manager.
	 */
	void commandMethodRegistered(CommandManager manager, CommandMethod method);

}
