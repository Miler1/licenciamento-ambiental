package controllers;

import beans.FluxoLicencasVO;
import beans.ListaAtividadesPermitidasVO;
import beans.TipoLicencaBuscaVO;
import models.caracterizacao.TipoLicenca;
import serializers.TipoLicencaBuscaVOSerializer;

import java.util.List;
import java.util.stream.Collectors;

/***
 *
 * Classe para realizar validações e busca sobre
 * os dados referentes aos Tipos de Licença e suas categorizações
 * para tornar dinâmica a visualização no frontend dos dados
 * (LI, LO, LP, RLO, entre outros)
 *
 ***/
public class TiposLicencas extends GenericController {

	/***
	 * Método para listar tipos de licenças referentes a sua
	 * finalidade no banco
	 * @param licencasPermitidas lista com as licenças permitidas para filtro
	 */
	public void listLicencas(ListaAtividadesPermitidasVO licencasPermitidas) {

		if(licencasPermitidas == null || licencasPermitidas.licencasAtividade == null ||
				licencasPermitidas.licencasAtividade.isEmpty()){
			renderJSON(new TipoLicencaBuscaVO());
		}

		List<Long> ids = licencasPermitidas.licencasAtividade.stream().map(l -> l.id).collect(Collectors.toList());

		List<TipoLicenca> tipoLicencas = TipoLicenca.find("id IN :ids").setParameter("ids", ids).fetch();

		List<TipoLicenca> tipoLicencaSolicitacao = tipoLicencas.stream().filter(TipoLicenca::isSolicitacao)
				.collect(Collectors.toList());

		List<TipoLicenca> tipoLicencaRenovacao = tipoLicencas.stream().filter(TipoLicenca::isRenovacao)
				.collect(Collectors.toList());

		List<TipoLicenca> tipoLicencaAtualizacao = tipoLicencas.stream().filter(TipoLicenca::isAtualizacao)
				.collect(Collectors.toList());

		List<TipoLicenca> tipoLicencaCadastro = tipoLicencas.stream().filter(TipoLicenca::isCadastro)
                .collect(Collectors.toList());

		renderJSON(new TipoLicencaBuscaVO(tipoLicencaSolicitacao, tipoLicencaRenovacao, tipoLicencaAtualizacao,tipoLicencaCadastro),
				TipoLicencaBuscaVOSerializer.findTipoLicenca);
	}

	public void listarFluxo(){
		renderJSON(new FluxoLicencasVO(TipoLicenca.findAll()));
	}

	public boolean isPeriodAccordingLicence(TipoLicenca tp){
		return (tp.sigla.equals("LP") && (tp.validadeEmAnos > 0 && tp.validadeEmAnos < 5)) || (tp.sigla.equals("ALP") && (tp.validadeEmAnos > 0 && tp.validadeEmAnos < 5)) ||
				(tp.sigla.equals("LI") && (tp.validadeEmAnos > 0 && tp.validadeEmAnos < 5)) || (tp.sigla.equals("RLI") && (tp.validadeEmAnos > 0 && tp.validadeEmAnos < 5)) ||
				(tp.sigla.equals("LO") && (tp.validadeEmAnos > 0 && tp.validadeEmAnos < 6)) || (tp.sigla.equals("RLO") && (tp.validadeEmAnos > 0 && tp.validadeEmAnos < 6)) ||
				(tp.sigla.equals("LAR") && (tp.validadeEmAnos > 0 && tp.validadeEmAnos < 6)) || (tp.sigla.equals("RLAU") && (tp.validadeEmAnos > 0 && tp.validadeEmAnos < 6)) ||
				(tp.sigla.equals("CA") && (tp.validadeEmAnos > 0 && tp.validadeEmAnos < 100));
	}

	public void validatePeriodoLicencaSolicitada(TipoLicenca tipoLicencaSolicitada){

		renderJSON(this.isPeriodAccordingLicence(tipoLicencaSolicitada));
	}

}
