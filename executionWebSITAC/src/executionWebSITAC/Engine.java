package executionWebSITAC;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipInputStream;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.runtime.Execution;
import org.activiti.engine.task.Task;

public class Engine {

	private static ProcessEngine processEngine;
	
	private static Service currentService;

	static {
		
		try {
			
			//Creation of the engine
			
			InputStream confFileInputStream = Engine.class.getClassLoader().
												getResourceAsStream("activiti.cfg.xml");
			
			processEngine = ProcessEngineConfiguration
								.createProcessEngineConfigurationFromInputStream(
										confFileInputStream).buildProcessEngine();
			
			
			//Deployment of the process
			
			InputStream processInputStream = Engine.class.getClassLoader().
					getResourceAsStream("MyProcess.bar");
			ZipInputStream zipin = new ZipInputStream(processInputStream);
		
			processEngine.getRepositoryService().createDeployment().addZipInputStream(zipin).deploy();
		}
		catch(Exception e) {	
			System.out.println(e.getMessage());
		}	
	}
	
	public static ProcessEngine getProcessEngine() {
		
		return processEngine;
	}
	
	public static void startProcessInstance() {
		
		//Delete all of the other running process instances
		
		List<Execution> pInstances= processEngine.getRuntimeService().
										createExecutionQuery().list();
		
		List<String> ids = new ArrayList<String>();
		
		for(Execution ex : pInstances) {
			ids.add(ex.getId());
		}
		
		for(String id : ids) {
			processEngine.getRuntimeService().deleteProcessInstance(id, null);
		}
		
		//Start a new process instance
		
		processEngine.getRuntimeService().startProcessInstanceByKey(
				getProcessId(), new HashMap<String, Object>());
		
	}
	
	public static void deleteProcessInstance() {
		
		String processInstanceId = processEngine.getRuntimeService().
				createExecutionQuery().singleResult().getProcessInstanceId();
		
		processEngine.getRuntimeService().deleteProcessInstance(processInstanceId, null);
	}
	
	public static Object getProcessVariable(String varId) {
		
		String processInstanceId = processEngine.getRuntimeService().
				createExecutionQuery().singleResult().getProcessInstanceId();
		
		return processEngine.getRuntimeService().getVariable(processInstanceId, varId);
	}
	
	public static void setProcessVariable(String varId, Object value) {
		
		String processInstanceId = processEngine.getRuntimeService().
				createExecutionQuery().singleResult().getProcessInstanceId();
		
		processEngine.getRuntimeService().setVariable(processInstanceId, varId, value);
	}
	
	public static Task getExecutableTask() {
		
		List<Task> tasks = processEngine.getTaskService().createTaskQuery().list();
		
		if(tasks.size() == 0) {
			return null;
		}
		
		return tasks.get(0);
	}
	
	public static void executeTask(String id) {
		
		processEngine.getTaskService().complete(id);
	}
	
	public static String getProcessId() {
		
		return processEngine.getRepositoryService()
				.createProcessDefinitionQuery().singleResult().getKey();
	}
	
	public static String getProcessName() {
		
		return processEngine.getRepositoryService()
				.createProcessDefinitionQuery().singleResult().getName();
	}

	public static Service getCurrentService() {
		return currentService;
	}

	public static void setCurrentService(Service currentService) {
		Engine.currentService = currentService;
	}
}
