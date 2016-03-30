// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

void FrontEnd::handleWithdrawal() {
  // Make sure the user is logged in.
  if (!isLoggedIn()) {
    std::cout << MESSAGE_ERROR_REQUIRES_LOGIN << std::endl << std::endl;
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
    name = currentUserName;
  }

  Account* account;
  if (promptForAccount(&account, MESSAGE_PROMPT_ACCOUNT_NUMBER, &name, true)) {
    return;
  }

  // Check if the account is at the daily limit already.
  if (account->withdrawalTotal > DAILY_WITHDRAWAL_LIMIT - MIN_WITHDRAWAL) {
    std::cout << MESSAGE_ERROR_AT_WITHDRAWAL_LIMIT << std::endl << std::endl;
    return;
  }

  // Get the amount to withdraw.
  unsigned int withdrawal;
  unsigned int transactionFee = getFee(account);

  for (;;) {
    if (promptForAmount(&withdrawal, MESSAGE_PROMPT_AMOUNT_WITHDRAWAL, true)) {
      return;
    }

    // Check if the withdrawal will put the account over the daily limit.
    if (account->withdrawalTotal + withdrawal > DAILY_WITHDRAWAL_LIMIT 
          && !currentUserIsAdmin) {
      std::cout
        << MESSAGE_ERROR_HIT_WITHDRAWAL_LIMIT(
            DAILY_WITHDRAWAL_LIMIT - account->withdrawalTotal)
        << std::endl << std::endl;
      continue;
    }

    // Check if the remaining balance will cover transaction fees.
    if (static_cast<int>(account->balance - withdrawal - transactionFee) < 0) {
      std::cout << MESSAGE_ERROR_BALANCE_TOO_LOW << std::endl << std::endl;
      continue;
    }

    break;
  }

  writeToTransactionFile(TRANSACTION_CODE_WITHDRAWAL, name, account->id,
                         withdrawal, "");

  if (!currentUserIsAdmin) {
    account->withdrawalTotal += withdrawal;
  }
  account->balance -= (withdrawal + transactionFee);

  std::cout << MESSAGE_SUCCESS_WITHDRAWAL(withdrawal) << std::endl <<std::endl;
}
