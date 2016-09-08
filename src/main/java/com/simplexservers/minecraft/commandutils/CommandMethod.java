package com.simplexservers.minecraft.commandutils;

import java.lang.reflect.Method;

/**
 * Represents a method that gets executed when a command is runAsync.
 *
 * @author Zach Abney
 */
public class CommandMethod {

	public static final String NO_PERMISSION_MESSAGE = "§cSorry, but you do not have the required permission to execute this command.";

	/**
	 * The instance of the handler the method belongs to.
	 */
	private CommandHandler instance;
	/**
	 * The Java reflection method to invoke.
	 */
	private Method method;
	/**
	 * The meta-data of the command the method is serving.
	 */
	private CommandProperties meta;

	/**
	 * Constructs a new CommandMethod with the given handler, Java method, and command meta-data.
	 *
	 * @param instance The instance of the handler the method belongs to.
	 * @param method The Java reflection method to invoke.
	 * @param meta The meta-data of the command the method is serving.
	 */
	public CommandMethod(CommandHandler instance, Method method, CommandProperties meta) {
		this.instance = instance;
		this.method = method;
		this.meta = meta;
	}

	/**
	 * Gets the Java reflection method to be invoked.
	 *
	 * @return The Java reflection method.
	 */
	public Method getMethod() {
		return method;
	}

	/**
	 * Gets the meta-data about the command the method serves.
	 *
	 * @return The command meta-data the method serves.
	 */
	public CommandProperties getMeta() {
		return meta;
	}

	/**
	 * Invokes the underlying Java method with the CommandSender and
	 * provided array of arguments to be passed to the method.
	 *
	 * @param sender The sender of the command.
	 * @param args The additional arguments to be passed to the method.
	 * @throws Exception If there was an issue invoking the method.
	 */
	public void invoke(CommandInvoker sender, Object... args) throws Exception {
		// Check if they have permission for this command
		if (!getMeta().permission().isEmpty()) {
			if (!sender.hasPermission(getMeta().permission())) {
				sender.sendMessage(NO_PERMISSION_MESSAGE);
				return;
			}
		}

		// Construct the method arguments with the CommandSender prepended.
		Object[] methodArgs = new Object[args.length + 1];
		// Ensure the native invoker can be cast to the method parameter.
		if (!method.getParameterTypes()[0].isAssignableFrom(sender.getNativeInvoker().getClass())) {
			if (!sender.isPlayer()) {
				sender.sendMessage("§cThis command can only be executed by a player.");
			} else {
				sender.sendMessage("§cThis command can not be invoked by someone of your type (" + sender.getNativeInvoker().getClass().getSimpleName() + ")");
			}
			return;
		}

		methodArgs[0] = sender.getNativeInvoker();

		System.arraycopy(args, 0, methodArgs, 1, args.length);

		method.invoke(instance, methodArgs);
	}

	/**
	 * Validates that the Java reflection method is a CommandMethod.
	 *
	 * @param method The Java reflection method to check against.
	 * @return true if the method is a CommandMethod, false otherwise.
	 */
	public static boolean validate(Method method) {
		CommandProperties meta = method.getAnnotation(CommandProperties.class);
		if (meta == null) return false;

		// Parameter check
		Class<?>[] parameterTypes = method.getParameterTypes();
		return parameterTypes.length > 0;
	}

	@Override
	public String toString() {
		return "CommandMethod{method=" + method.getName() + ",meta=" + meta.toString() + "}";
	}

}
