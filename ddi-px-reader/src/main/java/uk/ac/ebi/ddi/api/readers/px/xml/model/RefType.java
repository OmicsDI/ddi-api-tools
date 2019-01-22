
package uk.ac.ebi.ddi.api.readers.px.xml.model;

import javax.xml.bind.annotation.*;
import java.io.Serializable;


/**
 * <p>Java class for RefType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="RefType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="ref" use="required" type="{http://www.w3.org/2001/XMLSchema}IDREF" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RefType")
public class RefType
        implements Serializable, PXObject {

    private static final long serialVersionUID = 100L;
    @XmlAttribute(required = true)
    @XmlIDREF
    @XmlSchemaType(name = "IDREF")
    protected Object ref;

    /**
     * Gets the value of the ref property.
     *
     * @return possible object is
     * {@link Object }
     */
    public Object getRef() {
        return ref;
    }

    /**
     * Sets the value of the ref property.
     *
     * @param value allowed object is
     *              {@link Object }
     */
    public void setRef(Object value) {
        this.ref = value;
    }

}
