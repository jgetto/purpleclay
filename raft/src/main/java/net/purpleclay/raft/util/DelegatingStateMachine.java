/*
 * Copyright (c) 2013-2014, Seth Proctor. All rights reserved.
 *
 * This software is distributed under the BSD license. See the terms of the
 * license in the documentation provided with this software.
 */

package net.purpleclay.raft.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.purpleclay.raft.Command;
import net.purpleclay.raft.StateMachine;
import net.purpleclay.raft.encoding.CommandEncoder;


/**
 * Implementation of {@code StateMachine} that delegates to multiple different
 * machines based on command identifiers.
 */
public class DelegatingStateMachine implements StateMachine {

	// the mapping from supported identifiers to state machines
	private final ConcurrentHashMap<String,StateMachine> machines =
		new ConcurrentHashMap<String,StateMachine>();
	
	private final Map<String,CommandEncoder> commandMap = 
			new HashMap<String,CommandEncoder>();

	/* Implement StateMachine */

	@Override public void apply(Command command) {
		StateMachine machine = machines.get(command.getIdentifier());
		if (machine == null)
			throw new IllegalArgumentException("Unknown command: " +
											   command.getIdentifier());

		machine.apply(command);
	}

	/**
	 * Adds a delegated {@code StateMachine} for the given command identifier.
	 *
	 * @param machine the {@code StateMachine} to add
	 * @param commandIdentifier the {@code Command} identifier that this state
	 *                          machine accepts
	 *
	 * @throws IllegalArgumentException if this identifier is already accepted
	 *                                  by some other {@code StateMachine}
	 */
	public synchronized void addMachine(StateMachine machine, String commandIdentifier) {
		if (machines.putIfAbsent(commandIdentifier, machine) != null)
			throw new IllegalArgumentException("Identifier already registered");
		
		commandMap.putAll(machine.getCommandMapping());
	}

	@Override
	public Map<String, CommandEncoder> getCommandMapping() {
		return Collections.unmodifiableMap(commandMap);
	}

}
