package utils;

import play.Logger;

import java.sql.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class DataUtils {

	/*
	Remover horas de datas do tipo sql.Date
	 */
	public static Date tirarHoraData(Date dataComHora) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataComHora);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	/*
	Remover horas de datas do tipo util.Date
	 */
	public static java.util.Date tirarHoraData(java.util.Date dataComHora) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(dataComHora);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTimeInMillis());
	}

	public Date somarUmDia(Date dataComHora) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataComHora);
		c.add(Calendar.DATE, 1);
		return new Date(c.getTimeInMillis());
	}

	public static java.util.Date somarDias(java.util.Date date, int dias) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, dias);
		return new Date(c.getTimeInMillis());
	}

	public static java.util.Date somarDiasContandoHoras(java.util.Date date, int dias) {

		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DATE, dias);
		return c.getTime();
	}

	public static java.util.Date somarUmDia(java.util.Date data) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.DATE, 1);
		return c.getTime();

	}

	public static java.util.Date somarMilissegundos(java.util.Date data, int milliseconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.MILLISECOND, milliseconds);
		return c.getTime();

	}

	public static java.util.Date subtrairMilissegundos(java.util.Date data, int milliseconds) {
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.MILLISECOND, -milliseconds);
		return c.getTime();

	}

	public static boolean hojePosteriorA(java.util.Date data) throws NullPointerException {

		java.util.Date hoje = new java.util.Date();

		return hoje.after(data);
	}

	public Date removerUmDia(Date dataComHora) {
		Calendar c = Calendar.getInstance();
		c.setTime(dataComHora);
		c.add(Calendar.DATE, -1);
		return new Date(c.getTimeInMillis());
	}

	public static java.util.Date removeHoras(java.util.Date data, Integer quantidadeHorasRemover){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.HOUR, -quantidadeHorasRemover);
		return new java.util.Date(c.getTimeInMillis());
	}

	public static java.util.Date removeMeses(java.util.Date data, Integer quantidadeMesesRemover){
		Calendar c = Calendar.getInstance();
		c.setTime(data);
		c.add(Calendar.MONTH, -quantidadeMesesRemover);
		return new java.util.Date(c.getTimeInMillis());
	}

	public static Date stringParaData(String data) throws ParseException  {
		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return (Date) formatter.parse(data);
	}

	public static String longParaDataFormatada(Long dataAsLong) throws ParseException {

		DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

		Date data = new Date(dataAsLong);

		return formatter.format(data);
	}

	public static String formataData(java.util.Date data, String formato) {

		if(data == null) {
			return "";
		}

		DateFormat formatter = new SimpleDateFormat(formato);

		return formatter.format(data);
	}

	public static Long getDiferencaDias(java.util.Date data, java.util.Date data2) {

		Long diffInMillies = data.getTime() - data2.getTime();

		return Math.abs(TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS));
	}

	public static Long getDiferencaHoras(java.util.Date data, java.util.Date data2) {

		Long diffInMillies = data.getTime() - data2.getTime();

		return TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	public static Long getDiferencaEmMinutos(java.util.Date dataFim, java.util.Date dataInicio) {

		Long diffInMillies = dataFim.getTime() - dataInicio.getTime();

		return TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
	}

	public static Long getDiferencaSegundos(java.util.Date d1, java.util.Date d2) {

		return (d1.getTime() - d2.getTime()) / 1000;
	}

	public static String getDiferencaTempoCompleto(java.util.Date d1, java.util.Date d2) {

		if(d1 == null || d2 == null) {
			return "";
		}

		Long diferencaDias = getDiferencaDias(d1, d2);
		Long diferencaHoras = getDiferencaHoras(d1, d2) - 24 * diferencaDias;
		Long diferencaMinutos = getDiferencaEmMinutos(d1, d2) - (60 * diferencaHoras) - (24 * 60 * diferencaDias);
		Long diferencaSegundos = getDiferencaSegundos(d1, d2) - (60 * diferencaMinutos) - (60 * 60 * diferencaHoras) - (24 * 60 * 60 * diferencaDias);

		return diferencaDias + " dias " + diferencaHoras + " horas " + diferencaMinutos + " minutos " + diferencaSegundos + " segundos";
	}

	public static String obterDataFormatadaString() {
		DateFormat formatador = DateFormat.getDateInstance(DateFormat.FULL, new Locale("pt", "BR"));

		java.util.Date data = new java.util.Date();
		String dataExtenso = formatador.format(data);

		int index  = dataExtenso.indexOf(",");
		int lenght = dataExtenso.length();


		return dataExtenso.substring(++index, lenght);
	}

	public static String obterTextoPeriodo(java.util.Date dataInicio, java.util.Date dataFim) {

		if(dataInicio == null && dataFim == null) {
			return null;
		}

		String texto = "";

		if(dataInicio != null) {
			texto += "a partir de " + DataUtils.formataData(dataInicio, "dd/MM/yyyy") + " ";
		}

		if(dataFim != null) {
			texto += "até " + DataUtils.formataData(dataFim, "dd/MM/yyyy");
		}

		return texto;
	}

	public static java.util.Date formataData(String data) {

		String[] formatos = {
				"dd/MM/yyyy",
				"dd/MM/yyyy hh:mm:ss",
				"yyyy/MM/dd",
				"yyyy/MM/dd hh:mm:ss",
				"yyyy-MM-dd",
				"yyyy-MM-dd hh:mm:ss",
				"yyyy-MM-dd hh:mm:ss.ms",
				"yyyy.MM.dd",
				"yyyy.MM.dd hh:mm:ss",
		};

		for(String formato : formatos) {

			try {

//				Logger.info("formataData - " + formato);

				DateFormat formatter = new SimpleDateFormat(formato);

				java.util.Date dataFormatada = formatter.parse(data);

				return dataFormatada;

			} catch (ParseException e) {

				//e.printStackTrace();
			}
		}

		Logger.error("formataData - " + data);

		return null;
	}

	public static String formataDataStringParaPadraoBrasileiro(String data) {

		if(data == null || data.length() == 0) {
			return null;
		}

		String[] dataSeparada = data.split("-");
		return dataSeparada[2] + "/" + dataSeparada[1] + "/" + dataSeparada[0];
	}
}
