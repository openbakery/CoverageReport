package org.openbakery.coverage.report

import org.apache.commons.io.FileUtils
import org.openbakery.coverage.Report
import org.openbakery.coverage.command.CommandRunner
import org.openbakery.coverage.report.ReportData
import org.openbakery.coverage.model.SourceFile
import org.openbakery.coverage.report.TextReport
import spock.lang.Specification

/**
 * Created by René Pirringer
 */
class TextReportSpecification extends Specification {

	TextReport textReport
	File tmp

	def setup() {
		textReport = new TextReport()
		tmp = new File(System.getProperty("java.io.tmpdir"), "coverage")

	}

	def tearDown() {
		FileUtils.deleteDirectory(tmp)
	}

	SourceFile getSourceFile() {
		File dataFile = new File("source/test/resource/", "OBTableViewSection.txt")
		return new SourceFile(FileUtils.readLines(dataFile), null)
	}


	def "generated report exists"() {
		given:
		List<SourceFile> sourceFiles = []
		sourceFiles << getSourceFile();
		ReportData data = new ReportData(sourceFiles)

		when:
		textReport.generate(data, tmp)

		then:
		new File(tmp, "coverage.txt").exists()
	}

	def "generated report has data"() {
		given:
		List<SourceFile> sourceFiles = []
		sourceFiles << getSourceFile();
		ReportData data = new ReportData(sourceFiles)

		when:
		textReport.generate(data, tmp)
		List reportData = FileUtils.readLines(new File(tmp, "coverage.txt"))

		then:
		reportData.size() == 9
		reportData.get(5) == '...ller/Core/Source/OBTableViewSection.m       91       59      66%       20'
		reportData.get(7) == 'TOTAL                                          91       59      66%       20'
	}


	def "generated report with many data"() {
		given:
		Report report = new Report()
		report.commandRunner = new CommandRunner();
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'
		report.create()

		when:
		textReport.generate(report.getReportData(), tmp)
		List reportData = FileUtils.readLines(new File(tmp, "coverage.txt"))

		then:
		reportData.size() == 34
		reportData.get(30) == '...ctor/Core/Source/OBPropertyInjector.m      175      112       0%      112'
		reportData.get(32) == 'TOTAL                                        4831     1329      58%      552'

	}

}
