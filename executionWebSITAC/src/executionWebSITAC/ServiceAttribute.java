package executionWebSITAC;

import java.util.HashMap;
import java.util.Map;

public class ServiceAttribute {

	private String id;
	private String name;
	private String type;
	private Object value;
	
	private Map<String, String> options; //For attributes of type Single/Multiple Choices
	
	//This property (which only applies for input parameters) indicates whether the value
	//of the Service Attribute will be given by the user at runtime or taken from the output
	//of a previous service. In the first case, this property must be null. In the second
	//case, this property stores the id of a process variable
	private String takesValueFrom;
	
	//This property only applies for output parameters. It indicates whether the parameter
	//must be shown to the user after the execution of a service.
	private boolean mustBeShown;
	
	private String predefinedValue;
	
	public ServiceAttribute() {
		
		this.setOptions(new HashMap<String, String>());
		this.setMustBeShown(false);
	}
	
	public ServiceAttribute(String id, String name, String type) {
		
		this();
		
		this.id = id;
		this.name = name;
		this.type = type;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
		this.type = type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getTakesValueFrom() {
		return takesValueFrom;
	}

	public void setTakesValueFrom(String takesValueFrom) {
		this.takesValueFrom = takesValueFrom;
	}

	public boolean isMustBeShown() {
		return mustBeShown;
	}

	public void setMustBeShown(boolean mustBeShown) {
		this.mustBeShown = mustBeShown;
	}

	public String getPredefinedValue() {
		return predefinedValue;
	}

	public void setPredefinedValue(String predefinedValue) {
		this.predefinedValue = predefinedValue;
	}

	public Map<String, String> getOptions() {
		return options;
	}

	public void setOptions(Map<String, String> options) {
		this.options = options;
	}
}
