package controllers;

import com.vividsolutions.jts.geom.Geometry;
import models.*;
import models.sicar.ImovelSicar;
import models.sicar.SicarWebService;
import models.sicar.SobreposicoesSicar;
import models.sicar.TipoArea;
import org.geotools.feature.FeatureCollection;
import org.opengis.referencing.FactoryException;
import play.Play;
import play.libs.WS;
import security.Acao;
import serializers.EnvioEmailInfoSerializer;
import utils.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Imoveis extends InternalController {
	
	public static void getImoveisSimplificadosPorCpfCnpj(String cpfCnpj, Long idMunicipio) {
		
		verificarPermissao(Acao.CADASTRAR_EMPREENDIMENTO);
		
		if (cpfCnpj == null || cpfCnpj.isEmpty())
			renderMensagem(Mensagem.CPF_CNPJ_INVALIDO_NAO_INFORMADO);
		
		Municipio municipio = Municipio.findById(idMunicipio);
		
		List<ImovelSicar> imoveisEncontrados = 
				new SicarWebService().getImoveisSimplificadosPorCpfCnpj(cpfCnpj, municipio);
		
		renderJSON(imoveisEncontrados);

	}
	
	public static void getImovelByCodigo(String codigoImovel) {
		
		verificarPermissao(Acao.CADASTRAR_EMPREENDIMENTO);
		
		if(codigoImovel == null || codigoImovel.isEmpty()){
			renderMensagem(Mensagem.CODIGO_IMOVEL_INVALIDO_NAO_INFORMADO);
		}

		renderJSON(new SicarWebService().getImovelByCodigo(codigoImovel));
	}

	public static void verificaCondicoesDoImovel(String codigoImovel) {

		verificarPermissao(Acao.CADASTRAR_EMPREENDIMENTO);

		if(codigoImovel == null || codigoImovel.isEmpty()){
			renderMensagem(Mensagem.CODIGO_IMOVEL_INVALIDO_NAO_INFORMADO);
		}

		if(!new SicarWebService().verificaCondicoesDoImovel(codigoImovel)){
			throw new IllegalStateException(String.valueOf(Mensagem.CONDICAO_DO_CAR_NAO_PERMITE_SOLICITACAO));
		}

		ok();
	}

	public static void getTemasByImovel(String codigoImovel, List<String> temas) {

		verificarPermissao(Acao.CADASTRAR_EMPREENDIMENTO);

		if(codigoImovel == null || codigoImovel.isEmpty()){
			renderMensagem(Mensagem.CODIGO_IMOVEL_INVALIDO_NAO_INFORMADO);
		}

		renderJSON(new SicarWebService().getTemasByImovel(codigoImovel, temas));

	}
	
	public static void getSobreposicoesMacroZEE(Geometry geometria) {
		
		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		if(geometria == null){
			renderMensagem(Mensagem.PARAMETRO_NAO_INFORMADO, "geometria");
		}
		
		SobreposicoesSicar s = new SicarWebService().getSobreposicoesMacroZEE(geometria.toText());

		renderJSON(s);

	}

	public static void getCamadaLDI(String codigoImovel) {
		
		getCamadaGeoServerByCodigoImovel(Configuracoes.GEOSERVER_WMS, Configuracoes.GEOSERVER_SICAR_LAYER_LDI, "the_geom", codigoImovel, "INTERSECTS");

	}
	
	public static void getCamadaPRODES(String codigoImovel) {

		getCamadaGeoServerByCodigoImovel(Configuracoes.GEOSERVER_SEMAS, Configuracoes.GEOSERVER_SEMAS_LAYER_PRODES, "geom", codigoImovel, "INTERSECTS");

	}
	
	public static void getCamadaAreaConsolidada(String codigoImovel) throws IOException, FactoryException {

		getCamadaGeoServerWithIntersection(Configuracoes.GEOSERVER_GETCAPABILITIES, Configuracoes.GEOSERVER_SEMAS_LAYER_AREA_CONSOLIDADA_CLASSIFICADA, "Área consolidada", codigoImovel);

	}	
	
	private static void getCamadaGeoServerByCodigoImovel(String urlGeoserver, String nomeCamada, String nomeGeom, String codigoImovel, String filterType) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		if(codigoImovel == null){
			renderMensagem(Mensagem.PARAMETRO_NAO_INFORMADO, "codigoImovel");
		}

		ImovelEmpreendimento imovelEmpreendimento = ImovelEmpreendimento.getImovelBy(codigoImovel);

		WS.WSRequest wsRequest = WS.url(urlGeoserver);
		
		getCamadaGeoServer(urlGeoserver, nomeCamada, nomeGeom, imovelEmpreendimento.limite, filterType);
	}
	
	private static void getCamadaGeoServerWithIntersection(String urlGeoserver, String nomeCamada, String descricaoCamada, String codigoImovel) throws IOException{
		
		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		if(codigoImovel == null){
			renderMensagem(Mensagem.PARAMETRO_NAO_INFORMADO, "codigoImovel");
		}

		ImovelEmpreendimento imovelEmpreendimento = ImovelEmpreendimento.getImovelBy(codigoImovel);
		
		String geoJson = GeoCalc.getCamadaGeoServerWithIntersection(urlGeoserver, nomeCamada, descricaoCamada, imovelEmpreendimento.limite);
		
		renderJSON(geoJson);
	}
	
	public static void getSobreposicoesAreaConsolidada(Geometry geometria) throws IOException {

		FeatureCollection features = GeoCalc.getCamadaGeoServerContains(Configuracoes.GEOSERVER_GETCAPABILITIES, Configuracoes.GEOSERVER_SEMAS_LAYER_AREA_CONSOLIDADA_CLASSIFICADA, geometria);
		
		renderJSON(GeoCalc.filterCollectionToJson(features));
	}	
	
	private static void getCamadaGeoServer(String urlGeoserver, String nomeCamada, String nomeGeom, Geometry geometria, String filterType) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		if(geometria == null){
			
			renderMensagem(Mensagem.PARAMETRO_NAO_INFORMADO, "geometria");
		}

		WS.WSRequest wsRequest = WS.url(urlGeoserver);

		wsRequest.setParameter("service", "WFS")
				.setParameter("version", "1.0.0")
				.setParameter("request", "GetFeature")
				.setParameter("typeName", nomeCamada)
				.setParameter("outputFormat", "application/json")
				.setParameter("CQL_FILTER", filterType +"(" + nomeGeom + ", " + geometria.getEnvelope().toText() + ")");

		WS.HttpResponse response = await(wsRequest.getAsync());

		renderJSON(response.getJson());
	}	

	public static void getSobreposicoesTemasCAR(String codigoImovel, Geometry geometria) {

		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);

		if(geometria == null){
			renderMensagem(Mensagem.PARAMETRO_NAO_INFORMADO, "geometria");
		}
		
		List<Long> idsTemas = ListUtil.createList(
				TipoArea.AREA_CONSOLIDADA.id,
				TipoArea.ARL_AVERBADA.id,
				TipoArea.ARL_APROVADA_NAO_AVERBADA.id,
				TipoArea.ARL_PROPOSTA.id);

		SobreposicoesSicar s = new SicarWebService().getSobreposicoesTemasCAR(codigoImovel, geometria, idsTemas);

		renderJSON(s);
	}

	public static void getPassivoImovelCAR(Long idEmpreendimento) {
		
		verificarPermissao(Acao.CADASTRAR_CARACTERIZACAO);
		
		Empreendimento empreendimento = Empreendimento.findById(idEmpreendimento);
		
		if(empreendimento == null){
			renderMensagem(Mensagem.PARAMETRO_NAO_INFORMADO, "Empreendimento");
		}
		
		if(!empreendimento.localizacao.equals(TipoLocalizacao.ZONA_RURAL) ||
				empreendimento.imovel == null){
			renderMensagem(Mensagem.ERRO_PADRAO);
		}
		
		renderJSON(new SicarWebService().getPassivoImovelCAR(empreendimento));
	
	}

	public static void enviarEmail(String cpfCnpj, String denominacao) {

		Empreendimento emp = WebServiceEntradaUnica.findEmpreendimentosByCpfCnpjCadastroEmpreendimento(cpfCnpj);

		emp.empreendimentoEU.denominacao = denominacao;

		EnvioEmailInfo empInfo = new EnvioEmailInfo(cpfCnpj);

		empInfo.emailEnviado = EnvioEmailInfo.findByCpfCnpj(cpfCnpj);

		if(!empInfo.emailEnviado) {

			List<String> destinatarios = new ArrayList<String>();
			destinatarios.add(Configuracoes.MAIL_CAR_SEMA);
			destinatarios.add(Configuracoes.MAIL_SEMA_COPIA);

			EmailAvisoEmpreendimentoCadastroCar emailEmpreendimento = new EmailAvisoEmpreendimentoCadastroCar(emp, destinatarios);

			empInfo._save();
			empInfo.emailEnviado = true;
			emailEmpreendimento.enviar();

		}

		renderJSON(empInfo, EnvioEmailInfoSerializer.informacoesEnvioEmail);

	}

}
