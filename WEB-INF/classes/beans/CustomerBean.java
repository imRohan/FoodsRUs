package beans;

public class CustomerBean {
	public String name;
	public String number;

	public CustomerBean(String name, String number) {
		setName(name);
		setNumber(number);
	}

	public CustomerBean() {
		// TODO Auto-generated constructor stub
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
	
	public String toString() {
		return "CustomerBean [name=" + name + ", number=" + number + "]";
	}

}
