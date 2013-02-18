package friendlab2;


public class Tuple {
	String attribute, state;
	
	public String getAttribute() {
		return attribute;
	}

	public String getState() {
		return state;
	}

	public Tuple(String attribute, String state) {
		this.attribute = attribute;
		this.state = state;
	}
}
