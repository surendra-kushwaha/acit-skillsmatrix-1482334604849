package com.acit.myalliance.timer;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Timer;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.acit.myalliance.util.Utility;

 
@WebServlet("/getSkillsData")
public class TimerServlet extends HttpServlet {
 
/**
* 
*/
private static final long serialVersionUID = 1L;
TaskScheduler schedulerJobs = new TaskScheduler();
int array = -1;
 
@Override
public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    
         add(request, response);
}
 
@Override
public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        doGet(request,response); 
}
 
void add(HttpServletRequest request, HttpServletResponse response) throws IOException {
{
        //String jobName = request.getParameter(CRON_NAME);
		System.out.println("cron add method called");
		String jobName =Utility.getProperties("BatchJobName"); 
        int interval = Integer.parseInt(Utility.getProperties("SchedulerInterval"));
        boolean state=Boolean.parseBoolean(Utility.getProperties("EnableBatchJob"));
 
        schedulerJobs = new TaskScheduler();
        schedulerJobs.setCronName(jobName);
        schedulerJobs.setInterval(interval);
        schedulerJobs.setEnableState(state);
        String data=schedulerJobs.runSharePoint();
        System.out.println("State Interval::"+state+interval);
        // running timer task as daemon thread
        Timer timer = new Timer(true);        
        //timer.scheduleAtFixedRate(schedulerJobs, 0,interval * 60 * 1000);
        // return
        PrintWriter out = response.getWriter();
        out.println(jobName + " successful" + "Data::"+data);
        //System.out.println("inside add  added successfully");
        response.setContentType("text/html");
        response.setStatus(HttpServletResponse.SC_OK);
        return;
        }
} 
}
