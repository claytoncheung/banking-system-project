// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#ifndef SRC_FRONTEND_FRONTEND_H_
#define SRC_FRONTEND_FRONTEND_H_

#include <unordered_map>
#include <memory>
#include <fstream>
#include <string>
#include "./Account.h"
#include "./constants.h"

class FrontEnd {
 private:
  // Maps from account ID to account.
  std::unordered_map<unsigned int, std::unique_ptr<Account> > accounts;

  // The name of the user that is currently logged in,
  //   or "" if an admin or no one is logged in.
  std::string currentUserName;

  // true if the current user is an admin,
  //   false if they are a normal user or no one is logged in.
  bool currentUserIsAdmin;

  // An output stream that can be used to write to the transaction log.
  std::ofstream transactionFileStream;

  // true if the constructor failed to open any of necessary files,
  //   false otherwise.
  bool failedToOpenFiles;

 public:
  // Constructs a new FrontEnd instance, loading the accounts from
  //   accountsFilePath and opening transactionFilePath for writing.
  FrontEnd(const std::string& accountsFilePath,
           const std::string& transactionFilePath);

  // Starts executing the front end command line interface, returning true if
  //   the end of input was reached without any errors, or false if an
  //   unrecoverable error was encountered (e.g. a file could not be opened).
  bool run();

 private:
  // Loads accounts from accountsFileStream into memory.
  void loadAccounts(std::ifstream* accountFileStream);

  // Writes a single line to the transaction log, using the arguments as the
  //   fields of the line.
  void writeToTransactionFile(
    unsigned int code,
    const std::string& accountHolderName,
    unsigned int accountNumber,
    unsigned int transactionAmount,
    const std::string& miscellaneousInfo);

  // Checks whether accountHolderName is someone who has at least one account.
  bool checkAccountHolderName(const std::string& accountHolderName) const;

  // Checks whether the account has a student plan or not.
  // Returns the fee required for the transaction.
  inline unsigned int getFee(const Account* account) const {
    if (currentUserIsAdmin) return 0;

    return account->isStudentPlan
      ? TRANSACTION_FEE_STUDENT
      : TRANSACTION_FEE_NORMAL;
  }

  // Checks whether an user is currently logged in.
  // Returns true if an user name is set or an admin is logged in.
  inline bool isLoggedIn() const {
    return currentUserName != "" || currentUserIsAdmin;
  }

  // Handles the “login” command, guiding the user through the login process.
  void handleLogin();

  // Handles the “logout” command, logging the current user out.
  void handleLogout();

  // Handles the “deposit” command, guiding the user through a
  //   deposit transaction.
  void handleDeposit();

  // Handles the “withdrawal” command, guiding the user through a
  //   withdrawal transaction.
  void handleWithdrawal();

  // Handles the “transfer” command, guiding the user through a
  //   transfer transaction.
  void handleTransfer();

  // Handles the “paybill” command, guiding the user through a
  //   paybill transaction.
  void handlePayBill();

  // Handles the “create” command, guiding the user through a
  //   create transaction.
  void handleCreate();

  // Handles the “delete” command, guiding the user through a
  //   delete transaction.
  void handleDelete();

  // Handles the “enable” command, guiding the user through an
  //   enable transaction.
  void handleEnable();

  // Handles the “disable” command, guiding the user through a
  //   disable transaction.
  void handleDisable();

  // Handles the “changeplan” command, guiding the user through a
  //   changeplan transaction.
  void handleChangePlan();

  // Prompts the user for an amount, continuing to prompt them until the
  //   amount entered is valid or they cancel the prompt.
  // The prompt displayed is the prompt parameter.
  // If isWithdrawal is true, the amount must be a multiple of $5.
  // Returns true if cancelled, false otherwise. The amount parameter is set
  //   to the amount if not cancelled.
  bool promptForAmount(unsigned int* amount,
                       const std::string& prompt = MESSAGE_PROMPT_AMOUNT,
                       bool isWithdrawal = false);

  // Prompts the user for an account holder’s name, continuing to prompt them
  //   until the name entered is valid or they cancel the prompt.
  // Returns true if cancelled, false otherwise.
  // accountHolder is set to the account holder’s name if not cancelled.
  bool promptForAccountHolder(std::string* name, bool mustAlreadyExist = true);

  // Prompts the user for an account number, continuing to prompt them until
  //   the number entered is valid and the corresponding account belongs to
  //   the specified account holder (if accountHolder is not NULL) and is
  //   enabled (if mustBeEnabled is true), or the user cancels the prompt.
  // The prompt shown to the user is the prompt parameter.
  // Returns true if cancelled, false otherwise.
  // account parameter is set to a pointer to the Account if not cancelled.
  bool promptForAccount(Account** account, const std::string& prompt,
                        const std::string* accountHolder, bool mustBeEnabled);

  // Prompts the user for a company’s initials, continuing to prompt them
  //   until the initials are valid or they cancel the prompt.
  // Returns true if cancelled, false otherwise.
  // The company parameter is set to the ID of the company if not cancelled.
  bool promptForCompany(Company* company);
};

#endif  // SRC_FRONTEND_FRONTEND_H_
