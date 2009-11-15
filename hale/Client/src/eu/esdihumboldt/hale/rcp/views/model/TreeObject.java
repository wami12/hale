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
package eu.esdihumboldt.hale.rcp.views.model;

import org.opengis.feature.type.Name;

import eu.esdihumboldt.goml.align.Entity;
import eu.esdihumboldt.goml.omwg.FeatureClass;
import eu.esdihumboldt.goml.omwg.Property;
import eu.esdihumboldt.goml.rdf.About;

/**
 * A TreeObject for TreeViewers.
 *  
 * @author cjauss, Simon Templer
 * @partner 01 / Fraunhofer Institute for Computer Graphics Research
 * @version $Id$ 
 */
public class TreeObject implements SchemaItem, Comparable<TreeObject> {
	
	private final String label;
	private TreeParent parent;
	private final TreeObjectType type;
	
	private final Name name;
	
	/**
	 * Constructor
	 * 
	 * @param label the item label
	 * @param name the entity name
	 * @param type the entity type
	 */
	public TreeObject(String label, Name name, TreeObjectType type) {
		this.label = label;
		this.type = type;
		this.name = name;
	}
	
	/**
	 * @see SchemaItem#getEntity()
	 */
	public Entity getEntity() {
		Entity entity = null;
		
		if (entity == null) {
			
			if (isType()) {
				// feature type
				if (name != null) {
					entity = new FeatureClass(
							new About(name.getNamespaceURI(), 
									name.getLocalPart()));
				}
			}
			else if (isAttribute()) {
				// attributes
				if (parent != null && parent.getName() != null) {
					entity = new Property(
							new About(name.getNamespaceURI(), 
									parent.getName().getLocalPart(),
									name.getLocalPart()));
				}
			}
		}
		
		return entity;
	}
	
	/**
	 * @see eu.esdihumboldt.hale.rcp.views.model.SchemaItem#isAttribute()
	 */
	public boolean isAttribute() {
		switch (type) {
		case ABSTRACT_FT:
		case CONCRETE_FT:
		case PROPERTY_TYPE:
		case ROOT:
			return false;
		default:
			return true;
		}
	}
	
	/**
	 * @see eu.esdihumboldt.hale.rcp.views.model.SchemaItem#isType()
	 */
	public boolean isType() {
		return isFeatureType() || type.equals(TreeObjectType.PROPERTY_TYPE);
	}
	
	/**
	 * @see eu.esdihumboldt.hale.rcp.views.model.SchemaItem#isFeatureType()
	 */
	public boolean isFeatureType() {
		switch (type) {
		case ABSTRACT_FT:
		case CONCRETE_FT:
			return true;
		default:
			return false;
		}
	}
	
	/**
	 * @return the type, either ROOT, 
	 */
	public TreeObjectType getType() {
		return type;
	}
	
	/**
	 * Get the item label
	 * 
	 * @return the item label
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * @see eu.esdihumboldt.hale.rcp.views.model.SchemaItem#getName()
	 */
	public Name getName() {
		return name;
	}

	/**
	 * Set the parent tree item
	 * 
	 * @param parent the parent tree item
	 */
	public void setParent(TreeParent parent) {
		this.parent = parent;
	}
	
	/**
	 * Get the parent tree item
	 * 
	 * @return the parent tree item
	 */
	public TreeParent getParent() {
		return parent;
	}
	
	/**
	 * @see Object#toString()
	 */
	@Override
	public String toString() {
		return getLabel();
	}
	
	/**
	 * Item types
	 */
	public enum TreeObjectType {
		/** root item */
		ROOT,
		/** abstract feature type item */
		ABSTRACT_FT,
		/** concrete feature type item **/
		CONCRETE_FT,
		/** property type */
		PROPERTY_TYPE,
		/** numeric attribute item */
		NUMERIC_ATTRIBUTE,
		/** string attribute item */
		STRING_ATTRIBUTE,
		/** complex attribute item */
		COMPLEX_ATTRIBUTE,
		/** geometric attribute item */
		GEOMETRIC_ATTRIBUTE
	}

	/**
	 * @see SchemaItem#getChildren()
	 */
	@Override
	public SchemaItem[] getChildren() {
		return null;
	}

	/**
	 * @see SchemaItem#hasChildren()
	 */
	@Override
	public boolean hasChildren() {
		return false;
	}

	/**
	 * @see Comparable#compareTo(Object)
	 */
	@Override
	public int compareTo(TreeObject other) {
		return label.compareToIgnoreCase(other.label);
	}

}
