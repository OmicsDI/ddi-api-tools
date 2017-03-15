
package uk.ac.ebi.ddi.api.readers.px.xml.model;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * The list of the origins of this dataset. This list can link to other ProteomeXchange datasets or other resources. 
 *         If this dataset contains previously unreported data, then a 'new dataset' annotation should be used.
 * 
 * <p>Java class for DatasetOriginListType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="DatasetOriginListType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="DatasetOrigin" type="{}DatasetOriginType"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatasetOriginListType", propOrder = {
    "datasetOrigin"
})
public class DatasetOriginListType
    implements Serializable, PXObject
{

    private final static long serialVersionUID = 100L;
    @XmlElement(name = "DatasetOrigin", required = true)
    protected DatasetOriginType datasetOrigin;

    /**
     * Gets the value of the datasetOrigin property.
     * 
     * @return
     *     possible object is
     *     {@link DatasetOriginType }
     *     
     */
    public DatasetOriginType getDatasetOrigin() {
        return datasetOrigin;
    }

    /**
     * Sets the value of the datasetOrigin property.
     * 
     * @param value
     *     allowed object is
     *     {@link DatasetOriginType }
     *     
     */
    public void setDatasetOrigin(DatasetOriginType value) {
        this.datasetOrigin = value;
    }

}
