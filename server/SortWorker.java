/*
CSci364 - HW3, SortWorker.java
Patrick Dougherty
patrick.r.dougherty@und.edu
26Mar2020
 */
package server;

import java.util.*;

import api.Worker;

/**
 * @author david
 * and a few modification by Patrick
 */
class SortWorker<T extends Comparable<? super T>> extends Worker {
	private static final long serialVersionUID = -4051898108461870504L;
	private List<T> list = new ArrayList<T>();
	
	/**
	 * @param id 
	 * @param arr an array of items to be sorted 
	 */
	@SafeVarargs
	SortWorker(UUID id, T ...arr) {
		super(id, "Sort");
		Collections.addAll(list, arr);
	}

	/* (non-Javadoc)
	 * @see hw3.Worker#doWork()
	 */
	@Override
	public void doWork() {
		Collections.sort(list);
	}
		
	/**
	 * 
	 * @return 
	 */
	public List<T> getList() {
		return list;
	}

	/**
	 * Test harness
	 * @param args There are no arguments.
	*/
	public static void main(String[] args) {
		UUID id = UUID.randomUUID();
		SortWorker<String> sw1 = new SortWorker<>(id, "cat", "mouse", "dog");
		sw1.doWork();
		System.out.println(sw1.list);

		id = UUID.randomUUID();
		SortWorker<Integer> sw2 = new SortWorker<>(id, 3, 1, 5, 4);
		sw2.doWork();
		System.out.println(sw2.list);
	}

}
