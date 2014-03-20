package net.purpleclay.raft;

public interface CommandEncoder {
	Command decode(EncodedObject enc);
	void encode(EncodedObject enc, Command command);
}
