package org.openbakery.coverage.command

/**
 * Created by rene on 22.02.16.
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
