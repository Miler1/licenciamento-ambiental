package utils;

import play.i18n.Messages;

public enum Mensagem {

	ERRO_PADRAO,
	PERMISSAO_NEGADA,
	CPF_CNPJ_INVALIDO_NAO_INFORMADO,
	ERRO_VALIDACAO,
	PARAMETRO_NAO_INFORMADO,

	CARACTERIZACAO_CADASTRADA_SUCESSO,
	CARACTERIZACAO_RESPOSTAS_NAO_PERMITE_LICENCIAMENTO,
	CARACTERIZACAO_COORDENADAS_FORA_DO_MUNICIPIO,
	CARACTERIZACAO_COORDENADAS_FORA_DO_ESTADO,
	CARACTERIZACAO_COORDENADAS_FORA_DO_IMOVEL,
	CARACTERIZACAO_COORDENADAS_FORA_DO_EMPREENDIMENTO,
	CARACTERIZACAO_PERGUNTAS_NAO_RESPONDIDAS,
	CARACTERIZACAO_RESPOSTAS_INCONSISTENTES,
	SOLICITACOES_DOCUMENTO_NAO_ATENDIDAS,
	CARACTERIZACAO_DECLARACAO_VERACIDADE_INFORMACOES_PENDENTE,
	SOLICITACAO_DOCUMENTO_NAO_ENCONTRADA,
	DOCUMENTO_DA_SOLICITACAO_NAO_ENCONTRADO,
	DOCUMENTO_DA_ATIVIDADE_NAO_ENCONTRADO,
	DOCUMENTO_REMOVIDO_SUCESSO,
	CARACTERIZACAO_TIPO_LICENCA_OBRIGATORIO,
	CARACTERIZACAO_SIMPLIFICADO_TIPO_LICENCA_INVALIDO,
	TIPO_LOCALIZACAO_EMPREENDIMENTO_INVALIDO_LAR,
	CARACTERIZACAO_TIPO_LICENCA_REPETIDO,
	CARACTERIZACAO_NAO_AGUARDANDO_EMISSAO_DAE,
	CARACTERIZACAO_DAE_EMITIDO_SUCESSO,
	CARACTERIZACAO_ERRO_EMISSAO_DAE,
	CARACTERIZACAO_AREA_VETORIZADA_DIFERENTE_PARAMETRO,
	CARACTERIZACAO_ATIVIDADE_NAO_PERTENCE_TIPO_LICENCA,
	CARACTERIZACAO_ENQUADRAMENTO_ORDINARIO,
	CARACTERIZACAO_FINALIZADA_SUCESSO,
	CARACTERIZACAO_REMOVIDA_SUCESSO,
	CARACTERIZACAO_REMOVIDA_ERRO,
	CARACTERIZACAO_OCULTAR_LISTAGEM,
	CARACTERIZACAO_NUMERO_OUTORGA_OBRIGATORIO,
	CARACTERIZACAO_NUMERO_OUTORGA_INVALIDO_VENCIDO,
	CARACTERIZACAO_ATIVIDADES_DENTRO_E_FORA_EMPREENDIMENTO,
	ATIVIDADE_LICENCIAMENTO_MUNICIPAL,
	ENDERECOS_EXTRAS_NAO_PERMITIDOS,
	EMPREENDIMENTO_CADASTRADO_SUCESSO,
	EMPREENDIMENTO_ATUALIZADO_SUCESSO,
	EMPREENDIMENTO_EXCLUIDO_SUCESSO,
	EMPREENDIMENTO_JA_EXISTENTE,
	EMPREENDIMENTO_JA_CADASTRADO,
	EMPREENDIMENTO_NAO_ENCONTRADO,
	EMPREENDIMENTO_COORDENADAS_FORA_DO_MUNICIPIO,
	EMPREENDIMENTO_COORDENADAS_FORA_DO_IMOVEL,
	EMPREENDIMENTO_IMOVEL_NAO_VINCULADO_AO_CPF_CNPJ,
	CPF_CNPJ_NAO_VINCULADO_EMPREENDEDOR,
	CPF_CNPJ_NAO_ENCONTRADO_EMPREENDEDOR,
	EMPREENDIMENTO_RESPONSAVEL_DOCUMENTO_OBRIGATORIO,
	EMPREENDIMENTO_RESPONSAVEL_LEGAL_OBRIGATORIO,
	EMPREENDIMENTO_NAO_REMOVIVEL_POSSUI_CARACTERIZACOES_VINCULADAS,
	EMPREENDIMENTO_PROPRIETARIO_REPETIDO,
	EMPREENDIMENTO_PROPRIETARIO_OBRIGATORIO,
	EMPRENDIMENTO_ZONA_RURAL_SEM_IMOVEL,
	EMPREENDEDOR_USUARIO_NAO_REPRESENTANTE,
	EMPREENDEDOR_REPRESENTANTE_REPETIDO,
	CODIGO_IMOVEL_INVALIDO_NAO_INFORMADO,
	CONDICAO_DO_CAR_NAO_PERMITE_SOLICITACAO,
	UPLOAD_DOCUMENTO,
	UPLOAD_ERRO,
	UPLOAD_EXTENSAO_NAO_SUPORTADA,
	UPLOAD_TAMANHO_DOCUMENTO,
	EMPREENDIMENTO_FORA_DO_ESTADO,
	ERRO_AREA_CONSOLIDADA_CLASSIFICADA_SEMAS,
	SOMENTE_PESSOA_FISICA_PODE_EMITIR_DLA,
	ERRO_PROCESSAR_DLA,
	NAO_FORAM_ENCONTRADAS_TODAS_LICENCAS,
	TELEFONE_OU_CELULAR_MAIOR_DO_QUE_PERMITIDO,
	CONTATO_NULO,
	LICENCA_CANCELADA_OU_SUSPENSA, 
	TIPOS_CARACTERIZACAOES_DIFERENTES,
	JUSTIFICATIVA_ADICIONADA_SUCESSO,
	JUSTIFICATIVA_REMOVIDA_SUCESSO,
	LEITURA_REGISTRADA_SUCESSO,
	ATUALIZACAO_EMPREENDIMENTO_EMPREENDEDOR_NECESSARIA,
	EMAIL_INVALIDO_OU_NULO_EU,
	PESSOA_SEM_ENDERECO,
	PARAMETROS_INSUFICIENTES_PARA_CALCULO,
	PORTE_INDISPONIVEL_PARA_ATIVIDADE,

	LICENCA_CANCELADA_SUCESSO,
    DAE_NAO_PAGA,

	ERRO_ENVIAR_EMAIL,

	LOGIN_ENTRADA_UNICA,
	ENTRADA_UNICA_FALHA_COMUNICACAO,

	USUARIO_SEM_CADASTRO_ENTRADA_UNICA,

	ALP_NAO_PODE_SER_ATUALIZADA,
	TIPO_DE_RENOVACAO_DE_LICENCA_NAO_PERMITIDO,

	ATIVIDADE_DE_ORIGEM_NAO_PODE_SER_BLOQUEDA,

	SEFAZ_INTEGRACAO_COMUNICACAO_FALHA;

    public String getTexto(Object ... args) {

		return Messages.get(name(), args);
	}

}
