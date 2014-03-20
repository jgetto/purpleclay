/*
 * Copyright (c) 2013-2014, Seth Proctor. All rights reserved.
 *
 * This software is distributed under the BSD license. See the terms of the
 * license in the documentation provided with this software.
 */

package net.purpleclay.raft.local;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.purpleclay.raft.encoding.EncodedObject;
import net.purpleclay.raft.encoding.Encoder;
import net.purpleclay.raft.encoding.MessageEncoder;


/** Response to a {@code CommandRequestMsg}. */
class CommandResponseMsg extends AbstractMessage {

	/** Stable identifier for this message type. */
	static final String IDENTIFIER = "CommandResponse";

	/** standard serialization version identifier */
	public static final long serialVersionUID = 1L;

	// the identifier from the associated request
	private final long requestId;

	// the index of the appended entry, or FAILED_APPEND
	private final long entryIndex;

	// constant for an unsuccessful append request
	private static final long FAILED_APPEND = -1L;
	
	private static final String REQUEST_ID_KEY = "RequestId";
	private static final String ENTRY_INDEX_KEY = "EntryIndex";
	
	private static final Map<String,MessageEncoder<CommandResponseMsg>> messageEncoderMap = 
			new HashMap<String,MessageEncoder<CommandResponseMsg>>();
	static {
		messageEncoderMap.put(IDENTIFIER, new CommandResponseEncoder());
	}
	
	/**
	 * Creates an instance of {@code CommandResponseMsg} for failed requests.
	 * 
	 * @param senderId the sender's unique identifier
	 * @param term the sender's term
	 * @param requestId the associated request identifier
	 */
	CommandResponseMsg(long senderId, long term, long requestId) {
		this(senderId, term, requestId, FAILED_APPEND);
	}

	/**
	 * Creates an instance of {@code CommandResponseMsg} for successful requests.
	 * 
	 * @param senderId the sender's unique identifier
	 * @param term the sender's term
	 * @param requestId the associated request identifier
	 * @param entryIndex the index where the command was appended
	 */
	CommandResponseMsg(long senderId, long term, long requestId, long entryIndex) {
		super(senderId, term, IDENTIFIER);

		this.requestId = requestId;
		this.entryIndex = entryIndex;
	}

	/**
	 * Returns the identifier for the associated request.
	 *
	 * @return the identifier for the associated request
	 */
	long getRequestId() {
		return requestId;
	}

	/**
	 * Returns whether or not the append request was successful.
	 * 
	 * @return {@code true} if the associated append request succeeded
	 *         {@code false} otherwise
	 */
	boolean commandAccepted() {
		return entryIndex != FAILED_APPEND;
	}

	/**
	 * Returns the index where the command was appended.
	 * 
	 * @return the index where the command was appended
	 * 
	 * @throws IllegalStateException if the request failed
	 */
	long getEntryIndex() {
		if (entryIndex == FAILED_APPEND)
			throw new IllegalStateException("no index was assigned");
		return entryIndex;
	}

	@Override public String toString() {
		return String.format("%s requestId=[%d] commandAccepted=[%b] entryIndex=[%d]", 
				super.toString(), getRequestId(), commandAccepted(), getEntryIndex());
	}

	private static class CommandResponseEncoder implements MessageEncoder<CommandResponseMsg> {

		@Override
		public CommandResponseMsg decode(Encoder encoder, EncodedObject encObj) {
			long senderId = AbstractMessage.extractSenderId(encObj);
			long term = AbstractMessage.extractTerm(encObj);
			
			long requestId = encObj.getLongAttribute(REQUEST_ID_KEY);
			long entryIndex = encObj.getLongAttribute(ENTRY_INDEX_KEY);
			
			return new CommandResponseMsg(senderId, term, requestId, entryIndex);
		}

		@Override
		public void encode(Encoder encoder, EncodedObject encObj, CommandResponseMsg message) {
			AbstractMessage.encodeBase(encObj, message);
			encObj.addAttribute(REQUEST_ID_KEY, message.getRequestId());
			encObj.addAttribute(ENTRY_INDEX_KEY, message.getEntryIndex());
		}
		
	}
	
	public static Map<String,MessageEncoder<CommandResponseMsg>> getMessageEncoderMap() {
		return Collections.unmodifiableMap(messageEncoderMap);
	}
	
	@Override
	public void encode(Encoder encoder, EncodedObject encObj) {
		messageEncoderMap.get(IDENTIFIER).encode(encoder, encObj, this);
	}
}
