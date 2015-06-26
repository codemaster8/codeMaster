// Copyright (c) 2014 Omkar, Sanjeet, Utkarsh - Neustar, Inc. All Rights Reserved.
package generalUtils;
import com.eviware.soapui.SoapUI.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import org.xml.sax.SAXException;
/**
 * Provides Utilities that will verify the Schema validation against XML with XSD
 * 
 * @author Omkar Khatavkar, Sanjeet Singh	 
 * @version 1.0
 */
public class xsdUtils
{
	private log;
	private context;
	private testRunner;
	/**
	 * This below code will get the Response and will 
	 * validate against XSD schema.
	 */
	xsdUtils(log,testRunner,context) 
	{
		this.log = log;
		this.testRunner = testRunner;
		this.context = context;
		this.log.info("xsdUtil Object Initialized successfully.");	
	}
	def verifyResponseSchema(def testSuiteName, def testCaseName, def testStepName)
	{	
		def restTestStep = this.testRunner.testCase.testSuite.project.getTestSuiteByName(testSuiteName).getTestCaseByName(testCaseName).getTestStepByName(testStepName);
		def responseAsXml = restTestStep.getTestRequest().getResponseContentAsXml();
		this.log.info "Validate Response schema against XSD";
		def groovyUtils = new com.eviware.soapui.support.GroovyUtils(context);
		def projectPath = groovyUtils.projectPath;
		File xsdFolder = null;
		if (System.properties['os.name'].toLowerCase().contains('windows')) 
		{
			log.info "it's Windows OS!!!";
			xsdFolder = new File( projectPath + "\\..\\..\\Coord-XSD\\");     
		} 
		else 
		{
			log.info "it's Linux OS!!!";
			xsdFolder = new File(projectPath + "/../../Coord-XSD/");
		}  
		log.info xsdFolder.getCanonicalPath();
		this.log.info "Setting path for XSD files."
		def path = xsdFolder.getCanonicalPath();
		// declared all required variables 
		def deceXSD, mdXSD, metadataXSD, AssertionXSD, schemaXSD, xmldsigXSD, xmlXSD ;
		def XSD1, XSD2, XSD3, XSD4, XSD5, XSD6, XSD7, schemaFiles ;
		try {
			this.log.info "Initializing all XSD files."
			deceXSD = path+ "/dece-1.11.xsd"
			mdXSD = path+ "/md-1.11.xsd"
			metadataXSD = path+ "/saml-schema-metadata-2.0.xsd"
			AssertionXSD = path+ "/saml-schema-assertion-2.0.xsd"
			schemaXSD = path+ "/xenc-schema.xsd"
			xmldsigXSD = path+ "/xmldsig-core-schema.xsd"
			xmlXSD = path + "/xml.xsd"
		}
		catch(Throwable e){
			this.log.info e.toString();
			this.log.info "Not able to locate xsd files in specified location."
		}
		XSD1 = new StreamSource(new FileInputStream(deceXSD)) 
		XSD2 = new StreamSource(new FileInputStream(mdXSD)) 
		XSD3 = new StreamSource(new FileInputStream(metadataXSD)) 
		XSD4 = new StreamSource(new FileInputStream(AssertionXSD)) 
		XSD5 = new StreamSource(new FileInputStream(schemaXSD)) 
		XSD6 = new StreamSource(new FileInputStream(xmldsigXSD))
		XSD7 = new StreamSource(new FileInputStream(xmlXSD))
		schemaFiles = [XSD7, XSD6, XSD5, XSD4, XSD3, XSD2, XSD1]
				try {
					def factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
					def schema = factory.newSchema((Source[])schemaFiles.toArray())
							def validator = schema.newValidator();
					validator.validate(new StreamSource(new StringReader(responseAsXml)));
					log.info ("Response validation again Xsd passed!!!");
				}
		catch(Throwable e){
			this.log.info e.toString();
			this.log.info "Failed to validate the response with XSD."
			String errorMessage="Validation failed due to ===>  " + e.toString();
			throw new Exception(errorMessage)
		}
	}
}