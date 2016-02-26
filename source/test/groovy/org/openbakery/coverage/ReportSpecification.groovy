package org.openbakery.coverage

import org.apache.commons.io.FileUtils
import org.openbakery.coverage.command.CommandRunner
import org.openbakery.coverage.report.HTMLReport
import org.openbakery.coverage.report.ReportData
import org.openbakery.coverage.model.SourceFile
import org.openbakery.coverage.report.XMLReport
import spock.lang.Specification

/**
 * Created by René Pirringer
 */
class ReportSpecification extends Specification {


	CommandRunner commandRunner = Mock(CommandRunner)
	Report report

	File tmp

	def setup() {
		report = new Report()
		report.commandRunner = commandRunner

		tmp = new File(System.getProperty("java.io.tmpdir"), "coverage")
	}

	def tearDown() {
		FileUtils.deleteDirectory(tmp)
	}

	File createFile(String filePath) {
		File file = new File(tmp, filePath)
		FileUtils.writeStringToFile(file, "dummy")
		return file;
	}

	def "profdata not specified"() {

		when:
		//report.profileData = 'build/Demo/Coverage.profdata'
		report.create("dummy")

		then:
		IllegalArgumentException exception = thrown()
		exception.message == "No profileData specified"

	}

	def "profdata not found"() {
		when:
		report.profileData = 'build/Demo/Coverage.profdata'
		report.create("dummy")

		then:
		IllegalArgumentException exception = thrown()
		exception.message.startsWith("profileData file not found")
	}

	def "no binary specified"() {
		when:
		report.profileData = createFile('build/Demo/Coverage.profdata')
		report.create("dummy")

		then:
		IllegalArgumentException exception = thrown()
		exception.message == "No binary specified"

	}

	def "binary not found"() {
		when:
		report.profileData = createFile('build/Demo/Coverage.profdata')
		report.binary = "build/Demo/Demo"
		report.create("dummy")

		then:
		IllegalArgumentException exception = thrown()
		exception.message.startsWith("Binary not found")
	}


	def "source not found"() {
		when:
		report.profileData = createFile('build/Demo/Coverage.profdata')
		report.binary = createFile("build/Demo/Demo")
		report.create("Source/test.m")

		then:
		IllegalArgumentException exception = thrown()
		exception.message.startsWith("Source file not found")
	}

	def "report for file with source file"() {
		def commandList
		def expectedCommandList

		when:
		report.profileData = createFile('build/Demo/Coverage.profdata')
		report.binary = createFile('build/Demo/Demo')
		File source = createFile("Source/test.m")
		report.create(source.getPath())

		then:
		1 * commandRunner.runWithResult(_, _) >> { arguments -> commandList = arguments[0] }

		interaction {
			expectedCommandList = [
										"xcrun",
										"llvm-cov",
										"show",
										"-instr-profile=" + tmp.absolutePath + "/build/Demo/Coverage.profdata",
										tmp.absolutePath + "/build/Demo/Demo",
										tmp.absolutePath + "/Source/test.m"]
		}
		commandList == expectedCommandList

	}

	def "report for file"() {
		def commandList
		def expectedCommandList

		when:
		report.profileData = createFile('build/Demo/Coverage.profdata')
		report.binary = createFile('build/Demo/Demo')
		report.create()

		then:
		1 * commandRunner.runWithResult(_, _) >> { arguments -> commandList = arguments[0] }

		interaction {
			expectedCommandList = [
										"xcrun",
										"llvm-cov",
										"show",
										"-instr-profile=" + tmp.absolutePath + "/build/Demo/Coverage.profdata",
										tmp.absolutePath + "/build/Demo/Demo"]
		}
		commandList == expectedCommandList

	}

	def "report for test files"() {
		def commandList
		def expectedCommandList
		def currentDirectory = new File("")

		when:
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'
		report.create()

		then:
		1 * commandRunner.runWithResult(_, _) >> { arguments -> commandList = arguments[0] }

		interaction {
			expectedCommandList = [
										"xcrun",
										"llvm-cov",
										"show",
										"-instr-profile=" + currentDirectory.absolutePath + "/source/test/resource/Coverage.profdata",
										currentDirectory.absolutePath + "/source/test/resource/Demo"]
		}
		commandList == expectedCommandList

	}



	def "report for profdata"() {
		given:
		report.commandRunner = new CommandRunner();
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'

		when:
		report.create()

		then:
		report.sourceFiles.size == 26;
	}

	def "report for profdata last source file"() {
		given:
		report.commandRunner = new CommandRunner();
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'

		when:
		report.create()
		SourceFile sourceFile = report.sourceFiles.last()

		then:
		sourceFile != null;
		sourceFile.filename.endsWith("OBPropertyInjector.m")
		sourceFile.sourceLines.size() == 175
	}


	def "report for profdata for OBTableViewSection"() {
		given:
		report.commandRunner = new CommandRunner();
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'

		when:
		report.create()
		SourceFile sourceFile = report.sourceFiles.get(19)

		then:
		sourceFile != null;
		sourceFile.filename.endsWith("OBTableViewSection.m")
		sourceFile.sourceLines.size() == 91
		sourceFile.methods.size() == 7
	}

	def "report data from report"() {
		given:
		report.commandRunner = new CommandRunner();
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'

		when:
		report.create()
		ReportData reportData = report.getReportData()

		then:
		reportData != null
		reportData.sourcePackages.size() == 12
	}

	def "default destination path"() {
		expect:
		report.destinationPath == new File("coverage")
	}

	def "default report is txt"() {
		given:
		report.destinationPath = new File(tmp, "coverage")
		report.commandRunner = new CommandRunner();
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'

		when:
		report.create()

		then:
		report.destinationPath.exists()
		new File(report.destinationPath, "coverage.txt").exists()
	}


	def "html report"() {

		given:
		report.type = Report.Type.HTML

		when:
		def report = report.getReportGenerator()

		then:
		report instanceof HTMLReport
	}

	def "xml report"() {

		given:
		report.type = Report.Type.XML

		when:
		def report = report.getReportGenerator()

		then:
		report instanceof XMLReport
	}

	def "command runner"() {
		when:
		report = new Report()

		then:
		report.commandRunner instanceof CommandRunner
	}

	def "default base directory"() {
		when:
		report = new Report()

		then:
		report.baseDirectory.endsWith("/")
	}


	def "include Core/Source"() {
		given:
		report.destinationPath = new File(tmp, "coverage")
		report.commandRunner = new CommandRunner();
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'
		report.include = "Core/Source/.*"
		report.baseDirectory = "/Users/rene/workspace/openbakery/OBTableViewController"

		when:
		report.create()

		then:
		report.reportData.sourceFiles.size() == 13
		report.reportData.sourceFiles[0].filename.startsWith("Core/Source")
		report.reportData.sourceFiles[12].filename.startsWith("Core/Source")
	}

	def "include Core/Source multiple"() {
		given:
		report.destinationPath = new File(tmp, "coverage")
		report.commandRunner = new CommandRunner();
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'
		report.include = "Core/Source/.*|Demo/Source.*"
		report.baseDirectory = "/Users/rene/workspace/openbakery/OBTableViewController"

		when:
		report.create()

		then:
		report.reportData.sourceFiles.size() == 17
		report.reportData.sourceFiles[0].filename.startsWith("Core/Source")
		report.reportData.sourceFiles[12].filename.startsWith("Core/Source")
		report.reportData.sourceFiles[16].filename.startsWith("Demo/Source")
	}

	def "exclude Core/Source multiple"() {
		given:
		report.destinationPath = new File(tmp, "coverage")
		report.commandRunner = new CommandRunner();
		report.profileData = 'source/test/resource/Coverage.profdata'
		report.binary = 'source/test/resource/Demo'
		report.exclude = "/Applications/.*"
		report.baseDirectory = "/Users/rene/workspace/openbakery/OBTableViewController"

		when:
		report.create()

		then:
		report.reportData.sourceFiles.size() == 19
		report.reportData.sourceFiles[0].filename.startsWith("Core/Source")
		report.reportData.sourceFiles[12].filename.startsWith("Core/Source")
		report.reportData.sourceFiles[16].filename.startsWith("Demo/Source")
	}

}
