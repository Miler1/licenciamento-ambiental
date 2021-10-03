package utils;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class DateUtil {

	public static String formatarMesMinusculo(Date data) {

		SimpleDateFormat DATE_FORMAT;

		DATE_FORMAT = new SimpleDateFormat("EEEE, dd 'de' MMMM 'de' yyyy");
		DateFormatSymbols symbols = new DateFormatSymbols(new Locale("pt", "BR"));
		symbols.setMonths(new String[] {"janeiro","fevereiro","março","abril","maio","junho","julho","agosto","setembro","outubro","novembro","dezembro"});
		DATE_FORMAT.setDateFormatSymbols(symbols);

		String st;
		synchronized (DATE_FORMAT) {
			st = DATE_FORMAT.format(data);
		}
       
       return st;
				       

	}	

}
