
package uk.ac.ebi.ddi.api.readers.px.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * A place for all additional information that may be added to the
 * broadcast message and that could not be fit into another location. Here should
 * also go references to other ProteomeXchange submissions.
 *
 * <p>Java class for AdditionalInformationType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="AdditionalInformationType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="cvParam" type="{}CvParamType" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="userParam" type="{}UserParamType" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AdditionalInformationType", propOrder = {
        "cvParam",
        "userParam"
})
public class AdditionalInformationType
        implements Serializable, PXObject {

    private static final long serialVersionUID = 100L;
    protected List<CvParamType> cvParam;
    protected List<UserParamType> userParam;

    /**
     * Gets the value of the cvParam property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the cvParam property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getCvParam().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CvParamType }
     */
    public List<CvParamType> getCvParam() {
        if (cvParam == null) {
            cvParam = new ArrayList<>();
        }
        return this.cvParam;
    }

    /**
     * Gets the value of the userParam property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the userParam property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUserParam().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UserParamType }
     */
    public List<UserParamType> getUserParam() {
        if (userParam == null) {
            userParam = new ArrayList<>();
        }
        return this.userParam;
    }

}
