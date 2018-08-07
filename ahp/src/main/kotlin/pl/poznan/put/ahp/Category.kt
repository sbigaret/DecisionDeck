package pl.poznan.put.ahp

import koma.matrix.ejml.backend.div
import org.ejml.simple.SimpleMatrix
import java.math.BigDecimal
import java.math.RoundingMode

sealed class Node(val name: String)
class Alternative(name: String, val preferences: MutableMap<String, Double> = mutableMapOf()) : Node(name) {

    companion object {
        @JvmStatic
        fun List<Alternative>.ranking() = map { it.name to it.total() }.sortedByDescending { it.second }
    }

    operator fun get(name: String) = preferences[name]

    fun total() = preferences.map { it.value }.sum()
    override fun toString() = "Alternative(name=$name, preferences=$preferences,)"
}

class Category(name: String,
               val subNodes: List<Node>,
               val preferenceMat: List<List<Double>>) : Node(name) {
    companion object {
        val ri = arrayOf(0.0, 0.0, 0.0, 0.58, 0.9, 1.12, 1.24, 1.32, 1.41, 1.45, 1.49).map { BigDecimal(it) }
    }

    var cr = BigDecimal.ONE
    var ci = BigDecimal.ONE

    val validity get() = cr <= "0.1".toBigDecimal()

    init {
        validate()
        initialize()
    }

    var preference: Double = 1.0
        set(value) {
            field = value
            subNodes.forEachIndexed { _, c ->
                when (c) {
                    is Category -> c.preference *= value
                    is Alternative -> c.preferences[name] = (c.preferences[name] ?: 1.0) * value
                }
            }
        }

    private fun validate() {
        require(subNodes.size == preferenceMat.size) { "categories number is not equal to preferences n" }
        require(subNodes.size < ri.size) {"number of categories must be less or equal to ${ri.size - 1}"}
        preferenceMat.forEachIndexed { i, row ->
            require(row.size == preferenceMat.size) {
                "row $i differs in size (${row.size} but expected ${preferenceMat.size})"
            }
        }
    }

    private fun initialize() {
        val n = subNodes.size
        val tempMat = preferenceMat.map { it.toDoubleArray() }.toTypedArray()
        val eig = SimpleMatrix(tempMat).eig()
        require(eig.numberOfEigenvalues == n) { "missing eigen values, got ${eig.numberOfEigenvalues}" }
        val eigenVector = eig.getEigenVector(n - 1)
        val normalized = eigenVector.div(eigenVector.elementSum())
        if (n > 1) {
            val bdn = BigDecimal(n)
            val maxValue = BigDecimal(
                    (0 until n)
                            .map { eig.getEigenvalue(it).magnitude }
                            .max() ?: 0.0
            ).setScale(8, RoundingMode.HALF_UP)
            ci = (maxValue - bdn) / (bdn - BigDecimal.ONE)
            cr = ci / ri[n]
        }

        subNodes.forEachIndexed { i, c ->
            when (c) {
                is Category -> c.preference = normalized[i]
                is Alternative -> c.preferences[name] = normalized[i]
            }
        }
    }

    private val totalCi: BigDecimal get() = ci + sumBy { totalCi }
    private val totalRi: BigDecimal get() = ri[subNodes.size] + sumBy { totalRi }

    private inline fun sumBy(f: Category.() -> BigDecimal) = subNodes
            .map { (it as? Category)?.f() ?: BigDecimal.ZERO }
            .reduce { a, b -> a + b }

    fun getTotalCR() = totalCi / totalRi

    operator fun get(nodeName: String) = subNodes.find { it.name == nodeName }
    override fun toString() =
            "Category(name=$name, subNodes=$subNodes, preferenceMat=$preferenceMat, preference=$preference, cr=$cr, ci=$ci)"
}