package com.simplexservers.minecraft.commandutils;

/**
 * Exception thrown when an error occurs parsing an argument.
 *
 * @author Zach Abney
 */
public class ArgumentParseException extends Exception {

	private static final long serialVersionUID = -8759158437989761137L;
	/**
	 * The message to display to the player.
	 */
	private String displayMessage;

	/**
	 * Constructs a new ArgumentParseException with the given console message
	 * and message to display to the player.
	 *
	 * @param consoleMessage The message to show on the console.
	 * @param displayMessage The message to show to the player.
	 */
	public ArgumentParseException(String consoleMessage, String displayMessage) {
		super(consoleMessage);
		this.displayMessage = displayMessage;
	}

	/**
	 * Gets the display message to show to the player.
	 *
	 * @return The display message.
	 */
	public String getDisplayMessage() {
		return displayMessage;
	}

}
