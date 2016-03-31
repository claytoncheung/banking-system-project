//Class for reading in the current data from both accounts and transaction files
//Interacts with Transactions and Accounts
//Created from inside the main

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class CurrentData {

//Arraylist used to store the accounts within the system
public ArrayList<Account> accounts = new ArrayList<Account>();
//Arraylist for storing all the transactions being processed today
public ArrayList<Transaction> transactions = new ArrayList<Transaction>();

//Reads in the accounts from the current master accounts file
public void getCurrentAccounts(){
	boolean active, student;
	String line = null;
	String[] strArray = null;
	accounts.add(0, new Account(00000,"ADMIN",true,0,0,false));
	//begin the readin from accounts file
	try {
		FileReader fileReader = new FileReader("OldMasterBankAccounts.dat");
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		//tokenize each line of file into the data we need for the Account class
		while((line = bufferedReader.readLine()) != null) {
			strArray = line.split("_");

			for ( int i=0; i<6; i++) {
				int j = i;
				while(j < strArray.length && strArray[j].isEmpty()) {
					j++;
				}
				if(j>i && j<strArray.length) {
					strArray[i] = strArray[j];
					strArray[j] = "";
				}
			}

			if ( strArray[2].equals("A")) {
				active = true;
			}
			else {
				active = false;
			}

			if ( strArray[5].equals("S")) {
				student = true;
			}
			else {
				student = false;
			}

			//Create a new Account from the data
			//0 - AccountNum, 1 - AccountName, 3 - Balance, 4 - Total Transaction
			Account account = new Account(Integer.parseInt(strArray[0]), strArray[1], active,
			                              Double.parseDouble(strArray[3]), Integer.parseInt(strArray[4]), student);

			//Add new account to the accounts list
			accounts.add(Integer.parseInt(strArray[0]), account);
		}
		bufferedReader.close();
	System.out.println("Accounts Successfully Read In");
	}
	//Gracefully handle any errors
	catch(FileNotFoundException e) {
		System.out.println( "Unable to open file OldMasterBankAccounts.dat. Please make sure file is in the Project folder.");
	}
	catch(IOException e) {
		System.out.println("Error reading file OldMasterBankAccounts.dat");
	}
}

//Read in the transaction files and add them to the transactions log
public void getTransactions(){
	String line = null;
	String[] strArray = null;
	transactions.add(0, null);
	//read transaction file and tokenize what we need from it
	try {
		FileReader fileReader = new FileReader("Transactions.trans");
		BufferedReader bufferedReader = new BufferedReader(fileReader);

		while((line = bufferedReader.readLine()) != null) {
			strArray = line.split("_");

			for ( int i=0; i<5; i++) {
				int j = i;
				while(j < strArray.length && strArray[j].isEmpty()) {
					j++;
				}
				if(j>i && j<strArray.length) {
					strArray[i] = strArray[j];
					strArray[j] = "";
				}
			}

			//Create a new transaction from the information read in
			//0 - TransNum, 1 - Name, 2 - AccountNum, 3 - Money, 4 - MiscInfo
			Transaction trans = new Transaction(Integer.parseInt(strArray[0]), strArray[1],
			                                    Integer.parseInt(strArray[2]), Double.parseDouble(strArray[3]), strArray[4]);

			//Add transaction to our transaction list
			transactions.add(trans);
		}
		bufferedReader.close();
		System.out.println("Transactions Succesfully Read In");
	}
	//Gracefully handle any errors
	catch(FileNotFoundException ex1) {
		System.out.println( "Unable to open file Transactions.trans. Please make sure file is in the Project folder.");
	}
	catch(IOException ex) {
		System.out.println("Error reading file Transactions.trans");
	}
}

//Helper function for displaying all of the currently read in accounts
public void printAccounts(){
	int i = 1;
	while(i < accounts.size()) {
		System.out.println(accounts.get(i).toString(true));
		i++;
	}
}

//helper function for displaying all the currently read in transactions
public void printTransactions(){
	int i = 1;
	while(i < transactions.size()) {
		System.out.println(transactions.get(i));
		i++;
	}
}
}
