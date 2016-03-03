package org.openbakery.coverage.report

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
import org.apache.commons.io.FilenameUtils
import org.openbakery.coverage.model.SourceFile
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by René Pirringer
 * Class that generates the html report
 */
class HTMLReport {
	private static Logger logger = LoggerFactory.getLogger(HTMLReport.class)

	String bootstrap = 'https://github.com/twbs/bootstrap/releases/download/v3.3.6/bootstrap-3.3.6-dist.zip'

	/**
	 * Generates the HTML report using the specified report data and stores it into the specified destinationDirectory
	 * @param reportData
	 * @param destinationDirectory where the generated report should be stored
	 */
	void generate(ReportData reportData, File destinationDirectory) {

		downloadBootstrap(destinationDirectory)


		generateOverall(reportData, destinationDirectory)

		reportData.sourceFiles.each {
			generateReportForSourceFile(it, destinationDirectory)
		}

	}

	void generateOverall(ReportData reportData, File destinationDirectory) {
		InputStream inputStream = TextReport.class.getResourceAsStream("/HTMLReportOverall.html")
		Reader reader = new InputStreamReader(inputStream);

		File destinationFile = new File(destinationDirectory, "index.html")
		logger.debug("create html report {}", destinationFile)
		Writer writer = new FileWriter(destinationFile);

		MustacheFactory mustacheFactory = new DefaultMustacheFactory()
		Mustache mustache = mustacheFactory.compile(reader, "template")

		mustache.execute(writer, reportData.data)
	  writer.flush()
		reader.close()

	}


	void generateReportForSourceFile(SourceFile sourceFile, File destinationDirectory) {
		InputStream inputStream = TextReport.class.getResourceAsStream("/HTMLReportSourceFile.html")
		Reader reader = new InputStreamReader(inputStream);
		File destinationFile = new File(destinationDirectory, sourceFile.getFileBasename() + ".html")
		logger.debug("create html report {}", destinationFile)
		Writer writer = new FileWriter(destinationFile);

		MustacheFactory mustacheFactory = new DefaultMustacheFactory()
		Mustache mustache = mustacheFactory.compile(reader, "template")

		mustache.execute(writer, sourceFile)
	  writer.flush()
		reader.close()
	}


	void downloadBootstrap(File destinationDirectory) {
		if (bootstrap == null) {
			return
		}
		def ant = new AntBuilder()

		File destinationFile = new File(destinationDirectory, bootstrap.tokenize("/")[-1])

		ant.get(src: bootstrap, dest: destinationFile, verbose:true)
		ant.unzip(src: destinationFile, dest: destinationDirectory)

		File bootstrapDirectory = new File(destinationFile.toString() - ".zip")
		bootstrapDirectory.renameTo(new File(bootstrapDirectory.parentFile, "bootstrap"))

		destinationFile.delete()


	}

}
