package org.openbakery.coverage

/**
 * Created by rene on 22.02.16.
 */
class CoverageReport {

	def options
	def commandLine
	String profData
	String binary
	String include

	CoverageReport(String[] args) {
		commandLine = new CliBuilder(usage: 'coverageReport [options]')
		commandLine.with {
			h longOpt: 'help', 'Show usage information'
			p longOpt: 'profdata', args: 1, argName:'profdata', 'Profile model file'
			b longOpt: 'binary', args: 1, argName:'binary', 'binary file'
			i longOpt: 'include', args: 1, argName:'include', 'include file pattern'
		}
		options = commandLine.parse(args)
	}


	void run() {
		if  (options.help) {
			commandLine.usage()
			return
		}


		if (options.profdata && options.binary && options.include ) {
			profData = options.p
			binary = options.b
			include = options.i
			return;
		}
		commandLine.usage()
	}



	static void main(String[] args) {
		new CoverageReport(args).run()
	}

}
