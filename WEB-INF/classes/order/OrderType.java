package order;

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "orderType", propOrder = {
    "customer",
    "items",
    "total",
    "shipping",
    "hst",
    "grandTotal"
})

@XmlRootElement(name="order")
public class OrderType {

    @XmlElement(required = true)
    protected CustomerType customer;
    @XmlElement(required = true)
    protected ItemsListType items;
    @XmlElement(required = true)
    protected double total;
    @XmlElement(required = true)
    protected double shipping;
    @XmlElement(name = "HST", required = true)
    protected double hst;
    @XmlElement(required = true)
    protected double grandTotal;
    @XmlAttribute(name = "id", required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected BigInteger id;
    @XmlAttribute(name = "submitted")
    @XmlSchemaType(name = "date")
    protected XMLGregorianCalendar submitted;
    
	public CustomerType getCustomer()
	{
		return customer;
	}
	public void setCustomer(CustomerType customer)
	{
		this.customer = customer;
	}
	public ItemsListType getItems()
	{
		return items;
	}
	public void setItems(ItemsListType items)
	{
		this.items = items;
	}
	public double getTotal()
	{
		return total;
	}
	public void setTotal(double total)
	{
		this.total = total;
	}
	public double getShipping()
	{
		return shipping;
	}
	public void setShipping(double shipping)
	{
		this.shipping = shipping;
	}
	public double getHst()
	{
		return hst;
	}
	public void setHst(double hst)
	{
		this.hst = hst;
	}
	public double getGrandTotal()
	{
		return grandTotal;
	}
	public void setGrandTotal(double grandTotal)
	{
		this.grandTotal = grandTotal;
	}
	public BigInteger getId()
	{
		return id;
	}
	public void setId(BigInteger id)
	{
		this.id = id;
	}
	public XMLGregorianCalendar getSubmitted()
	{
		return submitted;
	}
	public void setSubmitted(XMLGregorianCalendar submitted)
	{
		this.submitted = submitted;
	}

}
