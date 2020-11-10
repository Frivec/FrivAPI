package fr.frivec.spigot.commands;

public class CommandManager {
	
	/**
	 * Register a command in the command map of the API.
	 * Create a class extending AbstractCommand, then the plugin will register the command by it self
	 * @param command: The AbstractCommand you want to register
	 */
	public void registerCommand(final AbstractCommand command) {
		
		command.register();
		
	}

}
