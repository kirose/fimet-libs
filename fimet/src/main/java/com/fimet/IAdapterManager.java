package com.fimet;

import java.util.List;

import com.fimet.adapter.ExtractAdapter;
import com.fimet.adapter.HexAdapter;
import com.fimet.adapter.IAdapter;
import com.fimet.adapter.IByteArrayAdapter;
import com.fimet.adapter.IStreamAdapter;
import com.fimet.adapter.IStringAdapter;
import com.fimet.adapter.LogHexParserAdapter;
import com.fimet.adapter.MliExclusiveAdapter;
import com.fimet.adapter.MliExclusiveSimQueueAdapter;
import com.fimet.adapter.MliInclusiveAdapter;
import com.fimet.adapter.MliInclusiveSimQueueAdapter;
import com.fimet.adapter.MliVisaExclusiveAdapter;
import com.fimet.adapter.MliVisaExclusiveSimQueueAdapter;
import com.fimet.adapter.NoneAdapter;
import com.fimet.adapter.RawcomAdapter;
import com.fimet.adapter.StxAdapter;
import com.fimet.adapter.StxSimQueueAdapter;
/**
 * 
 * @author <a href="mailto:marcoasb99@ciencias.unam.mx">Marco A. Salazar</a>
 *
 */
public interface IAdapterManager extends IManager {
	
	static final IAdapter STX                          = new StxAdapter(0, "Stx");
	static final IAdapter STC_SIM_QUEUE                = new StxSimQueueAdapter(1, "StxSimQueue");
	static final IAdapter MLI_VISA_EXCLUSIVE           = new MliVisaExclusiveAdapter(2, "VisaExclusive");
	static final IAdapter MLI_VISA_EXCLUSIVE_SIM_QUEUE = new MliVisaExclusiveSimQueueAdapter(3, "VisaExclusiveSimQueue");
	static final IAdapter MLI_EXCLISIVE_SIM_QUEUE      = new MliExclusiveSimQueueAdapter(4, "ExclusiveSimQueue");
	static final IAdapter MLI_INCLUSIVE_SIM_QUEUE      = new MliInclusiveSimQueueAdapter(5, "InclusiveSimQueue");
	static final IAdapter MLI_EXCLUSIVE                = new MliExclusiveAdapter(6, "Exclusive");
	static final IAdapter MLI_INCLUSIVE                = new MliInclusiveAdapter(7, "Inclusive");
	static final IAdapter HEX                          = new HexAdapter(8, "Hex");
	static final IAdapter RAWCOM                       = new RawcomAdapter(9, "Rawcom");
	static final IAdapter LOG_HEX                      = new LogHexParserAdapter(10, "LogHex");
	static final IAdapter EXTRACT                      = new ExtractAdapter(11, "Extract");
	static final IAdapter NONE                         = new NoneAdapter(12, "None");
	
	IAdapter getAdapter(String name);
	IAdapter getAdapter(Integer id);
	List<IAdapter> getAdapters();
	List<IStreamAdapter> getStreamAdapters();
	List<IByteArrayAdapter> getByteArrayAdapters();
	List<IStringAdapter> getStringAdapters();
	IStringAdapter adapterFor(String message);
	IByteArrayAdapter adapterFor(byte[] message);

}
