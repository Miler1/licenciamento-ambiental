package exceptions;

import utils.Mensagem;

public class AppException extends RuntimeException {

	private String mensagemTexto;
	private Mensagem mensagem;
	private Object [] msgArgs;

	
	public AppException(Mensagem mensagem, Object ... msgArgs) {
		
		super(mensagem.getTexto(msgArgs));
		this.mensagem = mensagem;
		this.msgArgs = msgArgs;
		this.mensagemTexto = null;
	}
	
	public AppException(Throwable throwable, Mensagem mensagem, Object ... msgArgs) {
		
		super(throwable);
		this.mensagem = mensagem;
		this.msgArgs = msgArgs;
		this.mensagemTexto = null;
	}

	public AppException() {
	}
	
	public String getTextoMensagem () {
		
		return this.mensagemTexto != null ? this.mensagemTexto : this.mensagem.getTexto(this.msgArgs);
	}

	public AppException(String mensagem) {

		super(mensagem);
		this.mensagemTexto = mensagem;
		this.mensagem = null;
		this.msgArgs = null;
	}
}
