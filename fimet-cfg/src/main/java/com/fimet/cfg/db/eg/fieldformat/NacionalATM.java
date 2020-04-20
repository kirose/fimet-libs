package com.fimet.cfg.db.eg.fieldformat;

import static com.fimet.commons.converter.Converter.NONE;
import static com.fimet.commons.numericparser.NumericParser.DEC;

import com.fimet.cfg.FieldFormatBuilder;
import com.fimet.cfg.enums.FieldFormatGroup;
import com.fimet.core.entity.sqlite.FieldFormat;
import com.fimet.core.persistence.dao.FieldFormatDAO;
import com.fimet.parser.field.VarFieldParser;
import com.fimet.parser.field.mx.*;

public class NacionalATM {
	FieldFormatDAO dao;
	int idGroup = FieldFormatGroup.NACIONAL_ATM.getId();
	public NacionalATM(FieldFormatDAO dao) {
		this.dao = dao;
	}
	public void insert() {
		dao.insertOrUpdate(new FieldFormat(idGroup,"002","2",NONE.getId(),NONE.getId(),DEC.getId(),"[0-9]+",2,19,"Primary account number (PAN)",VarFieldParser.class));
		dao.insertOrUpdate(new FieldFormat(idGroup,"035","35",NONE.getId(),NONE.getId(),DEC.getId(),"[0-9]{8,19}(=|D).+",2,37,"Track 2 data",VarFieldParser.class));
		new FieldFormatBuilder(new FieldFormat(idGroup,"126","126",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",3,999,"Reserved for private use",NatTokensVarFieldParser.class))
			.add(new FieldFormat(idGroup,"001","01",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token 01",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"002","Q1",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token Q1",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"003","Q2",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token Q2",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"004","Q3",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token Q3",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"005","Q4",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token Q4",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"006","Q6",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token Q6",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"007","04",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token 04",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"008","C0",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token C0",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"009","C4",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token C4",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"010","C5",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token C5",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"011","ER",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token ER",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"012","ES",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token ES",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"013","ET",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token ET",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"014","EW",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token EW",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"015","EX",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token EX",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"016","EY",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token EY",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"017","EZ",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token EZ",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"018","R1",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token R1",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"019","R2",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token R2",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"020","R3",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token R3",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"021","R7",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token R7",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"022","R8",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token R8",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"023","C6",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token C6",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"024","CE",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token CE",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"025","S3",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token S3",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"026","17",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token 17",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"027","20",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token 20",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"028","QS",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token QS",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"029","B0",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token B0",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"030","B1",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token B1",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"031","B2",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token B2",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"032","B3",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token B3",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"033","B4",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token B4",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"034","R0",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token R0",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"035","QF",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token QF",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"036","R4",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token R4",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"037","QR",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token QR",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"038","QO",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token QO",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"039","QP",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token QP",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"040","CZ",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token CZ",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"041","TV",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token TV",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"042","TM",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token TM",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"043","25",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token 25",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"044","30",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token 30",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"045","QM",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token QM",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"046","QN",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token QN",NatTokenVarFieldParser.class))
			.add(new FieldFormat(idGroup,"047","QC",NONE.getId(),NONE.getId(),DEC.getId(),"(?s).+",5,999,"Token QC",NatTokenVarFieldParser.class))
		.insertOrUpdate(dao);
	}
}
