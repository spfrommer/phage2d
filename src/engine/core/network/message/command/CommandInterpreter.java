package engine.core.network.message.command;

import java.util.ArrayList;

import utils.collections.TwoWayHashMap;
import engine.core.network.message.MessageDeclaration;

public class CommandInterpreter {
	private TwoWayHashMap<String, Integer> commandIDs;
	private static TwoWayHashMap<String, Integer> defaultIDs;

	static {
		defaultIDs = new TwoWayHashMap<String, Integer>();

		defaultIDs.put("socketshutdown", 0);

		defaultIDs.put("testmessage", 1);

		defaultIDs.put("setclientid", 2);

		defaultIDs.put("addentity", 3);

		defaultIDs.put("removeentity", 4);

		defaultIDs.put("update", 5);

		defaultIDs.put("endtransmission", 6);
	}

	{
		commandIDs = new TwoWayHashMap<String, Integer>();
	}

	public CommandInterpreter(ArrayList<MessageDeclaration> declarations) {
		commandIDs.putAll(defaultIDs);

		// additionals
		for (int i = 0; i < declarations.size(); i++) {
			MessageDeclaration declaration = declarations.get(i);
			commandIDs.put(declaration.getCommand(), i + defaultIDs.getKeys().size());
			// commandParameters.add(declaration.getCommand(), declaration.getNumParameters());
		}
	}

	/*public int getParameters(int id) {
		return getParameters(getCommand(id));
	}

	public int getParameters(String command) {
		return commandParameters.getForward(command);
	}*/

	public int getID(String command) {
		return commandIDs.getForward(command);
	}

	public String getCommand(int i) {
		return commandIDs.getBackward(i);
	}

}
