package serializers;

import flexjson.JSONSerializer;
import utils.SerializerUtil;

public class EmpreendedorSerializer {
	
	public static JSONSerializer getRepresentantes = SerializerUtil.create(
			"id",
			"dataVinculacao",
			"pessoa.id",
			"pessoa.type",
			"pessoa.cpf",
			"pessoa.nome",
			"pessoa.contato.email",
			"pessoa.contato.telefone",
			"pessoa.contato.celular",
			"pessoa.contato.id");
	
	public static JSONSerializer findByCpfCnpj = SerializerUtil.create(
			"id",
			"esfera",
			"pessoa.id",
			"pessoa.tipo.codigo",
			"pessoa.nome",
			"pessoa.cpf",
			"pessoa.rg.numero",
			"pessoa.nomeMae",
			"pessoa.dataNascimento",
			"pessoa.sexo.codigo",
			"pessoa.sexo.nome",
			"pessoa.sexo.descricao",
			"pessoa.tituloEleitoral.numero",
			"pessoa.tituloEleitoral.secao",
			"pessoa.tituloEleitoral.zona",
			"pessoa.estadoCivil.codigo",
			"pessoa.estadoCivil.descricao",
			"pessoa.naturalidade",
			"pessoa.cnpj",
			"pessoa.razaoSocial",
			"pessoa.nomeFantasia",
			"pessoa.inscricaoEstadual",
			"pessoa.dataConstituicao",
			"pessoa.municipio.id",
			"pessoa.municipio.nome",
			"pessoa.municipio.estado.nome",
			"pessoa.municipio.estado.sigla",
			"pessoa.municipio.estado.id",
			"pessoa.municipio.codigoIbge",
			"pessoa.contatos.id",
			"pessoa.contatos.principal",
			"pessoa.contatos.valor",
			"pessoa.contatos.tipo.id",
			"pessoa.contatos.tipo.descricao",
			"pessoa.enderecos.id",
			"pessoa.enderecos.tipo.id",
			"pessoa.enderecos.cep",
			"pessoa.enderecos.logradouro",
			"pessoa.enderecos.numero",
			"pessoa.enderecos.semNumero",
			"pessoa.enderecos.zonaLocalizacao.codigo",
			"pessoa.enderecos.complemento",
			"pessoa.enderecos.bairro",
			"pessoa.enderecos.caixaPostal",
			"pessoa.enderecos.roteiroAcesso",
			"pessoa.enderecos.municipio.id",
			"pessoa.enderecos.municipio.nome",
			"pessoa.enderecos.municipio.estado.sigla",
			"pessoa.enderecos.municipio.estado.nome",
			"pessoa.enderecos.municipio.codigoIbge",
			"pessoa.enderecos.correspondencia"
		);
}
