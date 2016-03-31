// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

void FrontEnd::handlePayBill() {
  static const char* companyNames[] = {
    "The Bright Light Electric Company",
    "Credit Card Company Q",
    "Low Definition TV, Inc."
  };

  static const char* companyCodes[] = {
    "EC",
    "CQ",
    "TV"
  };

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

  // Get the account to withdraw from.
  Account* account;

  for (;;) {
    if (promptForAccount(&account, MESSAGE_PROMPT_ACCOUNT_NUMBER, &name,
                         true)) {
      return;
    }

    if (account->balance == 0) {
      std::cout << MESSAGE_ERROR_BALANCE_TOO_LOW << std::endl << std::endl;
      continue;
    }

    break;
  }

  // Get the company code.
  Company company;

  for (;;) {
    if (promptForCompany(&company)) {
      return;
    }

    if (account->billPaymentTotal[company] == DAILY_PAYBILL_LIMIT) {
      std::cout
        << MESSAGE_ERROR_AT_PAYBILL_LIMIT(companyNames[company])
        << std::endl << std::endl;
      continue;
    }

    break;
  }

  unsigned int amount;
  unsigned int fee = getFee(account);

  // Get the amount to pay.
  for (;;) {
    if (promptForAmount(&amount)) {
      return;
    }

    // Check that they have enough money pay the amount and the fee.
    if (static_cast<int>(account->balance - amount - fee) < 0) {
      std::cout << MESSAGE_ERROR_BALANCE_TOO_LOW << std::endl << std::endl;
      continue;
    }

    // Make sure this doesn't exceed the daily limit.
    if (account->billPaymentTotal[company] + amount > DAILY_PAYBILL_LIMIT) {
      std::cout
        << MESSAGE_ERROR_HIT_PAYBILL_LIMIT(
            DAILY_PAYBILL_LIMIT - account->billPaymentTotal[company])
        << std::endl << std::endl;
      continue;
    }

    break;
  }

  // Remove money from account and add to paybill total.
  account->balance -= (amount + fee);
  account->billPaymentTotal[company] += amount;

  // Write transaction to transaction log.
  writeToTransactionFile(TRANSACTION_CODE_PAYBILL, name, account->id, amount,
                         companyCodes[company]);

  // Print success message.
  std::cout
    << MESSAGE_SUCCESS_PAYBILL(amount, companyNames[company])
    << std::endl << std::endl;
}
