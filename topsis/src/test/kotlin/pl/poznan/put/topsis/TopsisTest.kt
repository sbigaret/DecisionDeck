package pl.poznan.put.topsis

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test


class TopsisTest {
    // https://www.slideshare.net/pranavmishra22/topsis-a-multicriteria-decision-making-approach
    @Test
    fun test() {
        val criteria = listOf(
                Criterion("entertainment", 4.0, CriteriaType.PROFIT),
                Criterion("facilities", 2.0, CriteriaType.PROFIT),
                Criterion("TravelCost", 6.0, CriteriaType.COST),
                Criterion("Accommodation", 8.0, CriteriaType.COST))
        val alternatives = listOf(
                Alternative("Hogwarts", criteria.zip(listOf(9, 7, 6, 7)).toMap()),
                Alternative("Hogsmeade", criteria.zip(listOf(8, 7, 9, 6)).toMap()),
                Alternative("Azkaban", criteria.zip(listOf(7, 8, 6, 6)).toMap())
        )
        val topsis = Topsis(alternatives, criteria)
        val result = topsis.calculate()
        val expected = listOf(alternatives[2] to 0.74, alternatives[0] to 0.68, alternatives[1] to 0.34)
        assertEquals(result.size, expected.size)
        result.forEachIndexed { i, e ->
            val shouldBe = expected[i]
            Assertions.assertEquals(shouldBe.first, e.alternative,
                    "invalid alternative at position $i, expected ${shouldBe.first.name}, got ${e.alternative.name}")
            Assertions.assertEquals(shouldBe.second, e.value, 0.01,
                    "value for position is incorrect, expected ${shouldBe.second}, got ${e.value}")
        }
    }
}