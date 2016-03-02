package org.openbakery.coverage.model

/**
 * Created by René Pirringer
 */
class SourceLine {

	static long NOT_A_NUMBER = -1

	long number // hopefully no one has a source file that is longer as max int, but just to be sure
	long hits
	String code

	String modifies = "kMGTPEZY"

	SourceLine(String line) {
		if (line.length()>12) {
			this.hits = parseInt(line[0..6])
			this.number = parseInt(line[8..12])
		}
		if (line.length()>14) {
			this.code = line[14..-1]
		}
	}

	long parseInt(String text) {
		String trimmed = text.trim()
		if (trimmed.length() == 0) {
			return NOT_A_NUMBER
		}

		long result = 0
		char last = trimmed[-1]
		modifies.chars.eachWithIndex { value, index ->
			if (value == last) {
				result = Float.parseFloat(trimmed[0..-2])*1000
				result = result * 10.power(index*3)
				return
			}
		}
		if (result > 0) {
			return result
		}
		try {
			return Long.parseLong(text.trim())
		} catch (NumberFormatException ex) {
			return NOT_A_NUMBER
		}
	}

	long getNumber() {
		return this.number
	}

	long getHits() {
		return this.hits
	}


	String getHitsAsString() {
		if (this.hits < 0) {
			return ""
		}
		return this.hits.toString()
	}

	String getHitsRate() {
		if (this.hits == 0) {
			return "missing"
		}

		if (this.hits > 0) {
			return "covered"
		}
		return ""
	}

	String getCode() {
		return this.code
	}


	static double getCoverage(List<SourceLine> sourceLines) {
		long numberIgnored = sourceLines.count { it.hits == SourceLine.NOT_A_NUMBER }
		long linesCovered = sourceLines.count { it.hits > 0 }
		return linesCovered/(sourceLines.size()-numberIgnored)
	}

}
