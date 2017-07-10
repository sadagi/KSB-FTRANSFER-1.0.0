package sadagi.utilities.exceptions;

@SuppressWarnings("serial")
public class KSBNullElementExcetion extends KSBException{

 	   private String strErrMessage;
 	    @Override
 	    public String getMessage() {
 	    	return super.getMessage();
 	    }
 	    
 	   public KSBNullElementExcetion(){
	    	super("java.lang.Exception.KSBException.KSBNullElementExcetion: Not found required element within period.");
	    	strErrMessage = "java.lang.Exception.KSBException.KSBNullElementExcetion: Not found required element within period.";
	    }
 	   
 	   public KSBNullElementExcetion(String error){
 	    	super("java.lang.Exception.KSBException.KSBNullElementExcetion: "+error);
 	    	strErrMessage = "java.lang.Exception.KSBException.KSBNullElementExcetion: "+error;
 	    }
 	   
 	   public KSBNullElementExcetion(String error, String solution){
	    	super("java.lang.Exception.KSBException.KSBNullElementExcetion: "+error+"\n[SOLUTION] : "+solution);
	    	strErrMessage = "java.lang.Exception.KSBException.KSBNullElementExcetion: "+error+"\n[SOLUTION] : "+solution;
	    }
 	   
 	   public String toString(){
 		   return strErrMessage;
 	   }
}
