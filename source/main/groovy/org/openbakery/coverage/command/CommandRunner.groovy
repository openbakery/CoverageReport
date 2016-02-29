package org.openbakery.coverage.command

import org.openbakery.coverage.OutputAppender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.apache.commons.collections.buffer.CircularFifoBuffer

/**
 * The CommandRunner is a helper class to execute commands and get the result
 */
class CommandRunner {

	private static Logger logger = LoggerFactory.getLogger(CommandRunner.class)

	String defaultBaseDirectory = "."

	Thread readerThread


	public CommandRunner() {

	}



	private def commandListToString(List<String> commandList) {
		def result = ""
		commandList.each {
			item -> result += item + " "
		}
		return "'" + result.trim() + "'"
	}

	def run(String directory, List<String> commandList, OutputAppender outputAppender) {

		logger.debug("Run command: {}", commandListToString(commandList))

		def commandsAsStrings = commandList.collect { it.toString() } // GStrings don't play well with ProcessBuilder
		def processBuilder = new ProcessBuilder(commandsAsStrings)
		processBuilder.redirectErrorStream(true)
		processBuilder.directory(new File(directory))

		def process = processBuilder.start()

		def lastLines = processInputStream(process.inputStream, outputAppender)


		process.waitFor()
		readerThread.join()
		if (process.exitValue() > 0) {
			logger.debug("Exit Code: {}", process.exitValue())
			throw new CommandRunnerException("Command failed to run (exit code " + process.exitValue() + "): " + commandListToString(commandList) + "\nTail of output:\n" + lastLines)
		}

	}

	String processInputStream(InputStream inputStream, OutputAppender outputAppender) {
		Collection<String> commandOutputBuffer = new CircularFifoBuffer(20); ;

		Runnable runnable = new Runnable() {
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))

			public void run() {

				boolean running = true
				while (running) {
					String line = reader.readLine()
					if (line == null) {
						running = false
					} else {
						//logger.debug(line)
						outputAppender.appendLine(line)
						commandOutputBuffer.add(line)
					}
				}
				reader.close()
			}
		}

		readerThread = new Thread(runnable)
		readerThread.start()
		return commandOutputBuffer.toArray().join("\n")
	}

/*
	def run(String directory, List<String> commandList) {
		run(directory, commandList, null, null)
	}

	def run(List<String> commandList) {
		run(defaultBaseDirectory, commandList, null, null)
	}

	def run(String... commandList) {
		run(Arrays.asList(commandList))
	}

	def run(List<String> commandList, Map<String, String> environment) {
		run(defaultBaseDirectory, commandList, environment, null)
	}
	*/

	String runWithResult(String... commandList) {
		return runWithResult(Arrays.asList(commandList))
	}

	void runWithResult(List<String> commandList, OutputAppender appender) {
		runWithResult(defaultBaseDirectory, commandList, appender)
	}

	String runWithResult(String directory, List<String> commandList, OutputAppender appender) {
		run(directory, commandList, appender)
	}



}