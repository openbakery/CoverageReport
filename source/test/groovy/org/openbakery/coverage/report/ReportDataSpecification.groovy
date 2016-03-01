package org.openbakery.coverage.report

import org.apache.commons.io.FileUtils
import org.openbakery.coverage.Report
import org.openbakery.coverage.command.CommandRunner
import spock.lang.Specification

import java.text.SimpleDateFormat

/**
 * Created by René Pirringer
 */
class ReportDataSpecification extends Specification {


	ReportData getReportData(String baseDirectory) {
		Report report = new Report()
		report.baseDirectory = baseDirectory
		List<String> data = FileUtils.readLines(new File("source/test/resource/Coverage.profdata.txt"));
		data.each {report.appendLine(it) }
		return report.getReportData()
	}

	def "GetSourceFiles"() {
		when:
		ReportData data = getReportData()

		then:
		data.sourceFiles.size() == 26
	}

	def "getPackages"() {
		when:
		ReportData data = getReportData(null)


		then:
		data.sourcePackages.size() == 12
		data.sourcePackages.values().collect { it.name } == [
						"/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator9.2.sdk/System/Library/Frameworks/Foundation.framework/Headers/",
						"/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator9.2.sdk/usr/include/",
						"/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator9.2.sdk/usr/include/dispatch/",
						"/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator9.2.sdk/usr/include/objc/",
						"/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator9.2.sdk/usr/include/sys/",
						"/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/../lib/clang/7.0.2/include/",
						"/Users/rene/workspace/openbakery/OBTableViewController/Core/Source/",
						"/Users/rene/workspace/openbakery/OBTableViewController/Core/Source/Binding/",
						"/Users/rene/workspace/openbakery/OBTableViewController/Core/Source/Model/",
						"/Users/rene/workspace/openbakery/OBTableViewController/Core/Source/OBReflection/",
						"/Users/rene/workspace/openbakery/OBTableViewController/Demo/Source/",
						"/Users/rene/workspace/openbakery/OBTableViewController/Pods/OBInjector/Core/Source/",
		]

	}


	def "get packages with base directory"() {
		when:
		ReportData data = getReportData("/Users/rene/workspace/openbakery/OBTableViewController")


		then:
		data.sourcePackages.size() == 12
		data.sourcePackages.values().collect { it.name } == [
						"/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator9.2.sdk/System/Library/Frameworks/Foundation.framework/Headers/",
						"/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator9.2.sdk/usr/include/",
						"/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator9.2.sdk/usr/include/dispatch/",
						"/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator9.2.sdk/usr/include/objc/",
						"/Applications/Xcode.app/Contents/Developer/Platforms/iPhoneSimulator.platform/Developer/SDKs/iPhoneSimulator9.2.sdk/usr/include/sys/",
						"/Applications/Xcode.app/Contents/Developer/Toolchains/XcodeDefault.xctoolchain/usr/bin/../lib/clang/7.0.2/include/",
						"Core/Source/",
						"Core/Source/Binding/",
						"Core/Source/Model/",
						"Core/Source/OBReflection/",
						"Demo/Source/",
						"Pods/OBInjector/Core/Source/",
		]

	}


	def "get total data"() {
		when:
		ReportData data = getReportData("/Users/rene/workspace/openbakery/OBTableViewController")
		SimpleDateFormat dateFormat = new SimpleDateFormat();


		then:
		data.data.totalLinesNumber == 4155
		data.data.totalCoverageInPercent == "58.5"
		data.data.totalCoverageRate.toString() == "Ok"
		data.data.currentDate.toString() == dateFormat.format(new Date())
	}
}
