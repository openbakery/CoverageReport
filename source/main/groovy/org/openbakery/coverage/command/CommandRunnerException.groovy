package org.openbakery.coverage.command

/**
 * Created by René Pirringer
 * The CommandRunnerException is thrown by the CommandRunner if something went wrong
 */
class CommandRunnerException extends IllegalStateException {

	CommandRunnerException() {
		super()
	}

	CommandRunnerException(String s) {
		super(s)
	}

	CommandRunnerException(String s, Throwable throwable) {
		super(s, throwable)
	}

	CommandRunnerException(Throwable throwable) {
		super(throwable)
	}

}
