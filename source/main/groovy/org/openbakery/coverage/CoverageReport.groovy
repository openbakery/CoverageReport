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
			p longOpt: 'profdata', args: 1, argName:'profdata', 'Instrumentation-based profile file (mandatory)'
			b longOpt: 'binary', args: 1, argName:'binary', 'Binary file (mandatory)'
			i longOpt: 'include', args: 1, argName:'include', 'Files to include as regex pattern'
			e longOpt: 'exclude', args: 1, argName:'exclude', 'Files to exclude as regex pattern'
			t longOpt: 'type', args: 1, argName:'type', 'Report type. Possible values: text, html or xml'
			o longOpt: 'output', args: 1, argName:'output', 'Output directory for the generated reports'
			d longOpt: 'debug', 'Enable debug log'

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

		if (options.exclude) {
			report.exclude = options.e
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
		} else {
			setLoggingLevel(Level.WARN)
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
