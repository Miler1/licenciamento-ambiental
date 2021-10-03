package models.caracterizacao;

import play.db.jpa.Model;

import javax.persistence.*;
import java.util.Map;
import java.util.Objects;

@Entity
@Table(schema = "licenciamento", name = "parametro_atividade")
public class ParametroAtividade extends Model {

	@Column(name = "nome")
	public String nome;

	@Column(name = "codigo")
	public String codigo;

	@Column(name = "casas_decimais")
	public Integer casasDecimais;

	@ElementCollection
	@CollectionTable(schema = "licenciamento", name = "rel_atividade_parametro_atividade",
			joinColumns = @JoinColumn(name = "id_parametro_atividade"))
	@MapKeyJoinColumn(name = "id_atividade", updatable = false, insertable = false)
	public Map<Atividade, Atividade.RelAtividadeParametrosAtividade> parametrosAtividade;

	@Transient
	public String descricao;

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public ParametroAtividade() {
	}

	public ParametroAtividade(ParametroAtividade parametroAtividade, String descricao) {
		this.id = parametroAtividade.id;
		this.nome = parametroAtividade.nome;
		this.codigo = parametroAtividade.codigo;
		this.casasDecimais = parametroAtividade.casasDecimais;
		this.descricao = descricao;
		this.willBeSaved = true;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof ParametroAtividade)) return false;
		if (!super.equals(o)) return false;
		ParametroAtividade that = (ParametroAtividade) o;
		return this.getId().equals(that.getId()) &&
				codigo.equals(that.codigo);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), nome, codigo);
	}
}
