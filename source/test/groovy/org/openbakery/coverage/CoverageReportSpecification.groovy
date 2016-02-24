package org.openbakery.coverage

import spock.lang.Specification

/**
 * Created by rene on 23.02.16.
 */
class CoverageReportSpecification extends Specification {


	def "main help"() {
		when:
		def args = ["--help"] as String[]
		CoverageReport coverageReport = new CoverageReport(args)

		then:
		coverageReport.options.h
	}

	def "main help short"() {
		when:
		def args = ["-h"] as String[]
		CoverageReport coverageReport = new CoverageReport(args)

		then:
		coverageReport.options.h
	}

	def "params"() {
		when:
		def args = []
		args << "--profdata"
		args << "profData"
		args << "--binary"
		args << "binary"
		args << "--include"
		args << "*"

		CoverageReport coverageReport = new CoverageReport(args as String[])
		coverageReport.run()

		then:
		coverageReport.profData == "profData"
		coverageReport.binary == "binary"
		coverageReport.include == "*"
	}


	def "params 2"() {
		when:
		def args = []
		args << "--profdata"
		args << "1"
		args << "--binary"
		args << "2"
		args << "--include"
		args << "3"

		CoverageReport coverageReport = new CoverageReport(args as String[])
		coverageReport.run()

		then:
		coverageReport.profData == "1"
		coverageReport.binary == "2"
		coverageReport.include == "3"
	}
}
