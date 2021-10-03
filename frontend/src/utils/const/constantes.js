var tiposLicencaSimplificado = {
	LP: {id: 2, nome: 'LP - Licença Prévia'},
	LI: {id: 3, nome: 'LI - Licença de Instalação'},
	LO: {id: 4, nome: 'LO - Licença de Operação'},
	LAU: {id: 5, nome: 'LAU - Licença Ambiental Rural'},
	CAD: {id: 10, nome: 'CAD - Cadastro'}
};

var tiposLicencaAtualizacao = {
	ALP: {id: 8, nome: 'ALP - Atualização de Licença Prévia'}
};

var tiposLicencaRenovacao = {
	RLO: {id: 7, nome: 'RLO - Renovação de Licença de Operação'},
	RLI: {id: 6, nome: 'RLI - Renovação de Licença de Instalação'},
	RLAU: {id: 9, nome: 'RLAU - Renovação de Licença Ambiental Única'}
};

var acoesFluxoLicenca = {
	renovacao: {const: 'renovacao',descricao:'Renovar solicitação para '},
	atualizacao: {const: 'atualizacao',descricao:'Atualizar solicitação para '},
	solicitacao: {const:'solicitacao',descricao:'Nova fase da solicitação para '}
};

var tiposLicencaDispensa = {
	DDLA: {id: 1, nome: 'DDLA - Declaração de Dispensa de Licenciamento Ambiental'},
};

var statusCaracterizacao = {
	FINALIZADO: {id: 1, nome: 'Finalizado'},
	EM_ANDAMENTO: {id: 2, nome: 'Em andamento'},
	AGUARDANDO_EMISSAO_TAXA_EXPEDIENTE: {id: 3, nome: 'Aguardando emissão da Taxa de Expediente'},
	AGUARDANDO_QUITACAO_TAXA_EXPEDIENTE: {id: 4, nome: 'Aguardando quitação da Taxa de Expediente'},
	EM_ANALISE: {id: 5, nome: 'Em Análise'},
	ARQUIVADO: {id: 6, nome: 'Arquivado'},
	SUSPENSO: {id: 7, nome: 'Suspenso'},
	CANCELADO: {id: 8, nome: 'Cancelado'},
	NOTIFICADO: {id: 9, nome: 'Notificado'},
	EM_ANALISE_RENOVACAO_SEM_ALTERACOES: {id: 10, nome: 'Em análise de renovação'},
	EM_ANALISE_RENOVACAO_COM_ALTERACOES: {id: 11, nome: 'Em análise de renovação'},
	VENCIDO: {id: 12, nome: 'Vencido'},
	VENCIDO_AGUARDANDO_PAGAMENTO_TAXA_EXPEDIENTE: {id: 13, nome: 'Vencido, aguardando identificação do pagamento da Taxa de Expediente'},
	VENCIDO_AGUARDANDO_EMISSAO_TAXA_EXPEDIENTE: {id: 14, nome: 'Vencido, aguardando emissão da Taxa de Expediente'},
	ANALISE_APROVADA: {id: 15, nome: 'Análise aprovada'},
	AGUARDANDO_EMISSAO_TAXA_LICENCIAMENTO: {id: 16, nome: 'Aguardando emissão da Taxa de Licenciamento'},
	AGUARDANDO_QUITACAO_TAXA_LICENCIAMENTO: {id: 17, nome: 'Aguardando quitação da Taxa de Licenciamento'},
	VENCIDO_AGUARDANDO_PAGAMENTO_TAXA_LICENCIAMENTO: {id: 18, nome: 'Vencido, aguardando identificação do pagamento da Taxa de Licenciamento'},
	VENCIDO_AGUARDANDO_EMISSAO_TAXA_LICENCIAMENTO: {id: 19, nome: 'Vencido, aguardando emissão da Taxa de Licenciamento'},
	ANALISE_REJEITADA: {id: 20, nome: 'Análise rejeitada'},
	NOTIFICADO_EM_ANDAMENTO: {id: 21, nome: 'Notificação em andamento'},
	AGUARDANDO_DOCUMENTACAO: {id: 23, nome: 'Notificação em andamento'},
	NOTIFICACAO_ATENDIDA: {id: 24, nome: 'Em Análise'},
	NOTIFICADO_EMPREENDIMENTO_ALTERADO: {id: 25, nome: 'Empreendimento alterado à partir de notificação'},
	NOTIFICADO_GEO_FINALIZADA: {id: 26, nome: 'Notificação geo finalizada'}
};

var tiposLicencas = {
	DECLARACAO_DE_INEXIGIBILIDADE: {id: 1, nome: 'DI - Declaração de Inexigibilidade'},
	LICENCA_PREVIA: {id: 2, nome: 'LP - Licença Prévia'},
	LICENCA_DE_INSTALACAO: {id: 3, nome: 'LI - Licença de Instalação'},
	LICENCA_DE_OPERACAO: {id: 4, nome: 'LO - Licença de Operação'},
	LICENCA_AMBIENTAL_UNICA: {id: 5, nome: 'LAU - Licença Ambiental Única'},
	RENOVAO_DE_LICENCA_DE_INSTALACAO: {id: 6, nome: 'RLI - Renovação de Licença de Instalação'},
	RENOVAO_DE_LICENCA_DE_OPERACAO: {id: 7, nome: 'RLO - Renovação de Licença de Operação'},
	ATUALIZACAO_LICENCA_PREVIA: {id: 8, nome: 'ALP - Atualização de Licença Prévia'},
	RENOVACAO_DE_LICENCA_UNICA: {id: 9, nome: 'LAU - Renovação de Licença Ambiental Única'},
	CADASTRO: {id: 10, nome: 'CA - Cadastro de Aquicultura'}
};

var atividadesPermitemIntervencaoAPP = {
	INSTALACAO_PORTUARIA: '0474',
	RAMPA_MOVIMENTACAO_CARGA: '0475'
};

var tiposPergunta = {
	VALIDA_APP: 'VALIDA_APP'
};

var localizacoesEmpreendimento = {
	URBANA: 0,
	RURAL: 1
};

var tiposRepresentanteLegal = {
	REPRESENTANTE: 'REPRESENTANTE',
	PROPRIETARIO: 'PROPRIETARIO',
	ARRENDATARIO: 'ARRENDATARIO'
};

var tiposResponsaveis = {
	LEGAL: 'LEGAL',
	TECNICO: 'TECNICO'
};

var tipos_outorga_ana = {

	ID_PERGUNTA_COPIA_OUTORGA_ANA_P23: 123,
	ID_PERGUNTA_COPIA_OUTORGA_ANA_P16: 124,
	ID_PERGUNTA_COPIA_OUTORGA_ANA_P3: 125
};

var tipoCaracterizacao = {

	SIMPLIFICADO: {id: 3, nome: 'Simplificado'},
};

var atividadeComRestricaoDLA = {

	ID_ENERGIA_REDE_DISTRIBUICAO:78,
	ID_ENERGIA_SUBDISTRICAO_URBANA:79,
	ID_ENERGIA_SUBDISTRICAO_RURAL:80,
	ID_SANEAMENTO_SISTEMA_SIMPLIFICADO:95,
	ID_SANEAMENTO_DRENAGEM_SUPERFICIAL:96,
	ID_PAVIMENTACAO_EXECUCAO:91,
	ID_RODOVIAS_RAMAIS_ESTRADA_VICINAL:92,
	ID_RODOVIAS_RAMAIS_SUBSTITUICAO_ESTRADA_VICINAL:9
};

var atividadeComRestricaoSimplificado = {
	PESQUISA_MINERAL:35
};

var tipologiasQuePermitemMaisDeUmaAtividade = {
	ID_AGROSSILVIPASTORIL:1
};

var tipologias = {
	INDUSTRIA_BORRACHA: 'INDUSTRIA_BORRACHA',
	INDUSTRIA_COURO_PELES: 'INDUSTRIA_COURO_PELES',
	INDUSTRIA_QUIMICA: 'INDUSTRIA_QUIMICA',
	INDUSTRIA_TEXTIL: 'INDUSTRIA_TEXTIL',
	INDUSTRIA_PRODUTOS_ALIMENTARES: 'INDUSTRIA_PRODUTOS_ALIMENTARES',
	INDUSTRIA_BEBIDAS_ALCOOL: 'INDUSTRIA_BEBIDAS_ALCOOL'
};

var tipoContato = {
	EMAIL: 1,
	TELEFONE_RESIDENCIAL: 2,
	TELEFONE_COMERCIAL: 3,
	TELEFONE_CELULAR: 4
};

var tipoEndereco = {
	PRINCIPAL: 1,
	CORRESPONDENCIA: 2

};

var tipoPessoa = {
	PESSOA_FISICA: 0,
	PESSOA_JURIDICA: 1

};

var estadoCivilEU = {
	SOLTEIRO: 0,
	CASADO: 1,
	DIVORCIADO: 2,
	VIUVO: 3,
	SEPARADO: 4,
	UNIAO_ESTAVEL: 5 
};

exports.TIPOS_LICENCA_SIMPLIFICADO = tiposLicencaSimplificado;
exports.TIPOS_LICENCA_DISPENSA = tiposLicencaDispensa;
exports.TIPOS_LICENCA_RENOVACAO = tiposLicencaRenovacao;
exports.TIPOS_LICENCA_ATUALIZACAO = tiposLicencaAtualizacao;
exports.STATUS_CARACTERIZACAO = statusCaracterizacao;
exports.TIPOS_LICENCAS = tiposLicencas;
exports.ATIVIDADES_PERMITEM_INTERVENCAO_APP = atividadesPermitemIntervencaoAPP;
exports.TIPOS_PERGUNTA = tiposPergunta;
exports.LOCALIZACOES_EMPREENDIMENTO = localizacoesEmpreendimento;
exports.TIPOS_REPRESENTANTE_LEGAL = tiposRepresentanteLegal;
exports.TIPOS_RESPONSAVEIS = tiposResponsaveis;
exports.TIPO_CARACTERIZACAO = tipoCaracterizacao;
exports.TIPOS_OUTORGA_ANA = tipos_outorga_ana;
exports.ATIVIDADE_COM_RESTRICAO_DLA = atividadeComRestricaoDLA;
exports.ATIVIDADE_COM_RESTRICAO_SIMPLIFICADO = atividadeComRestricaoSimplificado;
exports.TIPOLOGIAS_QUE_PERMITEM_MAIS_DE_UMA_ATIVIDADE = tipologiasQuePermitemMaisDeUmaAtividade;
exports.ACOES_FLUXO_LICENCA = acoesFluxoLicenca;
exports.TIPOLOGIAS = tipologias;
exports.TIPO_CONTATO = tipoContato;
exports.TIPO_ENDERECO = tipoEndereco;
exports.TIPO_PESSOA = tipoPessoa;
exports.ESTADO_CIVIL_EU = estadoCivilEU;
