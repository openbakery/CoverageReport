package org.openbakery.coverage.report

import org.apache.commons.io.FileUtils
import org.openbakery.coverage.model.SourceFile
import spock.lang.Specification

/**
 * Created by René Pirringer
 */
class HTMLReportSpecification extends Specification {

	HTMLReport htmlReport
	File tmp

	def setup() {
		htmlReport = new HTMLReport()
		tmp = new File(System.getProperty("java.io.tmpdir"), "coverage")
		tmp.mkdirs()
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
/*
	def "generated report exists"() {
		given:
		htmlReport.bootstrap = null
		ReportData data = getReportData()

		when:
		htmlReport.generate(data, tmp)

		then:
		new File(tmp, "index.html").exists()
	}


	def "bootstrap download"() {
		given:
		ReportData data = getReportData()

		when:
		htmlReport.generate(data, tmp)

		then:
		new File(tmp, "bootstrap/css/bootstrap.css").exists()
	}


	XmlParser getXmlParser() {
		def factory = javax.xml.parsers.SAXParserFactory.newInstance()
		factory.validating = false
		return new XmlParser(factory.newSAXParser().getXMLReader());
	}

	def "test html report"() {
		given:
		htmlReport.bootstrap = null
		ReportData data = getReportData()

		when:
		htmlReport.generate(data, tmp)
		File xmlFile = new File(tmp, "index.html")
		def parser= getXmlParser()
		def html =  parser.parse(xmlFile)

		then:
		html.body.div.table[0].tbody.tr.size() == 2
		html.body.div.table[0].tbody.tr[0].td[0].value()[0] == "All Files"
		html.body.div.table[0].tbody.tr[0].td[2].value()[0] == "91"
		html.body.div.table[0].tbody.tr[0].td[3].value()[0] == "59"
		html.body.div.table[0].tbody.tr[0].td[4].value()[0] == "20"


		html.body.div.table[1].tbody.tr.size() == 1
		html.body.div.table[1].tbody.tr[0].td[0].a[0].value()[0] == "Core/Source/OBTableViewSection.m"
		html.body.div.table[1].tbody.tr[0].td[2].value()[0] == "91"
		html.body.div.table[1].tbody.tr[0].td[3].value()[0] == "59"
		html.body.div.table[1].tbody.tr[0].td[4].value()[0] == "20"
	}

	def "test html report title"() {
		given:
		htmlReport.bootstrap = null
		ReportData data = getReportData()
		data.title = "My Project"

		when:
		htmlReport.generate(data, tmp)
		File xmlFile = new File(tmp, "index.html")
		def parser= getXmlParser()
		def html =  parser.parse(xmlFile)

		then:

		html.body.div[0].div[0].h1[0].value()[0] == "My Project"
	}

	def "test html report default title"() {
		given:
		htmlReport.bootstrap = null
		ReportData data = getReportData()

		when:
		htmlReport.generate(data, tmp)
		File xmlFile = new File(tmp, "index.html")
		def parser= getXmlParser()
		def html =  parser.parse(xmlFile)

		then:

		html.body.div[0].div[0].h1[0].value()[0] == "Coverage Report"
	}


	def "generated report exists: OBTableViewSection "() {
		given:
		htmlReport.bootstrap = null
		ReportData data = getReportData()

		when:
		htmlReport.generate(data, tmp)

		then:
		new File(tmp, "Core_Source_OBTableViewSection.html").exists()
	}

	def "generated report contents: OBTableViewSection "() {
		given:
		htmlReport.bootstrap = null
		ReportData data = getReportData()

		when:
		htmlReport.generate(data, tmp)
		File xmlFile = new File(tmp, "Core_Source_OBTableViewSection.html")
		def parser= getXmlParser()
		def html =  parser.parse(xmlFile)

		then:
		html.body.div.table[1].tbody.tr.size() == 91
		html.body.div.table[1].tbody.tr[0].td[0].div[0].value()[0] == null
		html.body.div.table[1].tbody.tr[0].td[0].attributes()["class"] == "hits"
		html.body.div.table[1].tbody.tr[21].td[0].div[0].attributes()["class"] == "covered"
		html.body.div.table[1].tbody.tr[50].td[0].div[0].attributes()["class"] == "missing"
	}
  */
}
