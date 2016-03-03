package org.openbakery.coverage

import org.openbakery.coverage.command.CommandRunner
import org.openbakery.coverage.report.HTMLReport
import org.openbakery.coverage.report.ReportData
import org.openbakery.coverage.model.SourceFile
import org.openbakery.coverage.report.TextReport
import org.openbakery.coverage.report.XMLReport
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by René Pirringer
 */
class Report implements OutputAppender {
	private static Logger logger = LoggerFactory.getLogger(Report.class)


	static enum Type {
		Text("text"),
		HTML("html"),
		XML("xml")

		String value;

		Type(String value) {
			this.value = value;
		}

		static Type typeFromString(String string) {
			if (string == null) {
				return Text;
			}
			for (Type type in Type.values()) {
				if (string.toLowerCase().startsWith(type.value.toLowerCase())) {
					return type;
				}
			}
			return Text;
		}
	}


	CommandRunner commandRunner = new CommandRunner()
	File profileData
	File binary
	List<SourceFile> sourceFiles = []
	List<String> coverageLines = []
	String baseDirectory
	File destinationPath
	Type type
	String include
	String exclude
	String title

	Report() {
		setBaseDirectory(new File("").absolutePath)
		destinationPath = new File("coverage")
	}

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
		logger.debug("create Report with: {}", this)

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
			throw new IllegalArgumentException("Binary not found: " + binary)
		}


		File sourceFile = null;
		if (source != null) {
			sourceFile = new File(source)
			if (!sourceFile.exists()) {
				throw new IllegalArgumentException("Source file not found: " + sourceFile)
			}
		}


		createCoverageData(sourceFile)

		logger.debug("createReport")
		createReport()
	}

	void createCoverageData(File sourceFile) {
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
		logger.debug("command: {}", command)
		commandRunner.runWithResult(command, this)
	}

	def getReportGenerator() {
		switch (type) {
			case Type.HTML:
				return new HTMLReport()
			case Type.XML:
				return new XMLReport()
			default:
				break;
		}
		return new TextReport()
	}

	void createReport() {
		if (reportData == null) {
			println "No Report data found, so nothing to generate"
			return
		}
		destinationPath.mkdirs()
		getReportGenerator().generate(reportData, destinationPath)
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
	}

	def getSourceFiles() {
		return sourceFiles
	}

	List<SourceFile>getFilteredSourceFiles() {
		List<SourceFile> result = sourceFiles;

		if (include != null) {
			result = result.findAll {
				it.filename.matches(include)
			}
		}
		if (exclude != null) {
			result = result.findAll {
				!it.filename.matches(exclude)
			}
		}
		return result
	}

	def getReportData() {
		if (sourceFiles != null) {
			return new ReportData(getFilteredSourceFiles())
		}
		return null
	}

	@Override
	public String toString() {
		return "Report{" +
						"profileData=" + profileData + "\n" +
						", binary=" + binary + "\n" +
						", baseDirectory='" + baseDirectory + '\'' + "\n" +
						", destinationPath=" + destinationPath + "\n" +
						", type=" + type + "\n" +
						", include='" + include + '\'' + "\n" +
						", exclude='" + exclude + '\'' + "\n" +
						'}';
	}


}
