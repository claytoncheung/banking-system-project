// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

void FrontEnd::handleDisable() {
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

  // Get the account.
  Account* account;

  for (;;) {
    // Prompt for the account.
    if (promptForAccount(&account, MESSAGE_PROMPT_ACCOUNT_NUMBER,
                         &accountHolder, false)) {
      return;
    }

    // Make sure the account isn't already disabled.
    if (account->isDisabled) {
      std::cout << MESSAGE_ERROR_ALREADY_DISABLED << std::endl << std::endl;
      continue;
    }

    break;
  }

  // Disable the account in memory.
  account->isDisabled = true;

  // Write transaction to transaction log.
  writeToTransactionFile(
    TRANSACTION_CODE_DISABLE, accountHolder, account->id, 0, "");

  // Print success message.
  std::cout << MESSAGE_SUCCESS_DISABLE << std::endl << std::endl;
}
