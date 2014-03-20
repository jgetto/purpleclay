package net.purpleclay.raft;

public interface Encodable {
	/**
	 * The identifier for this {@code Encodable}.
	 *
	 * @return a {@code String} identifying this {@code Encodable}
	 */
	String getIdentifier();
	
	void encode(EncodedObject enc);
}
