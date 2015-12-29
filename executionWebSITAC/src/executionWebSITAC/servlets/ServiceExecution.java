package executionWebSITAC.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import executionWebSITAC.Engine;
import executionWebSITAC.Service;
import executionWebSITAC.ServiceAttribute;


public class ServiceExecution extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
       
    
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	
		String cancelButton = request.getParameter("cancelButton");
		
		if(cancelButton != null) {
			
			Engine.setCurrentService(null);
			Engine.deleteProcessInstance();
			
			request.getRequestDispatcher("index.jsp").forward(request, response);
		}
		else {
		
			String taskId = (String) request.getParameter("taskExecutionId");
			
			//Fill the values of the service input parameters
			
			Service serv = Engine.getCurrentService();
			
			if(serv != null) {
				for(ServiceAttribute at : serv.getInputParams()) {
					String takesValueFrom = at.getTakesValueFrom();
					String predefinedValue = at.getPredefinedValue();
					if(takesValueFrom == null && predefinedValue == null) { //At runtime
						if(at.getType().equals("multipleChoice")) {
							at.setValue(request.getParameterValues(at.getId()));
						}
						else if(at.getType().equals("latLon")) {
							String lat = request.getParameter(at.getId() + "Lat");
							String lng = request.getParameter(at.getId() + "Lng");
							String[] latLon = new String[] {lat, lng};
							at.setValue(latLon);
						}
						else {
							at.setValue(request.getParameter(at.getId()));
						}
					}
					else if(takesValueFrom != null){
						Object value = Engine.getProcessVariable(takesValueFrom);
						at.setValue(value);
					}
				}
			}
			
			serv.execute();
			
			Engine.executeTask(taskId);			
			
			//Dispatch request to the User Task servlet; this servlet checks
			//whether there is another user task to execute or the process is finished
			
			request.getRequestDispatcher("UserTask").forward(request, response);
		}
	}

}
