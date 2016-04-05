//class to represent an account within the system
//Interacts with all classes as the main data type

public class Account {

//All variables needed to represent an account
int accountNum, totalTransactions;
String accountName;
boolean isActive, isStudent;
double currentBalance;

//constructor for creating a new account
public Account(int num, String name, boolean active, double balance, int transactions, boolean student){
	this.accountNum = num;
	this.accountName = name;
	this.isActive = active;
	this.currentBalance = balance;
	this.totalTransactions = transactions;
	this.isStudent = student;
}

//converts the account to the format required by the account file
//argument is if it should be formated for master accounts or current accounts
public String toString(boolean master){
	String theString;
	String formattedName;
	//create a string appended with underscores for proper formatting
	formattedName = accountName + new String(new char[20 - accountName.length()]).replace("\0", " ");
	//merge all the data from the account into a proper formatted string
	theString = String.format("%1$05d",accountNum) + " " + formattedName + " " +
	            ((isActive==true) ? "A" : "D") + " " + String.format("%1$08.2f",currentBalance) +
	            ((master==true) ? " " + String.format("%1$04d",totalTransactions) : "") + " " + ((isStudent==true) ? "S" : "N");

	return theString;
}
}
