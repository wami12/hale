/*
 * HUMBOLDT: A Framework for Data Harmonisation and Service Integration.
 * EU Integrated Project #030962                 01.10.2006 - 30.09.2010
 * 
 * For more information on the project, please refer to the this web site:
 * http://www.esdi-humboldt.eu
 * 
 * LICENSE: For information on the license under which this program is 
 * available, please refer to http:/www.esdi-humboldt.eu/license.html#core
 * (c) the HUMBOLDT Consortium, 2007 to 2010.
 */

package eu.esdihumboldt.hale.io.xml.validator.internal;

import java.io.InputStream;
import java.net.URI;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.SchemaFactory;

import eu.esdihumboldt.hale.common.cache.Request;
import eu.esdihumboldt.hale.io.xml.validator.Report;
import eu.esdihumboldt.hale.io.xml.validator.Validator;

/**
 * Validate using the XML API.
 *
 * @author Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 */
public class XMLApiValidator implements Validator {

	private final URI[] schemaLocations;
	
	/**
	 * Constructor
	 * @param schemaLocations the schema locations
	 */
	public XMLApiValidator(URI[] schemaLocations) {
		super();
		this.schemaLocations = schemaLocations;
	}

	/**
	 * @see Validator#validate(InputStream)
	 */
	@Override
	public Report validate(InputStream xml) {
		javax.xml.validation.Schema validateSchema;
		try {
			URI mainUri = null;
			Source[] sources = new Source[schemaLocations.length];
			for (int i = 0; i < this.schemaLocations.length; i++) {
				URI schemaLocation = this.schemaLocations[i];
						    
				if (mainUri == null) { // use first schema location for main URI
					mainUri = schemaLocation;
				}
		
			    // load a WXS schema, represented by a Schema instance
//				sources[i] = new StreamSource(schema.getLocation().openStream());
				sources[i] = new StreamSource(Request.getInstance().get(schemaLocation));
			}
			// create a SchemaFactory capable of understanding WXS schemas
		    SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
		    factory.setResourceResolver(new SchemaResolver(mainUri));
		    validateSchema = factory.newSchema(sources);
		} catch (Exception e) {
			throw new IllegalStateException("Error parsing schema for XML validation", e); //$NON-NLS-1$
		}

	    // create a Validator instance, which can be used to validate an instance document
	    javax.xml.validation.Validator validator = validateSchema.newValidator();
	    ReportImpl report = new ReportImpl();
		validator.setErrorHandler(new ReportErrorHandler(report));
	    
	    // validate the XML document
		try {
			validator.validate(new StreamSource(xml));
			return report;
		} catch (Exception e) {
			throw new IllegalStateException("Error validating XML file", e); //$NON-NLS-1$
		}
	}

}