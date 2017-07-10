package sadagi.utilities.examples;

import com.jcraft.jsch.JSchException;

import sadagi.utilities.servers.KSBUtility;

public class Commons {

	public static void main(String[] args) throws JSchException {
	
		
		KSBUtility utility = new KSBUtility("root", "root123", "10.184.21.239", 22, "sftp");
		
		//boolean b = utility.isExisTFile("/home/test/Yes.txt");
		boolean b = utility.isExisTFile("/home/test/finall");
		
		System.out.println(b);
		
		utility.closeSession();

	}

}
