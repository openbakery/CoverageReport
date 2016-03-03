package org.openbakery.coverage.report

import org.apache.commons.io.FilenameUtils
import org.openbakery.coverage.model.SourceFile
import org.openbakery.coverage.model.SourcePackage

import java.text.SimpleDateFormat

/**
 * Created by René Pirringer
 */
class ReportData {

	//List<SourceFile> sourceFiles

	Map<String, SourcePackage> sourcePackages
	String baseDirectory
	String title

	public ReportData(List<SourceFile>sourceFiles) {
		this(sourceFiles, null)
	}
	public ReportData(List<SourceFile>sourceFiles, String title) {
		if (title != null) {
			this.title = title
		} else {
			this.title = "Coverage Report"
		}

		sourcePackages = new TreeMap<>()
		sourceFiles.each {
			String packageName = getPackageName(it.filename)
			SourcePackage sourcePackage = sourcePackages.get(packageName)
			if (sourcePackage == null) {
				sourcePackage = new SourcePackage(packageName, [])
				sourcePackages.put(packageName, sourcePackage)
			}
			sourcePackage.sourceFiles << it
		}
	}

	String getPackageName(String filename) {
		String packageName = FilenameUtils.getFullPath(filename)
		if (this.baseDirectory) {
			return packageName - this.baseDirectory
		}
		return packageName
	}


	def getDataTruncated() {
		def sourceFileData = []

		sourceFiles.each {

			String filename = it.filename
			if (filename.length() > 40) {
				filename = "..." + filename[-37..-1]
			}

			sourceFileData << [
				filename: filename,
				lines:  "${it.sourceLines.size()}".padLeft(8),
				exec: "${it.linesExecuted}".padLeft(8),
				cover: "${(it.coverage*100).intValue()}%".padLeft(8),
				missing: "${it.linesNotCovered}".padLeft(8)
			]
		}

		def data = [
						title               : this.title,
						sourceFiles         : sourceFileData,
						totalLinesNumber    : "${SourceFile.getTotalLines(sourceFiles)}".padLeft(8),
						totalLinesExecuted  : "${SourceFile.getLinesExecuted(sourceFiles)}".padLeft(8),
						totalCoverage       : "${(SourceFile.getCoverage(sourceFiles) * 100).intValue()}%".padLeft(8),
						totalLinesNotCovered: "${SourceFile.getLinesNotCovered(sourceFiles)}".padLeft(8)
		]
		return data
	}

	def getData() {
		SimpleDateFormat dateFormat = new SimpleDateFormat()

		return [
						title                 : this.title,
						sourceFiles           : this.sourceFiles,
						sourcePackages        : this.sourcePackages.values(),
						totalBrancheRate      : "0.0",
						totalLinesNumber      : SourceFile.getTotalLines(this.sourceFiles),
						totalLinesExecuted    : SourceFile.getLinesExecuted(this.sourceFiles),
						totalCoverage         : SourceFile.getCoverage(this.sourceFiles),
						totalCoverageInPercent: "${((int) (SourceFile.getCoverage(this.sourceFiles) * 1000)) / 10}",
						totalLinesNotCovered  : SourceFile.getLinesNotCovered(this.sourceFiles),
						totalCoverageRate     : SourceFile.getCoverageRate(SourceFile.getCoverage(this.sourceFiles)),
						currentTime           : (long) (System.currentTimeMillis() / 1000),
						currentDate           : dateFormat.format(new Date())
		]
	}

	List<SourceFile> getSourceFiles() {
		List<SourceFile> result = []
		this.sourcePackages.values().each {
			result.addAll(it.sourceFiles)
		}
		return result
	}


}
