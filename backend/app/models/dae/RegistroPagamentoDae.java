package models.dae;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RegistroPagamentoDae {

	private static final String PAGAMENTO_REGISTRADO = "REGISTRADO";
	
	public String statusPagamentoDae;
	public List<Pagamento> pagamento;
	
	public boolean isPago() {
	
		return this.statusPagamentoDae != null && this.statusPagamentoDae.equals(PAGAMENTO_REGISTRADO);
	}
	
	public static class Pagamento {
		
		private static final String DATA_PAGAMENTO_FORMAT = "yyyy-MM-dd'T'hh:mm:ss.SSS";
		
		public Long id;
		
		// O formato retornado para a data é "2017-03-27T22:37:25.406+0000"
		public String dataPagamento;
		
		public Date getParsedDataPagamento() {
			
			try {
				
				return new SimpleDateFormat(DATA_PAGAMENTO_FORMAT).parse(dataPagamento);
				
			} catch (ParseException e) {
				
				throw new IllegalArgumentException(e.getMessage());
			}
		}
	}
}
