package models.correios;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import play.db.jpa.Model;

@Entity
@Table(name = "bairro_faixa", schema = "correios")
public class BairroFaixa extends Model {
	
	@Column(name="cep_inicio")
	public Integer cepInicio;
	
	@Column(name="cep_fim")
	public Integer cepFim;
	
}
