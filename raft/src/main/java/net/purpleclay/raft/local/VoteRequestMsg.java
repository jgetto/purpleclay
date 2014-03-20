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


/** Request to vote for a new leader. */
class VoteRequestMsg extends AbstractMessage {

	private static final String LAST_LOG_TERM_KEY = "LastLogTerm";

	private static final String LAST_LOG_INDEX_KEY = "LastLogIndex";

	private static final String CANDIDATE_ID_KEY = "CandidateId";

	/** Stable identifier for this message type. */
	static final String IDENTIFIER = "VoteRequest";

	/** standard serialization version identifier */
	public static final long serialVersionUID = 1L;

	// the sender's last log entry index and term
	private final long lastLogIndex;
	private final long lastLogTerm;

	private static final Map<String,MessageEncoder<VoteRequestMsg>> messageEncoderMap = 
			new HashMap<String,MessageEncoder<VoteRequestMsg>>();
	static {
		messageEncoderMap.put(IDENTIFIER, new VoteRequestEncoder());
	}
	
	/**
	 * Creates an instance of {@code VoteRequestMsg}.
	 * 
	 * @param senderId the sender's unique identifier
	 * @param term the sender's current term
	 * @param lastLogIndex the sender's last log entry index
	 * @param lastLogTerm the sender's last log entry term
	 */
	VoteRequestMsg(long senderId, long term, long lastLogIndex, long lastLogTerm) {
		super(senderId, term, IDENTIFIER);

		this.lastLogIndex = lastLogIndex;
		this.lastLogTerm = lastLogTerm;
	}
	
	/**
	 * Returns the server identifier for the candidate requesting a vote.
	 * 
	 * @return the candidate's unique identifier
	 */
	long getCandidateId() {
		return getSenderId();
	}

	/**
	 * Returns the candidate's last log entry index.
	 * 
	 * @return the candidate's last log entry index
	 */
	long getLastLogIndex() {
		return lastLogIndex;
	}

	/**
	 * Returns the candidate's last log entry term.
	 * 
	 * @return the candidate's last log entry term
	 */
	long getLastLogTerm() {
		return lastLogTerm;
	}
	
	@Override public String toString() {
		return String.format("%s candidateId=[%d] lastLogIndex=[%d] lastLogTerm=[%d]", 
				super.toString(), getCandidateId(), getLastLogIndex(), getLastLogTerm());
	}
	
	private static class VoteRequestEncoder implements MessageEncoder<VoteRequestMsg> {

		@Override
		public VoteRequestMsg decode(Encoder encoder, EncodedObject encObj) {
			long senderId = AbstractMessage.extractSenderId(encObj);
			long term = AbstractMessage.extractTerm(encObj);
			long lastLogIndex = encObj.getLongAttribute(LAST_LOG_INDEX_KEY);
			long lastLogTerm = encObj.getLongAttribute(LAST_LOG_TERM_KEY);
			return new VoteRequestMsg(senderId, term, lastLogIndex, lastLogTerm);
		}

		@Override
		public void encode(Encoder encoder, EncodedObject encObj, VoteRequestMsg message) {
			AbstractMessage.encodeBase(encObj, message);
			encObj.addAttribute(CANDIDATE_ID_KEY, message.getCandidateId());
			encObj.addAttribute(LAST_LOG_INDEX_KEY, message.getLastLogIndex());
			encObj.addAttribute(LAST_LOG_TERM_KEY, message.getLastLogTerm());
		}
		
	}
	
	public static Map<String,MessageEncoder<VoteRequestMsg>> getMessageEncoderMap() {
		return Collections.unmodifiableMap(messageEncoderMap);
	}
	
	@Override
	public void encode(Encoder encoder, EncodedObject encObj) {
		messageEncoderMap.get(IDENTIFIER).encode(encoder, encObj, this);
	}

}
