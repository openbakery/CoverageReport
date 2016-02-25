package org.openbakery.coverage.model

import org.apache.commons.io.FilenameUtils

/**
 * Created by rene on 23.02.16.
 */
class SourceFile {

	String filename
	List<SourceLine> sourceLines
	List<Function> methods = []

	SourceFile(List<String> lines, String baseDirectory) {
		def currentMethod = null
		sourceLines = []

		lines.eachWithIndex { value, index ->

			if (index == 0) {
				filename = parseFilename(value, baseDirectory)
			} else {
				SourceLine sourceLine = new SourceLine(value)
				if (currentMethod == null && sourceLine.hits != SourceLine.NOT_A_NUMBER) {
					currentMethod = new Function()
					methods << currentMethod
				} else if (currentMethod != null && sourceLine.hits == SourceLine.NOT_A_NUMBER) {
					currentMethod = null;
				}
				if (currentMethod) {
					currentMethod.sourceLines << sourceLine
				}
				sourceLines << sourceLine
			}
		}

	}

	String parseFilename(String line, String baseDirectory) {
		if (line.endsWith(":")) {
			return line[0..-2]  - baseDirectory
		}
		return line - baseDirectory
	}

	List<SourceLine>getSourceLinesCovered() {
		return sourceLines.findAll { it.hits != SourceLine.NOT_A_NUMBER }
	}

	long getLinesCovered() {
		return sourceLines.count { it.hits > 0 }
	}

	long getLinesNotCovered() {
		return sourceLines.count { it.hits == 0 }
	}

	long getLinesExecuted() {
		return getLinesCovered() + getLinesNotCovered()
	}

	double getCoverage() {
		return SourceLine.getCoverage(sourceLines)
	}

	String getName() {
		return FilenameUtils.getName(filename)
	}

	static long getLinesCovered(List<SourceFile> sourceFiles) {
		return sourceFiles.sum { it.getLinesCovered()}
	}

	static long getLinesNotCovered(List<SourceFile> sourceFiles) {
		return sourceFiles.sum { it.getLinesNotCovered()}
	}

	static long getLinesExecuted(List<SourceFile> sourceFiles) {
		return sourceFiles.sum { it.getLinesExecuted()}
	}

	static long getTotalLines(List<SourceFile> sourceFiles) {
		return sourceFiles.sum {
			it.sourceLines.size()
		}
	}

	static double getCoverage(List<SourceFile> sourceFiles) {
		return getLinesCovered(sourceFiles) / getLinesExecuted(sourceFiles)
	}


	@Override
	public String toString() {
		return "SourceFile{" +
						"filename='" + filename + '\'' +
						'}';
	}
}
