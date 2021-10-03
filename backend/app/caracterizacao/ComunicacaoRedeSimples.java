package models.caracterizacao;

import play.db.jpa.GenericModel;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(schema = "licenciamento", name = "comunicacao_rede_simples")
public class ComunicacaoRedeSimples extends GenericModel {

	private static final String SEQ = "licenciamento.comunicacao_rede_simples_id_seq";

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQ)
	@SequenceGenerator(name = SEQ, sequenceName = SEQ, allocationSize = 1)
	public Long id;

	@OneToOne
	@JoinColumn(name="id_caracterizacao", referencedColumnName="id")
	public Caracterizacao caracterizacao;

	@Column(name = "tentativas")
	public Integer tentativas;

	@Column(name = "data_cadastro")
	public Date dataCadastro;

	public ComunicacaoRedeSimples(Caracterizacao caracterizacao){
		this.caracterizacao = caracterizacao;
		this.tentativas = 0;
		this.dataCadastro = new Date();
	}

}
