/*
CSci364 - HW3, Worker.java
Patrick Dougherty
patrick.r.dougherty@und.edu
26Mar2020
 */
package api;

import java.io.Serializable;
import java.util.UUID;

/**
 * @author david apostal
 *
 */
public abstract class Worker implements Serializable {
	private static final long serialVersionUID = -1697662415894139584L;
	private UUID taskId;
	private String taskName;
	
	/**
	 * @param id the task id for the work task
	 * @param name the name of this task
	 */
	public Worker(UUID id, String name) {
		taskId = id;
		taskName = name;
	}
	
	/**
	 * 
	 * @return the id of this worker. A unique value that distinguishes this 
	 * instance of a Worker from all other instances.
	 */
	public UUID getTaskId() {
		return taskId;
	}
	
	/**
	 * 
	 * @return the name of this worker
	 */
	public String getTaskName() {
		return taskName;
	}

	/**
	 * Does the work for this worker. This method shall only be called by the 
	 * volunteer client. 
	 */
	public abstract void doWork();

}
