package sadagi.utilities.exceptions;

@SuppressWarnings("serial")
public class KSBException extends Exception{

 	   private String strErrMessage;
 	    @Override
 	    public String getMessage() {
 	    	return super.getMessage();
 	    }
 	   KSBException(String error){
 	    	super(error);
 	    	strErrMessage = error;
 	    }
 	   public String toString(){
 		   return strErrMessage;
 	   }
}
