//Entry point for the program deals with starting the program up
//Program can be compiled using "make" and run using "make run"
//Input files are "OldMasterBankAccounts.dat" and "Transactions.trans"
//Output files are "CurrentBankAccounts.dat" and "MasterBankAccounts.dat"

import java.util.ArrayList;

public class main {

public static void main(String[] args){

	//Start by reading in current data from transactions and old master
	CurrentData data = new CurrentData();
	//get the accounts from the master accounts file
	data.getCurrentAccounts();
	//get the transactions from the transaction log
	data.getTransactions();


	//Process transactions that have been read in by currentData
	TransactionHandler transHandler = new TransactionHandler(data.accounts, data.transactions);
	//Create a new list to store the updated accounts
	ArrayList<Account> newAccounts;
	//process transactions and return the new accounts information
	newAccounts = transHandler.HandleTransactions();


	//create a BankAccountWriter to generate a new master and current
	//pass in the new accounts information that we created from the transaction handler
	BankAccountWriter bankWriter = new BankAccountWriter(newAccounts);
	//Write new master bank accounts file
	bankWriter.writeAccounts(true);
	//Write new current bank accounts file
	bankWriter.writeAccounts(false);


	System.out.println("Execution Complete");
}
}
