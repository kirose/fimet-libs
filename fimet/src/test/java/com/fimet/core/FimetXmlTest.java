package com.fimet.core;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import com.fimet.IClassLoaderManager;
import com.fimet.IThreadManager;
import com.fimet.parser.EParser;
import com.fimet.simulator.ESimulator;
import com.fimet.simulator.ESimulatorModel;
import com.fimet.usecase.IUseCaseStore;
import com.fimet.utils.ClassLoaderManager;
import com.fimet.utils.ThreadManager;
import com.fimet.xml.ExtensionXml;
import com.fimet.xml.FimetXml;
import com.fimet.xml.ManagerXml;
import com.fimet.xml.PropertyXml;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class FimetXmlTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public FimetXmlTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( FimetXmlTest.class );
    }
    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
    }
	public void testRead() throws Exception {
		try {
			//File xmlFile = new File("C:\\eclipse\\wsfimetapp\\fimet-core\\src\\main\\resources\\fimet.xml");
			//InputStream stream = Manager.class.getClass().getResourceAsStream("fimet.xml");
			File xmlFile = new File("resources\\fimet.xml");
			JAXBContext jaxbContext = JAXBContext.newInstance(FimetXml.class);              
		    Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		    FimetXml fimet = (FimetXml) jaxbUnmarshaller.unmarshal(xmlFile);
		    //FimetXml fimet = (FimetXml) jaxbUnmarshaller.unmarshal(stream);
		    System.out.println("xml parsed:");
		    System.out.println(fimet);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void testWrite() throws Exception {
		try {
			FimetXml fimet = new FimetXml();
			List<ManagerXml> managers = new ArrayList<>();
			managers.add(new ManagerXml(IClassLoaderManager.class.getName(),ClassLoaderManager.class.getName()));
			managers.add(new ManagerXml(IThreadManager.class.getName(),ThreadManager.class.getName()));
			fimet.setManagers(managers);
			List<PropertyXml> properties = new ArrayList<>();
			properties.add(new PropertyXml("name","value"));
			fimet.setProperties(properties);
			List<ESimulatorModel> simulatorModels = new ArrayList<>();
			ESimulatorModel s = new ESimulatorModel("MTest","com.fimet.test.STest");
			simulatorModels.add(s);
			fimet.setSimulatorModels(simulatorModels);
			List<ESimulator> simulators = new ArrayList<>();
			ESimulator si = new ESimulator("STest","MTest","PNational","127.0.0.1",8582,true,"Exclusive");
			simulators.add(si);
			fimet.setSimulators(simulators);
			List<ExtensionXml> extensions = new ArrayList<>();
			extensions.add(new ExtensionXml(IUseCaseStore.class.getName(), "com.fimet.usecase.UseCaseStore"));
			fimet.setExtensions(extensions);
			List<EParser> parsers = new ArrayList<>();
			parsers.add(new EParser("PNational", "FNational", "com.fimet.parser.National", "None"));
			fimet.setParsers(parsers);
			JAXBContext context = JAXBContext.newInstance(FimetXml.class);  
		    Marshaller marshaller = context.createMarshaller();  
		    marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);  
		    marshaller.marshal(fimet, new FileOutputStream("resources\\fimet.xml"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
