package com.fimet.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.fimet.parser.EParser;

@XmlRootElement(name="parsers")
@XmlAccessorType(XmlAccessType.NONE)
public class ParsersXml {
	@XmlElement(name="parser")
	private List<EParser> parsers;
	public ParsersXml() {
		super();
	}
	public ParsersXml(List<EParser> parsers) {
		super();
		this.parsers = parsers;
	}
	public List<EParser> getParsers() {
		return parsers;
	}
	public void setParsers(List<EParser> parsers) {
		this.parsers = parsers;
	}
}
