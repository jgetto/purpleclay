/*
 * Copyright (c) 2013-2014, Seth Proctor. All rights reserved.
 *
 * This software is distributed under the BSD license. See the terms of the
 * license in the documentation provided with this software.
 */

package net.purpleclay.raft;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.purpleclay.raft.util.Preconditions;


/** Testing class that provides a key-value {@code StateMachine}. */
public class KVStateMachine implements StateMachine {

	public static final String COMMAND_ID = KVCommand.class.getName();

	private final Map<String,String> kvMap =
		new ConcurrentHashMap<String,String>();

	@Override public void apply(Command command) {
		if (! (command instanceof KVCommand))
			throw new IllegalArgumentException("Unknown command type");

		KVCommand kvCmd = (KVCommand) command;
		kvMap.put(kvCmd.key, kvCmd.value);
	}

	public String getValue(String key) {
		return kvMap.get(key);
	}

	public static Command createCommand(String key, String value) {
		return new KVCommand(key, value);
	}
	
	public static Command decodeCommand(EncodedObject enc) {
		return new KVCommand(enc);
	}

	private static class KVCommand implements Command, Serializable {
		private static final long serialVersionUID = -6729316535780966854L;
		final String key;
		final String value;
		KVCommand(String key, String value) {
			this.key = key;
			this.value = value;
		}
		private KVCommand(EncodedObject enc) {
			Preconditions.checkArgument(COMMAND_ID.equals(enc.getIdentifier()), "Not a valid KVCommand");
			this.key = enc.getAttribute("Key");
			this.value = enc.getAttribute("Value");
		}
		@Override public String getIdentifier() {
			return COMMAND_ID;
		}
		
		@Override
		public void encode(EncodedObject enc) {
			enc.addIdentifier(getIdentifier());
			enc.addAttribute("Key", key);
			enc.addAttribute("Value", value);	
		}
		
		private static class KVCommandEncoder implements CommandEncoder {

			@Override
			public Command decode(EncodedObject enc) {
				return new KVCommand(enc);
			}

			@Override
			public void encode(EncodedObject enc, Command command) {
				command.encode(enc);
			}
			
		}
	}

}
