package models.correios;

import models.Endereco;

public class Correio{
	
	private static final String DISTRITO = "D";
	
	private static Endereco getEnderecoLocalidadeByCep(Integer cep){
		
		Localidade localidade = Localidade.find("byCep", cep).first();

		if (localidade == null)
			return null;
		
		Endereco endereco = new Endereco();
		endereco.cep = cep.toString();
		
		if (localidade.municipio == null && localidade.tipoLocalidade.equals(DISTRITO)){

			if (localidade.localidadePai != null && localidade.localidadePai.municipio != null)
				endereco.municipio = localidade.localidadePai.municipio.toMunicipioEU();
			
		}else{
			endereco.municipio = localidade.municipio.toMunicipioEU();
		}
		
		return endereco;
	}

	private static Endereco getEnderecoLogradouroByCep(Integer cep){
		
		Logradouro logradouro = Logradouro.find("byCep", cep).first();
		
		if (logradouro == null)
			return null;
		
		Endereco endereco = new Endereco();
		
		if (logradouro.localidade.municipio == null && logradouro.localidade.tipoLocalidade == DISTRITO){

			if (logradouro.localidade.localidadePai != null && logradouro.localidade.localidadePai.municipio != null)
				endereco.municipio = logradouro.localidade.localidadePai.municipio.toMunicipioEU();
			
		}else{
			endereco.municipio = logradouro.localidade.municipio.toMunicipioEU();
		}
		
		endereco.cep = cep.toString();
		endereco.bairro = logradouro.bairro.nome;
		endereco.logradouro = logradouro.nome;
		return endereco;
		
	}

	private static Endereco getEnderecoGrandeUsuarioByCep(Integer cep){
		
		GrandeUsuario grandeUsuario = GrandeUsuario.find("byCep", cep).first();
		
		if (grandeUsuario == null)
			return null;
		
		Endereco endereco = new Endereco();
		
		if (grandeUsuario.logradouro.localidade.municipio == null && grandeUsuario.logradouro.localidade.tipoLocalidade == DISTRITO){

			if (grandeUsuario.logradouro.localidade.localidadePai != null && grandeUsuario.logradouro.localidade.localidadePai.municipio != null)
				endereco.municipio = grandeUsuario.logradouro.localidade.localidadePai.municipio.toMunicipioEU();
			
		}else{
			endereco.municipio = grandeUsuario.logradouro.localidade.municipio.toMunicipioEU();
		}
		
		endereco.cep = cep.toString();

		if (grandeUsuario.logradouro != null){
			endereco.bairro = grandeUsuario.logradouro.bairro.nome;
			endereco.logradouro = grandeUsuario.logradouro.nome;
			
		}
		
		return endereco;
		
	}
	
	private static Endereco getEnderecoUnidadeOperacionalByCep(Integer cep){
		
		UnidadeOperacional unidadeOperacional = UnidadeOperacional.find("byCep", cep).first();
		
		if (unidadeOperacional == null || unidadeOperacional.logradouro == null)
			return null;
		
		Endereco endereco = new Endereco();
		
		if (unidadeOperacional.logradouro.localidade.municipio == null && unidadeOperacional.logradouro.localidade.tipoLocalidade == DISTRITO){

			if (unidadeOperacional.logradouro.localidade.localidadePai != null && unidadeOperacional.logradouro.localidade.localidadePai.municipio != null)
				endereco.municipio = unidadeOperacional.logradouro.localidade.localidadePai.municipio.toMunicipioEU();
			
		}else{
			endereco.municipio = unidadeOperacional.logradouro.localidade.municipio.toMunicipioEU();
		}
		
		
		
		endereco.cep = cep.toString();

		if (unidadeOperacional.logradouro != null){
			endereco.bairro = unidadeOperacional.logradouro.bairro.nome;
			endereco.logradouro = unidadeOperacional.logradouro.nome;
		}
		
		return endereco;
		
	}
	
	private static Endereco getEnderecoPorFaixaDeCep(Integer cep){
		
		Endereco endereco = new Endereco();
		
		LocalidadeFaixa localidadeFaixa = LocalidadeFaixa.find("cepInicio <= :cep AND cepFim >= :cep")
				.setParameter("cep", cep).first();
		
		if (localidadeFaixa == null || localidadeFaixa.id == null){
			return null;
		}
		
		Localidade localidade = Localidade.findById(localidadeFaixa.id);
		
		endereco.cep = cep.toString();
		
		if (localidade.municipio == null && localidade.tipoLocalidade == DISTRITO){

			if (localidade.localidadePai != null && localidade.localidadePai.municipio != null)
				endereco.municipio = localidade.localidadePai.municipio.toMunicipioEU();
			
		}else{
			endereco.municipio = localidade.municipio.toMunicipioEU();
		}
		
		return endereco;
		
	}
	
	public static Endereco getEnderecoByCep(Integer cep){
		
		Endereco endereco = getEnderecoLocalidadeByCep(cep);
		
		if (endereco != null)
			return endereco;
		
		endereco = getEnderecoLogradouroByCep(cep);
		
		if (endereco != null)
			return endereco;
		
		endereco = getEnderecoUnidadeOperacionalByCep(cep);
		
		if (endereco != null)
			return endereco;
		
		endereco = getEnderecoPorFaixaDeCep(cep);
		
		if (endereco != null)
			return endereco;
		
		return null;
	}
	
}
