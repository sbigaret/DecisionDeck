package pl.poznan.put.topsisimport org.xmcda.XMCDAimport pl.poznan.put.xmcda.InputsHandlerobject IdealAlternativesInputsHandler : InputsHandler<TopsisInputs> {    /**     * This class contains every element which are needed to compute     * TOPSIS.<br></br>     * It is populated by [TopsisInputsHandler.checkAndExtractInputs].     */    /**     * @param xmcda     * @return     */    override fun checkAndExtractInputs(xmcda: XMCDA) = singleInputTopsisHandler.checkAndExtractInputs(xmcda)}