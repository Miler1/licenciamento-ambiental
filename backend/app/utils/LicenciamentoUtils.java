package utils;

import java.text.ParseException;

import javax.swing.text.MaskFormatter;

import br.com.caelum.stella.format.CNPJFormatter;
import br.com.caelum.stella.format.CPFFormatter;

public class LicenciamentoUtils {
	
	/**
	 * Formata para exibição o CEP especificado.
	 * 
	 * @param cepSemFormatacao
	 * @return CEP formatado para exibição
	 */
    public static String formatarCep(String cepSemFormatacao) {
    	
        MaskFormatter mf;
        String pattern = "#####-###";
        
        while(cepSemFormatacao.length() < 8) {
        	cepSemFormatacao = "0" + cepSemFormatacao;
        }
        
        try {
            mf = new MaskFormatter(pattern);
            mf.setValueContainsLiteralCharacters(false);
            return mf.valueToString(cepSemFormatacao);
        } catch (ParseException ex) {
            return cepSemFormatacao;
        }
        
    }
    
	/**
	 * Formata para exibição o CPF especificado.
	 * 
	 * @param cpfSemFormatacao
	 * @return CPF formatado para exibição
	 */
	public static String formatarCpf(String cpfSemFormatacao) {
		try {
			return new CPFFormatter().format(cpfSemFormatacao);
		} catch (IllegalArgumentException e) {
			return cpfSemFormatacao;
		}
	}
	
	/**
	 * Formata para exibição o CNPJ especificado.
	 * 
	 * @param cnpjSemFormatacao
	 * @return CNPJ formatado para exibição
	 */
	public static String formatarCnpj(String cnpjSemFormatacao) {
		try {
			return new CNPJFormatter().format(cnpjSemFormatacao);
		} catch (IllegalArgumentException e) {
			return cnpjSemFormatacao;
		}
	}

}
