package executionWebSITAC;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.activiti.engine.impl.util.json.JSONObject;

public class Service {

	private String id;
	private String name;
	private String url;
	private List<ServiceAttribute> inputParams;
	private List<ServiceAttribute> outputParams;
	private String inputType;
	private String method;
	
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
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
	
	public List<ServiceAttribute> getInputParams() {
		return inputParams;
	}
	
	public void setInputParams(List<ServiceAttribute> inputParams) {
		this.inputParams = inputParams;
	}

	public List<ServiceAttribute> getOutputParams() {
		return outputParams;
	}

	public void setOutputParams(List<ServiceAttribute> outputParams) {
		this.outputParams = outputParams;
	}
	
	public String getInputType() {
		return inputType;
	}

	public void setInputType(String inputType) {
		this.inputType = inputType;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}
	
	public void execute() {
		
		try {
			String input = "";
			
			if(this.getInputType().equals("queryString")) {
				
				//Arrange the Query String
				
				input += "?";
				
				for(ServiceAttribute att : this.inputParams) {
					if(att.getType().equals("multipleChoice")|| 
							att.getType().equals("latLon")) {
						String[] attValues = (String[]) att.getValue();
						for(int i = 0; i < attValues.length; i++) {
							input += att.getId() + "=" + attValues[i] + "&";
						}
					}
					else {
						String value = att.getPredefinedValue() != null? 
									att.getPredefinedValue() : att.getValue().toString();
									
						input += att.getId() + "=" + value + "&";
					}
				}
				
				input = input.substring(0, input.length() - 1);
				
			}
			else { //payload
				
				//Arrange input parameters in JSON format
				
				input += "{";
				
				for(ServiceAttribute att : this.inputParams) {
					String value = "";
					
					if(att.getType().equals("multipleChoice") || 
							att.getType().equals("latLon")) {
						value = "[";
						String[] attValues = (String[]) att.getValue();
						for(int i = 0; i < attValues.length; i++) {
							value += attValues[i] + ",";
						}
						if(attValues.length > 0) {
							value = value.substring(0, value.length() - 1);
						}
						value += "]";
					}
					else {
						value = att.getPredefinedValue() != null? 
									att.getPredefinedValue() : att.getValue().toString();
					}
					
					input += att.getId() + ":" + value + ",";
				}
				
				input = input.substring(0, input.length() - 1) + "}";
			}
			
			//Create connection object
			
			String URLstring = this.getUrl();
			if(this.getInputType().equals("queryString")) {
				URLstring += input;
			}
			
			URL urlobj = new URL(URLstring);
			HttpURLConnection httpCon = (HttpURLConnection) urlobj.openConnection();
			httpCon.setDoOutput(true);
			httpCon.setRequestMethod(this.getMethod());
			
			if(this.getInputType().equals("payload")) {
				httpCon.setRequestProperty("Content-Type", "application/json");
				OutputStreamWriter out = new OutputStreamWriter(httpCon.getOutputStream());
				out.write(input);
				out.close();
			}
			
			//Get response
			
			InputStream is = httpCon.getInputStream();
		    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
		    String inputLine, json = "";
	        while ((inputLine = rd.readLine()) != null) {
	        	json += inputLine + "\n";
	        }
	        rd.close();
	        
	        //Extract result from JSON
	        
	        //TODO: Support for output parameters of type MultipleChoice
	        
	        JSONObject jsonObj = new JSONObject(json);
	        for(ServiceAttribute att : this.outputParams) {
	        	Object value = jsonObj.get(att.getId());
	        	att.setValue(value);
	        	
	        	//TODO: Create a process variable only when necessary
	        	//(here, I am always creating a process variable)
	        	
	        	Engine.setProcessVariable(att.getId(), value);
	        }
		}
		catch(IOException e) {
			System.out.println(e.getMessage());
		}
	}

	
	

}
