package com.acit.myalliance.util;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
/**
 * 
 * @author Dhinakaran P.
 */
// Create a class extends with TimerTask
public class SchedulerTask1 extends TimerTask {
	
	public static void main(String args[]) throws InterruptedException {
		Timer time = new Timer(); // Instantiate Timer Object
		SchedulerTask1 st = new SchedulerTask1(); // Instantiate SheduledTask class
		time.schedule(st, 0, 6000); // Create Repetitively task for every 1 secs
	}
	Date now; // to display current time

	// Add your task here
	public void run() {
		now = new Date(); // initialize date
		System.out.println("Time is@@ :" + now); // Display current time
	}
}
