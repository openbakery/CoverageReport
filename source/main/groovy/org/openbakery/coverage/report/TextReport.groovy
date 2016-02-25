package org.openbakery.coverage.report

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by René Pirringer
 */
class TextReport {
	private static Logger logger = LoggerFactory.getLogger(TextReport.class)

	void generate(ReportData reportData, File destinationDirectory) {

		InputStream inputStream = TextReport.class.getResourceAsStream("/TextReport.txt")
		Reader reader = new InputStreamReader(inputStream);

		File destinationFile = new File(destinationDirectory, "coverage.txt")
		logger.debug("create text report {}", destinationFile)
		Writer writer = new FileWriter(destinationFile);

		MustacheFactory mustacheFactory = new DefaultMustacheFactory()
		Mustache mustache = mustacheFactory.compile(reader, "template")

		mustache.execute(writer, reportData.dataTruncated)
	  writer.flush()
		reader.close()

	}
}
