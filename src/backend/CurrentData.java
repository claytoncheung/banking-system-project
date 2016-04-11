//Class for reading in the current data from both accounts and transaction files
//Interacts with Transactions and Accounts
//Created from inside the main
package backend;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.Map.Entry;

public class CurrentData {

//Map used to store the accounts within the system
public Map<Integer, Account> accounts = new HashMap<Integer, Account>();
//Arraylist for storing all the transactions being processed today
public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

//Reads in the accounts from the current master accounts file
public void getCurrentAccounts(String fileName){
	boolean active, student;
	String line = null;
	
	
//	Scanner in = new Scanner(System.in);
//	System.out.println("Enter Accounts File Name: ");
//	String fileName = in.nextLine();
	
	try {
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		//tokenize each line of file into the data we need for the Account class
		while((line = bufferedReader.readLine()) != null) {
			String[] strArray = new String[15];
			int i = 0, j = 1;
			StringTokenizer st = new StringTokenizer(line);
		    while (st.hasMoreTokens()) {
		    	strArray[i] = st.nextToken();
		    	i++;
		    }
		    while(Character.isLetter(strArray[j].charAt(0))){
		    	j++;
		    }
		    j--;
		    for(int k = 2; k < j; k++){
		    	strArray[1] = strArray[1] + " " + strArray[k];
		    }
	
			if ( strArray[j].equals("A")) {
				active = true;
			}
			else {
				active = false;
			}

			if ( strArray[j+3].equals("S")) {
				student = true;
			}
			else {
				student = false;
			}

			//Create a new Account from the data
			//0 - AccountNum, 1 - AccountName, 4 - Balance, 5 - Total Transaction
			Account account = new Account(Integer.parseInt(strArray[0]), strArray[1], active,
			                              Double.parseDouble(strArray[j+1]), Integer.parseInt(strArray[j+2]), student);
			
			//Add new account to the accounts map
			accounts.put(Integer.parseInt(strArray[0]), account);
		}
		bufferedReader.close();
		System.out.println("Accounts Successfully Read In");
	}
	//Gracefully handle any errors
	catch(FileNotFoundException e) {
		System.out.println( "Unable to open Accounts file. Please make sure file is in the Project folder.");
	}
	catch(IOException e) {
		System.out.println("Error reading Accounts file.");
	}
}

//Read in the transaction files and add them to the transactions log
public void getTransactions(String fileName){
	String line = null;
	transactions.add(0, null);
	
//	Scanner in = new Scanner(System.in);
//	System.out.println("Enter Transactions File Name: ");
//	String fileName = in.nextLine();
	
	try {
		FileReader fileReader = new FileReader(fileName);
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		//tokenize each line of file into the data we need for the Account class
		while((line = bufferedReader.readLine()) != null) {
			String[] strArray = new String[14];
			int i = 0, j = 1;
			StringTokenizer st = new StringTokenizer(line);
		    while (st.hasMoreTokens()) {
		    	strArray[i] = st.nextToken();
		    	i++;
		    }
		    while(Character.isLetter(strArray[j].charAt(0))){
		    	j++;
		    }
	
		    for(int k = 2; k < j; k++){
		    	strArray[1] = strArray[1] + " " + strArray[k];
		    }
		    
		    
		    if (strArray[j+2] == null){
		    	strArray[j+2] = "__";
		    }

			//Create a new transaction from the information read in
			//0 - TransNum, 1 - Name, 3 - AccountNum, 4 - Money, 5 - MiscInfo
			Transaction trans = new Transaction(Integer.parseInt(strArray[0]), strArray[1],
			                                    Integer.parseInt(strArray[j]), Double.parseDouble(strArray[j+1]), strArray[j+2]);

			//Add transaction to our transaction list
			transactions.add(trans);
		}
		bufferedReader.close();
		System.out.println("Transactions Succesfully Read In");
	}
	//Gracefully handle any errors
	catch(FileNotFoundException ex1) {
		System.out.println( "Unable to open transaction file. Please make sure file is in the Project folder.");
	}
	catch(IOException ex) {
		System.out.println("Error reading file transaction file.");
	}
}
}
