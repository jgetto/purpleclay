package net.purpleclay.raft.encoding;

import net.purpleclay.raft.Message;

public interface MessageEncoder<T extends Message> {
	T decode(Encoder encoder, EncodedObject encObj);
	void encode(Encoder encoder, EncodedObject encObj, T message);
}
