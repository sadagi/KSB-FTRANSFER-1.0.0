package sadagi.utilities.servers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

import sadagi.utilities.exceptions.KSBEmptyPathsExcetion;
import sadagi.utilities.properties.Config;
import sadagi.utilities.validators.PuttyUtilityValidator;

/**
 * <p>
 * KSBUtility class is a business class of api. 
 * It provides all functionality for getting transaction between user application and server. 
 * 
 * @author Chandan Singh Sroniyan (Contact : +91 7827711464, Email : sadagi.chandan@gmail.com).
 */
public class KSBUtility implements Config{
	
	/**
	 * Global variable for process.
	 */
	private Session session = null;
	private String channeltype = null;
	private ChannelSftp channelSftp = null;
	private Channel channel = null;
	
	/**
	  * <p>
	  * It is parameterized constructor. Which initialize session of server transaction.
	  *  
	  * @param USER It is a String variable for username of server access.
	  * @param PASS It is a String variable for password of server access.
	  * @param IP It is a String variable for ip of server access.
	  * @param PORT It is a Integer variable for port number of server access.
	  * @param CHANNELTYPE It is a String (value:sftp only allowed) variable for channel type of server access.
	  * @throws JSchException  Its method throw JSchException
	  */
	public KSBUtility(String USER, String PASS, String IP, int PORT, String CHANNELTYPE) throws JSchException{
		
		this.ConstructorValidator(USER, PASS,IP, PORT, CHANNELTYPE);
		
		this.channeltype = CHANNELTYPE;
		
		JSch jsch = new JSch();
		this.session = jsch.getSession(USER, IP, PORT);
		session.setPassword(PASS);
		Properties config = new Properties();
		config.put("StrictHostKeyChecking", "no");
		session.setConfig(config);
		session.setConfig("PreferredAuthentications", "publickey,keyboard-interactive,password");
		session.connect();
		
		this.channel = this.session.openChannel(this.channeltype);
		this.channel.connect();
		this.channelSftp = (ChannelSftp) channel;
	}
	
	/**
	  * <p>
	  * It is a public method. It downloads files as result for online process.
	  *  
	  * @param PATHS It is a HashMap parameters which contains source and destination files path only.
	  * 
	  */
	public void downloadFiles(HashMap<String, String> PATHS)
	{
		this.methodValidator(PATHS);
		 OutputStream output =null;
		 
		 try
			{
				if (!PATHS.isEmpty()) {
		            
					for(Map.Entry<String, String> EN : PATHS.entrySet())
					{
		            	output = null;
		                output = new FileOutputStream(EN.getValue());
		                this.channelSftp.get(EN.getKey(), output);
		                output.close();
		            }

		        }else {
		            throw new KSBEmptyPathsExcetion("Not found required element in HashSet." ,"Please fill the source and destination in HashSet (HashSet<source, destination>).");
		            
		        }
		    } catch (SftpException ex) {
		    	ex.printStackTrace();
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }catch (Exception ex) {
		       ex.printStackTrace();
		    }finally {
		    	 output =null;
				 System.runFinalization();
				 System.gc();
		    }
	}
	
	/**
	  * <p>
	  * It is a public method. It downloads files as result for offline process.
	  *  
	  * @param PATHS It is a HashMap parameters which contains source and destination files path only.
	  * @param commandPB It is a JProgressBar variable which contains JProgressBar object for capturing progress.
	  * @param commandL It is a JLabel variable which contains JLabel object for capturing progress name.
	  * 
	  */
	public void downloadFiles(HashMap<String, String> PATHS, JProgressBar commandPB, JLabel commandL)
	{
		this.methodValidator(PATHS, commandPB, commandL);
		
		
		 OutputStream output =null;
		 int pIdex = commandPB.getValue();
		 int sIndex = (100-pIdex)/PATHS.size();
		
		 try
			{
				if (!PATHS.isEmpty()) {
		            
					for(Map.Entry<String, String> EN : PATHS.entrySet())
					{
						commandL.setText("Downloading : "+EN.getKey());
		            	output = null;
		                output = new FileOutputStream(EN.getValue());
		                this.channelSftp.get(EN.getKey(), output);
		                output.close();
		                
		                pIdex = pIdex+sIndex;
		                commandPB.setValue(pIdex);
		                
		            }

		        }else {
		            throw new KSBEmptyPathsExcetion("Not found required element in HashSet." ,"Please fill the source and destination in HashSet (HashSet<source, destination>).");
		            
		        }
		    } catch (SftpException ex) {
		    	ex.printStackTrace();
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }catch (Exception ex) {
		       ex.printStackTrace();
		    }finally {
		    	 output =null;
				 System.runFinalization();
				 System.gc();
		    }
	}
	
	/**
	  * <p>
	  * It is a public method. It uploads files as result for online process.
	  *  
	  * @param PATHS It is a HashMap parameters which contains source and destination files path only.
	  * 
	  */
	public void uploadFiles(HashMap<String, String> PATHS)
	{
		this.methodValidator(PATHS);
		File f = null;
		 
		 try
			{
				if (!PATHS.isEmpty()) {
		            
					for(Map.Entry<String, String> EN : PATHS.entrySet())
					{
		                channelSftp.cd(EN.getValue());
		                f = new File(EN.getKey());
		                channelSftp.put(new FileInputStream(f), f.getName());
		            }

		        }else {
		            throw new KSBEmptyPathsExcetion("Not found required element in HashSet." ,"Please fill the source and destination in HashSet (HashSet<source, destination>).");
		            
		        }
		    } catch (SftpException ex) {
		    	ex.printStackTrace();
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }catch (Exception ex) {
		       ex.printStackTrace();
		    }finally {
		    	 f = null;
				 System.runFinalization();
				 System.gc();
		    }
	}
	
	/**
	  * <p>
	  * It is a public method. It uploads files as result for online process.
	  *  
	  * @param PATHS It is a HashMap parameters which contains source and destination files path only.
	  * @param commandPB It is a JProgressBar variable which contains JProgressBar object for capturing progress.
	  * @param commandL It is a JLabel variable which contains JLabel object for capturing progress name.
	  * 
	  */
	public void uploadFiles(HashMap<String, String> PATHS, JProgressBar commandPB, JLabel commandL)
	{
		File f = null;
		this.methodValidator(PATHS);
		int pIdex = commandPB.getValue();
		 int sIndex = (100-pIdex)/PATHS.size();
		 
		 try
			{
				if (!PATHS.isEmpty()) {
		            
					for(Map.Entry<String, String> EN : PATHS.entrySet())
					{
						commandL.setText("Uploading : "+EN.getKey());
						
		                channelSftp.cd(EN.getValue());
		                f = new File(EN.getKey());
		                channelSftp.put(new FileInputStream(f), f.getName());
		                
		                pIdex = pIdex+sIndex;
		                commandPB.setValue(pIdex);
		            }

		        }else {
		            throw new KSBEmptyPathsExcetion("Not found required element in HashSet." ,"Please fill the source and destination in HashSet (HashSet<source, destination>).");
		            
		        }
		    } catch (SftpException ex) {
		    	ex.printStackTrace();
		    } catch (IOException ex) {
		        ex.printStackTrace();
		    }catch (Exception ex) {
		       ex.printStackTrace();
		    }finally {
		    	f= null;
				 System.runFinalization();
				 System.gc();
		    }
	}
	
	/**
	  * <p>
	  * It is a public method. It check file existence for both processes.
	  *  
	  * @param PATH It is a String parameter which contains server file path only.
	  * 
	  */
	public boolean isExisTFile(String PATH)
	{
		boolean status = false;
		
		if(! PuttyUtilityValidator.textHasContent(PATH) ){
		      throw new IllegalArgumentException("Error ! invalid string passed for path.");
		    }
		 
		 try
			{
		            
			 InputStream  is = this.channelSftp.get(PATH);
			 
			 if(is != null)
			 {
				 status = true;
			 }

		    } catch (SftpException ex) {
		    	//ex.printStackTrace();
		    } catch (Exception ex) {
		       //ex.printStackTrace();
		    }finally {
				 System.runFinalization();
				 System.gc();
		    }
		 return status;
	}
	
	/**
	  * <p>
	  * It is a public method. It check directory existence for both processes.
	  *  
	  * @param PATH It is a String parameter which contains server directory path only.
	  * 
	  */
	public boolean isExisTDir(String PATH)
	{
		boolean status = false;
		
		if(! PuttyUtilityValidator.textHasContent(PATH) ){
		      throw new IllegalArgumentException("Error ! invalid string passed for path.");
		    }
		 
		 try
			{
		            
			 InputStream  is = this.channelSftp.get(PATH);
			 
			 if(is != null)
			 {
				 status = true;
			 }

		    } catch (SftpException ex) {
		    	//ex.printStackTrace();
		    } catch (Exception ex) {
		       //ex.printStackTrace();
		    }finally {
				 System.runFinalization();
				 System.gc();
		    }
		 return status;
	}
	
	
	/**
	  * <p>
	  * It is a public method. It uploads files as result for online process.
	  *  
	  * @param serverdir It is a server dir path as string parameters which contains source path real path only.
	  * @param localdir It is a local dir path as string parameters which contains destination path real path only.
	  * 
	  */
	public void downloadDir(String serverdir, String localdir) { // With subfolders and all files.
		
		this.methodValidator(localdir, serverdir);
	    // Create local folders if absent.
	    try {
	    	
	        new File(localdir).mkdirs();
	        channelSftp.lcd(localdir);
	    
	    } catch (SftpException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }

	    // Copy remote folders one by one.
	    lsFolderCopy(serverdir, localdir, null, null); // Separated because loops itself inside for subfolders.
	}
	
	/**
	  * <p>
	  * It is a public method. It uploads files as result for online process.
	  *  
	  * @param serverdir It is a server dir path as string parameters which contains source path real path only.
	  * @param localdir It is a local dir path as string parameters which contains destination path real path only.
	  * @param commandPB It is a JProgressBar variable which contains JProgressBar object for capturing progress.
	  * @param commandL It is a JLabel variable which contains JLabel object for capturing progress name.
	  * 
	  */
	public void downloadDir(String serverdir, String localdir,  JProgressBar commandPB, JLabel commandL) { // With subfolders and all files.
	    
		this.methodValidator(localdir, serverdir, commandPB, commandL);
		// Create local folders if absent.
	    try {
	    	
	        new File(localdir).mkdirs();
	        channelSftp.lcd(localdir);
	    
	    } catch (SftpException e) {
	        e.printStackTrace();
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }

	    // Copy remote folders one by one.
	    lsFolderCopy(serverdir, localdir, commandPB, commandL); // Separated because loops itself inside for subfolders.
	}

	@SuppressWarnings("unchecked")
	private void lsFolderCopy(String sourcePath, String destPath, JProgressBar commandPB, JLabel commandL){ // List source (remote, sftp) directory and create a local copy of it - method for every single directory.
		
		FileOutputStream output = null;
		int pIdex = 0;
		int sIndex = 0;
		Vector<ChannelSftp.LsEntry> list = null;
		
		try{
			
			list = channelSftp.ls(sourcePath); // List source directory structure.
			
			if(commandPB != null)
			{
				pIdex = commandPB.getValue();
				sIndex = (100-pIdex)/list.size();
			}
		
	    for (ChannelSftp.LsEntry oListItem : list) { // Iterate objects in the list to get file/folder names.
	        if (!oListItem.getAttrs().isDir()) { // If it is a file (not a directory).
	            if (!(new File(destPath + "/" + oListItem.getFilename())).exists() || (oListItem.getAttrs().getMTime() > Long.valueOf(new File(destPath + "/" + oListItem.getFilename()).lastModified() / (long) 1000).intValue())) { // Download only if changed later.
	            	if(commandL != null){ commandL.setText("Downloading : "+sourcePath + "/" + oListItem.getFilename());}
	            	
	                output = new FileOutputStream(destPath + "/" + oListItem.getFilename());
	                channelSftp.get(sourcePath + "/" + oListItem.getFilename(), output); // Grab file from source ([source filename], [destination filename]).
	                output.close();
	            }
	        }
	            else if (! ((".".equals(oListItem.getFilename())) || ("..".equals(oListItem.getFilename())))){
	            new File(destPath + "/" + oListItem.getFilename()).mkdirs(); // Empty folder copy.
	            lsFolderCopy(sourcePath + "/" + oListItem.getFilename(), destPath + "/" + oListItem.getFilename(), commandPB, commandL); // Enter found folder on server to read its contents and create locally.
	        }
	        
	        if(commandPB != null)
			{
	        pIdex = pIdex+sIndex;
	        commandPB.setValue(pIdex);
			}
	    }
		}
		catch(SftpException | IOException e)
		{
			e.printStackTrace();
		}
		finally {
			output = null;
			list = null;
			System.gc();
			System.runFinalization();
		}
	}
	
	/**
	  * <p>
	  * It is a public method. It uploads directories as result for online process.
	  *  
	  * @param localdir It is a local directory as string parameters which contains local dir path only.
	  * @param serverdir It is a server directory as string parameters which contains server dir path only.
	  * 
	  */
	public void uploadDir(String localdir, String serverdir)
	{
		this.methodValidator(localdir, serverdir);
		try
		{
			File dir = new File(localdir);
			File fs[];
			if(dir != null)
			{
				if(dir.isDirectory())
				{
					try
					{
						channelSftp.stat(serverdir);
					}
					catch(SftpException e) { 
						channelSftp.mkdir(serverdir);
					}
					
					fs = dir.listFiles();  
					for(File f : fs)
					{    
						if(!f.isDirectory())
						{
							channelSftp.cd(serverdir); 
				            channelSftp.put(new FileInputStream(f), f.getName());
						}
						else
						{
							this.uploadDir(localdir+"/"+f.getName(), serverdir+"/"+f.getName());
						}
					}
				}
				else
				{
					 throw new KSBEmptyPathsExcetion("Not found valid local in parameter." ,"Please fill valid real path of local machine.");
				}
			}
		}
		catch(SftpException | KSBEmptyPathsExcetion | FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}

	/**
	  * <p>
	  * It is a public method. It uploads directories as result for online process.
	  *  
	  * @param localdir It is a local directory as string parameters which contains local dir path only.
	  * @param serverdir It is a server directory as string parameters which contains server dir path only.
	  * @param commandPB It is a JProgressBar variable which contains JProgressBar object for capturing progress.
	  * @param commandL It is a JLabel variable which contains JLabel object for capturing progress name. 
	  */
	
	public void uploadDir(String localdir, String serverdir, JProgressBar commandPB, JLabel commandL)
	{
		this.methodValidator(localdir, serverdir, commandPB, commandL);
		try
		{
			File dir = new File(localdir);
			File fs[];
			if(dir != null)
			{
				if(dir.isDirectory())
				{
					try
					{
						channelSftp.stat(serverdir);
					}
					catch(SftpException e) { 
						channelSftp.mkdir(serverdir);
					}
					
					fs = dir.listFiles();  
					
					int pIdex = commandPB.getValue();
					int sIndex = (100-pIdex)/fs.length;
					
					for(File f : fs)
					{    
						if(!f.isDirectory())
						{
							commandL.setText("Uploading : "+f.getAbsolutePath());
							
							channelSftp.cd(serverdir); 
				            channelSftp.put(new FileInputStream(f), f.getName());
				            
				            pIdex = pIdex+sIndex;
				            commandPB.setValue(pIdex);
						}
						else
						{
							this.uploadDir(localdir+"/"+f.getName(), serverdir+"/"+f.getName());
						}
					}
				}
				else
				{
					 throw new KSBEmptyPathsExcetion("Not found valid local in parameter." ,"Please fill valid real path of local machine.");
				}
			}
		}
		catch(SftpException | KSBEmptyPathsExcetion | FileNotFoundException e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	  * <p>
	  * It is a method. Which close session of server transaction.
	  */
	   
	public void closeSession()
	{
		if (channelSftp.isConnected()) {
	        try {
	            session.disconnect();
	            channel.disconnect();
	            channelSftp.quit();
	        } catch (Exception ioe) {
	            ioe.printStackTrace();
	        }
	        finally {
	     	   session = null;
	     	   channel = null;
	     	   channelSftp = null;
					 System.runFinalization();
					 System.gc();
			      }
	    }
	}
	
	/**
	  * <p>
	  * It is a private method. It validate parameterized constructor arguments.
	  *  
	  * @param USER It is a String variable for username of server access.
	  * @param PASS It is a String variable for password of server access.
	  * @param IP It is a String variable for ip of server access.
	  * @param PORT It is a Integer variable for port number of server access.
	  * @param CHANNELTYPE It is a String (value:sftp only allowed) variable for channel type of server access.
	  */
	private void ConstructorValidator(String USER, String PASS, String IP, int PORT, String CHANNELTYPE)
	{
		if( ! PuttyUtilityValidator.textHasContent(USER) ){
		      throw new IllegalArgumentException("Error ! Invalid Username passed.");
		    }
		
		if( ! PuttyUtilityValidator.textHasContent(PASS) ){
		      throw new IllegalArgumentException("Error ! Invalid password passed.");
		    }
		
		if( ! PuttyUtilityValidator.textHasContent(CHANNELTYPE) ){
		      throw new IllegalArgumentException("Error ! Invalid channel type passed.");
		    }
		
		if( !CHANNELTYPE.equals("sftp") ){
		      throw new IllegalArgumentException("Error ! only sftp channel type is allowed in api.");
		    }
		
		if( ! PuttyUtilityValidator.textHasIp(IP) ){
		      throw new IllegalArgumentException("Error ! Invalid ip passed.");
		    }
	}
	
	/**
	  * <p>
	  * It is a private method. It validate downloadFiles() and uploadFiles() method arguments for online process.
	  *  
	  * @param PATHS It is a HashMap parameters which contains source and destination files path only.
	  */
	private void methodValidator(HashMap<String, String> PATHS)
	{
		
		if( ! PuttyUtilityValidator.checkForNull(PATHS) ){
		      throw new IllegalArgumentException("Error ! null object passed for files list.");
		    }
	}
	
	/**
	  * <p>
	  * It is a private method. It validate downloadFiles() and uploadFiles() method arguments for offline process.
	  *  
	  * @param PATHS It is a HashMap parameters which contains source and destination files path only.
	  * @param commandPB It is a JProgressBar variable which contains JProgressBar object for capturing progress.
      * @param commandL It is a JLabel variable which contains JLabel object for capturing progress name.
	  */
	private void methodValidator(HashMap<String, String> PATHS, JProgressBar commandPB, JLabel commandL)
	{
		
		if( ! PuttyUtilityValidator.checkForNull(PATHS) ){
		      throw new IllegalArgumentException("Error ! null object passed for files list.");
		    }
		
		if( ! PuttyUtilityValidator.checkForNull(commandPB) ){
		      throw new IllegalArgumentException("Error ! null progress obj passed.");
		    }
		
		if( ! PuttyUtilityValidator.checkForNull(commandL) ){
		      throw new IllegalArgumentException("Error ! null level obj passed.");
		    }
	}
	
	/**
	  * <p>
	  * It is a private method. It validate downloadFiles() and uploadFiles() method arguments for offline process.
	  *  
	  * @param serverdir It is a server dir path as string parameters which contains source path real path only.
	  * @param localdir It is a local dir path as string parameters which contains destination path real path only.
	  * @param commandPB It is a JProgressBar variable which contains JProgressBar object for capturing progress.
      * @param commandL It is a JLabel variable which contains JLabel object for capturing progress name.
	  */
	private void methodValidator(String localdir, String serverdir, JProgressBar commandPB, JLabel commandL)
	{
		
		if( ! PuttyUtilityValidator.textHasContent(localdir) ){
		      throw new IllegalArgumentException("Error ! invalid string passed for localdir.");
		    }
		
		if( ! PuttyUtilityValidator.textHasContent(serverdir) ){
		      throw new IllegalArgumentException("Error ! invalid string passed for serverdir.");
		    }
		
		if( ! PuttyUtilityValidator.checkForNull(commandPB) ){
		      throw new IllegalArgumentException("Error ! null progress obj passed.");
		    }
		
		if( ! PuttyUtilityValidator.checkForNull(commandL) ){
		      throw new IllegalArgumentException("Error ! null level obj passed.");
		    }
	}
	
	/**
	  * <p>
	  * It is a private method. It validate downloadFiles() and uploadFiles() method arguments for offline process.
	  *  
	  * @param serverdir It is a server dir path as string parameters which contains source path real path only.
	  * @param localdir It is a local dir path as string parameters which contains destination path real path only.
	  */
	private void methodValidator(String localdir, String serverdir)
	{
		
		if( ! PuttyUtilityValidator.textHasContent(localdir) ){
		      throw new IllegalArgumentException("Error ! invalid string passed for localdir.");
		    }
		
		if( ! PuttyUtilityValidator.textHasContent(serverdir) ){
		      throw new IllegalArgumentException("Error ! invalid string passed for serverdir.");
		    }
	}
}
