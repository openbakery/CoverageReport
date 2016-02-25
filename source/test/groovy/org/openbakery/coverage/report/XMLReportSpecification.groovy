package org.openbakery.coverage.report

import org.apache.commons.io.FileUtils
import org.openbakery.coverage.Report
import org.openbakery.coverage.command.CommandRunner
import org.openbakery.coverage.model.SourceFile
import spock.lang.Specification

/**
 * Created by René Pirringer
 */
class XMLReportSpecification extends Specification {

	XMLReport xmlReport
	File tmp

	def setup() {
		xmlReport = new XMLReport()
		tmp = new File(System.getProperty("java.io.tmpdir"), "coverage")

	}

	def tearDown() {
		FileUtils.deleteDirectory(tmp)
	}

	SourceFile getSourceFile() {
		File dataFile = new File("source/test/resource/", "OBTableViewSection.txt")
		return new SourceFile(FileUtils.readLines(dataFile), "/Users/rene/workspace/openbakery/OBTableViewController/")
	}

	ReportData getReportData() {
		List<SourceFile> sourceFiles = []
		sourceFiles << getSourceFile();
		return new ReportData(sourceFiles)
	}


	def "generated report exists"() {
		given:
		ReportData data = getReportData()

		when:
		xmlReport.generate(data, tmp)

		then:
		new File(tmp, "cobertura.xml").exists()
	}

	XmlParser getXmlParser() {
	    def factory = javax.xml.parsers.SAXParserFactory.newInstance()
	    factory.validating = false
	    factory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
	    return new XmlParser(factory.newSAXParser().getXMLReader());
	}


	def "test xml report"() {
		given:
		ReportData data = getReportData()

		when:
		xmlReport.generate(data, tmp)
		File xmlFile = new File(tmp, "cobertura.xml")
		def parser= getXmlParser()
		def coverage =  parser.parse(xmlFile)

		then:
		coverage.attributes().size() == 4
		coverage.attributes()."branch-rate" == "0.0"
		coverage.attributes()."line-rate" == "0.6610169492"
		coverage.attributes().timestamp[0..-3] == System.currentTimeSeconds().toString()[0..-3]
		coverage.attributes().version == "CoverageReport-1.0"
	}

	def "test xml report packages"() {
		given:
		ReportData data = getReportData()

		when:
		xmlReport.generate(data, tmp)
		File xmlFile = new File(tmp, "cobertura.xml")
		def parser= getXmlParser()
		def coverage =  parser.parse(xmlFile)

		then:
		coverage.packages.size() == 1
		coverage.packages.package.size() == 1
		coverage.packages.package[0].attributes()."branch-rate" == "0.0"
		coverage.packages.package[0].attributes().complexity == "0.0"
		coverage.packages.package[0].attributes()."line-rate" == "0.6610169492"
		coverage.packages.package[0].attributes().name == "Core/Source/"
	}

	def "test xml report classes"() {
		given:
		ReportData data = getReportData()


		when:
		xmlReport.generate(data, tmp)
		File xmlFile = new File(tmp, "cobertura.xml")
		def parser= getXmlParser()
		def coverage =  parser.parse(xmlFile)

		then:
		coverage.packages.size() == 1
		coverage.packages.package.size() == 1
		coverage.packages.package.classes.class.size() == 1
		coverage.packages.package[0].classes.class[0].attributes()."branch-rate" == "0.0"
		coverage.packages.package[0].classes.class[0].attributes().filename == "Core/Source/OBTableViewSection.m"
		coverage.packages.package[0].classes.class[0].attributes()."line-rate" == "0.6610169492"
		coverage.packages.package[0].classes.class[0].attributes().name == "OBTableViewSection.m"
	}


	def "test xml report classes lines"() {
		given:
		ReportData data = getReportData()

		when:
		xmlReport.generate(data, tmp)
		File xmlFile = new File(tmp, "cobertura.xml")
		def parser= getXmlParser()
		def coverage =  parser.parse(xmlFile)

		then:
		coverage.packages.size() == 1
		coverage.packages.package.size() == 1
		coverage.packages.package.classes.class[0].lines.line.size() == 59
		coverage.packages.package.classes.class[0].lines.line[0].attributes().number == "21"
		coverage.packages.package.classes.class[0].lines.line[0].attributes().hits == "32"
		coverage.packages.package.classes.class[0].lines.line[58].attributes().number == "88"
		coverage.packages.package.classes.class[0].lines.line[58].attributes().hits == "0"
	}


	def "test xml long report"() {
		given:
		Report report = new Report()
		report.commandRunner = new CommandRunner();
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'
		report.create()

		ReportData data = report.getReportData()

		when:
		xmlReport.generate(data, tmp)
		File xmlFile = new File(tmp, "cobertura.xml")
		def parser = getXmlParser()
		def coverage = parser.parse(xmlFile)

		then:
		coverage.packages.size() == 1
		coverage.packages.package.size() == 12
		coverage.packages.package[0].classes.class.size() == 2
		coverage.packages.package[0].classes.class[0].lines.line.size() == 11
		coverage.packages.package[11].classes.class[1].lines.line.size() == 112
	}

}
