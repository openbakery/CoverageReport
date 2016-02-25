package org.openbakery.coverage.report

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory

/**
 * Created by René Pirringer
 */
class HTMLReport {

	String bootstrap = 'https://github.com/twbs/bootstrap/releases/download/v3.3.6/bootstrap-3.3.6-dist.zip'

	void generate(ReportData reportData, File destinationDirectory) {

		downloadBootstrap(destinationDirectory)

		InputStream inputStream = TextReport.class.getResourceAsStream("/HTMLReport.html")
		Reader reader = new InputStreamReader(inputStream);

		File destinationFile = new File(destinationDirectory, "index.html")
		Writer writer = new FileWriter(destinationFile);

		MustacheFactory mustacheFactory = new DefaultMustacheFactory()
		Mustache mustache = mustacheFactory.compile(reader, "template")

		mustache.execute(writer, reportData.data)
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
