package net.purpleclay.raft.encoding;

import net.purpleclay.raft.Command;
import net.purpleclay.raft.Message;

public interface Encoder {
	public Command decodeCommand(EncodedObject enc);
	public void encodeCommand(EncodedObject enc, Command command);
	
	public Message decodeMessage(EncodedObject enc);
	public void enocdeMessage(EncodedObject enc, Message msg);
}
