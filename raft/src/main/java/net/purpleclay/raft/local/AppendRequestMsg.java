/*
 * Copyright (c) 2013-2014, Seth Proctor. All rights reserved.
 *
 * This software is distributed under the BSD license. See the terms of the
 * license in the documentation provided with this software.
 */

package net.purpleclay.raft.local;

import net.purpleclay.raft.Command;
import net.purpleclay.raft.EncodedObject;


/** Request to append a command (or issue a heartbeat). */
class AppendRequestMsg extends AbstractMessage {

	/** Stable identifier for this message type. */
	static final String IDENTIFIER = "AppendRequest";

	/** standard serialization version identifier */
	public static final long serialVersionUID = 1L;

	// the previous log entry details
	private final long prevLogIndex;
	private final long prevLogTerm;

	// the entries in this message, which may be empty
	private final Command [] entries;

	// the commit index at the sender (which must be the leader)
	private final long leaderCommit;
	
	private static final String PREV_LOG_INDEX_KEY = "PrevLogIndex";
	private static final String PREV_LOG_TERM_KEY = "PrevLogTerm";
	private static final String LEADER_COMMIT_KEY = "LeaderCommit";

	/**
	 * Creates an instance of {@code AppendRequestMsg} with no entries, which
	 * is used as a heartbeat message.
	 * 
	 * @param senderId the sender's identifier
	 * @param term the sender's current term
	 * @param prevLogIndex the index of the previous log entry
	 * @param prevLogTerm the term of the previous log entry
	 * @param leaderCommit the current commit index
	 */
	AppendRequestMsg(long senderId, long term, long prevLogIndex,
					 long prevLogTerm, long leaderCommit)
	{
		this(senderId, term, prevLogIndex, prevLogTerm, null, leaderCommit);
	}

	/**
	 * Creates an instance of {@code AppendRequestMsg} with entries.
	 * 
	 * @param senderId the sender's identifier
	 * @param term the sender's current term
	 * @param prevLogIndex the index of the previous log entry
	 * @param prevLogTerm the term of the previous log entry
	 * @param entries the log entries to append
	 * @param leaderCommit the current commit index
	 */
	AppendRequestMsg(long senderId, long term, long prevLogIndex,
					 long prevLogTerm, Command [] entries,
					 long leaderCommit)
	{
		super(senderId, term, IDENTIFIER);

		this.prevLogIndex = prevLogIndex;
		this.prevLogTerm = prevLogTerm;
		this.leaderCommit = leaderCommit;

		this.entries = entries != null ? entries : new Command[0];
	}
	
	AppendRequestMsg(EncodedObject enc) {
		super(enc, IDENTIFIER);
		this.prevLogIndex = enc.getLongAttribute(PREV_LOG_INDEX_KEY);
		this.prevLogTerm = enc.getLongAttribute(PREV_LOG_TERM_KEY);
		this.leaderCommit = enc.getLongAttribute(LEADER_COMMIT_KEY);

		this.entries = enc.getCommands();
	}
	

	/**
	 * Returns the previous log index.
	 * 
	 * @return the previous log index
	 */
	long getPrevLogIndex() {
		return prevLogIndex;
	}

	/**
	 * Returns the previous log term.
	 * 
	 * @return the previous log term
	 */
	long getPrevLogTerm() {
		return prevLogTerm;
	}

	/**
	 * Returns the ordered set of entries in this message, which may be empty.
	 * 
	 * @return the ordered entries to append
	 */
	Command [] getEntries() {
		return entries;
	}

	/**
	 * Returns the current commit index.
	 * 
	 * @return the current commit index
	 */
	long getLeaderCommit() {
		return leaderCommit;
	}
	
	@Override public void encode(EncodedObject enc) {
		super.encodeBase(enc);
		enc.addAttribute(PREV_LOG_INDEX_KEY, getPrevLogIndex());
		enc.addAttribute(PREV_LOG_TERM_KEY, getPrevLogTerm());
		enc.addAttribute(LEADER_COMMIT_KEY, getLeaderCommit());
		enc.addCommands(getEntries());
		
	}

	@Override public String toString() {
		return String.format("%s prevLogIndex=[%d] prevLogTerm=[%d] entries=[%s] leaderCommit=[%d]", 
				super.toString(), getPrevLogIndex(), getPrevLogTerm(), getEntries(), getLeaderCommit());
	}

}
