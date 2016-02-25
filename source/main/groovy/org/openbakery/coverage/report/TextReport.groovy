package org.openbakery.coverage.report

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory

/**
 * Created by René Pirringer
 */
class TextReport {

	void generate(ReportData reportData, File destinationDirectory) {

		InputStream inputStream = TextReport.class.getResourceAsStream("/TextReport.txt")
		Reader reader = new InputStreamReader(inputStream);

		File destinationFile = new File(destinationDirectory, "coverage.txt")
		Writer writer = new FileWriter(destinationFile);

		MustacheFactory mustacheFactory = new DefaultMustacheFactory()
		Mustache mustache = mustacheFactory.compile(reader, "template")

		mustache.execute(writer, reportData.dataTruncated)
	  writer.flush()
		reader.close()

	}
}
