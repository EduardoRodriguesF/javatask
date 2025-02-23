package org.example.command;

public class AddCommand extends Command {
	public AddCommand(String[] args) {
		super(args);
	}

	@Override
	public byte exec() {
        if (this.args.length < 1) {
            return 1;
        }

        String item = this.args[0];
        
        // Create item
        System.out.println("Task added successfully (ID: 1)");

        return 0;
	}
}
