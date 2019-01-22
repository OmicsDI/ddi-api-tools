
package uk.ac.ebi.ddi.api.readers.px.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;
import java.io.Serializable;


/**
 * A single user-defined parameter.
 *
 * <p>Java class for UserParamType complex type.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 *
 * <pre>
 * &lt;complexType name="UserParamType">
 *   &lt;complexContent>
 *     &lt;extension base="{}AbstractParamType">
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "UserParamType")
public class UserParamType
        extends AbstractParamType
        implements Serializable {

    private static final long serialVersionUID = 100L;

}
