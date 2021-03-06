package pl.poznan.put.topsis

import pl.poznan.put.xmcda.XmcdaLoaderBase

object NormalizationWeightingCLI {
    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {
        XmcdaLoaderBase.load(args, NormalizationWeightingCliXMCDAv2::main, NormalizationWeightingCliXMCDAv3::main)
    }
}
