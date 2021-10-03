package models.sicar;

public enum TipoArea {
	
	// Tipos de áreas do SiCAR utilizadas no Licenciamento.
	// Os IDs são os mesmos IDs do SiCAR
	
	AREA_CONSOLIDADA(1L),
	ARL_PROPOSTA(23L),
	ARL_AVERBADA(24L),
	ARL_APROVADA_NAO_AVERBADA(25L);
	
	public Long id;
	
	TipoArea (Long id) {
		
		this.id = id;

	}

}
