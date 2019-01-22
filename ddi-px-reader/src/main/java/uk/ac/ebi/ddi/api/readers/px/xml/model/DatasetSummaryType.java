
package uk.ac.ebi.ddi.api.readers.px.xml.model;

import javax.xml.bind.annotation.*;
import javax.xml.datatype.XMLGregorianCalendar;
import java.io.Serializable;


/**
 * Broad description of the dataset being announced, inlcuding a title, abstract, review level, etc.
 *
 * <p>Java class for DatasetSummaryType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="DatasetSummaryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="Description" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="ReviewLevel" type="{}ReviewLevelType"/>
 *         &lt;element name="RepositorySupport" type="{}RepositorySupportType"/>
 *       &lt;/sequence>
 *       &lt;attribute name="announceDate" use="required" type="{http://www.w3.org/2001/XMLSchema}date" />
 *       &lt;attribute name="hostingRepository" use="required" type="{}HostingRepositoryType" />
 *       &lt;attribute name="title" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatasetSummaryType", propOrder = {
        "description",
        "reviewLevel",
        "repositorySupport"
})
public class DatasetSummaryType
        implements Serializable, PXObject {

    private static final long serialVersionUID = 100L;
    @XmlElement(name = "Description", required = true)
    protected String description;
    @XmlElement(name = "ReviewLevel", required = true)
    protected ReviewLevelType reviewLevel;
    @XmlElement(name = "RepositorySupport", required = true)
    protected RepositorySupportType repositorySupport;
    @XmlAttribute(required = true)
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar announceDate;
    @XmlAttribute(required = true)
    protected HostingRepositoryType hostingRepository;
    @XmlAttribute(required = true)
    protected String title;

    /**
     * Gets the value of the description property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDescription() {
        return description;
    }

    /**
     * Sets the value of the description property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDescription(String value) {
        this.description = value;
    }

    /**
     * Gets the value of the reviewLevel property.
     *
     * @return possible object is
     * {@link ReviewLevelType }
     */
    public ReviewLevelType getReviewLevel() {
        return reviewLevel;
    }

    /**
     * Sets the value of the reviewLevel property.
     *
     * @param value allowed object is
     *              {@link ReviewLevelType }
     */
    public void setReviewLevel(ReviewLevelType value) {
        this.reviewLevel = value;
    }

    /**
     * Gets the value of the repositorySupport property.
     *
     * @return possible object is
     * {@link RepositorySupportType }
     */
    public RepositorySupportType getRepositorySupport() {
        return repositorySupport;
    }

    /**
     * Sets the value of the repositorySupport property.
     *
     * @param value allowed object is
     *              {@link RepositorySupportType }
     */
    public void setRepositorySupport(RepositorySupportType value) {
        this.repositorySupport = value;
    }

    /**
     * Gets the value of the announceDate property.
     *
     * @return possible object is
     * {@link XMLGregorianCalendar }
     */
    public XMLGregorianCalendar getAnnounceDate() {
        return announceDate;
    }

    /**
     * Sets the value of the announceDate property.
     *
     * @param value allowed object is
     *              {@link XMLGregorianCalendar }
     */
    public void setAnnounceDate(XMLGregorianCalendar value) {
        this.announceDate = value;
    }

    /**
     * Gets the value of the hostingRepository property.
     *
     * @return possible object is
     * {@link HostingRepositoryType }
     */
    public HostingRepositoryType getHostingRepository() {
        return hostingRepository;
    }

    /**
     * Sets the value of the hostingRepository property.
     *
     * @param value allowed object is
     *              {@link HostingRepositoryType }
     */
    public void setHostingRepository(HostingRepositoryType value) {
        this.hostingRepository = value;
    }

    /**
     * Gets the value of the title property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the value of the title property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTitle(String value) {
        this.title = value;
    }

}
