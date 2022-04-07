package com.fimet.test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.fimet.parser.IMessage;
import com.fimet.parser.IParser;
import com.fimet.parser.mx.MasterCardParser;
import com.fimet.parser.mx.VisaParser;
import com.fimet.utils.ParserBuilder;
import com.fimet.utils.converter.Converter;

@SpringBootTest
@ExtendWith(SpringExtension.class)
public class ParserTest {


	@Test
	public void testVisa() {
		IParser parser = new ParserBuilder()
				.name("Visa")
				.converter(Converter.ASCII_TO_HEX)
				.parserClass(VisaParser.class)
				.fieldGroup("Visa")
				.build();
		test(parser, "16010200B5000000312019000000000000000000000001007224648108E08036109966510899032610000000000000049900020913030100033222105411060401200006474570F2F0F4F0F1F3F0F0F0F3F3F2F0F0F0F0F7F4F6F8F1F0F0F0F0F0F44040404040404040C6D6D6C4D3D6E2D6C6E8404040404040404040404040404040D3C9D4C140D7D9E44040404040D7C506040AF0F0F0F0F0F8F4F2F0F0050000000005098000000000000000E8058000000000");
	}
	@Test
	public void testMasterCard() {
		IParser parser = new ParserBuilder()
				.name("MasterCard")
				.parserClass(MasterCardParser.class)
				.fieldGroup("MasterCard")
				.build();
		test(parser, "F0F4F0F0F23C44018EE180080000004200000000F1F6F9F9F9F1F2F0F0F6F0F0F0F0F7F7F1F8F0F0F0F0F0F0F0F0F0F0F0F0F1F0F0F0F0F0F0F2F1F3F2F1F5F9F2F0F0F0F0F0F8F5F1F5F5F9F2F0F0F2F1F3F2F5F1F2F5F4F1F1F1F0F0F0F6F0F3F1F3F5F6F0F6F1F2F3F4F5F6F0F0F0F0F0F0F9F7F8F2F4F6F6F6F2F2F1F7F6F8F0C9F3F5F4F2F8F1F1F0F0F0F0F0F44040404040404040C6D6D6C4D3D6E2D6C6E840404040404040404040404040D3C9D4C140D7D9E4404040404040D7C5D9F0F6F8E3F2F0F0F1E2F4F2F0F7F0F1F0F3F2F1F2F4F3F2F888A2E8E4D1C1D7F4D1614ED9E8A6C188D56197D8C291E8C1C1C1C17EF6F3F1F5D4C3E6C1C8C7F1C4E6F0F2F1F34040F6F0F4F0F2F6F1F0F2F5F1F0F0F0F0F6F0F0F0F6F0F4F0F0F0F0F0F8F4F2F0F0F0F1F0F0F0F0F0F0F8F4F0F2F1F3F2F1F5F9F1F9F0F0F0F0F0F0F3F1F3F5F6F0F0F0F0F0F1F2F3F4F5F6F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0");
		test(parser, "F0F4F1F0F22000018A8180000000004200000000F1F6F9F9F9F1F2F0F0F6F0F0F0F0F7F7F1F8F0F0F0F0F0F0F0F0F0F0F0F0F1F0F0F0F0F0F0F2F1F3F2F1F5F9F2F0F0F0F0F0F8F5F0F6F0F3F1F3F5F6F0F6F1F2F3F4F5F6F0F0F0F0F0F0F9F7F8F2F4F6F0F0F0C9F3F5F4F2F8F1F0F2F3E3F2F0F0F6F3F1F5F3F0F0F1F2F9F6F2F5F4F1F2F9F1F2F6F0F4F0F1F0F0F0F0F0F0F8F4F0F2F1F3F2F1F5F9F1F9F0F0F0F0F0F0F3F1F3F5F6F0F0F0F0F0F1F2F3F4F5F6F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0F0");
	}
	public void test(IParser parser, String hex) {
		IMessage msg = parser.parseMessage(Converter.HEX_TO_ASCII.convert(hex.getBytes()));
        assertNotNull(msg);
        System.out.println(msg);
	}
}
