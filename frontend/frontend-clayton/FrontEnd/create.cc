// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

void FrontEnd::handleCreate() {
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

  // Get the account holder's name. They don't have to already exist.
  std::string accountHolder;
  if (promptForAccountHolder(&accountHolder, false)) {
    return;
  }

  // Get the initial balance.
  unsigned int initialBalance;
  if (promptForAmount(&initialBalance, MESSAGE_PROMPT_INITIAL_BALANCE)) {
    return;
  }

  // Write transaction to transaction log.
  writeToTransactionFile(TRANSACTION_CODE_CREATE, accountHolder, 0,
                         initialBalance, "");

  // Print success message.
  std::cout << MESSAGE_SUCCESS_CREATE << std::endl << std::endl;
}
