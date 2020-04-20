package com.fimet.cfg.db.eg.fieldformat.extract;

import static com.fimet.commons.converter.Converter.NONE;

import com.fimet.cfg.FieldFormatBuilder;
import com.fimet.cfg.enums.FieldFormatGroup;
import com.fimet.core.entity.sqlite.FieldFormat;
import com.fimet.core.persistence.dao.FieldFormatDAO;
import com.fimet.parser.field.FixedFieldParser;

public class NacionalATMExtractBase {
	FieldFormatDAO dao;
	int idGroup = FieldFormatGroup.EXTRACT_BASE_ATM.getId();
	public NacionalATMExtractBase(FieldFormatDAO dao) {
		this.dao = dao;
	}
	public void insert() {
		dao.insertOrUpdate(new FieldFormat(idGroup,"001","1",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-TRANS-FI-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"002","2",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-TRANS-ABA-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"003","3",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-TRANS-BR-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"004","4",NONE.getId(),null,null,"(?s).+",2,null,"DESC_ATM-APPL-NAME",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"005","5",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-TRML-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"006","6",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-TRML-SEQ-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"007","7",NONE.getId(),null,null,"(?s).+",6,null,"DESC_ATM-TRML-RECEIPT-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"008","8",NONE.getId(),null,null,"(?s).+",1,null,"DESC_ATM-TRML-TYPE-CODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"009","9",NONE.getId(),null,null,"(?s).+",1,null,"DESC_ATM-TRML-SUBTYPE-CODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"010","10",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-OPERATING-FI-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"011","11",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-OPERATING-ABA-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"012","12",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-OPERATING-BR-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"013","13",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-OPERATOR-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"014","14",NONE.getId(),null,null,"(?s).+",4,null,"DESC_ATM-TRAN-CODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"015","15",NONE.getId(),null,null,"(?s).+",4,null,"DESC_ATM-RESULT-CODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"016","16",NONE.getId(),null,null,"(?s).+",8,null,"DESC_ATM-TRAN-DATE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"017","17",NONE.getId(),null,null,"(?s).+",6,null,"DESC_ATM-TRAN-TIME",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"018","18",NONE.getId(),null,null,"(?s).+",2,null,"DESC_ATM-SERVICE-ID",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"019","19",NONE.getId(),null,null,"(?s).+",1,null,"DESC_ATM-FORCE-POST-IND  ",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"020","20",NONE.getId(),null,null,"(?s).+",1,null,"DESC_ATM-REVERSAL-IND",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"021","21",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-CARD-FI-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"022","22",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-CARD-BR-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"023","23",NONE.getId(),null,null,"(?s).+",2,null,"DESC_ATM-CARD-APPL-CODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"024","24",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-CARD-MEMBER",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"025","25",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-CARD-LOI  ",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"026","26",NONE.getId(),null,null,"(?s).+",1,null,"DESC_ATM-AUTH-METHOD",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"027","27",NONE.getId(),null,null,"(?s).+",1,null,"DESC_ATM-AUTH-IND  ",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"028","28",NONE.getId(),null,null,"(?s).+",2,null,"DESC_ATM-FILLER",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"029","29",NONE.getId(),null,null,"(?s).+",1,null,"DESC_ATM-POS-DATA-IND  ",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"030","30",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-SWTOUT-RESP-TICKS",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"031","31",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-PREV-XACT-RESP-TICKS",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"032","32",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-PREV-XACT-SEQ-NBR  ",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"033","33",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-PRE-SEGMENT-LENGTH  ",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"034","34",NONE.getId(),null,null,"(?s).+",13,null,"DESC_ATM-AMT-DEBIT",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"035","35",NONE.getId(),null,null,"(?s).+",99,null,"DESC_ATM-TRANS-UNIQUE-DATA",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"036","36",NONE.getId(),null,null,"(?s).+",6,null,"DESC_ATM-SWITCH-SEQ-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"037","37",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-SWITCH-NETWORK-ID",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"038","38",NONE.getId(),null,null,"(?s).+",6,null,"DESC_ATM-SWITCH-BUS-DATE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"039","39",NONE.getId(),null,null,"(?s).+",5,null,"DESC_ATM-SWITCH-IN-NETWORK-ID",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"040","40",NONE.getId(),null,null,"(?s).+",6,null,"DESC_ATM-SWITCH-IN-SEQ-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"041","41",NONE.getId(),null,null,"(?s).+",4,null,"SEG-TRAN-TYPE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"042","42",NONE.getId(),null,null,"(?s).+",4,null,"SEG-TT",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"043","43",NONE.getId(),null,null,"(?s).+",12,null,"SEG-RRN",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"044","44",NONE.getId(),null,null,"(?s).+",4,null,"SEG-TRAN-DATE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"045","45",NONE.getId(),null,null,"(?s).+",6,null,"SEG-TRAN-TIME",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"046","46",NONE.getId(),null,null,"(?s).+",4,null,"SEG-CAPTURE-DATE  ",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"047","47",NONE.getId(),null,null,"(?s).+",10,null,"SEG-GMT-TIME",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"048","48",NONE.getId(),null,null,"(?s).+",6,null,"SEG-PROCESSING-CODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"049","49",NONE.getId(),null,null,"(?s).+",4,null,"SEG-MERCHANT-TYPE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"050","50",NONE.getId(),null,null,"(?s).+",3,null,"SEG-POS-ENTRY-MODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"051","51",NONE.getId(),null,null,"(?s).+",2,null,"SEG-RESPONSE-CODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"052","52",NONE.getId(),null,null,"(?s).+",6,null,"SEG-AUTH-CODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"053","53",NONE.getId(),null,null,"(?s).+",6,null,"SEG-STAN",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"054","54",NONE.getId(),null,null,"(?s).+",4,null,"SEG-SETTLEMENT-DATE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"055","55",NONE.getId(),null,null,"(?s).+",16,null,"SEG-CARD-ACCEPTR-ID",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"056","56",NONE.getId(),null,null,"(?s).+",40,null,"SEG-CARD-ACCEPTR-DATA",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"057","57",NONE.getId(),null,null,"(?s).+",1,null,"SEG-ACCT-TYPE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"058","58",NONE.getId(),null,null,"(?s).+",3,null,"SEG-CCY-CODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"059","59",NONE.getId(),null,null,"(?s).+",5,null,"SEG-OUT-SWAP",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"060","60",NONE.getId(),null,null,"(?s).+",2,null,"SEG-LEN-ACCT",FixedFieldParser.class));
		new FieldFormatBuilder(new FieldFormat(idGroup,"061","61",NONE.getId(),null,null,"(?s).+",19,null,"SEG-TRACK2",FixedFieldParser.class))
			.add(new FieldFormatBuilder(new FieldFormat(idGroup,"001","1",NONE.getId(),null,null,"(?s).+",16,null,"POS-SEG-TRACK2-PAN",FixedFieldParser.class))
				.add(new FieldFormat(idGroup,"001","1",NONE.getId(),null,null,"(?s).+",6,null,"POS-SEG-TRACK2-BIN",FixedFieldParser.class))
				.add(new FieldFormat(idGroup,"002","2",NONE.getId(),null,null,"(?s).+",10,null,"POS-SEG-TRACK2-PLASTICO",FixedFieldParser.class))
			)
			.add(new FieldFormat(idGroup,"002","2",NONE.getId(),null,null,"(?s).+",3,null,"POS-SEG-TRACK2-EXT-PLASTICO",FixedFieldParser.class))
		.insertOrUpdate(dao);
		dao.insertOrUpdate(new FieldFormat(idGroup,"062","62",NONE.getId(),null,null,"(?s).+",11,null,"SEG-ACQ-INST-ID  ",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"063","63",NONE.getId(),null,null,"(?s).+",44,null,"SEG-NETW-ATM-GEN-PUR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"064","64",NONE.getId(),null,null,"(?s).+",16,null,"SEG-TERMINAL",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"065","65",NONE.getId(),null,null,"(?s).+",19,null,"SEG-ISS-ACQ-DATA",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"066","66",NONE.getId(),null,null,"(?s).+",10,null,"SEG-ISS-TRAN-DATA",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"067","67",NONE.getId(),null,null,"(?s).+",16,null,"SEG-PIN-OFFSET",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"068","68",NONE.getId(),null,null,"(?s).+",14,null,"SEG-REPL-AMT  ",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"069","69",NONE.getId(),null,null,"(?s).+",11,null,"SEG-RCV-INST-ID",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"070","70",NONE.getId(),null,null,"(?s).+",28,null,"SEG-FROM-ACCT",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"071","71",NONE.getId(),null,null,"(?s).+",33,null,"SEG-TRML-ABR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"072","72",NONE.getId(),null,null,"(?s).+",4,null,"SEG-JIFFY-POP  ",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"073","73",NONE.getId(),null,null,"(?s).+",19,null,"SEG-126-TK25-SCHG-AMT",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"074","74",NONE.getId(),null,null,"(?s).+",19,null,"SEG-126-TK30-LFEE-AMT",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"075","75",NONE.getId(),null,null,"(?s).+",19,null,"SEG-126-TK30-USOLINEA-AMT",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"076","76",NONE.getId(),null,null,"(?s).+",20,null,"SEG-126-TKB1-BANCO",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"077","77",NONE.getId(),null,null,"(?s).+",1,null,"SEG-126-SCHG-FLAG",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"078","78",NONE.getId(),null,null,"(?s).+",1,null,"SEG-COD-SERVICIO-TRACK",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"079","79",NONE.getId(),null,null,"(?s).+",2,null,"SEG-BANDERA-PRESENCIA-TKB4",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"080","80",NONE.getId(),null,null,"(?s).+",2,null,"SEG-126-TKB4-ENTRY-MODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"081","81",NONE.getId(),null,null,"(?s).+",1,null,"SEG-126-TKB4-CAP-PIN",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"082","82",NONE.getId(),null,null,"(?s).+",1,null,"SEG-126-TKB4-TERM-CAP",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"083","83",NONE.getId(),null,null,"(?s).+",2,null,"SEG-126-TKB4-PAN-SEQ-NBR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"084","84",NONE.getId(),null,null,"(?s).+",1,null,"SEG-126-TKB4-ARQC-VRFY",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"085","85",NONE.getId(),null,null,"(?s).+",2,null,"SEG-BANDERA-PRESENCIA-TKB2",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"086","86",NONE.getId(),null,null,"(?s).+",16,null,"SEG-126-TKB2-ARQC",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"087","87",NONE.getId(),null,null,"(?s).+",2,null,"SEG-BANDERA-PRESENCIA-TKB5",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"088","88",NONE.getId(),null,null,"(?s).+",16,null,"SEG-126-TKB5-ARPC",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"089","89",NONE.getId(),null,null,"(?s).+",4,null,"SEG-126-TKB5-ARPC-RESP-CODE",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"090","90",NONE.getId(),null,null,"(?s).+",1,null,"SEG-126-TKQ1-CAM-EMISOR",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"091","91",NONE.getId(),null,null,"(?s).+",8,null,"SEG- X- TKB3-TAG9f33-TERM-CAP",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"092","92",NONE.getId(),null,null,"(?s).+",12,null,"SEG-ADD-DATA-TXN-AMT (DE004)",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"093","93",NONE.getId(),null,null,"(?s).+",3,null,"SEG-ADD-DATA-TXN-CCY-CODE (DE049)",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"094","94",NONE.getId(),null,null,"(?s).+",12,null,"SEG-ADD-DATA-SETT-AMT (DE005)",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"095","95",NONE.getId(),null,null,"(?s).+",3,null,"SEG-ADD-DATA-SETT-CCY-CODE (DE050)",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"096","96",NONE.getId(),null,null,"(?s).+",12,null,"SEG-ADD-DATA-SURCHARCE-AMT-DCC",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"097","97",NONE.getId(),null,null,"(?s).+",8,null,"SEG-ADD-TASA-CONV-DCC",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"098","98",NONE.getId(),null,null,"(?s).+",1,null,"SEG-RESPONSABILIDAD-EMV",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"099","99",NONE.getId(),null,null,"(?s).+",12,null,"SEG-RRN-ISS",FixedFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"100","100",NONE.getId(),null,null,"(?s).+",15,null,"FILLER",FixedFieldParser.class));
	}
}
