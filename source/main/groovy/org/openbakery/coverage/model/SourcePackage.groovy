package org.openbakery.coverage.model

/**
 * Created by René Pirringer
 */
class SourcePackage {

	String name
	List<SourceFile> sourceFiles

	SourcePackage(String name, List<SourceFile> sourceFiles) {
		this.name = name
		this.sourceFiles = sourceFiles
	}


	@Override
	public String toString() {
		return "SourcePackage{" +
						"name='" + name + '\'' +
						'}';
	}


	String getBrancheRate() { return "0.0" }

	String getLines() { SourceFile.getTotalLines(this.sourceFiles) }

	String getExecuted() { SourceFile.getLinesExecuted(this.sourceFiles) }

	String getCoverage() { SourceFile.getCoverage(this.sourceFiles) }

	String getMissing() { SourceFile.getLinesNotCovered(this.sourceFiles) }


}
