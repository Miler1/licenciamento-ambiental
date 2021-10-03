package serializers;

import utils.SerializerUtil;
import flexjson.JSONSerializer;

public class PessoasSerializer {
	
	public static JSONSerializer findByCpfCnpj = SerializerUtil.create(
			"id",
			"nome",
			"cpf",
			"tipo.codigo",
			"tipo.nome",
			"rg.numero",
			"nomeMae",
			"dataNascimento",
			"sexo.descricao",
			"sexo.codigo",
			"sexo.nome",
			"tituloEleitoral.numero",
			"tituloEleitoral.zona",
			"tituloEleitoral.secao",
			"estadoCivil.id",
			"estadoCivil.nome",
			"estadoCivil.codigo",
			"naturalidade",
			"cnpj",
			"razaoSocial",
			"nomeFantasia",
			"inscricaoEstadual",
			"dataConstituicao",
			"esfera",
			"contatos.id",
			"contatos.tipo.id",
			"contatos.tipo.descricao",
			"contatos.valor",
			"contatos.principal",
			"enderecos.id",
			"enderecos.tipo.id",
			"enderecos.tipo.nome",
			"enderecos.cep",
			"enderecos.logradouro",
			"enderecos.numero",
			"enderecos.semNumero",
			"enderecos.complemento",
			"enderecos.bairro",
			"enderecos.caixaPostal",
			"enderecos.roteiroAcesso",
			"enderecos.municipio.id",
			"enderecos.municipio.nome",
			"enderecos.municipio.estado.sigla",
			"enderecos.municipio.estado.nome",
			"enderecos.zonaLocalizacao.codigo",
			"enderecos.zonaLocalizacao.nome",
			"enderecos.correspondencia"
		);
	
	public static JSONSerializer findByCpf = SerializerUtil.create(
			"id",
			"type",
			"idEntradaUnica",
			"nome",
			"cpf",
			"contato.email");

}
