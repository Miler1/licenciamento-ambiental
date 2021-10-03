package models;

import exceptions.ValidacaoException;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.FilterDefs;
import org.hibernate.annotations.ParamDef;
import play.Logger;
import play.data.validation.Required;
import play.db.jpa.GenericModel;
import utils.Mensagem;
import utils.PessoaUtils;
import utils.WebServiceEntradaUnica;
import utils.validacao.Validacao;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Empreendedor {

	public Long id;

	public Pessoa pessoa;

	public boolean ativo;


	public Empreendedor() {

	}

	public Empreendedor(Empreendedor empreendedor) {
		this.id = empreendedor.id;
		this.pessoa = empreendedor.pessoa;
		this.ativo = empreendedor.ativo;
	}

}
