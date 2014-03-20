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


/** Response to a {@code VoteRequestMsg}. */
class VoteResponseMsg extends AbstractMessage {

	/** Stable identifier for this message type. */
	static final String IDENTIFIER = "VoteResponse";

	/** standard serialization version identifier */
	public static final long serialVersionUID = 1L;

	// the response to a request for a vote
	private final boolean response;
	
	private final static String RESPONSE_KEY = "Response";

	private static final Map<String,MessageEncoder<VoteResponseMsg>> messageEncoderMap = 
			new HashMap<String,MessageEncoder<VoteResponseMsg>>();
	static {
		messageEncoderMap.put(IDENTIFIER, new VoteResponseEncoder());
	}
	
	/**
	 * Creates an instance of {@code VoteResponseMsg}.
	 * 
	 * @param senderId the sender's unique identifier
	 * @param term the term for the vote
	 * @param response whether or not the sender votes for the candidate
	 */
	VoteResponseMsg(long senderId, long term, boolean response) {
		super(senderId, term, IDENTIFIER);

		this.response = response;
	}

	/**
	 * Returns whether the sender casts their vote for the requeting candidate
	 * in the given term.
	 * 
	 * @return {@code true} if a vote was granted {@code false} otherwise
	 */
	boolean getResponse() {
		return response;
	}

	@Override public String toString() {
		return String.format("%s voteGranted=[%b]", super.toString(), getResponse());
	}
	
	private static class VoteResponseEncoder implements MessageEncoder<VoteResponseMsg> {

		@Override
		public VoteResponseMsg decode(Encoder encoder, EncodedObject encObj) {
			long senderId = AbstractMessage.extractSenderId(encObj);
			long term = AbstractMessage.extractTerm(encObj);
			
			boolean response = encObj.getBooleanAttribute(RESPONSE_KEY);
			
			return new VoteResponseMsg(senderId, term, response);
		}

		@Override
		public void encode(Encoder encoder, EncodedObject encObj, VoteResponseMsg message) {
			AbstractMessage.encodeBase(encObj, message);
			encObj.addAttribute(RESPONSE_KEY, message.getResponse());
		}
		
	}
	
	public static Map<String,MessageEncoder<VoteResponseMsg>> getMessageEncoderMap() {
		return Collections.unmodifiableMap(messageEncoderMap);
	}
	
	@Override
	public void encode(Encoder encoder, EncodedObject encObj) {
		messageEncoderMap.get(IDENTIFIER).encode(encoder, encObj, this);
	}

}
