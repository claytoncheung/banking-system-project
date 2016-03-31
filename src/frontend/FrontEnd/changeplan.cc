// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

void FrontEnd::handleChangePlan() {
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
  if (promptForAccount(&account, MESSAGE_PROMPT_ACCOUNT_NUMBER, &accountHolder,
                       false)) {
    return;
  }

  // Change the account's plan.
  account->isStudentPlan = !account->isStudentPlan;

  // Write transaction to transaction log.
  writeToTransactionFile(
    TRANSACTION_CODE_CHANGEPLAN, accountHolder, account->id, 0, "");

  // Print success message.
  std::cout
    << MESSAGE_SUCCESS_CHANGEPLAN(
        account->isStudentPlan ? "student" : "non-student")
    << std::endl << std::endl;
}
