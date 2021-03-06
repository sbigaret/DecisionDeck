package pl.poznan.put.topsis

import pl.poznan.put.xmcda.InvalidCommandLineException
import pl.poznan.put.xmcda.XMCDAv3Client

object IdealAlternativesCliXMCDAv3 : XMCDAv3Client<TopsisInputs, IdealAlternatives>() {
    override val files = v3Base
    override val manager = idealAlternativesComputationManager

    @Throws(InvalidCommandLineException::class)
    @JvmStatic
    fun main(args: Array<String>) = run(args)
}
