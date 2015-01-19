package order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customerType", propOrder = {"name"})
public class CustomerType {

    protected String name;
    @XmlAttribute(name = "account")
    protected String account;

    public String getName() {
        return name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String value) {
        this.account = value;
    }

}
