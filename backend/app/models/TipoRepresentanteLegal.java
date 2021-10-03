package models;

public enum TipoRepresentanteLegal {
	
	REPRESENTANTE(0, "Representante"),
	PROPRIETARIO(1, "Proprietário"),
	ARRENDATARIO(2, "Arrendatário");

	public int id;
	public String nome;
	
	TipoRepresentanteLegal(int id, String nome) {
		
		this.id = id;
		this.nome = nome;
	}
	
	public int getId() {
		
		return id;
	}
	
	public String getNome() {
	
		return nome;
	}
}
