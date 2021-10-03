package models.caracterizacao;

import play.db.jpa.GenericModel;
import utils.Identificavel;

import javax.persistence.*;

@Entity
@Table(schema = "licenciamento", name = "tipo_atividade")
public class TipoAtividade extends GenericModel {


	public enum Tipos {

		RURAL(1, "Atividade Rural", "RURAL"),

		URBANA (2, "Atividade Urbana", "URBANA");

		public int id;
		public String nome;
		public String codigo;

		Tipos(int id, String nome, String codigo) {

			this.id = id;
			this.nome = nome;
			this.codigo = codigo;
		}

	}

	@Id
	public Long id;

	public String nome;
	
	public String codigo;

}