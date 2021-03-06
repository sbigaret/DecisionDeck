library(rJava)
library(XMCDA3)
XMCDA_v2_TAG_FOR_FILENAME <- list(
  # output name -> XMCDA v2 tag
  q = "alternativesValues",
  compromiseSolution = "alternatives",
  messages = "methodMessages"
)
XMCDA_v3_TAG_FOR_FILENAME <- list(
  # output name -> XMCDA v3 tag
  q = "alternativesValues",
  compromiseSolution = "alternatives",
  messages = "programExecutionResult"
)
xmcda_v3_tag <- function(outputName) XMCDA_v3_TAG_FOR_FILENAME[[outputName]]

xmcda_v2_tag <- function(outputName) XMCDA_v2_TAG_FOR_FILENAME[[outputName]]

assignAlternatives <- function(values, programExecutionResult) {
  xmcdaResult <- .jnew("org/xmcda/XMCDA")
  tmp <- handleException(
    function() putAlternativesValues(xmcdaResult, values),
    programExecutionResult,
    humanMessage = "Could not put overall values in tree, reason: "
  )
  # if an error occurs, return null, else a dictionnary "xmcdaTag -> xmcdaObject"
  if (is.null(tmp)){
    NULL
  } else {
    xmcdaResult
  }
}

getAlternativesNames <- function(alternativesValues, programExecutionResult){
  xmcdaResult <- .jnew("org/xmcda/XMCDA")
  tmp <- handleException(
    function() {
      xmcdaResult$alternatives<-.jnew("org/xmcda/Alternatives")
      for (i in 1:length(alternativesValues)){
        xmcdaResult$alternatives$add(.jnew("org/xmcda/Alternative",alternativesValues[i]))
      }
      xmcdaResult
    },
    programExecutionResult,
    humanMessage = "Could not put overall values in tree, reason: "
  )
  # if an error occurs, return null, else a dictionnary "xmcdaTag -> xmcdaObject"
  if (is.null(tmp)){
    NULL
  } else {
    xmcdaResult
  }

}

convert <- function(results, programExecutionResult) {
  q = assignAlternatives(results$Q, programExecutionResult)
  compromises = getAlternativesNames(results$compromiseSolution, programExecutionResult)
  if (is.null(q) || is.null(compromises)) {
    NULL
  } else{
    list(q = q, compromise_solution = compromises)
  }
}
