package net.purpleclay.raft.encoding;

public interface Encodable {
	/**
	 * The identifier for this {@code Encodable}.
	 *
	 * @return a {@code String} identifying this {@code Encodable}
	 */
	String getIdentifier();
	
	// TODO: REMOVE after changing messages
	//
}
