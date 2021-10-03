package models.caracterizacao;

import play.db.jpa.GenericModel;

import javax.persistence.*;

import models.TipoLocalizacao;

import java.util.List;

@Entity
@Table(schema = "licenciamento", name = "pergunta")
public class Pergunta extends GenericModel {

	@Id
	public Long id;

	public String texto;

	public String codigo;

	@OneToMany(mappedBy="pergunta")
	public List<Resposta> respostas;

	@Column(name="tipo_localizacao_empreendimento")
	@Enumerated(EnumType.ORDINAL)
	public TipoLocalizacao localizacaoEmpreendimento;
	
	@Column(name="tipo_pergunta")
	@Enumerated(EnumType.STRING)
	public TipoPergunta tipoPergunta;

	@Column(name="ativo")
	public Boolean ativo;

	@ManyToMany
	@JoinTable(schema = "licenciamento", name = "rel_atividade_pergunta",
			joinColumns = @JoinColumn(name = "id_pergunta"),
			inverseJoinColumns = @JoinColumn(name = "id_atividade"))
	public List<Atividade> atividades;

	@Transient
	public Integer ordem;

}
