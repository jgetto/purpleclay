package net.purpleclay.raft.encoding;

import net.purpleclay.raft.Command;

public interface CommandEncoder {
	Command decode(EncodedObject enc);
	void encode(EncodedObject enc, Command command);
}
