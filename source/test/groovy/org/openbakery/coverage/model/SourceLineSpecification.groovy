package org.openbakery.coverage.model

import spock.lang.Specification

/**
 * Created by rene on 23.02.16.
 */
class SourceLineSpecification extends Specification {



	def "1.11k hits"() {
		when:
		SourceLine line = new SourceLine("  1.11k|   20|")

		then:
		line.hits == 1110
	}

	def "11 hits"() {
		when:
		SourceLine line = new SourceLine("     11|   20|")

		then:
		line.hits == 11
	}

	def "1.11M hits"() {
		when:
		SourceLine line = new SourceLine("  1.11M|   20|")

		then:
		line.hits == 1110000
	}

	def "1.11G hits"() {
		when:
		SourceLine line = new SourceLine("  1.11G|   20|")

		then:
		line.hits == 1110000000
	}
}
