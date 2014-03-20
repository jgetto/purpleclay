package net.purpleclay.raft.util;

public class Preconditions {
	// TODO: Consolidate with com.nuodb.impl.util version
	
	public static void checkArgument(boolean expression, Object errorMessage) {
		if (!expression) {
			throw new IllegalArgumentException(String.valueOf(errorMessage));
		}
	}

	public static void checkArgument(boolean expression,
			String errorMessageTemplate, Object... errorMessageArgs) {
		if (!expression) {
			throw new IllegalArgumentException(String.format(String.valueOf(errorMessageTemplate), 
					errorMessageArgs));
		}
	}
}
