package security.services;

import play.Logger;
import play.mvc.Http.Request;
import play.mvc.results.Unauthorized;
import security.utils.InetAddressComparator;
import utils.Configuracoes;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ExternalService {

	public static void validateAdress(Request request) {
		
		String validateAdress = Configuracoes.VALIDATE_ADDRESS;
		
		if (validateAdress.isEmpty() || !Boolean.valueOf(validateAdress))
			return;
		
		String remoteAdress = request.remoteAddress;
		String adressList = Configuracoes.ADRESS_LIST;
		
		Logger.info("Serviço requisitado pelo IP: " + remoteAdress);
		
		// Se nao estiver configurada a seguranca por IP
		if (adressList == null || adressList.trim().isEmpty() || remoteAdress == null)
			throw new Unauthorized("Unauthorized");
		
		String[] list = adressList.split(",");

		InetAddress addr = null;
		InetAddress remoteAddr = null;
		
		Boolean isPermitted = false;
		
		try {
			
			remoteAddr = InetAddress.getByName(remoteAdress);				
			
			for (int i = 0; i < list.length; i++) {
				
				InetAddressComparator addressComparator = new InetAddressComparator();

				addr = InetAddress.getByName(list[i]);
				
				if (addressComparator.compare(remoteAddr, addr) == 0)
					isPermitted = true;
			}
			

		} catch (UnknownHostException e) {
			throw new Unauthorized("Unauthorized");
		}
		
		if (!isPermitted)
			throw new Unauthorized("Unauthorized");			
	}

	
}
