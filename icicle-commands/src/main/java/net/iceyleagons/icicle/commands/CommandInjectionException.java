package net.iceyleagons.icicle.commands;

/**
 * @since Nov. 1, 2021
 */
public class CommandInjectionException extends Exception {

    /**
     * @param command the command name
     * @param cause   the cause of the exception
     */
    public CommandInjectionException(String command, String cause) {
        super("The command named " + command + " can not be injected! Cause: " + cause);
    }

    /**
     * @param command the command
     * @param cause   the cause of the exception
     */
    public CommandInjectionException(String command, Throwable cause) {
        super("The command named " + command + " can not be injected! Cause: ", cause);
    }
}