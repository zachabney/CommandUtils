package com.simplexservers.minecraft.commandutils;

/**
 * An adapter with default implementation of a CommandRegistrant.
 *
 * @author Zach Abney
 */
public class CommandRegistrantAdapter implements CommandRegistrant {

	/**
	 * The default implementation to handle a base command being registered.
	 * Inherently this does nothing, it's meant to be overridden.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void baseCommandRegistered(CommandManager manager, String baseCommand) {

	}

	/**
	 * The default implementation to handle a base command being registered.
	 * Inherently this does nothing, it's meant to be overridden.
	 *
	 * {@inheritDoc}
	 */
	@Override
	public void commandMethodRegistered(CommandManager manager, CommandMethod method) {

	}

}
