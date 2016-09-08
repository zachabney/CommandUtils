package com.simplexservers.minecraft.commandutils;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility for simplified usage of environment flags.
 *
 * @author Zach Abney
 */
public class EnvironmentFlags {

	/**
	 * Represents the LOCAL environment. Usually for a feature being tested locally.
	 */
	public static final int LOCAL = 1;
	/**
	 * Represents the DEV environment. Usually for testing the Develop branch where features work together.
	 */
	public static final int DEV = 2;
	/**
	 * Represents the RELEASE environment. Usually a release candidate that contains no more features, only bug fixes.
	 */
	public static final int RELEASE = 4;
	/**
	 * Represents the PROD environment. This is the live environment that end-users access.
	 */
	public static final int PROD = 8;
	/**
	 * Represents all the environments. Used to represent all the environments.
	 */
	public static final int ALL = 15;

	/**
	 * The fixed key for the environment flag. Used when setting the environment when launching the application.
	 */
	public static final String ENVIRONMENT_KEY = "net.prospectmc.environment";
	/**
	 * The current environment type represented as a flag. Defaults to PROD.
	 */
	public static final int CURRENT_FLAG;

	/**
	 * Sets the CURRENT_FLAG based on command-line parameter and environment variable. Defaults the flag to PROD.
	 */
	static {
		int environmentFlag = EnvironmentFlags.PROD;

		// Check for command-line property first.
		String flagValue = System.getProperty(ENVIRONMENT_KEY);
		if (flagValue == null) {
			flagValue = System.getenv(ENVIRONMENT_KEY);
		}

		// Check for system environment variable if a command-line property isn't provided.
		if (flagValue != null) {
			try {
				environmentFlag = EnvironmentFlags.parseFlag(flagValue);
			} catch (IllegalArgumentException e) {
				Logger.getGlobal().log(Level.WARNING, "Invalid environment flag. Using 'PROD' as default.", e);
			}
		}

		CURRENT_FLAG = environmentFlag;
	}

	/**
	 * Checks if the flag being checked for is in the list of flags represented by a bit mask.
	 *
	 * @param flagToCheckFor The specific flag to check for in the list.
	 * @param flagList The list of flags represented as a bit mask.
	 * @return true if the flag is contained in the list, false otherwise.
	 */
	public static boolean hasFlag(int flagToCheckFor, int flagList) {
		return (flagList & flagToCheckFor) == flagToCheckFor;
	}

	/**
	 * Parses the given input to an environment flag type.
	 *
	 * @param input The input to parse to an environment type.
	 * @return The environment represented as a bit flag.
	 * @throws IllegalArgumentException If the input does not represent a legal environment type.
	 */
	public static int parseFlag(String input) throws IllegalArgumentException {
		switch (input.toUpperCase()) {
			case "LOCAL":
			case "FEATURE":
				return LOCAL;
			case "DEV":
			case "DEVELOP":
			case "DEVELOPMENT":
				return DEV;
			case "RELEASE":
			case "REL":
				return RELEASE;
			case "PROD":
			case "PRODUCTION":
			case "LIVE":
			case "MASTER":
				return PROD;
			case "ALL":
				return ALL;
			default:
				throw new IllegalArgumentException("Invalid environment type.");
		}
	}

}
