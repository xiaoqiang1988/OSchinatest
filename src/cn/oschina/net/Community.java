package cn.oschina.net;

public class Community {

	private String name;
	private String houseNumber;
	private String carNumber;
	private String type;
	private String green;
	public Community(String name, String houseNumber, String carNumber,
			String type, String green) {
		super();
		this.name = name;
		this.houseNumber = houseNumber;
		this.carNumber = carNumber;
		this.type = type;
		this.green = green;
	}
	
	
	public Community(String name) {
		super();
		this.name = name;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHouseNumber() {
		return houseNumber;
	}
	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}
	public String getCarNumber() {
		return carNumber;
	}
	public void setCarNumber(String carNumber) {
		this.carNumber = carNumber;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getGreen() {
		return green;
	}
	public void setGreen(String green) {
		this.green = green;
	}
	
	
	public String getProperty(int index){
		String property = "δ֪"; 
		switch (index) {
		case 0:
			property = this.getName();
			break;
		case 1:
			property = this.getHouseNumber();
			break;
		case 2:
			property = this.getCarNumber();
			break;
		case 3:
			property = this.getGreen();
			break;
		case 4:
			property = this.getType();
			break;
		default:
			break;
		}
		return property;
	}
	
	
}
