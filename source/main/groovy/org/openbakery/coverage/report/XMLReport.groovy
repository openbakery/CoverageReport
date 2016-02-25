package org.openbakery.coverage.report

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory

/**
 * Created by rene on 25.02.16.
 */
class XMLReport {

	void generate(ReportData reportData, File destinationDirectory) {
		InputStream inputStream = TextReport.class.getResourceAsStream("/XMLReport.template")
		Reader reader = new InputStreamReader(inputStream);

		File destinationFile = new File(destinationDirectory, "cobertura.xml")
		Writer writer = new FileWriter(destinationFile);

		MustacheFactory mustacheFactory = new DefaultMustacheFactory()
		Mustache mustache = mustacheFactory.compile(reader, "template")

		mustache.execute(writer, reportData.data)
	  writer.flush()
		reader.close()
	}
}
