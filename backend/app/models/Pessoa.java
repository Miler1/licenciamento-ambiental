package models;

import br.ufla.lemaf.beans.pessoa.*;
import br.ufla.lemaf.beans.pessoa.Contato;
import br.ufla.lemaf.beans.pessoa.Endereco;
import br.ufla.lemaf.beans.pessoa.EstadoCivil;
import exceptions.AppException;
import exceptions.ValidacaoException;
import play.data.validation.Valid;
import play.db.jpa.GenericModel;
import utils.Configuracoes;
import utils.ListUtil;
import utils.Mensagem;
import utils.WebServiceEntradaUnica;
import utils.validacao.Validacao;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class Pessoa extends br.ufla.lemaf.beans.pessoa.Pessoa implements Serializable {

	public Pessoa() {
	}


	public String getCpfCnpj() {
		return this.isPessoaFisica() ? this.cpf : this.cnpj;
	}

	public String getNomeRazaoSocial() {
		return this.isPessoaFisica() ? this.nome : this.razaoSocial;
	}

	public boolean isPessoaFisica() {
		return this.cpf != null && !this.cpf.isEmpty();
	}

	public static String getNomeRazaoSocialPessoaEU(br.ufla.lemaf.beans.pessoa.Pessoa pessoaEU) {
		return isPessoaFisicaEU(pessoaEU) ? pessoaEU.nome : pessoaEU.razaoSocial;
	}

	public static boolean isPessoaFisicaEU(br.ufla.lemaf.beans.pessoa.Pessoa pessoaEU) {
		return pessoaEU.cpf != null && !pessoaEU.cpf.isEmpty();
	}

	public static String getCpfCnpjPessoaEU(br.ufla.lemaf.beans.pessoa.Pessoa pessoaEU) {
		return isPessoaFisicaEU(pessoaEU) ? pessoaEU.cpf : pessoaEU.cnpj;
	}

	public boolean isPessoaJuridica() {
		return !this.isPessoaFisica();
	}

	public static Pessoa convert(br.ufla.lemaf.beans.pessoa.Pessoa p){

		Pessoa pessoa = new Pessoa();

		pessoa.id = p.id;
		pessoa.contatos = p.contatos;
		pessoa.cpf = p.cpf;
		pessoa.dataAtualizacao = p.dataAtualizacao;
		pessoa.dataCadastro = p.dataCadastro;
		pessoa.dataNascimento = p.dataNascimento;
		pessoa.enderecos = p.enderecos;
		pessoa.estadoCivil = p.estadoCivil;
		pessoa.estrangeiro = p.estrangeiro;
		pessoa.isUsuario = p.isUsuario;
		pessoa.naturalidade = p.naturalidade;
		pessoa.nome = p.nome;
		pessoa.nomeMae = p.nomeMae;
		pessoa.passaporte = p.passaporte;
		pessoa.rg = p.rg;
		pessoa.sexo = p.sexo;
		pessoa.tipo = p.tipo;
		pessoa.tituloEleitoral = p.tituloEleitoral;
		pessoa.cnpj = p.cnpj;
		pessoa.dataConstituicao = p.dataConstituicao;
		pessoa.inscricaoEstadual = p.inscricaoEstadual;
		pessoa.nomeFantasia = p.nomeFantasia;
		pessoa.razaoSocial = p.razaoSocial;
		pessoa.usuario = p.usuario;

		return pessoa;
	}

}
