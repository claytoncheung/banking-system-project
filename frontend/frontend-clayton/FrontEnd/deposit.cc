// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

void FrontEnd::handleDeposit() {
  // Make sure the user is logged in.
  if (!isLoggedIn()) {
    std::cout << MESSAGE_ERROR_REQUIRES_LOGIN << std::endl <<std::endl;
    return;
  }

  // If the user is an admin, get the account holder's name.
  // Otherwise just use the current user's name.
  std::string name;

  if (currentUserIsAdmin) {
    if (promptForAccountHolder(&name)) {
      return;
    }
  } else {
    // account holder name is their own
    name = currentUserName;
  }

  // Get the account.
  Account* account;
  if (promptForAccount(&account, MESSAGE_PROMPT_ACCOUNT_NUMBER, &name, true)) {
    return;
  }

  // ask for deposit amount
  unsigned int deposit;
  unsigned int fee = getFee(account);

  for (;;) {
    if (promptForAmount(&deposit)) {
      return;
    }

    // check if current balance + deposit is less than max allowed
    if ((account->balance + deposit - fee) > MAX_BALANCE) {
      std::cout << MESSAGE_ERROR_BALANCE_TOO_HIGH << std::endl << std::endl;
      continue;
    }

    // Check that they have enough money to cover the fee.
    if (static_cast<int>(account->balance + deposit - fee) < 0) {
      std::cout << MESSAGE_ERROR_BALANCE_TOO_LOW << std::endl << std::endl;
      continue;
    }

    break;
  }

  // write transaction
  writeToTransactionFile(TRANSACTION_CODE_DEPOSIT, name, account->id,
                         deposit, "");
  account->balance -= fee;
  std::cout << MESSAGE_SUCCESS_DEPOSIT(deposit) << std::endl << std::endl;
}
