package models.caracterizacao;

import play.db.jpa.GenericModel;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(schema = "licenciamento", name = "status_caracterizacao")
public class StatusCaracterizacao extends GenericModel {

	public static final Long DEFERIDO = 1l;
	public static final Long EM_ANDAMENTO = 2l;
	public static final Long AGUARDANDO_EMISSAO_TAXA_EXPEDIENTE = 3l;
	public static final Long AGUARDANDO_QUITACAO_TAXA_EXPEDIENTE = 4l;
	public static final Long EM_ANALISE = 5l;
	public static final Long ARQUIVADO = 6l;
	public static final Long SUSPENSO = 7l;
	public static final Long CANCELADO = 8l;
	public static final Long NOTIFICADO = 9l;
	public static final Long EM_RENOVACAO_SEM_ALTERACAO = 10l;
	public static final Long EM_RENOVACAO_COM_ALTERACAO = 11l;
	public static final Long VENCIDO = 12L;
	public static final Long VENCIDO_AGUARDANDO_PAGAMENTO_TAXA_EXPEDIENTE = 13L;
	public static final Long VENCIDO_AGUARDANDO_EMISSAO_TAXA_EXPEDIENTE = 14L;
	public static final Long ANALISE_APROVADA = 15L;
	public static final Long AGUARDANDO_EMISSAO_TAXA_LICENCIAMENTO = 16L;
	public static final Long AGUARDANDO_QUITACAO_TAXA_LICENCIAMENTO = 17L;
	public static final Long VENCIDO_AGUARDANDO_PAGAMENTO_TAXA_LICENCIAMENTO = 18L;
	public static final Long VENCIDO_AGUARDANDO_EMISSAO_TAXA_LICENCIAMENTO = 19L;
	public static final Long ANALISE_REJEITADA = 20L;
	public static final Long NOTIFICADO_EM_ANDAMENTO = 21L;
	public static final Long NOTIFICADO_HISTORICO = 22L;
	public static final Long AGUARDANDO_DOCUMENTACAO = 23L;
	public static final Long NOTIFICACAO_ATENDIDA = 24L;
	public static final Long NOTIFICADO_EMPREENDIMENTO_ALTERADO = 25L;
	public static final Long NOTIFICADO_GEO_FINALIZADA = 26L;

	@Id
	public Long id;

	@Column
	public String nome;

	@Column
	public String codigo;

}