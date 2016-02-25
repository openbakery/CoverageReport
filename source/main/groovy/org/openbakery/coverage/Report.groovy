package org.openbakery.coverage

import org.openbakery.coverage.command.CommandRunner
import org.openbakery.coverage.report.ReportData
import org.openbakery.coverage.model.SourceFile

/**
 * Created by rene on 22.02.16.
 */
class Report implements OutputAppender {
	CommandRunner commandRunner
	File profileData
	File binary
	List<SourceFile> sourceFiles = []
	List<String> coverageLines = []
	String baseDirectory



	void setBaseDirectory(String baseDirectory) {
		this.baseDirectory = baseDirectory
		if (this.baseDirectory != null && !this.baseDirectory.endsWith("/")) {
			this.baseDirectory += "/"
		}
	}



	boolean isOSX() {
	    String osName = System.getProperty("os.name");
	    return osName.contains("OS X");
	}

	def create() {
		create(null)
	}

	def create(String source) {

		if (profileData == null) {
			throw new IllegalArgumentException("No profileData specified")
		}

		if (!profileData.exists()) {
			throw new IllegalArgumentException("profileData file not found: " + profileData)
		}

		if (binary == null) {
			throw new IllegalArgumentException("No binary specified")
		}

		if (!binary.exists()) {
			throw new IllegalArgumentException("Binary not found: " + profileData)
		}


		File sourceFile = null;
		if (source != null) {
			sourceFile = new File(source)
			if (!sourceFile.exists()) {
				throw new IllegalArgumentException("Source file not found: " + sourceFile)
			}
		}

		def command = []

		if (isOSX()) {
			command << "xcrun"
		}
		command << "llvm-cov"
		command << "show"
		command << "-instr-profile=" + profileData.absolutePath
		command << binary.absolutePath
		if (sourceFile != null) {
			command << sourceFile.absolutePath
		}

		commandRunner.runWithResult(command, this)
	}


	void setProfileData(def profileData) {
		if (profileData instanceof File) {
			this.profileData = profileData
		} else {
			this.profileData = new File(profileData.toString())
		}
	}

	void setBinary(def binary) {
		if (binary instanceof File) {
			this.binary = binary
		} else {
			this.binary = new File(binary.toString())
		}
	}

	@Override
	void appendLine(String line) {

		if (line.equals("")) {
			sourceFiles << new SourceFile(coverageLines, baseDirectory)
			coverageLines = []
			return;
		}

		if (line.startsWith("warning: ")) {
			// skip warnings
			return;
		}

		coverageLines << line;

		/*if (line.equals("")) {
			sourceFiles << currentSourceFile
			currentSourceFile = new SourceFile()
		}*/

	}

	def getSourceFiles() {
		return sourceFiles
	}

	def getReportData() {
		return new ReportData(sourceFiles)
	}
}
