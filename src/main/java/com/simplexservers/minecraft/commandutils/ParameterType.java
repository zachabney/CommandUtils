package com.simplexservers.minecraft.commandutils;

import java.util.HashMap;

/**
 * The support parameter types and their class types.
 *
 * @author Zach Abney
 */
public enum ParameterType {
	STRING(String.class),
	INTEGER(int.class),
	FLOAT(float.class),
	DOUBLE(double.class),
	BOOLEAN(boolean.class),
	USER_DEFINED(Object.class);

	/**
	 * The map of user defined parameter types and their corresponding ParameterTypeParser.
	 */
	private static final HashMap<Class<?>, ParameterTypeParser<?>> userDefinedParameterTypes = new HashMap<>();

	/**
	 * The class the ParameterType represents.
	 */
	private final Class<?> clazz;

	/**
	 * Constructs a new ParameterType that represents the given class.
	 *
	 * @param clazz The class the ParameterType represents.
	 */
	ParameterType(Class<?> clazz) {
		this.clazz = clazz;
	}

	/**
	 * Gets the ParameterType that represents the provided class or null if one doesn't exist.
	 * If the given class is a user defined parameter type it will return ParameterType.USER_DEFINED.
	 *
	 * @param clazz The class that the ParameterType represents.
	 * @return The ParameterType representing the class or null if one doesn't exist.
	 */
	public static ParameterType getParameterType(Class<?> clazz) {
		for (ParameterType parameterType : values()) {
			if (parameterType.clazz == clazz) {
				return parameterType;
			}
		}

		if (userDefinedParameterTypes.containsKey(clazz)) {
			return USER_DEFINED;
		}

		return null;
	}

	/**
	 * Registers a custom user defined parameter type and it's corresponding parser.
	 *
	 * @param type The parameter type to register.
	 * @param parser The ParameterTypeParser used to convert from user input to the parameter type.
	 * @param <T> The parameter type.
	 */
	public static <T> void registerParameterType(Class<T> type, ParameterTypeParser<T> parser) {
		userDefinedParameterTypes.put(type, parser);
	}

	/**
	 * Gets the parser used for converting user input to the user defined parameter type.
	 *
	 * @param type The parameter type to get the parser for.
	 * @param <T> The parameter type.
	 * @return The ParameterTypeParser for the user defined parameter type.
	 */
	@SuppressWarnings("unchecked")
	public static <T> ParameterTypeParser<T> getUserDefinedParameterParser(Class<T> type) {
		return (ParameterTypeParser<T>) userDefinedParameterTypes.get(type);
	}

	/**
	 * Attempts to parse the given argument entered by the CommandSender into the
	 * given class type.
	 *
	 * @param arg The argument entered by the CommandSender.
	 * @param clazz The class the argument should be in the form of.
	 * @return The parsed argument.
	 * @throws ArgumentParseException If there is a format issue parsing the argument to the class type.
	 * @throws IllegalArgumentException If the required class type is not supported.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T parseArgument(String arg, Class<T> clazz) throws ArgumentParseException, IllegalArgumentException {
		String parseError = "Can't parse argument '" + arg + "' to " + clazz.getName();
		ParameterType parameterType = ParameterType.getParameterType(clazz);

		if (parameterType != null) {
			switch (parameterType) {
				case STRING:
					return (T) arg;
				case INTEGER:
					try {
						return (T) (Integer) Integer.parseInt(arg);
					} catch (NumberFormatException e) {
						throw new ArgumentParseException(parseError, "'" + arg + "' must be a whole number.");
					}
				case FLOAT:
					try {
						return (T) (Float) Float.parseFloat(arg);
					} catch (NumberFormatException e) {
						throw new ArgumentParseException(parseError, "'" + arg + "' must be a number. Ex: 3.14");
					}
				case DOUBLE:
					try {
						return (T) (Double) Double.parseDouble(arg);
					} catch (NumberFormatException e) {
						throw new ArgumentParseException(parseError, "'" + arg + "' must be a number. Ex: 3.14");
					}
				case BOOLEAN:
					switch (arg.toLowerCase()) {
						case "on":
						case "true":
						case "yes":
							return (T) (Boolean) true;
						case "off":
						case "false":
						case "no":
							return (T) (Boolean) false;
						default:
							throw new ArgumentParseException(parseError, "'" + arg + "' must be true or false.");
					}
				case USER_DEFINED:
					ParameterTypeParser<T> parser = ParameterType.getUserDefinedParameterParser(clazz);
					if (parser != null) {
						return parser.parse(arg);
					}
			}
		}

		throw new IllegalArgumentException("Unsupported argument parameterType " + clazz.getName() + ".");
	}

}