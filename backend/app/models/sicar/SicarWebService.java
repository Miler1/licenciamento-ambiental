package models.sicar;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.ws.WebServiceException;

import com.google.gson.JsonElement;

import models.Empreendimento;
import models.Municipio;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.vividsolutions.jts.geom.Geometry;

import org.geotools.data.ows.HTTPResponse;
import play.libs.WS.HttpResponse;
import deserializers.DateDeserializer;
import utils.Configuracoes;
import utils.WebService;

public class SicarWebService {
	
	private GsonBuilder gsonBuilder = new GsonBuilder().setDateFormat(DateDeserializer.DATE_FORMAT);
	
	public List<ImovelSicar> getImoveisSimplificadosPorCpfCnpj(String cpfCnpj, Municipio municipio) {
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("cpfCnpj", cpfCnpj);
		params.put("codMunicipioIbge", municipio.id);
		
		HttpResponse response = new WebService().get(Configuracoes.URL_SICAR_IMOVEIS_SIMPLIFICADOS, params);

		if(!response.success()){
			throw new WebServiceException("Erro ao consultar imóveis no CAR Federal");
		}
		
		Type type = new TypeToken<MensagemSicar<List<ImovelSicar>>>(){}.getType();

		MensagemSicar<List<ImovelSicar>> retorno = gsonBuilder.create().fromJson(response.getJson(), type);
		
		if(!retorno.status.equals(StatusSiCAR.SUCESSO.sigla)){
			throw new WebServiceException(String.format("Erro ao consultar imóveis no CAR Federal: %s", retorno.mensagem));
		}
		
		return retorno.dados;
		
	}

	public Boolean verificaCondicoesDoImovel(String codigoImovel) {

		String url = Configuracoes.URL_SICAR_IMOVEL_COMPLETO
				.replace("{codigoImovel}", codigoImovel)
				.replace("{completo}", "false");

		HttpResponse response = new WebService().get(url);

		Type type = new TypeToken<MensagemSicar<ImovelCompleto>>(){}.getType();

		MensagemSicar<ImovelCompleto> retorno = gsonBuilder.create().fromJson(response.getJson(), type);

		return response.success() && retorno.dados.podeSolicitarLicenca();
	}
	
	public ImovelSicar getImovelByCodigo(String codigoImovel) {
		
		String url = Configuracoes.URL_SICAR_IMOVEIS_COMPLETOS.replace("{codigoImovel}", codigoImovel);

		HttpResponse response = new WebService().get(url);
		
		if(!response.success()){
			throw new WebServiceException("Erro ao consultar imóveis, Favor entrar em contato com a SEMA para verificação.");
		}
		
		Type type = new TypeToken<MensagemSicar<ImovelSicar>>(){}.getType();

		MensagemSicar<ImovelSicar> retorno = gsonBuilder.create().fromJson(response.getJson(), type);
		
		if(!retorno.status.equals(StatusSiCAR.SUCESSO.sigla)){
			throw new WebServiceException(String.format("Erro ao consultar imóveis no CAR Federal: %s", retorno.mensagem));
		}
		
		return retorno.dados;
		
	}
	
//	TODO: Não está sendo usada. O cast para <T> parece não funcionar.
	private <T> T validarRetornoSicar(HttpResponse response) {
		
		if(!response.success()){
			throw new WebServiceException("Erro ao consultar imóveis no CAR Federal");
		}
		
		Type type = new TypeToken<MensagemSicar<T>>(){}.getType();
		MensagemSicar<T> retorno = gsonBuilder.create().fromJson(response.getJson(), type);
		
		if(!retorno.status.equals(StatusSiCAR.SUCESSO.sigla)){
			throw new WebServiceException(String.format("Erro ao consultar imóveis no CAR Federal: %s", retorno.mensagem));
		}
		
		return retorno.dados;
		
	}

	public JsonElement getTemasByImovel(String codigoImovel, List<String> temas) {

		String listString = "";

		for (String s : temas) {
			
			if(temas.size() > 1)
				listString += s + ",";
			else
				listString = s;
			
		}

		String url = Configuracoes.URL_SICAR_TEMAS_IMOVEIS_COMPLETOS.replace("{codigoImovel}", codigoImovel) + "?temas=" + listString;

		HttpResponse response = new WebService().get(url);
		return response.getJson();

	}
	
	public SobreposicoesSicar getSobreposicoesZEEOuCAR(String url, Map<String, Object> params) {
		
		HttpResponse response = new WebService().post(url, params);
		
		if(!response.success()){
			throw new WebServiceException("Erro ao consultar imóveis no CAR Federal");
		}
		
		Type type = new TypeToken<MensagemSicar<SobreposicoesSicar>>(){}.getType();

		MensagemSicar<SobreposicoesSicar> sobreposicoes = gsonBuilder.create().fromJson(response.getJson(), type);
		
//		Quando necessário verificar as sobreposições, usar esse comando.
//		sobreposicoes.dados.possuiSobreposicaoComTema(SobreposicoesSicar.UNIDADE_CONSERVACAO);

		return sobreposicoes.dados;
		
	}
	
	public SobreposicoesSicar getSobreposicoesMacroZEE(String geometriaWKT) {
		
		String url = Configuracoes.URL_SICAR_SOBREPOSICAO_MZEE;
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("geometriaWKT", geometriaWKT);
		
		return getSobreposicoesZEEOuCAR(url, params);

	}
	
	public SobreposicoesSicar getSobreposicoesTemasCAR(String codigoImovel, Geometry geometria, List<Long> idsTemas) {
		
		String url = Configuracoes.URL_SICAR_SOBREPOSICAO_TEMAS_CAR.replace("{codigoImovel}", codigoImovel);
		
		Map<String, Object> params = new HashMap<String, Object>();
		
		params.put("geometriaWKT", geometria.toText());
		params.put("idsTemas", idsTemas);
		
		SobreposicoesSicar sobreposicoes = getSobreposicoesZEEOuCAR(url, params);
		
		return sobreposicoes.processaSobreposicaoAreaConsolidada(geometria);

	}
	
	public JsonElement getPassivoImovelCAR(Empreendimento empreendimento) {
		
		String url = Configuracoes.URL_SICAR_EXCEDENTE_PASSIVO.replace("{codigoImovel}", empreendimento.imovel.codigo);

		HttpResponse response = new WebService().get(url);
		return response.getJson();
		
	}

	public enum StatusSiCAR {
		
		SUCESSO("s"),
		ERROR("e"),
		ALERTA("a");
		
		public String sigla;

		private StatusSiCAR(String sigla) {
			
			this.sigla = sigla;
		}

	}
	
	public static class MensagemSicar<T> {
		
		public static String SUCESSO = "s";
		
		public String status;
		public String mensagem;
		public T dados;

	}

}
