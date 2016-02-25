package org.openbakery.coverage

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger
import org.slf4j.LoggerFactory;

/**
 * Created by René Pirringer
 */
class CoverageReport {

	def options
	def commandLine
	Report report

	CoverageReport(String[] args) {
		commandLine = new CliBuilder(usage: 'CoverageReport [options]')
		commandLine.with {
			h longOpt: 'help', 'Show usage information'
			p longOpt: 'profdata', args: 1, argName:'profdata', 'Profile model file'
			b longOpt: 'binary', args: 1, argName:'binary', 'binary file'
			i longOpt: 'include', args: 1, argName:'include', 'include file pattern'
			t longOpt: 'type', args: 1, argName:'type', 'report type (text, html, xml)'
			o longOpt: 'output', args: 1, argName:'output', 'output directory for the generated reports'
			d longOpt: 'debug', 'enable debug log'

		}
		options = commandLine.parse(args)
	}

	void processOptions() {
		report = new Report()
		if (options.type) {
			report.type = Report.Type.typeFromString(options.t)
		}

		if (options.include) {
			report.include = options.i
		}

		if (options.output) {
			report.destinationPath = new File(options.o)
		}

		if (options.profdata) {

			report.profileData = options.p
		}
		if (options.binary) {
			report.binary = options.b
		}

		if (options.debug) {
			setLoggingLevel(Level.DEBUG)
		}



	}

	void setLoggingLevel(Level level) {
		Logger root = (Logger)LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
		root.setLevel(level);
	}

	void run() {
		if  (options.help) {
			commandLine.usage()
			return
		}

		if (options.profdata && options.binary ) {
			report.create()
			return;
		}
		commandLine.usage()
	}



	static void main(String[] args) {
		CoverageReport coverageReport = new CoverageReport(args)
		coverageReport.processOptions()
		coverageReport.run()
	}

}
