//Class for taking the current accounts withing the system and creating
//the new current accounts and master accounts files
//Created from main and interfaces with the Accounts class
package backend;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Writer;

public class BankAccountWriter {

//List of accounts to write
private Map<Integer, Account> accounts;
//BanckAccountWriter Constructor
public BankAccountWriter(Map<Integer, Account> newAccounts){
	//assign the accounts to be used from the parameter
	accounts = newAccounts;
}

//create the new account files
//argument of if you are creating the master or current
public void writeAccounts(boolean master){
	//create file for master or current based on our argument
	String filename = (master==true) ? "MasterBankAccounts.dat" : "CurrentBankAccounts.dat";

	try{
		//open up file using decided name
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
		Map<Integer, Account> map = new TreeMap<Integer, Account>(accounts);
		Set set = map.entrySet();
		Iterator iterator = set.iterator();
		while(iterator.hasNext()){
			Map.Entry me = (Map.Entry)iterator.next();
			writer.write(((Account) me.getValue()).myToString(master)+"\n");
		}
		writer.close();
		System.out.println(((master==true) ? "Master" : "Current") + " Accounts File Written Successfully");
	}
	//Handle errors gracefully
	catch(IOException e) {
		System.out.println("IOException!!!");
	}

}
}
