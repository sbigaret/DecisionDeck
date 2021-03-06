library(XMCDA3)

getPerformanceTable <- function(xmcdaData) {
	activeAlternatives <- handleException(
		function() getNumericPerformanceTableList(xmcdaData),
		xmcdaMessages,
		humanMessage = "Unable to extract the performance table, reason: "
	)
}

getCriteriaTypes <- function(xmcdaData) {
	activeAlternatives <- handleException(
		function() getCriteriaPreferenceDirectionsList(xmcdaData),
		xmcdaMessages,
		humanMessage = "Unable to extract the performance table, reason: "
	)
}
getWeights <- function(xmcdaData) {
	activeAlternatives <- handleException(
		function() getNumericCriteriaValuesList(xmcdaData),
		xmcdaMessages,
		humanMessage = "Unable to extract the performance table, reason: "
	)
}

validateExactOne <- function(size, objectName) {
	if (size == 0) {
		msg <- paste(objectName, "has not been supplied")
		putProgramExecutionResult(xmcdaMessages, errors = msg)
		FALSE
	} else if (size > 1) {
		msg <- paste("More than one", objectName, "has been supplied")
		putProgramExecutionResult(xmcdaMessages, errors = msg)
		FALSE
	} else {
		TRUE
	}
}

checkAndExtractInputs <- function(xmcdaData) {
	error <- FALSE
	noAlternativesSupplied = xmcdaData$alternatives$isEmpty()
	if (noAlternativesSupplied) {
		msg <- "Alternatives not supplied"
		putProgramExecutionResult(xmcdaMessages, errors = msg)
		error <- TRUE
	}

	error <- !validateExactOne(xmcdaData$performanceTablesList$size(), "performance table") || error
	error <- !validateExactOne(xmcdaData$criteriaScalesList$size(), "criterias types") || error
	error <- !validateExactOne(xmcdaData$criteriaValuesList$size(), "weights") || error
	for (i in 0:(xmcdaData$performanceTablesList$size()-1)) {
		if (xmcdaData$performanceTablesList$get(as.integer(i))$hasMissingValues()) {
			msg <- "performance table has missing values"
			putProgramExecutionResult(xmcdaMessages, errors = msg)
			error <- TRUE
		}
	}

	if (error) return(NULL)

	performanceList = getPerformanceTable(xmcdaData)
	weights = getWeights(xmcdaData)
	criteriaTypes = getCriteriaTypes(xmcdaData)
	if (xmcdaMessages$programExecutionResultsList$size() > 0) {
		if (xmcdaMessages$programExecutionResultsList$get(as.integer(0))$isError())
		NULL
	} else {
		list(
			performanceTable = performanceList[[1]],
			weights = weights[[1]],
			criteriaTypes = criteriaTypes[[1]]
		)
	}
}