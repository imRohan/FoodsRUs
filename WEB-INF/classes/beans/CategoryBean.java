package beans;

import java.sql.Blob;

public class CategoryBean {
	
	private int id;
	private String name;
	private String description;
	private byte[] picture;

	/**
	 * @param id
	 * @param name
	 * @param description
	 */
	public CategoryBean(int id, String name, String description, byte[] picture) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.picture = picture;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	public byte[] getPicture()
	{
		return picture;
	}
	public void setPicture(byte[] picture)
	{
		this.picture = picture;
	}
	

}
