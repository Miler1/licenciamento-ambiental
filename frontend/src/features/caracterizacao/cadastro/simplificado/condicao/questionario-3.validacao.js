var ValidadorPerguntasQuestionario3 = function(){

	var validador = this;

	validador.validar = validar;
	validador.dataAtual = new Date();

	function isTrue(value) {
		return value === 'true' || value === true;
	}

	function isFalse(value) {
		return value === 'false' || value === false;
	}

	function isNumber(value) {
		return ((typeof value) === 'number');
	}

	function exist(value) {
		return !!value;
	}

	function textoNaoVazio(value) {
		return value && value.length > 0;
	}

	function validar(respostas) {

		if(!respostas) {
			return false;
		}

		validador.respostasRecebidas = respostas;

		return verificarRespostaConsumoAgua() && verificarRespostaEfluentes() && verificarRespostaResiduosSolidos();
	}

	function verificarRespostaConsumoAgua() {

		if(isFalse(validador.respostasRecebidas.consumoAgua)) {
			return true;
		}
		else if(isTrue(validador.respostasRecebidas.consumoAgua)) {
			return verificarRespostaConsumoAguaTrue();
		}

		return false;
	}

	function verificarRespostaConsumoAguaTrue() {

		if(!
			(
				validador.respostasRecebidas.consumoAguaRedePublica ||
				validador.respostasRecebidas.consumoAguaAguaSubterranea ||
				validador.respostasRecebidas.consumoAguaAguaSuperficial
			)
		) {
			return false;
		}

		return verificarRespostaConsumoAguaRedePublica() &&
			verificarRespostaConsumoAguaAguaSubterranea() &&
			verificarRespostaConsumoAguaAguaSuperficial();
	}

	function verificarRespostaConsumoAguaRedePublica() {

		if(isTrue(validador.respostasRecebidas.consumoAguaRedePublica)) {
			return !!(isNumber(validador.respostasRecebidas.consumoAguaRedePublicaConsumoMedio) &&
				(
					validador.respostasRecebidas.consumoAguaRedePublicaUsoDomestico ||
					validador.respostasRecebidas.consumoAguaRedePublicaUsoIndustrial
				));
		}

		return true;
	}

	function verificarRespostaConsumoAguaAguaSubterranea() {

		if(isTrue(validador.respostasRecebidas.consumoAguaAguaSubterranea)) {
			return !!(isNumber(validador.respostasRecebidas.consumoAguaAguaSubterraneaConsumoMedio) &&
				(
					validador.respostasRecebidas.consumoAguaAguaSubterraneaUsoDomestico ||
					validador.respostasRecebidas.consumoAguaAguaSubterraneaUsoIndustrial
				));
		}

		return true;
	}

	function verificarRespostaConsumoAguaAguaSuperficial() {

		if(isTrue(validador.respostasRecebidas.consumoAguaAguaSuperficial)) {
			return !!(isNumber(validador.respostasRecebidas.consumoAguaAguaSuperficialConsumoMedio) &&
				(
					validador.respostasRecebidas.consumoAguaAguaSuperficialUsoDomestico ||
					validador.respostasRecebidas.consumoAguaAguaSuperficialUsoIndustrial
				));
		}

		return true;
	}

	function verificarRespostaEfluentes() {

		if(isFalse(validador.respostasRecebidas.efluentes)) {
			return true;
		}
		else if(isTrue(validador.respostasRecebidas.efluentes)) {
			return verificarRespostaEfluentesTrue();
		}

		return false;
	}

	function verificarRespostaEfluentesTrue() {

		if(!
			(
				validador.respostasRecebidas.efluentesDomestico ||
				validador.respostasRecebidas.efluentesIndustrial
			)
		) {
			return false;
		}

		return verificarRespostaEfluentesDomestico() && verificarRespostaEfluentesIndustrial();
	}

	function verificarRespostaEfluentesDomestico() {

		if(isTrue(validador.respostasRecebidas.efluentesDomestico)) {
			if(isNumber(validador.respostasRecebidas.efluentesDomesticoVazaoMedia) &&
				exist(validador.respostasRecebidas.efluentesDomesticoRegimeCarga) &&
				(
					validador.respostasRecebidas.efluentesDomesticoTipoTratamentoEte ||
					validador.respostasRecebidas.efluentesDomesticoTipoTratamentoFossaSeptica ||
					validador.respostasRecebidas.efluentesDomesticoTipoTratamentoFossaFiltro ||
					validador.respostasRecebidas.efluentesDomesticoTipoTratamentoRemocaoOleoGraxa ||
					(
						validador.respostasRecebidas.efluentesDomesticoTipoTratamentoOutros &&
						textoNaoVazio(validador.respostasRecebidas.efluentesDomesticoTipoTratamentoOutrosEspecificar)
					)
				) &&
				(
					validador.respostasRecebidas.efluentesDomesticoDestinoFinalFossaSeptica ||
					validador.respostasRecebidas.efluentesDomesticoDestinoFinalFossaFiltro ||
					validador.respostasRecebidas.efluentesDomesticoDestinoFinalRemocaoOleoGraxa ||
					(
						validador.respostasRecebidas.efluentesDomesticoDestinoFinalOutros &&
						textoNaoVazio(validador.respostasRecebidas.efluentesDomesticoDestinoFinalOutrosEspecificar)
					)
				)
			) {

				if(validador.respostasRecebidas.efluentesDomesticoTipoTratamentoOutros) {

					if(!textoNaoVazio(validador.respostasRecebidas.efluentesDomesticoTipoTratamentoOutrosEspecificar)) {
						return false;
					}
				}

				if(validador.respostasRecebidas.efluentesDomesticoDestinoFinalOutros) {

					if(!textoNaoVazio(validador.respostasRecebidas.efluentesDomesticoDestinoFinalOutrosEspecificar)) {
						return false;
					}
				}

				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	function verificarRespostaEfluentesIndustrial() {

		if(isTrue(validador.respostasRecebidas.efluentesIndustrial)) {
			if(isNumber(validador.respostasRecebidas.efluentesIndustrialVazaoMedia) &&
				exist(validador.respostasRecebidas.efluentesIndustrialRegimeCarga) &&
				(
					validador.respostasRecebidas.efluentesIndustrialTipoTratamentoEte ||
					validador.respostasRecebidas.efluentesIndustrialTipoTratamentoFossaSeptica ||
					validador.respostasRecebidas.efluentesIndustrialTipoTratamentoFossaFiltro ||
					validador.respostasRecebidas.efluentesIndustrialTipoTratamentoRemocaoOleoGraxa ||
					(
						validador.respostasRecebidas.efluentesIndustrialTipoTratamentoOutros &&
						textoNaoVazio(validador.respostasRecebidas.efluentesIndustrialTipoTratamentoOutrosEspecificar)
					)
				) &&
				(
					validador.respostasRecebidas.efluentesIndustrialDestinoFinalFossaSeptica ||
					validador.respostasRecebidas.efluentesIndustrialDestinoFinalFossaFiltro ||
					validador.respostasRecebidas.efluentesIndustrialDestinoFinalRemocaoOleoGraxa ||
					(
						validador.respostasRecebidas.efluentesIndustrialDestinoFinalOutros &&
						textoNaoVazio(validador.respostasRecebidas.efluentesIndustrialDestinoFinalOutrosEspecificar)
					)
				)
			) {

				if(validador.respostasRecebidas.efluentesIndustrialTipoTratamentoOutros) {

					if(!textoNaoVazio(validador.respostasRecebidas.efluentesIndustrialTipoTratamentoOutrosEspecificar)) {
						return false;
					}

				}

				if(validador.respostasRecebidas.efluentesIndustrialDestinoFinalOutros) {

					if(!textoNaoVazio(validador.respostasRecebidas.efluentesIndustrialDestinoFinalOutrosEspecificar)) {
						return false;
					}
				}

				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	function verificarRespostaResiduosSolidos() {

		if(isFalse(validador.respostasRecebidas.residuosSolidos)) {
			return true;
		}
		else if(isTrue(validador.respostasRecebidas.residuosSolidos)) {
			return verificarRespostaResiduosSolidosTrue();
		}

		return false;
	}

	function verificarRespostaResiduosSolidosTrue() {

		if(!
			(
				validador.respostasRecebidas.residuosSolidosDomestico ||
				validador.respostasRecebidas.residuosSolidosEscritorio ||
				validador.respostasRecebidas.residuosSolidosIndustrial ||
				validador.respostasRecebidas.residuosSolidosIndustrialPerigosos ||
				validador.respostasRecebidas.residuosSolidosOutros
			)
		) {
			return false;
		}

		return verificarRespostaResiduosSolidosDomestico() &&
			verificarRespostaResiduosSolidosEscritorio() &&
			verificarRespostaResiduosSolidosIndustrial() &&
			verificarRespostaResiduosSolidosIndustrialPerigosos() &&
			verificarRespostaResiduosSolidosOutros();
	}

	function verificarRespostaResiduosSolidosDomestico() {

		if(isTrue(validador.respostasRecebidas.residuosSolidosDomestico)) {
			if(isNumber(validador.respostasRecebidas.residuosSolidosDomesticoQuantidadeMedia) &&
				(
					validador.respostasRecebidas.residuosSolidosDomesticoTratamentoDisposicaoAterroSanitario ||
					validador.respostasRecebidas.residuosSolidosDomesticoTratamentoDisposicaoAterroCeuAberto ||
					validador.respostasRecebidas.residuosSolidosDomesticoTratamentoDisposicaoIncineracao ||
					validador.respostasRecebidas.residuosSolidosDomesticoTratamentoDisposicaoReciclagem ||
					validador.respostasRecebidas.residuosSolidosDomesticoTratamentoDisposicaoCompostagem ||
					(
						validador.respostasRecebidas.residuosSolidosDomesticoTratamentoDisposicaoOutros &&
						textoNaoVazio(validador.respostasRecebidas.residuosSolidosDomesticoTratamentoDisposicaoOutrosEspecificar)
					)
				) &&
				(
					validador.respostasRecebidas.residuosSolidosDomesticoTipoColetaPublica ||
					validador.respostasRecebidas.residuosSolidosDomesticoTipoColetaPropria ||
					(
						validador.respostasRecebidas.residuosSolidosDomesticoTipoColetaTerceiros &&
						textoNaoVazio(validador.respostasRecebidas.residuosSolidosDomesticoTipoColetaTerceirosEspecificar)
					)
				)
			) {
				if(validador.respostasRecebidas.residuosSolidosDomesticoTratamentoDisposicaoOutros) {

					if(!textoNaoVazio(validador.respostasRecebidas.residuosSolidosDomesticoTratamentoDisposicaoOutrosEspecificar)) {
						return false;
					}
				}

				if(validador.respostasRecebidas.residuosSolidosDomesticoTipoColetaTerceiros) {

					if(!textoNaoVazio(validador.respostasRecebidas.residuosSolidosDomesticoTipoColetaTerceirosEspecificar)) {
						return false;
					}
				}

				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	function verificarRespostaResiduosSolidosEscritorio() {

		if(isTrue(validador.respostasRecebidas.residuosSolidosEscritorio)) {
			if(isNumber(validador.respostasRecebidas.residuosSolidosEscritorioQuantidadeMedia) &&
				(
					validador.respostasRecebidas.residuosSolidosEscritorioTratamentoDisposicaoAterroSanitario ||
					validador.respostasRecebidas.residuosSolidosEscritorioTratamentoDisposicaoAterroCeuAberto ||
					validador.respostasRecebidas.residuosSolidosEscritorioTratamentoDisposicaoIncineracao ||
					validador.respostasRecebidas.residuosSolidosEscritorioTratamentoDisposicaoReciclagem ||
					(
						validador.respostasRecebidas.residuosSolidosEscritorioTratamentoDisposicaoOutros &&
						textoNaoVazio(validador.respostasRecebidas.residuosSolidosEscritorioTratamentoDisposicaoOutrosEspecificar)
					)
				) &&
				(
					validador.respostasRecebidas.residuosSolidosEscritorioTipoColetaPublica ||
					validador.respostasRecebidas.residuosSolidosEscritorioTipoColetaPropria ||
					(
						validador.respostasRecebidas.residuosSolidosEscritorioTipoColetaTerceiros &&
						textoNaoVazio(validador.respostasRecebidas.residuosSolidosEscritorioTipoColetaTerceirosEspecificar)
					)
				)
			) {

				if(validador.respostasRecebidas.residuosSolidosEscritorioTratamentoDisposicaoOutros) {
					if(!textoNaoVazio(validador.respostasRecebidas.residuosSolidosEscritorioTratamentoDisposicaoOutrosEspecificar)) {
						return false;
					}
				}

				if(validador.respostasRecebidas.residuosSolidosEscritorioTipoColetaTerceiros) {
					if(!textoNaoVazio(validador.respostasRecebidas.residuosSolidosEscritorioTipoColetaTerceirosEspecificar)) {
						return false;
					}
				}

				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	function verificarRespostaResiduosSolidosIndustrial() {

		if(isTrue(validador.respostasRecebidas.residuosSolidosIndustrial)) {
			if(isNumber(validador.respostasRecebidas.residuosSolidosIndustrialQuantidadeMedia) &&
				(
					validador.respostasRecebidas.residuosSolidosIndustrialTratamentoDisposicaoAterroSanitario ||
					validador.respostasRecebidas.residuosSolidosIndustrialTratamentoDisposicaoAterroCeuAberto ||
					validador.respostasRecebidas.residuosSolidosIndustrialTratamentoDisposicaoIncineracao ||
					validador.respostasRecebidas.residuosSolidosIndustrialTratamentoDisposicaoReciclagem ||
					validador.respostasRecebidas.residuosSolidosIndustrialTratamentoDisposicaoAterroIndustrial ||
					validador.respostasRecebidas.residuosSolidosIndustrialTratamentoDisposicaoReaproveitamentoProcesso ||
					(
						validador.respostasRecebidas.residuosSolidosIndustrialTratamentoDisposicaoOutros &&
						textoNaoVazio(validador.respostasRecebidas.residuosSolidosIndustrialTratamentoDisposicaoOutrosEspecificar)
					)
				) &&
				(
					validador.respostasRecebidas.residuosSolidosIndustrialTipoColetaPublica ||
					validador.respostasRecebidas.residuosSolidosIndustrialTipoColetaPropria ||
					(
						validador.respostasRecebidas.residuosSolidosIndustrialTipoColetaTerceiros &&
						textoNaoVazio(validador.respostasRecebidas.residuosSolidosIndustrialTipoColetaTerceirosEspecificar)
					)
				)
			) {
				if(validador.respostasRecebidas.residuosSolidosIndustrialTratamentoDisposicaoOutros) {
					if(!textoNaoVazio(validador.respostasRecebidas.residuosSolidosIndustrialTratamentoDisposicaoOutrosEspecificar)) {
						return false;
					}
				}

				if(validador.respostasRecebidas.residuosSolidosIndustrialTipoColetaTerceiros) {
					if(!textoNaoVazio(validador.respostasRecebidas.residuosSolidosIndustrialTipoColetaTerceirosEspecificar)) {
						return false;
					}
				}

				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	function verificarRespostaResiduosSolidosIndustrialPerigosos() {

		if(isTrue(validador.respostasRecebidas.residuosSolidosIndustrialPerigosos)) {
			if(isNumber(validador.respostasRecebidas.residuosSolidosIndustrialPerigososQuantidadeMedia) &&
				(
					validador.respostasRecebidas.residuosSolidosIndustrialPerigososTratamentoDisposicaoAterroSanitario ||
					validador.respostasRecebidas.residuosSolidosIndustrialPerigososTratamentoDisposicaoAterroCeuAberto ||
					validador.respostasRecebidas.residuosSolidosIndustrialPerigososTratamentoDisposicaoIncineracao ||
					validador.respostasRecebidas.residuosSolidosIndustrialPerigososTratamentoDisposicaoReciclagem ||
					validador.respostasRecebidas.residuosSolidosIndustrialPerigososTratamentoDisposicaoAterroIndustrial ||
					(
						validador.respostasRecebidas.residuosSolidosIndustrialPerigososTratamentoDisposicaoOutros &&
						textoNaoVazio(validador.respostasRecebidas.residuosSolidosIndustrialPerigososTratamentoDisposicaoOutrosEspecificar)
					)
				) &&
				(
					validador.respostasRecebidas.residuosSolidosIndustrialPerigososTipoColetaPublica ||
					validador.respostasRecebidas.residuosSolidosIndustrialPerigososTipoColetaPropria ||
					(
						validador.respostasRecebidas.residuosSolidosIndustrialPerigososTipoColetaTerceiros &&
						textoNaoVazio(validador.respostasRecebidas.residuosSolidosIndustrialPerigososTipoColetaTerceirosEspecificar)
					)
				)
			) {

				if(validador.respostasRecebidas.residuosSolidosIndustrialPerigososTratamentoDisposicaoOutros) {
					if(!textoNaoVazio(validador.respostasRecebidas.residuosSolidosIndustrialPerigososTratamentoDisposicaoOutrosEspecificar)) {
						return false;
					}
				}

				if(validador.respostasRecebidas.residuosSolidosIndustrialPerigososTipoColetaTerceiros) {
					if(!textoNaoVazio(validador.respostasRecebidas.residuosSolidosIndustrialPerigososTipoColetaTerceirosEspecificar)) {
						return false;
					}
				}

				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

	function verificarRespostaResiduosSolidosOutros() {

		if(isTrue(validador.respostasRecebidas.residuosSolidosOutros)) {
			if(textoNaoVazio(validador.respostasRecebidas.residuosSolidosOutrosOrigem) &&
				isNumber(validador.respostasRecebidas.residuosSolidosOutrosQuantidadeMedia) &&
				(
					validador.respostasRecebidas.residuosSolidosOutrosTratamentoDisposicaoAterroSanitario ||
					validador.respostasRecebidas.residuosSolidosOutrosTratamentoDisposicaoAterroCeuAberto ||
					validador.respostasRecebidas.residuosSolidosOutrosTratamentoDisposicaoIncineracao ||
					(
						validador.respostasRecebidas.residuosSolidosOutrosTratamentoDisposicaoOutros &&
						textoNaoVazio(validador.respostasRecebidas.residuosSolidosOutrosTratamentoDisposicaoOutrosEspecificar)
					)
				) &&
				(
					validador.respostasRecebidas.residuosSolidosOutrosTipoColetaPublica ||
					validador.respostasRecebidas.residuosSolidosOutrosTipoColetaPropria ||
					(
						validador.respostasRecebidas.residuosSolidosOutrosTipoColetaTerceiros &&
						textoNaoVazio(validador.respostasRecebidas.residuosSolidosOutrosTipoColetaTerceirosEspecificar)
					)
				)
			) {
				if(validador.respostasRecebidas.residuosSolidosOutrosTratamentoDisposicaoOutros) {
					if(!textoNaoVazio(validador.respostasRecebidas.residuosSolidosOutrosTratamentoDisposicaoOutrosEspecificar)) {
						return false;
					}
				}

				if(validador.respostasRecebidas.residuosSolidosOutrosTipoColetaTerceiros) {
					if(!textoNaoVazio(validador.respostasRecebidas.residuosSolidosOutrosTipoColetaTerceirosEspecificar)) {
						return false;
					}
				}

				return true;
			}
			else {
				return false;
			}
		}

		return true;
	}

};

exports.ValidadorPerguntasQuestionario3 = ValidadorPerguntasQuestionario3;
