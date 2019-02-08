package org.jenkinsci.plugins;


import hudson.EnvVars;
import hudson.Extension;
import hudson.Launcher;
import hudson.model.BuildListener;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.BuildStepDescriptor;
import hudson.tasks.BuildStepMonitor;
import hudson.tasks.Notifier;
import hudson.tasks.Publisher;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.StaplerRequest;

public class Chat_Room extends Notifier {

 
 private final String url;
 @DataBoundConstructor
 public Chat_Room(final String url) 
 {
  
  this.url = url;
 }
 
 public String getUrl() {
   return url;
  }
 @Override
 public boolean perform(
 @SuppressWarnings("rawtypes") final AbstractBuild build,
 final Launcher launcher, final BuildListener listener) {
 
  try {
  	 EnvVars envVars = build.getEnvironment(listener); 
	 String job = envVars.get("JOB_NAME");
	 listener.getLogger().println("Executon finished for : "+job);
     String message = " JOB_NAME : **"+job+"# "+
    		  			envVars.get("BUILD_NUMBER")+"** "+
    		  			" STATUS : **_"+ build.getResult()+"_**"+
    		  			" Duration: **_"+build.getTimestampString()+"_**"+
    		  			" URL: "+envVars.get("BUILD_URL");
     listener.getLogger().println("Build details : "+message);
     listener.getLogger().println("Sending build details to google chat group..");
     boolean b =sendChatMessage(listener,message);
     if(b==true){
	  listener.getLogger().println("Build result successfully send to google chat group..");
     }else{
      listener.getLogger().println("Error occured while sending message to google chat group");
     }
	  
  	} catch (Exception e) {
  		  	listener.getLogger().printf("Error Occurred .....: %s ", e);
  	}
 
  return true;
 }

    @Override
    public void perform(@Nonnull Run<?, ?> run, @Nonnull FilePath workspace, @Nonnull Launcher launcher, @Nonnull TaskListener listener) {

       
  try {
  	 EnvVars envVars = build.getEnvironment(listener); 
	 String job = envVars.get("JOB_NAME");
	 listener.getLogger().println("Executon finished for : "+job);
     String message = " JOB_NAME : **"+job+"# "+
    		  			envVars.get("BUILD_NUMBER")+"** "+
    		  			" STATUS : **_"+ build.getResult()+"_**"+
    		  			" Duration: **_"+build.getTimestampString()+"_**"+
    		  			" URL: "+envVars.get("BUILD_URL");
     listener.getLogger().println("Build details : "+message);
     listener.getLogger().println("Sending build details to google chat group..");
     boolean b =sendChatMessage(listener,message);
     if(b==true){
	  listener.getLogger().println("Build result successfully send to google chat group..");
     }else{
      listener.getLogger().println("Error occured while sending message to google chat group");
     }
	  
  	} catch (Exception e) {
  		  	listener.getLogger().printf("Error Occurred .....: %s ", e);
  	}
    }	
	
	
 @Override
 public DescriptorImpl getDescriptor() 
 {
  return (DescriptorImpl) super.getDescriptor();
 }

 @Extension
 public static class DescriptorImpl extends BuildStepDescriptor<Publisher>  
 {
  public DescriptorImpl() {
  load();
 }

 @Override
 public boolean configure(StaplerRequest req, JSONObject formData)
 throws FormException {
  save();
  return super.configure(req, formData);
 }
 @Override
 public boolean isApplicable(
 @SuppressWarnings("rawtypes") Class<? extends AbstractProject> jobType) {
  // Indicates that this builder can be used with all kinds of project 
  // types.
  return true;
 }

 @Override
 public String getDisplayName() {
  return "Google Chat Notification";
 }
 }

 public BuildStepMonitor getRequiredMonitorService() {
 return BuildStepMonitor.NONE;
 }

 private boolean sendChatMessage(BuildListener listener,String msg) throws Exception {
   
   String url_orig = url;
   URL url = new URL(url_orig);
    HttpURLConnection conn = (HttpURLConnection) url.openConnection(); 
	conn.setDoOutput(true);
	conn.setRequestMethod("POST");
	conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
	conn.setRequestProperty("Accept", "application/json");
	JSONObject parent = new JSONObject();
	parent.put("text",msg);
	OutputStream wr = conn.getOutputStream();
	wr.write(parent.toString().getBytes("UTF-8"));
	wr.close();
	  if (conn.getResponseCode() != 200) 
	  {
		   throw new RuntimeException("Failed : HTTP error code : "+ conn.getResponseCode());
	   } else{
		 wr.close();
		 conn.disconnect();


	   } 
  return true; 
  }
}
