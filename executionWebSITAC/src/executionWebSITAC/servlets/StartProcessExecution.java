package executionWebSITAC.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import executionWebSITAC.Engine;

public class StartProcessExecution extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		
		//Start a new instance of the process 
		
		Engine.startProcessInstance();

		//Dispatch request to the servlet that handles user tasks
		
		request.getRequestDispatcher("UserTask").forward(request, response);
	}

}
