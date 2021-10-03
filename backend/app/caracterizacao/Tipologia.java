package models.caracterizacao;


import models.Diretoria;
import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "tipologia")
public class Tipologia  extends GenericModel {
	
	public static Long ID_AGROSSILVIPASTORIL = 1l;
	public static Long ID_INDUSTRIA_MADEIREIRA = 11l;

	@Id
	public Long id;

	@Column(name =  "nome")
	public String nome;

	@Column(name =  "ativo")
	public Boolean ativo;

	@Column(name =  "codigo")
	public String codigo;

	@OneToOne
	@JoinColumn(name = "id_diretoria")
	public Diretoria diretoria;

	@OneToMany(mappedBy = "tipologia")
	public List<Atividade> atividades;


	/*
	 * Retorna todas as tipologias baseadas no tipo de licencimento dispensa ou simplificado
	 */
	public static List<Tipologia> findTipologias(FiltroTipologia filtro) {

		String jpql = "SELECT DISTINCT tipologia " +
				"FROM " + Tipologia.class.getSimpleName() + " tipologia " +
				"   JOIN tipologia.atividades atividades " +
				"   JOIN atividades.tiposAtividade tiposAtividade  " +
				"   JOIN atividades.tiposCaracterizacaoAtividade tiposCaracterizacaoAtividade " +
				"WHERE ";

		if(filtro.dispensaLicenciamento != null) {

			jpql += " (tiposCaracterizacaoAtividade.dispensaLicenciamento = :dispensaLicenciamento) ";
		}

		else if(filtro.licenciamentoSimplificado != null) {

			jpql += " (tiposCaracterizacaoAtividade.licenciamentoSimplificado = :licenciamentoSimplificado " +
					"OR tiposCaracterizacaoAtividade.licenciamentoDeclaratorio = :licenciamentoDeclaratorio) ";
		}

		if (filtro.tipoEndereco == 0){ //empreendimento urbano
			jpql+= " AND (tiposAtividade.codigo = 'URBANA') ";
		}
		else{ //empreendimento rural
			jpql+= " AND (tiposAtividade.codigo = 'RURAL') ";
		}

		if(filtro.listaCodigosCnaes != null) {

			if(filtro.listaCodigosCnaes.isEmpty()) {
				return new ArrayList<>();
			}

			jpql += " AND (tiposCaracterizacaoAtividade.atividadeCnae.codigo IN :listaCodigosCnae) ";
		}

		jpql += " AND tipologia.ativo=true AND atividades.ativo=true AND atividades.itemAntigo=false";

		JPAQuery jpaQuery = Tipologia.find(jpql);

		if(filtro.dispensaLicenciamento != null) {

			jpaQuery.setParameter("dispensaLicenciamento", filtro.dispensaLicenciamento);
		}

		else if(filtro.licenciamentoSimplificado != null) {

			jpaQuery.setParameter("licenciamentoSimplificado", filtro.licenciamentoSimplificado);
			jpaQuery.setParameter("licenciamentoDeclaratorio", filtro.licenciamentoSimplificado);
		}

		if(filtro.listaCodigosCnaes != null) {

			jpaQuery.setParameter("listaCodigosCnae", filtro.listaCodigosCnaes);
		}

		List<Tipologia> tipologias = jpaQuery.fetch();

		return tipologias;

	}

	public static class FiltroTipologia {
		public Long idTipologia;
		public Boolean licenciamentoSimplificado;
		public Boolean dispensaLicenciamento;
		public List<String> listaCodigosCnaes;
		public Integer tipoEndereco; //0- Urbana, 1- Rural

		public FiltroTipologia() {

		}
	}
}
