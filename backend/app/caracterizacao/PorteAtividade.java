package models.caracterizacao;

import exceptions.ValidacaoException;
import play.db.jpa.Model;
import utils.Mensagem;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "porte_atividade")
public class PorteAtividade extends Model {

	@Column(name="codigo")
	public Integer codigo;

	@ManyToOne
	@JoinColumn(name = "id_porte_empreendimento")
	public PorteEmpreendimento porteEmpreendimento;

	@ManyToOne
	@JoinColumn(name = "id_parametro_um")
	public ParametroAtividade parametroUm;

	@ManyToOne
	@JoinColumn(name = "id_parametro_dois")
	public ParametroAtividade parametroDois;

	@Column(name = "limite_inferior_um")
	public Double limiteInferiorParametroUm;

	@Column(name = "limite_inferior_dois")
	public Double limiteInferiorParametroDois;

	@Column(name = "limite_superior_um")
	public Double limiteSuperiorParametroUm;

	@Column(name = "limite_superior_dois")
	public Double limiteSuperiorParametroDois;

	@Column(name = "limite_inferior_um_incluso")
	public Boolean limiteInferiorParametroUmIncluso;

	@Column(name = "limite_inferior_dois_incluso")
	public Boolean limiteInferiorParametroDoisIncluso;

	@Column(name = "limite_superior_um_incluso")
	public Boolean limiteSuperiorParametroUmIncluso;

	@Column(name = "limite_superior_dois_incluso")
	public Boolean limiteSuperiorParametroDoisIncluso;

	public static PorteEmpreendimento calcularPorteEmpreendimento(AtividadeCaracterizacao atividadeCaracterizacao) {

		if (atividadeCaracterizacao.atividade.portesAtividade == null || atividadeCaracterizacao.atividade.portesAtividade.isEmpty()) {
			throw new ValidacaoException(Mensagem.PORTE_INDISPONIVEL_PARA_ATIVIDADE);
		}

		AtividadeCaracterizacaoParametros parametroUm = null;
		AtividadeCaracterizacaoParametros parametroDois = null;

		//Todas as entradas de porte são iguais para cada parâmetro, independente de qual seja o limite superior e inferior.
		for (AtividadeCaracterizacaoParametros acp : atividadeCaracterizacao.atividadeCaracterizacaoParametros) {

			if (acp.parametroAtividade.equals(atividadeCaracterizacao.atividade.portesAtividade.get(0).parametroUm)) {
				parametroUm = acp;
			} else if (acp.parametroAtividade.equals(atividadeCaracterizacao.atividade.portesAtividade.get(0).parametroDois)) {
				parametroDois = acp;
			}

		}
		if(parametroUm == null && parametroDois != null) {
			parametroUm = parametroDois;
			parametroDois = null;
		}

		for (PorteAtividade porteDisponivelParaAtividade : atividadeCaracterizacao.atividade.portesAtividade) {

			boolean limite_inferior_um_incluso_e_superior_um_nao_incluso = false;
			boolean limite_inferior_um_incluso_e_superior_um_incluso = false;
			boolean limite_inferior_um_nao_incluso_e_superior_um_nao_incluso = false;
			boolean limite_inferior_um_nao_incluso_e_superior_um_incluso = false;

			boolean limite_inferior_um_nulo_e_superior_um_nao_incluso = false;
			boolean limite_inferior_um_nao_incluso_e_superior_um_nulo = false;
			boolean limite_inferior_um_nulo_e_superior_um_incluso = false;
			boolean limite_inferior_um_incluso_e_superior_um_nulo = false;

			boolean limite_inferior_um_nulo_e_superior_um_nulo = false;

			//Atribui valor às variáveis com nomes intuitivos para verificação
			if(
					(porteDisponivelParaAtividade.limiteInferiorParametroUm == null &&
							porteDisponivelParaAtividade.limiteSuperiorParametroUm == null) &&
							(!porteDisponivelParaAtividade.limiteInferiorParametroUmIncluso &&
									!porteDisponivelParaAtividade.limiteSuperiorParametroUmIncluso)
			){
				limite_inferior_um_nulo_e_superior_um_nulo = true;
			}
			else if(
					(porteDisponivelParaAtividade.limiteInferiorParametroUm == null &&
							porteDisponivelParaAtividade.limiteSuperiorParametroUm != null) &&
							(!porteDisponivelParaAtividade.limiteInferiorParametroUmIncluso &&
									!porteDisponivelParaAtividade.limiteSuperiorParametroUmIncluso) &&
							(porteDisponivelParaAtividade.limiteSuperiorParametroUm > parametroUm.valorParametro)
			){
				limite_inferior_um_nulo_e_superior_um_nao_incluso = true;
			}
			else if(
					(porteDisponivelParaAtividade.limiteInferiorParametroUm == null &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUm != null) &&
					(!porteDisponivelParaAtividade.limiteInferiorParametroUmIncluso &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUmIncluso) &&
					(porteDisponivelParaAtividade.limiteSuperiorParametroUm >= parametroUm.valorParametro)
			){
				limite_inferior_um_nulo_e_superior_um_incluso = true;
			}
			else if(
					(porteDisponivelParaAtividade.limiteInferiorParametroUm != null &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUm == null) &&
					(!porteDisponivelParaAtividade.limiteInferiorParametroUmIncluso &&
					!porteDisponivelParaAtividade.limiteSuperiorParametroUmIncluso) &&
					(porteDisponivelParaAtividade.limiteInferiorParametroUm < parametroUm.valorParametro)
			) {
				limite_inferior_um_nao_incluso_e_superior_um_nulo = true;
			}
			else if(
					(porteDisponivelParaAtividade.limiteInferiorParametroUm != null &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUm == null) &&
					(porteDisponivelParaAtividade.limiteInferiorParametroUmIncluso &&
					!porteDisponivelParaAtividade.limiteSuperiorParametroUmIncluso) &&
					(porteDisponivelParaAtividade.limiteInferiorParametroUm <= parametroUm.valorParametro)
			){
				limite_inferior_um_incluso_e_superior_um_nulo = true;
			}
			else if(
					(porteDisponivelParaAtividade.limiteInferiorParametroUm != null &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUm != null) &&
					(porteDisponivelParaAtividade.limiteInferiorParametroUmIncluso &&
					!porteDisponivelParaAtividade.limiteSuperiorParametroUmIncluso) &&
					(porteDisponivelParaAtividade.limiteInferiorParametroUm <= parametroUm.valorParametro &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUm > parametroUm.valorParametro)
			) {
				limite_inferior_um_incluso_e_superior_um_nao_incluso = true;
			}
			else if(
					(porteDisponivelParaAtividade.limiteInferiorParametroUm != null &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUm != null) &&
					(!porteDisponivelParaAtividade.limiteInferiorParametroUmIncluso &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUmIncluso) &&
					(porteDisponivelParaAtividade.limiteInferiorParametroUm < parametroUm.valorParametro &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUm >= parametroUm.valorParametro)
			) {
				limite_inferior_um_nao_incluso_e_superior_um_incluso = true;
			}
			else if(
					(porteDisponivelParaAtividade.limiteInferiorParametroUm != null &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUm != null) &&
					(!porteDisponivelParaAtividade.limiteInferiorParametroUmIncluso &&
					!porteDisponivelParaAtividade.limiteSuperiorParametroUmIncluso) &&
					(porteDisponivelParaAtividade.limiteInferiorParametroUm < parametroUm.valorParametro) &&
					(porteDisponivelParaAtividade.limiteSuperiorParametroUm > parametroUm.valorParametro)
			) {
				limite_inferior_um_nao_incluso_e_superior_um_nao_incluso = true;
			}
			else if(
					(porteDisponivelParaAtividade.limiteInferiorParametroUm != null &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUm != null) &&
					(porteDisponivelParaAtividade.limiteInferiorParametroUmIncluso &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUmIncluso) &&
					(porteDisponivelParaAtividade.limiteInferiorParametroUm <= parametroUm.valorParametro &&
					porteDisponivelParaAtividade.limiteSuperiorParametroUm >= parametroUm.valorParametro)
			) {
				limite_inferior_um_incluso_e_superior_um_incluso = true;
			}

			//Se nenhum parametro
			if ((parametroUm == null) && (parametroDois == null)) {

				if (
						limite_inferior_um_nulo_e_superior_um_nulo
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
			}

			//Se apenas um parametro
			else if ((parametroUm != null) && (parametroDois == null)) {

				//Se limite inferior um incluso e superior um não incluso
				// <= > || <= null
				if (
					limite_inferior_um_incluso_e_superior_um_nao_incluso ||
					limite_inferior_um_incluso_e_superior_um_nulo
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}

				//Se limite inferior um não incluso e superior um não incluso
				// < > || null > || null >
				if (
					limite_inferior_um_nao_incluso_e_superior_um_nao_incluso ||
					limite_inferior_um_nao_incluso_e_superior_um_nulo ||
					limite_inferior_um_nulo_e_superior_um_nao_incluso
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}

				//Se limite inferior incluso um e limite superior um incluso
				// <= >=
				if (
						limite_inferior_um_incluso_e_superior_um_incluso
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}

				//Se limite superior um não incluso um e limite superior um incluso
				// < >= || null >=
				if (
						limite_inferior_um_nao_incluso_e_superior_um_incluso ||
						limite_inferior_um_nulo_e_superior_um_incluso
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
			}

			//Se dois parametros
			else if(parametroUm != null && parametroDois != null) {

				boolean limite_inferior_dois_incluso_e_superior_dois_nao_incluso = false;
				boolean limite_inferior_dois_nao_incluso_e_superior_dois_nao_incluso = false;
				boolean limite_inferior_dois_incluso_e_superior_dois_incluso = false;
				boolean limite_inferior_dois_nao_incluso_e_superior_dois_incluso = false;

				boolean limite_inferior_dois_nulo_e_superior_dois_nao_incluso = false;
				boolean limite_inferior_dois_nao_incluso_e_superior_dois_nulo = false;
				boolean limite_inferior_dois_nulo_e_superior_dois_incluso = false;
				boolean limite_inferior_dois_incluso_e_superior_dois_nulo = false;

				if(
						(porteDisponivelParaAtividade.limiteInferiorParametroDois == null &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois != null) &&
						(!porteDisponivelParaAtividade.limiteInferiorParametroDoisIncluso &&
						!porteDisponivelParaAtividade.limiteSuperiorParametroDoisIncluso) &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois > parametroDois.valorParametro
				){
					limite_inferior_dois_nulo_e_superior_dois_nao_incluso = true;
				}
				else if(
						(porteDisponivelParaAtividade.limiteInferiorParametroDois == null &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois != null) &&
						(!porteDisponivelParaAtividade.limiteInferiorParametroDoisIncluso &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDoisIncluso) &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois >= parametroDois.valorParametro
				){
					limite_inferior_dois_nulo_e_superior_dois_incluso = true;
				}
				else if(
						(porteDisponivelParaAtividade.limiteInferiorParametroDois != null &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois == null) &&
						(!porteDisponivelParaAtividade.limiteInferiorParametroDoisIncluso &&
						!porteDisponivelParaAtividade.limiteSuperiorParametroDoisIncluso) &&
						(porteDisponivelParaAtividade.limiteInferiorParametroDois < parametroDois.valorParametro)
				) {
					limite_inferior_dois_nao_incluso_e_superior_dois_nulo = true;
				}
				else if(
						(porteDisponivelParaAtividade.limiteInferiorParametroDois != null &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois == null) &&
						(porteDisponivelParaAtividade.limiteInferiorParametroDoisIncluso &&
						!porteDisponivelParaAtividade.limiteSuperiorParametroDoisIncluso) &&
						(porteDisponivelParaAtividade.limiteInferiorParametroDois <= parametroDois.valorParametro)
				){
					limite_inferior_dois_incluso_e_superior_dois_nulo = true;
				}
				else if(
						(porteDisponivelParaAtividade.limiteInferiorParametroDois != null &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois != null) &&
						(porteDisponivelParaAtividade.limiteInferiorParametroDoisIncluso &&
						!porteDisponivelParaAtividade.limiteSuperiorParametroDoisIncluso) &&
						(porteDisponivelParaAtividade.limiteInferiorParametroDois <= parametroDois.valorParametro &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois > parametroDois.valorParametro)
				) {
					limite_inferior_dois_incluso_e_superior_dois_nao_incluso = true;
				}
				else if(
						(porteDisponivelParaAtividade.limiteInferiorParametroDois != null &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois != null) &&
						(!porteDisponivelParaAtividade.limiteInferiorParametroDoisIncluso &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDoisIncluso) &&
						(porteDisponivelParaAtividade.limiteInferiorParametroDois < parametroDois.valorParametro) &&
						(porteDisponivelParaAtividade.limiteSuperiorParametroDois >= parametroDois.valorParametro)
				) {
					limite_inferior_dois_nao_incluso_e_superior_dois_incluso = true;
				}
				else if(
						(porteDisponivelParaAtividade.limiteInferiorParametroDois != null &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois != null) &&
						(!porteDisponivelParaAtividade.limiteInferiorParametroDoisIncluso &&
						!porteDisponivelParaAtividade.limiteSuperiorParametroDoisIncluso) &&
						(porteDisponivelParaAtividade.limiteInferiorParametroDois < parametroDois.valorParametro &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois > parametroDois.valorParametro)
				) {
					limite_inferior_dois_nao_incluso_e_superior_dois_nao_incluso = true;
				}
				else if(
						(porteDisponivelParaAtividade.limiteInferiorParametroDois != null &&
						porteDisponivelParaAtividade.limiteInferiorParametroDois != null) &&
						(porteDisponivelParaAtividade.limiteInferiorParametroDoisIncluso &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDoisIncluso) &&
						(porteDisponivelParaAtividade.limiteInferiorParametroDois <= parametroDois.valorParametro &&
						porteDisponivelParaAtividade.limiteSuperiorParametroDois >= parametroDois.valorParametro)
				) {
					limite_inferior_dois_incluso_e_superior_dois_incluso = true;
				}

				//Se limite inferior um não incluso e superior um não incluso e limite inferior dois não incluso e superior dois não incluso
				// < > < > || null > < > || < null < > || < > null > || < > < null ||
				// null > null > || null > < null || < null null > || < null < null
				if(
					(
							limite_inferior_um_nao_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_nao_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nulo &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nulo
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_nao_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_nao_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nulo
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nulo &&
							limite_inferior_dois_nulo_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nulo &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nulo
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um não incluso e superior um não incluso e limite inferior dois não incluso e superior dois incluso
				// < > < >= || null > < >= || < null < >= || < > null >= ||null > null >= ||< null null >=
				if (
						(
							limite_inferior_um_nao_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_nao_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nulo &&
							limite_inferior_dois_nao_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_nao_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nulo &&
							limite_inferior_dois_nulo_e_superior_dois_incluso
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um não incluso e superior um não incluso e limite inferior dois incluso e superior dois incluso
				// < > <= >= || null > <= >= || < null <= >=
				if(
						(
							limite_inferior_um_nao_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_nao_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nulo &&
							limite_inferior_dois_incluso_e_superior_dois_incluso
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um não incluso e superior um incluso e limite inferior dois incluso e superior dois incluso
				// < >= <= >= || null >= <= >=
				if(
						(
							limite_inferior_um_nao_incluso_e_superior_um_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_incluso
						)
				) {
				return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um incluso e superior um incluso e limite inferior dois não incluso e superior dois incluso
				// <= >= < >= || <= >= null >=
				if(
						(
							limite_inferior_um_incluso_e_superior_um_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_incluso
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um incluso e superior um não incluso e limite inferior dois incluso e superior dois incluso
				// <= > <= >= || <= null <= >=
				if(
						(
							limite_inferior_um_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nulo &&
							limite_inferior_dois_incluso_e_superior_dois_incluso
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um incluso e superior um incluso e limite inferior dois incluso e superior dois incluso
					// <= >= <= >=
				if(
							(
								limite_inferior_um_incluso_e_superior_um_incluso &&
								limite_inferior_dois_incluso_e_superior_dois_incluso
							)
					) {
						return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um incluso e superior um não incluso e limite inferior dois não incluso e superior dois não incluso
				// <= > < > || <= null null > || <= null < null || <= null < > || <= > null > || <= > < null
				if(
					(
							limite_inferior_um_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nulo &&
							limite_inferior_dois_nulo_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nulo &&
							limite_inferior_dois_incluso_e_superior_dois_nulo
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nulo &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nulo
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um não incluso e superior um incluso e limite inferior dois não incluso e superior dois não incluso
				// < >= < > || null >= < > || null >= null > || null >= < null || < >= null > || < >= < null
				if(
						(
							limite_inferior_um_nao_incluso_e_superior_um_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nulo
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nulo
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um não incluso e superior um não incluso e limite inferior dois incluso e superior dois não incluso
				// < > <= > || null > <= null || null > <= > || < null <= null || < > <= null || < null <= >
				if(
						(
							limite_inferior_um_nao_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_nao_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nulo
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_nao_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nulo &&
							limite_inferior_dois_incluso_e_superior_dois_nulo
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nulo
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_nulo &&
							limite_inferior_dois_incluso_e_superior_dois_nao_incluso
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um incluso e superior um incluso e limite inferior dois incluso e superior dois não incluso
				// <= >= <= > || <= >= <= null
				if(
						(
							limite_inferior_um_incluso_e_superior_um_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nulo
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um incluso e superior um incluso e limite inferior dois não incluso e superior dois não incluso
				// <= >= < > || <= >= null > || <= >= < null
				if(
						(
							limite_inferior_um_incluso_e_superior_um_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_nulo
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um incluso e superior um não incluso e limite inferior dois não incluso e superior dois incluso
				// <= > < >= || <= null null >= || <= null < >= || <= > null >=
				if(
						(
							limite_inferior_um_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nulo &&
							limite_inferior_dois_nulo_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nulo &&
							limite_inferior_dois_nao_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_incluso
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um não incluso e superior um incluso e limite inferior dois incluso e superior dois não incluso
				// < >= <= > || null >= <= null || null >= <= > || < >= <= null
				if(
						(
							limite_inferior_um_nao_incluso_e_superior_um_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nulo
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nulo
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um incluso e superior um não incluso e limite inferior dois incluso e superior dois não incluso
				// <= > <= > || <= null <= > || <= null <= null || <= > <= null
				if(
						(
							limite_inferior_um_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nulo &&
							limite_inferior_dois_incluso_e_superior_dois_nao_incluso
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nulo &&
							limite_inferior_dois_incluso_e_superior_dois_nulo
						) ||
						(
							limite_inferior_um_incluso_e_superior_um_nao_incluso &&
							limite_inferior_dois_incluso_e_superior_dois_nulo
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
				//Se limite inferior um não incluso e superior um incluso e limite inferior dois não incluso e superior dois incluso
				// < >= < >= || null >= null >= || < >= null >= || null >= < >=
				if(
						(
							limite_inferior_um_nao_incluso_e_superior_um_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nao_incluso_e_superior_um_incluso &&
							limite_inferior_dois_nulo_e_superior_dois_incluso
						) ||
						(
							limite_inferior_um_nulo_e_superior_um_incluso &&
							limite_inferior_dois_nao_incluso_e_superior_dois_incluso
						)
				) {
					return porteDisponivelParaAtividade.porteEmpreendimento;
				}
			}
		}
		throw new ValidacaoException(Mensagem.PARAMETROS_INSUFICIENTES_PARA_CALCULO);
	}
}
