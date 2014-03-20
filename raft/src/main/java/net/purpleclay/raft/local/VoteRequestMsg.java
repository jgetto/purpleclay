/*
 * Copyright (c) 2013-2014, Seth Proctor. All rights reserved.
 *
 * This software is distributed under the BSD license. See the terms of the
 * license in the documentation provided with this software.
 */

package net.purpleclay.raft.local;

import net.purpleclay.raft.EncodedObject;


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
	
	VoteRequestMsg(EncodedObject enc) {
		super(enc, IDENTIFIER);
		this.lastLogIndex = enc.getLongAttribute(LAST_LOG_INDEX_KEY);
		this.lastLogTerm = enc.getLongAttribute(LAST_LOG_TERM_KEY);
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
	
	@Override public void encode(EncodedObject enc) {
		super.encodeBase(enc);
		enc.addAttribute(CANDIDATE_ID_KEY, getCandidateId());
		enc.addAttribute(LAST_LOG_INDEX_KEY, getLastLogIndex());
		enc.addAttribute(LAST_LOG_TERM_KEY, getLastLogTerm());
	}

	@Override public String toString() {
		return String.format("%s candidateId=[%d] lastLogIndex=[%d] lastLogTerm=[%d]", 
				super.toString(), getCandidateId(), getLastLogIndex(), getLastLogTerm());
	}

}
