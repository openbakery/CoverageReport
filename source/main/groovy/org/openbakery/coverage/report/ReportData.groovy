package org.openbakery.coverage.report

import org.apache.commons.io.FilenameUtils
import org.openbakery.coverage.model.SourceFile
import org.openbakery.coverage.model.SourcePackage

/**
 * Created by René Pirringer
 */
class ReportData {

	//List<SourceFile> sourceFiles

	Map<String, SourcePackage> sourcePackages
	String baseDirectory

	public ReportData(List<SourceFile>sourceFiles) {
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
						sourceFiles: sourceFileData,
						totalLines:  "${SourceFile.getTotalLines(sourceFiles)}".padLeft(8),
						totalExecuted: "${SourceFile.getLinesExecuted(sourceFiles)}".padLeft(8),
						totalCoverage: "${(SourceFile.getCoverage(sourceFiles)*100).intValue()}%".padLeft(8),
						totalMissing: "${SourceFile.getLinesNotCovered(sourceFiles)}".padLeft(8)
		]
		return data
	}

	def getData() {
		return [
						sourceFiles     : this.sourceFiles,
						sourcePackages  : this.sourcePackages.values(),
						totalBrancheRate: "0.0",
						totalLines      : SourceFile.getTotalLines(this.sourceFiles),
						totalExecuted   : SourceFile.getLinesExecuted(this.sourceFiles),
						totalCoverage   : SourceFile.getCoverage(this.sourceFiles),
						totalMissing    : SourceFile.getLinesNotCovered(this.sourceFiles),
						currentTime     : System.currentTimeSeconds()
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
