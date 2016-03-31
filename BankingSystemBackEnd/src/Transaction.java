//Class to represent a transaction within the system
//Used with currentData and TransactionHandler
public class Transaction {

//all variables needed to represent a transaction
int transType, accountNum;
String accountName, miscInfo;
double moneyInvolved;

//Constructor for creating a new transaction
public Transaction(int trans, String name, int num, double money, String misc){
	this.transType = trans;
	this.accountName = name;
	this.accountNum = num;
	this.moneyInvolved = money;
	this.miscInfo = misc;
}

//helper function for printing transactions in proper format
public String toString(){
	String theString;
	String formattedName;
	//create a string appended with underscores for proper formatting
	formattedName = accountName + new String(new char[20 - accountName.length()]).replace("\0", "_");
	//merge all the data from the transaction into a proper formatted string
	theString = String.format("%1$02d",transType) + "_" + String.format("%1$05d",accountNum)+ "_" +
	            formattedName + "_" + String.format("%1$08.2f",moneyInvolved) + "_" + miscInfo;

	return theString;
}
}
