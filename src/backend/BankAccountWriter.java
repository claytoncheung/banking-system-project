//Class for taking the current accounts withing the system and creating
//the new current accounts and master accounts files
//Created from main and interfaces with the Accounts class
package backend;

//import java.util.ArrayList;
import java.util.Map;
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
public void writeAccounts(boolean master, String masterName, String currentName){
	//create file for master or current based on our argument
	String filename = (master==true) ? masterName : currentName;

	try{
		//open up file using decided name
		Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
		Object[] array =  accounts.values().toArray();
		//iterate through all accounts and write them to file
		for(Object acc : array) {
			if(acc!=null) {
				writer.write(((Account) acc).myToString(master)+"\n");
			}
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
