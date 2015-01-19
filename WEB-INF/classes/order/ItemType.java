package order;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemType", propOrder = {
    "name",
    "price",
    "quantity",
    "extended"
})
public class ItemType {
	
	@XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected double price;
    @XmlElement(required = true)
    @XmlSchemaType(name = "positiveInteger")
    protected int quantity;
    @XmlElement(required = true)
    protected double extended;
    @XmlAttribute(name = "number")
    protected String number;

    public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public double getPrice()
	{
		return price;
	}
	public void setPrice(double price)
	{
		this.price = price;
	}
	public int getQuantity()
	{
		return quantity;
	}
	public void setQuantity(int quantity)
	{
		this.quantity = quantity;
	}
	public double getExtended()
	{
		return extended;
	}
	public void setExtended(double extended)
	{
		this.extended = extended;
	}
	public String getNumber()
	{
		return number;
	}
	public void setNumber(String number)
	{
		this.number = number;
	}

}
