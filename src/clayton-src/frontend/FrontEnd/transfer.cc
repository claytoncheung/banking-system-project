// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

void FrontEnd::handleTransfer() {
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

  Account* sourceAccount;
  Account* destinationAccount;

  // Get the account to transfer from.
  for (;;) {
    if (promptForAccount(&sourceAccount,
                         MESSAGE_PROMPT_ACCOUNT_TRANSFER_SOURCE,
                         &name, true)) {
      return;
    }

    if (sourceAccount->transferTotal == DAILY_TRANSFER_LIMIT) {
      std::cout << MESSAGE_ERROR_AT_TRANSFER_LIMIT << std::endl << std::endl;
      continue;
    }

    break;
  }

  // Get the account to transfer to.
  if (promptForAccount(&destinationAccount,
                       MESSAGE_PROMPT_ACCOUNT_TRANSFER_DESTINATION,
                       nullptr, true)) {
    return;
  }

  unsigned int transferAmount;
  unsigned int fee = getFee(sourceAccount);

  // Get the amount to transfer.
  for (;;) {
    if (promptForAmount(&transferAmount)) {
      return;
    }

    // Make sure they have enough money to cover the transfer and the fee.
    if (static_cast<int>(sourceAccount->balance - transferAmount - fee) < 0) {
      std::cout << MESSAGE_ERROR_BALANCE_TOO_LOW << std::endl << std::endl;
      continue;
    }

    // Make sure they don't exceed the daily transfer limit.
    if (sourceAccount->transferTotal + transferAmount > DAILY_TRANSFER_LIMIT 
            && !currentUserIsAdmin) {
      std::cout
        << MESSAGE_ERROR_HIT_TRANSFER_LIMIT(
            DAILY_TRANSFER_LIMIT - sourceAccount->transferTotal)
        << std::endl << std::endl;
      continue;
    }

    // Make sure the destination account doesn't go over the maximum balance.
    if (destinationAccount->balance + transferAmount > MAX_BALANCE) {
      std::cout << MESSAGE_ERROR_BALANCE_TOO_HIGH << std::endl << std::endl;
      continue;
    }

    break;
  }

  // Remove money from source account and add to transfer total.
  if (!currentUserIsAdmin) {
    sourceAccount->balance -= (transferAmount + fee);
  }
  sourceAccount->transferTotal += transferAmount;

  // Add money to destination account.
  destinationAccount->balance += transferAmount;

  // Write transaction to transaction log.
  writeToTransactionFile(TRANSACTION_CODE_TRANSFER, name, sourceAccount->id,
                         transferAmount, "");

  writeToTransactionFile(TRANSACTION_CODE_TRANSFER,
                         destinationAccount->accountHolderName,
                         destinationAccount->id, transferAmount, "");

  // Print success message.
  std::cout
    << MESSAGE_SUCCESS_TRANSFER(transferAmount)
    << std::endl << std::endl;
}
