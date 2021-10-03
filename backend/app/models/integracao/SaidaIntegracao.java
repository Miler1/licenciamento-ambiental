package models.integracao;

public class SaidaIntegracao<T> {
	
	public T Dados;
	public String Mensagem;
	public String Erros;
	
	public SaidaIntegracao(T Dados, String Mensagem, String Erros) {
		
		this.Dados = Dados;
		this.Mensagem = Mensagem;
		this.Erros = Erros;
	}

}
