package com.simplexservers.minecraft.commandutils;

/**
 * Represents a player, console, or anyone else who could invoke a command.
 *
 * @author Zach Abney
 */
public abstract class CommandInvoker<T> {

	/**
	 * The native instance of the invoker.
	 */
	private final T nativeInvoker;

	/**
	 * Constructs a new CommandInvoker that represents the given native invoker.
	 *
	 * @param nativeInvoker The native instance of the invoker.
	 */
	public CommandInvoker(T nativeInvoker) {
		this.nativeInvoker = nativeInvoker;
	}

	/**
	 * Gets the native instance of the invoker.
	 *
	 * @return The native invoker of the command.
	 */
	public final T getNativeInvoker() {
		return nativeInvoker;
	}

	/**
	 * Sends a message to the invoker.
	 *
	 * @param message The message to send.
	 */
	public abstract void sendMessage(String message);

	/**
	 * Checks if the invoker has the given permission node.
	 *
	 * @param perm The permission node to check for.
	 * @return true if the invoker has the given permission, false otherwise.
	 */
	public abstract boolean hasPermission(String perm);

	/**
	 * Checks if the instance of the invoker is a player.
	 *
	 * @return true if the invoker is a player.
	 */
	public abstract boolean isPlayer();

}
