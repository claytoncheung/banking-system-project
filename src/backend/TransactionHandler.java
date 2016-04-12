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
public void IncTrans(int num){
  //If admin it doesnt not count as a transaction
	if(!adminSession) {
		accounts.get(num).totalTransactions++;
	}
}

//Charge accounts the transaction fee
public void ChargeAccount(int num){
  //check if session is admin if so no fees
	if(!adminSession) {
		if(accounts.get(num).isStudent) {
			accounts.get(num).currentBalance -= 0.05;
		}else{
			accounts.get(num).currentBalance -= 0.10;
		}
	}
}

public double checkFee(int num){
	if(adminSession){
		return 0;
	}
	else if(accounts.get(num).isStudent){
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
				adminSession = false;
				break;
//Withdrawal
			case 1:
				try{
					if (accounts.get(trans.accountNum).currentBalance >= trans.moneyInvolved + checkFee(trans.accountNum) && accounts.get(trans.accountNum).isActive){
						accounts.get(trans.accountNum).currentBalance -= trans.moneyInvolved;
						IncTrans(trans.accountNum);
						ChargeAccount(trans.accountNum);
						System.out.println("Withdrawl Successful");
						break;
					}
					else {
						System.out.println("Withdrawl Unsuccessful");
						break;
					}
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist (Wd1)");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist (Wd2)");
					break;
				}
//Transfer -- 
			case 2:
				try{
					if ( (transfer % 2) != 0){
						if (accounts.get(trans.accountNum).currentBalance >= trans.moneyInvolved + checkFee(trans.accountNum) && accounts.get(trans.accountNum).isActive){
							accounts.get(trans.accountNum).currentBalance -= trans.moneyInvolved;
							IncTrans(trans.accountNum);
							ChargeAccount(trans.accountNum);
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
							accounts.get(transactions.get(currTrans-1).accountNum).currentBalance += trans.moneyInvolved + checkFee(trans.accountNum);
							System.out.println("Money Not Received");
							transfer++;
							break;
						}
				}
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist (Tr1)");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist (Tr2)");
					break;
				}
//Paybill
			case 3:
				try{
					if (accounts.get(trans.accountNum).currentBalance >= trans.moneyInvolved + checkFee(trans.accountNum) && accounts.get(trans.accountNum).isActive){
						accounts.get(trans.accountNum).currentBalance -= trans.moneyInvolved;
						IncTrans(trans.accountNum);
						ChargeAccount(trans.accountNum);
						System.out.println("Bill Paid");
						break;
					}
					else {
						System.out.println("Bill Not Paid");
						break;
					}
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist (Pb1)");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist (Pb2)");
					break;
				}
//Deposit
			case 4:
				try{
					if (accounts.get(trans.accountNum).isActive){
				        accounts.get(trans.accountNum).currentBalance+=trans.moneyInvolved;
				        IncTrans(trans.accountNum);
				        ChargeAccount(trans.accountNum);
						System.out.println("Deposit Successful");
						break;
					}
					else {
						System.out.println("Deposit Unsuccessful");
						break;
					}
				}
				catch(NullPointerException e){
					System.out.println("Account doesn't exist (Dp1)");
					break;
				}
				catch(IndexOutOfBoundsException e){
					System.out.println("Account doesn't exist (Dp2)");
					break;
				}
//Create - Admin -- 
			case 5:
				if(adminSession){
					int newNum = accounts.size() + 1;
					Account newAccount = new Account(newNum, trans.accountName, true, trans.moneyInvolved, 0, false);
					accounts.put(newNum, newAccount);
					System.out.println("Account Created");
				}
				else {
					System.out.println("Account Does Not Have Access To This Transaction");
				}
				break;
//Delete - Admin
			case 6:
				if(adminSession){
					try{
						accounts.remove(trans.accountNum);
						System.out.println("Account Deleted");
						break;
					}
					catch(NullPointerException e){
						System.out.println("Account doesn't exist (Dl1)");
						break;
					}
					catch(IndexOutOfBoundsException e){
						System.out.println("Account doesn't exist (Dl2)");
						break;
					}
				}
				else {
					System.out.println("Only Admin Can Access Delete");
					break;
				}
//Disable - Admin
			case 7:
				if(adminSession){
					try{
						accounts.get(trans.accountNum).isActive = false;
						System.out.println("Account Disabled");
						break;
					}
					catch(NullPointerException e){
						System.out.println("Account doesn't exist (Ds1)");
						break;
					}
					catch(IndexOutOfBoundsException e){
						System.out.println("Account doesn't exist (Ds2)");
						break;
					}
				}
				else {
					System.out.println("Only Admin Can Access Disable");
					break;
				}
//ChangePlan - Admin
			case 8:
				if(adminSession){
					try{
						accounts.get(trans.accountNum).isStudent = !accounts.get(trans.accountNum).isStudent;
						System.out.println("Plan Changed");
						break;
					}
					catch(NullPointerException e){
						System.out.println("Account doesn't exist (Cp1)");
						break;
					}
					catch(IndexOutOfBoundsException e){
						System.out.println("Account doesn't exist (Cp2)");
						break;
					}
				}
				else {
					System.out.println("Only Admin Can Access ChangePlan");
					break;
				}
//Enable Account - Admin -- 
			case 9:
				if(adminSession){
					try{
						accounts.get(trans.accountNum).isActive = true;
						System.out.println("Account Enabled");
						break;
					}
					catch(NullPointerException e){
						System.out.println("Account doesn't exist (En1)");
						break;
					}
					catch(IndexOutOfBoundsException e){
						System.out.println("Account doesn't exist (En2)");
						break;
					}	
				}
				else{
					System.out.println("Only Admin Can Access Enable");
					break;
				}
//Login
			case 10:
				//set current working account based on login
				//curAcc = accounts.get(trans.accountNum);
				//check if valid account
				if (trans.miscInfo.equals("S")){
					int check = transactions.get(currTrans+1).accountNum;
					if ( accounts.containsKey(check)){
						adminSession = false;
					}
					else {
						System.out.println("Account " + trans.accountNum +  " You Attempted To Log In As Does Not Exist!");
					}
				}
				else if(trans.miscInfo.equals("A")){
					System.out.println("Logged In As Admin");
					adminSession = true;
				}
				else {
					System.out.println("Unknown Account Type");
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
