package sadagi.utilities.exceptions;

@SuppressWarnings("serial")
public class KSBEmptyPathsExcetion extends KSBException{

 	   private String strErrMessage;
 	    @Override
 	    public String getMessage() {
 	    	return super.getMessage();
 	    }
 	    
 	   public KSBEmptyPathsExcetion(){
	    	super("java.lang.Exception.KSBException.KSBEmptyPathsExcetion: Not found required element in HashSet.");
	    	strErrMessage = "java.lang.Exception.KSBException.KSBEmptyPathsExcetion: Not found required element in HashSet.";
	    }
 	   
 	   public KSBEmptyPathsExcetion(String error){
 	    	super("java.lang.Exception.KSBException.KSBEmptyPathsExcetion: "+error);
 	    	strErrMessage = "java.lang.Exception.KSBException.KSBEmptyPathsExcetion: "+error;
 	    }
 	   
 	   public KSBEmptyPathsExcetion(String error, String solution){
	    	super("java.lang.Exception.KSBException.KSBEmptyPathsExcetion: "+error+"\n[SOLUTION] : "+solution);
	    	strErrMessage = "java.lang.Exception.KSBException.KSBEmptyPathsExcetion: "+error+"\n[SOLUTION] : "+solution;
	    }
 	   
 	   public String toString(){
 		   return strErrMessage;
 	   }
}
