package com.simplexservers.minecraft.commandutils;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The meta-data of the command that the method serves.
 *
 * @author Zach Abney
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CommandProperties {

	/**
	 * The command that the method serves.
	 *
	 * @return The full command the method serves.
	 */
	String command();

	/**
	 * The aliases for the main command the method should also serve.
	 *
	 * @return The aliases for the main command.
	 */
	String[] aliases() default {};

	/**
	 * A brief description of the command.
	 *
	 * @return A description of the command.
	 */
	String description();

	/**
	 * The valid usage of the command.
	 *
	 * @return The valid usage of the command.
	 */
	String usage() default "/<command>";

	/**
	 * The required permission to be able to execute the command.
	 *
	 * @return The required permission for the command.
	 */
	String permission() default "";

	/**
	 * The allowed environments that the command can be invoked from.
	 * <p>
	 * Note: This is a Bit Mask with flags from EnvironmentFlags.
	 * </p>
	 *
	 * @return The Bit Mask for the allowed environments of the command.
	 */
	int assertEnvironment() default EnvironmentFlags.ALL;

}