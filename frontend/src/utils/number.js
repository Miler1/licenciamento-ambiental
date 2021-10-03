Number.prototype.zeroEsquerda = function(length) {

	var numberString = '' + this;
	while (numberString.length < length) {
		numberString = '0' + numberString;
	}

	return numberString;

};