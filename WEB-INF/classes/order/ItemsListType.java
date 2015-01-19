package order;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "itemsType", propOrder = {"item"})
public class ItemsListType {

    @XmlElement(required = true)
    protected List<ItemType> item;

	public List<ItemType> getItem() {
		return item;
	}

	public void setItem(List<ItemType> item) {
		this.item = item;
	}
}
