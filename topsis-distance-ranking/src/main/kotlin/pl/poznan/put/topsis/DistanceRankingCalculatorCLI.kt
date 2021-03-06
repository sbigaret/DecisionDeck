package pl.poznan.put.topsis

import pl.poznan.put.xmcda.XmcdaLoaderBase

object DistanceRankingCalculatorCLI {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        XmcdaLoaderBase.load(args, DistanceRankingCalculatorCliXMCDAv2::main, DistanceRankingCalculatorCliXMCDAv3::main)
    }
}
