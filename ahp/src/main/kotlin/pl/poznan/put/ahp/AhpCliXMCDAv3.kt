package pl.poznan.put.ahp

import pl.poznan.put.xmcda.XMCDAv3Client

internal object AhpCliXMCDAv3 : XMCDAv3Client<AhpResult, AhpRanking>() {
    override val files = v3files
    override val manager = ahpComputationManager

    @JvmStatic
    fun main(args: Array<String>) = run(args)
}
