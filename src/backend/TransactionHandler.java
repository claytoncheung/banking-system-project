//Class for applying the transactions to the accounts
//Interaces with Accounts and Transactions
//Requires data to be created from currentData and then passed
package backend;

import java.util.ArrayList;
import java.util.Map;

public class TransactionHandler {

//Working set of accounts
private Map<Integer, Account> accounts;
//The transactions to process
private ArrayList<Transaction> transactions;
//The account we are currently applying transactions to
private Account curAcc;
private boolean adminSession, transferred = false;
private int transfer = 1, currTrans = 0;

//constructor which takes in the transactions to process
public TransactionHandler(Map<Integer, Account> accs,ArrayList<Transaction> trans){
	//set our current working accounts to the ones passed in
	accounts = accs;
	//set current transactions to those passed in
	transactions = trans;
}

//Increment the transaction count on the current account
public void IncTrans(){
  //If admin it doesnt not count as a transaction
	if(!adminSession) {
		curAcc.totalTransactions++;
	}
}

//Charge accounts the transaction fee
public void ChargeAccount(){
  //check if session is admin if so no fees
	if(!adminSession) {
		if(curAcc.isStudent) {
			curAcc.currentBalance -= 0.05;
		}else{
			curAcc.currentBalance -= 0.10;
		}
	}
}

public double checkFee(){
	if(adminSession){
		return 0;
	}
	else if(curAcc.isStudent){
		return 0.05;
	}
	else  {
		return 0.10;
	}
}

//Handle all transactions and update the accounts list
//Once complete return the new modified accounts list
public Map<Integer, Account> HandleTransactions(){
	//iterate over each transaction
	for(Transaction trans : transactions) {
		//Check if the transaction exists
		if(trans!=null) {
			//Check what type of transaction we are dealing with
			currTrans++;
			switch(trans.transType) {
//End of session
			case 0:
				//System.out.println(curAcc.myToString(true) + " Session Ended");
				//End our session with current account;
				curAcc = null;
				break;
//Withdrawal
			case 1:
				try{
					if (accounts.get(trans.accountNum).currentBalance >= trans.moneyInvolved + checkFee() && accounts.get(trans.accountNum).isActive){
						accounts.get(trans.accountNum).currentBalance -= trans.moneyInvolved;
						IncTrans();
						ChargeAccount();
						System.out.println("Withdrawl Successful");
						break;
					}
					else {
						System.out.println("Withdrawl Unsuccessful");
						break;
					}
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist");
					break;
				}
//Transfer -- 
			case 2:
				try{
					if ( (transfer % 2) != 0){
						if (accounts.get(trans.accountNum).currentBalance >= trans.moneyInvolved + checkFee() && accounts.get(trans.accountNum).isActive){
							accounts.get(trans.accountNum).currentBalance -= trans.moneyInvolved;
							IncTrans();
							ChargeAccount();
							transferred = true;
							System.out.println("Money Sent");
							transfer++;
							break;
						}
						else {
							transfer++;
							System.out.println("Money Not Sent");
							break;
						}
					}
					else {
						if (transferred && accounts.get(trans.accountNum).isActive){
							accounts.get(trans.accountNum).currentBalance += trans.moneyInvolved;
							transferred = false;
							System.out.println("Money Received");
							transfer++;
							break;
						}
						else {
							accounts.get(transactions.get(currTrans-1).accountNum).currentBalance += trans.moneyInvolved + checkFee();
							System.out.println("Money Not Received");
							transfer++;
							break;
						}
				}
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist");
					break;
				}
//Paybill
			case 3:
				try{
					if (accounts.get(trans.accountNum).currentBalance >= trans.moneyInvolved + checkFee() && accounts.get(trans.accountNum).isActive){
						accounts.get(trans.accountNum).currentBalance -= trans.moneyInvolved;
						IncTrans();
						ChargeAccount();
						System.out.println("Bill Paid");
						break;
					}
					else {
						System.out.println("Bill Not Paid");
						break;
					}
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist");
					break;
				}
//Deposit
			case 4:
				try{
					if (accounts.get(trans.accountNum).isActive){
				        curAcc.currentBalance+=trans.moneyInvolved;
				        IncTrans();
				        ChargeAccount();
						System.out.println("Deposit Successful");
						break;
					}
					else {
						System.out.println("Deposit Unsuccessful");
						break;
					}
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist");
					break;
				}
//Create - Admin -- 
			case 5:
				int newNum = accounts.size() + 1;
				Account newAccount = new Account(newNum, trans.accountName, true, trans.moneyInvolved, 0, false);
				accounts.put(newNum, newAccount);
				System.out.println("Account Created");
				break;
//Delete - Admin
			case 6:
				try{
					accounts.remove(trans.accountNum);
					System.out.println("Account Deleted");
					break;
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist");
					break;
				}
//Disable - Admin
			case 7:
				try{
					accounts.get(trans.accountNum).isActive = false;
					System.out.println("Account Disabled");
					break;
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist");
					break;
				}
//ChangePlan - Admin
			case 8:
				try{
					accounts.get(trans.accountNum).isStudent = !accounts.get(trans.accountNum).isStudent;
					System.out.println("Plan Changed");
					break;
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist");
					break;
				}
//Enable Account - Admin -- 
			case 9:
				try{
					accounts.get(trans.accountNum).isActive = true;
					System.out.println("Account Enabled");
					break;
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist");
					break;
				}	
//Login
			case 10:
				//set current working account based on login
				curAcc = accounts.get(trans.accountNum);
				//check if valid account
				if(curAcc!=null) {
					//check if the current session is to be an admin session
					if(trans.miscInfo.equals("A")) {
						adminSession = true;
					}else{
						adminSession = false;
					}
					//display which account is logged in
					System.out.println(curAcc.myToString(true) + " Session Started "+((adminSession==true) ? "Admin" : "Standard"));
				}
				else if(trans.accountName.equals("Admin")){
					adminSession = true;
					System.out.println("Admin Session");
				}
				else{
					System.out.println("Invalid Account");
				}
				break;
			default:
				System.out.println("Unknown Transaction Type???");
				break;
			}
		}
	}
	return accounts;
}

}
