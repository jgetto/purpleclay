package net.purpleclay.raft.encoding;

import net.purpleclay.raft.Command;


public interface EncodedObject {
	void addIdentifier(String id);
	void addAttribute(String key, String value);
	void addAttribute(String key, long value);
	void addAttribute(String key, boolean value);
	void addCommands(Encoder encoder, Command[] commands);
	
	String getIdentifier();
	String getAttribute(String key);
	long getLongAttribute(String key);
	boolean getBooleanAttribute(String key);
	Command[] getCommands(Encoder encoder);
}
