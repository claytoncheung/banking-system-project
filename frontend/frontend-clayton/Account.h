// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#ifndef SRC_FRONTEND_ACCOUNT_H_
#define SRC_FRONTEND_ACCOUNT_H_

#include <string>
#include "./constants.h"

// Account is a data structure that holds all the data that comprises a single
//   bank account.
struct Account {
  // The ID of the account, a.k.a the "account number".
  unsigned int id;

  // The name of the account holder.
  std::string accountHolderName;

  // The current balance of the account, in cents.
  unsigned int balance;

  // true if the account is a student plan, false otherwise.
  bool isStudentPlan;

  // true if the account is disabled, false otherwise.
  bool isDisabled;

  // The total of all withdrawals made today (not including fees).
  unsigned int withdrawalTotal;

  // The total of all transfers done today (not including fees).
  unsigned int transferTotal;

  // The total of all bill payments done today (not including fees)
  //   for each company.
  unsigned int billPaymentTotal[MAX_COMPANIES];

  Account(
    unsigned int id,
    std::string accountHolderName,
    unsigned int balance,
    bool isStudentPlan,
    bool isDisabled)
    : id(id),
      accountHolderName(accountHolderName),
      balance(balance),
      isStudentPlan(isStudentPlan),
      isDisabled(isDisabled),
      withdrawalTotal(0),
      transferTotal(0) {
    for (int i = 0; i < MAX_COMPANIES; i++) {
      billPaymentTotal[i] = 0;
    }
  }
};

#endif  // SRC_FRONTEND_ACCOUNT_H_
