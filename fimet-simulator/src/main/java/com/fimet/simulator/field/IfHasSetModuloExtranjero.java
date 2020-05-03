package com.fimet.simulator.field;

import com.fimet.commons.utils.StringUtils;
import com.fimet.iso8583.parser.Message;
import com.fimet.simulator.ISimulatorField;

public class IfHasSetModuloExtranjero implements ISimulatorField {
	private static IfHasSetModuloExtranjero instance;
	public static IfHasSetModuloExtranjero getInstance() {
		if (instance == null) {
			instance = new IfHasSetModuloExtranjero();
		}
		return instance;
	}
	@Override
	public void simulate(Message message, String idField) {
		if ("0200".equals(message.getMti()) && "004000".equals(message.getValue(3)) && message.hasField(idField)) {
			String tokenQN = message.getValue(idField);
			if (tokenQN != null && tokenQN.length() == 53) {
				String divisa = tokenQN.substring(0,3);
				Long monto = Long.parseLong(tokenQN.substring(11,25));
				String tipoDeCambio = "        ";
				if ("GBP".equals(divisa)) {//Libra Esterlina Reino Unido
					monto *= 2550L;
					tipoDeCambio = "00255000";
				} else if ("USD".equals(divisa)) {//D�lar Americano U.S.A.
					monto *= 2064L;
					tipoDeCambio = "00206400";
				} else if ("JPY".equals(divisa)) {//Yen Japon�s
					monto *= 1767L;
					tipoDeCambio = "00176700";
				} else if ("EUR".equals(divisa)) {//Moneda de la Uni�n Europea
					monto *= 2171L;
					tipoDeCambio = "00217100";
				} else if ("CAD".equals(divisa)) {//D�lar Canadiense
					monto *= 2064L;
					tipoDeCambio = "00206400";
				} else if ("CHF".equals(divisa)) {//Franco Suizo
					monto *= 2553L;
					tipoDeCambio = "00255300";
				} else if ("SEK".equals(divisa)) {//Corona Sueca
					monto *= 245L;
					tipoDeCambio = "00024500";
				}
				tokenQN = divisa +//3:Divisa
					tokenQN.substring(3,4)+//1:Funcion
					StringUtils.random(7)+//7:Folio
					StringUtils.leftPad(monto+"", 15, '0')+//15:Monto
					tipoDeCambio+//8:TipoDeCambio
					"0038610"+//7:Markup
					"            "//12:Uso futuro 
				;
				message.setValue(idField, tokenQN);
			}
		}
	}
}
	