package org.openbakery.coverage.report

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Created by René Pirringer
 */
class XMLReport {
	private static Logger logger = LoggerFactory.getLogger(XMLReport.class)

	void generate(ReportData reportData, File destinationDirectory) {
		InputStream inputStream = TextReport.class.getResourceAsStream("/XMLReport.xml")
		Reader reader = new InputStreamReader(inputStream);

		File destinationFile = new File(destinationDirectory, "cobertura.xml")
		logger.debug("create destinationFile report {}", destinationFile)

		Writer writer = new FileWriter(destinationFile);

		MustacheFactory mustacheFactory = new DefaultMustacheFactory()
		Mustache mustache = mustacheFactory.compile(reader, "template")

		mustache.execute(writer, reportData.data)
	  writer.flush()
		reader.close()
	}
}
