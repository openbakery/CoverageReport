package org.openbakery.coverage.report

import com.github.mustachejava.DefaultMustacheFactory
import com.github.mustachejava.Mustache
import com.github.mustachejava.MustacheFactory
import org.openbakery.coverage.SourceFile

/**
 * Created by rene on 24.02.16.
 */
class TextReport {

	void generate(List<SourceFile> sourceFiles, File destinationDirectory) {

		InputStream inputStream = TextReport.class.getResourceAsStream("/TextReport.template")
		Reader reader = new InputStreamReader(inputStream);

		File destinationFile = new File(destinationDirectory, "coverage.txt")
		Writer writer = new FileWriter(destinationFile);

		MustacheFactory mustacheFactory = new DefaultMustacheFactory()
		Mustache mustache = mustacheFactory.compile(reader, "template")

		def sourceFileData = []


		sourceFiles.each {

			String filename = it.filename
			if (filename.length() > 40) {
				filename = "..." + filename[-37..-1]
			}

			sourceFileData << [
				filename: filename,
				lines:  "${it.sourceLines.size()}".padLeft(8),
				exec: "${it.linesExecuted}".padLeft(8),
				cover: "${(it.coverage*100).intValue()}%".padLeft(8),
				missing: "${it.linesNotCovered}".padLeft(8)
			]
		}

		def data = [
						sourceFiles: sourceFileData,
						totalLines:  "${SourceFile.getTotalLines(sourceFiles)}".padLeft(8),
						totalExec: "${SourceFile.getLinesExecuted(sourceFiles)}".padLeft(8),
						totalCover: "${(SourceFile.getCoverage(sourceFiles)*100).intValue()}%".padLeft(8),
						totalMissing: "${SourceFile.getLinesNotCovered(sourceFiles)}".padLeft(8)
		]

		mustache.execute(writer, data)
	  writer.flush()
		reader.close()

	}
}
