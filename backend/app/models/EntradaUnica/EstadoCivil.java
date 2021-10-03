package models.EntradaUnica;

public enum EstadoCivil {

	SOLTEIRO(0, "Solteiro(a)"),
	CASADO(1, "Casado(a)"),
	DIVORCIADO(2, "Divorciado(a)"),
	VIUVO(3, "Viúvo(a)"),
	SEPARADO(4, "Separado(a)"),
	UNIAO_ESTAVEL(5, "União Estável");

	public Integer codigo;
	public String descricao;

	private EstadoCivil(Integer codigo, String descricao) {

		this.codigo = codigo;
		this.descricao = descricao;

	}
}
