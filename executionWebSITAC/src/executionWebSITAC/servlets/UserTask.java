package executionWebSITAC.servlets;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowElement;
import org.activiti.bpmn.model.Process;
import org.activiti.engine.impl.util.json.JSONArray;
import org.activiti.engine.impl.util.json.JSONObject;
import org.activiti.engine.task.Task;

import executionWebSITAC.Engine;
import executionWebSITAC.Service;
import executionWebSITAC.ServiceAttribute;

public class UserTask extends HttpServlet {
	
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//Store the result of the previous task, so it can be shown to the user
		
		request.setAttribute("ServiceResult", getServiceResult());
		
		//Get the next executable user task
		
		Task task = Engine.getExecutableTask();
		if(task == null) {
			Engine.setCurrentService(null);
			request.setAttribute("success", "aaa");
			request.getRequestDispatcher("index.jsp").forward(request, response);
			return;
		}
		
		//Set the properties that are needed to generate the jsp page
		
		request.setAttribute("TaskExecutionId", task.getId());
		
		org.activiti.bpmn.model.UserTask ut = getUserTask(task);
		
		String serviceId = getServiceId(ut.getDocumentation());
		
		Service service = new Service();
		
		service.setId(serviceId);
		
		service.setName(task.getName());
		
		setUrlInputTypeAndMethod(service);
		
		List<ServiceAttribute> inputParams = getServiceInputParams(serviceId, ut.getDocumentation());
		service.setInputParams(inputParams);
		
		List<ServiceAttribute> outputParams = getServiceOutputParams(serviceId, ut.getDocumentation());
		service.setOutputParams(outputParams);
		
		Engine.setCurrentService(service);
		
		//Dispatch request (if none of the input params must be specified by the user, then
		//the UserTask.jsp page must not be shown)
		
		boolean forwarded = false;
		
		for(int i = 0; i < inputParams.size() && !forwarded; i++) {
			
			ServiceAttribute at = inputParams.get(i);
			
			if(at.getTakesValueFrom() == null && at.getPredefinedValue() == null) { //At runtime
				forwarded = true;
				request.getRequestDispatcher("UserTask.jsp").forward(request, response);
			}
		}
		
		if(!forwarded) {
			request.getRequestDispatcher("ServiceExecution").forward(request, response);
		}
	}

	private org.activiti.bpmn.model.UserTask getUserTask(Task task) {
		
		BpmnModel model = Engine.getProcessEngine().getRepositoryService()
				.getBpmnModel(task.getProcessDefinitionId());

		Process p = model.getMainProcess();
		if(p != null) {
			for(FlowElement fe : p.getFlowElements()) {
				if(fe instanceof org.activiti.bpmn.model.UserTask) {
					org.activiti.bpmn.model.UserTask ut = (org.activiti.bpmn.model.UserTask) fe;
					if(ut.getName().equals(task.getName()) &&
							ut.getId().equals(task.getTaskDefinitionKey())) {
						return ut;
					}
				}
			}
		}
		
		return null;
	}
	
	private String getServiceId(String documentation) {
		
		String[] tokens = documentation.split("\\|", -1);
		
		if(tokens.length > 0) {
			return tokens[0];
		}
		
		return "";
	}
	
	private List<ServiceAttribute> getServiceInputParams(String serviceId, String documentation) {
		
		List<ServiceAttribute> result = new ArrayList<ServiceAttribute>();
				
		if(!serviceId.equals("")) {
			
			List<String> runtimeParams = new ArrayList<String>();
			Map<String, String> takesValueFromParams = new HashMap<String, String>();
			Map<String, String> predefinedValueParams = new HashMap<String, String>();
			
			String[] tokens = documentation.split("\\|", -1);
			
			for(int i = 1; i < tokens.length; i++) {
				if(!tokens[i].equals("")) {
					if(i == 1) {
						String[] tokens2 = tokens[i].split(",");
						for(int j = 0; j < tokens2.length; j++) {
							runtimeParams.add(tokens2[j]);
						}
					}
					else if(i == 2) {
						String[] tokens2 = tokens[i].split(",");
						for(int j = 0; j < tokens2.length; j++) {
							String[] tokens3 = tokens2[j].split(":");
							takesValueFromParams.put(tokens3[0], tokens3[1]);
						}
					}
					else if(i == 4) {
						String[] tokens2 = tokens[i].split(",");
						for(int j = 0; j < tokens2.length; j++) {
							String[] tokens3 = tokens2[j].split(":");
							predefinedValueParams.put(tokens3[0], tokens3[1]);
						}
					}
				}
			}
			
			//Get the service input params from the SITAC registry
			
			try {
				String json = getJSON(serviceId);
				JSONObject jsonObj = new JSONObject(json);
				
				JSONArray inputArgs = (JSONArray) jsonObj.get("inputArguments");
				for(int i = 0; i < inputArgs.length(); i++) {
					JSONObject arg = inputArgs.getJSONObject(i);
					String id = (String) arg.get("id");
					String name = (String) arg.get("name");
					String type = (String) arg.get("type");
					
					ServiceAttribute at = new ServiceAttribute(id, name, type);
					if(runtimeParams.contains(at.getId())) {
						at.setTakesValueFrom(null);
						at.setPredefinedValue(null);
					}
					else if(takesValueFromParams.containsKey(at.getId())){
						String value = takesValueFromParams.get(at.getId());
						at.setTakesValueFrom(value);
					}
					else if(predefinedValueParams.containsKey(at.getId())){
						String value = predefinedValueParams.get(at.getId());
						at.setPredefinedValue(value);
					}
					
					if(at.getType().equals("singleChoice") ||
							at.getType().equals("multipleChoice")) {
						fillOptions(at, arg);
					}
					
					result.add(at);
				}
			}
			catch(IOException e) {
				
			}
		}
		
		return result;
	}
	
	private List<ServiceAttribute> getServiceOutputParams(String serviceId, String documentation) {
		
		List<ServiceAttribute> result = new ArrayList<ServiceAttribute>();
		
		//TODO: Get (from the process model) the ids of the output params that must be shown to the user
				
		//...
		
		if(!serviceId.equals("")) {
			
			//Get the service output param from the SITAC registry
			
			try {
				String json = getJSON(serviceId);
				JSONObject jsonObj = new JSONObject(json);
				
				JSONArray inputArgs = (JSONArray) jsonObj.get("outputArguments");
				for(int i = 0; i < inputArgs.length(); i++) {
					JSONObject arg = inputArgs.getJSONObject(i);
					String id = (String) arg.get("id");
					String name = (String) arg.get("name");
					String type = (String) arg.get("type");
					
					ServiceAttribute sa = new ServiceAttribute(id, name, type);
					
					//TODO: Set the value of the "mustBeShown" property
					// I set it to true, change this code
					
					sa.setMustBeShown(true);
					
					result.add(sa);
				}
			}
			catch(IOException e) {
				
			}
		}
		
		return result;
	}
	
	private void setUrlInputTypeAndMethod(Service serv) {
		
		String serviceId = serv.getId();
		
		if(!serviceId.equals("")) {
			
			try {
				String json = getJSON(serviceId);
				JSONObject jsonObj = new JSONObject(json);
				
				String url = (String) jsonObj.getString("url");
				String inputType = (String) jsonObj.getString("inputType");
				String method = (String) jsonObj.getString("method");
				
				serv.setUrl(url);
				serv.setInputType(inputType);
				serv.setMethod(method);
			}
			catch(IOException e) {
				
			}
		}
	}
	
	private void fillOptions(ServiceAttribute at, JSONObject arg) {
		
		Map<String,String> options = new HashMap<String, String>();
		
		JSONArray array = (JSONArray) arg.get("options");
		
		for(int i = 0; i < array.length(); i++) {
			JSONObject elem = array.getJSONObject(i);
			String value = (String) elem.get("value");
			String label = (String) elem.get("label");
			
			options.put(value,  label);
		}
		
		at.setOptions(options);
	}
	
	private String getJSON(String serviceId) throws IOException {
		
		URL url = new URL("http://158.42.185.44:8182/sitacRegistry/registry/service/" + serviceId);
		HttpURLConnection httpCon = (HttpURLConnection) url.openConnection();
		httpCon.setRequestMethod("GET");						
		InputStream is = httpCon.getInputStream();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    String inputLine, json = "";
        while ((inputLine = rd.readLine()) != null) {
        	json += inputLine + "\n";
        }
        rd.close();
        
        return json;
	}
	
	private String getServiceResult() {
		
		String result = "";
		
		Service serv = Engine.getCurrentService();
		
		if(serv != null) {
			List<ServiceAttribute> outputParams = serv.getOutputParams();
			
			for(ServiceAttribute sa : outputParams) {
				if(sa.isMustBeShown()) {
					result += sa.getName() + ": " + sa.getValue() + "\\n";
				}
			}
			
			if(!result.equals("")) {
				result = "The output of the \\\""+ serv.getName() + "\\\" service is:\\n\\n" + result;
			}
		}
		
		return result;
	}
}
