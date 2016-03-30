// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

void FrontEnd::handleDelete() {
  // Make sure the user is logged in.
  if (!isLoggedIn()) {
    std::cout << MESSAGE_ERROR_REQUIRES_LOGIN << std::endl << std::endl;
    return;
  }

  // Make sure the user is an admin.
  if (!currentUserIsAdmin) {
    std::cout << MESSAGE_ERROR_REQUIRES_ADMIN << std::endl << std::endl;
    return;
  }

  // Get the name of the account holder.
  std::string accountHolder;
  if (promptForAccountHolder(&accountHolder)) {
    return;
  }

  // Get the account number.
  Account* account;
  if (promptForAccount(&account, MESSAGE_PROMPT_ACCOUNT_NUMBER,
                       &accountHolder, false)) {
    return;
  }

  unsigned int accountID = account->id;

  // Delete the account.
  accounts.erase(accountID);

  // Write transaction to transaction log.
  writeToTransactionFile(
    TRANSACTION_CODE_DELETE, accountHolder, accountID, 0, "");

  // Print success message.
  std::cout << MESSAGE_SUCCESS_DELETE << std::endl << std::endl;
}
