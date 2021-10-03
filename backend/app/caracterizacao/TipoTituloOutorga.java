package models.caracterizacao;

public enum TipoTituloOutorga {
	
	OUTORGA("112"),
	OUTORGA_PREVIA("113"),
	DISPENSA("114");
	
	private String id;
	
	private TipoTituloOutorga(String id) {
		
		this.id = id;
	}
	
	public String getId() {
		
		return this.id;
	}
}
