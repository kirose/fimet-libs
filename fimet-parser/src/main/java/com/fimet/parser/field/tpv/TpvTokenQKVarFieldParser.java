package com.fimet.parser.field.tpv;

import com.fimet.entity.EFieldFormat;
import com.fimet.IPreferencesManager;
import com.fimet.Manager;
import com.fimet.parser.IMessage;
import com.fimet.parser.ParserException;
import com.fimet.utils.ByteUtils;
import com.fimet.utils.EncryptionUtils;
import com.fimet.utils.converter.Converter;
import com.fimet.utils.data.IWriter;

public class TpvTokenQKVarFieldParser extends TpvTokenVarFieldParser {

	private static final String DEFAULT_BDK = "0123456789ABCDEFFEDCBA9876543210";
	private static IPreferencesManager preferencesManager = Manager.get(IPreferencesManager.class);
	public TpvTokenQKVarFieldParser(EFieldFormat fieldFormat) {
		super(fieldFormat);
	}
	@Override
	protected byte[] posParseValue(byte[] value, IMessage message) {
		value = decrypt(value, message);
		return super.posParseValue(value, message);
	}
	@Override
	protected byte[] preFormatValue(IWriter writer, IMessage message) {
		byte[] value = super.preFormatValue(writer, message);
		return encrypt(value, message);
	}
	private byte[] decrypt(byte[] value, IMessage message) {
		String fTokenQKEncrypted = new String(value);
		String fKeySerialNumber = message.getValue(idFieldParent+".EZ.1");// El Token EZ debe ser parseado primero
		if (fKeySerialNumber == null) {
			throw new ParserException("Token EZ.1 (Key serial Number) is required when include token QK");
		}
		String dataEncrypted = new String(ByteUtils.subArray(fTokenQKEncrypted.getBytes(), 0,80));
		byte[] crc32 = ByteUtils.subArray(fTokenQKEncrypted.getBytes(), 80, 96);
		String decrypt = EncryptionUtils.decrypt(dataEncrypted, fKeySerialNumber, preferencesManager.getString(IPreferencesManager.BDK, DEFAULT_BDK));
		message.setValue(idField, ByteUtils.concat(decrypt.getBytes(), crc32));
		return ByteUtils.concat(decrypt.getBytes(), crc32);
	}
	private byte[] encrypt(byte[] bytes, IMessage message) {
		String fTokenQkDecrypted = new String(bytes);
		String fKeySerialNumber = message.getValue(idFieldParent+".EZ.1");
		if (fKeySerialNumber == null) {
			throw new ParserException("Token EZ.1 (Key serial Number) is required when include token QK");
		}
		String encrypt = EncryptionUtils.encrypt(fTokenQkDecrypted, fKeySerialNumber, preferencesManager.getString(IPreferencesManager.BDK, DEFAULT_BDK));
		encrypt = encrypt.substring(0,80);
		String crc32 = EncryptionUtils.crc32Unmarshall(encrypt);
		return ByteUtils.concat(encrypt.getBytes(), Converter.asciiToHex(crc32.getBytes()));
	}
}
