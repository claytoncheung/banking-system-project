// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>
#include <memory>
#include <regex>

FrontEnd::FrontEnd(const std::string& accountsFilePath,
                   const std::string& transactionFilePath)
  : currentUserName(""),
    currentUserIsAdmin(false),
    transactionFileStream(transactionFilePath, std::ios_base::app),
    failedToOpenFiles(false) {
  // Check that the transaction file was opened.
  if (transactionFileStream.fail()) {
    std::cout
      << MESSAGE_ERROR_CANNOT_OPEN_FILE_WRITE(transactionFilePath)
      << std::endl;
    failedToOpenFiles = true;
    return;
  }

  // Open the accounts file.
  std::ifstream accountsFileStream(accountsFilePath);

  // If opening was successful, load accounts.
  if (accountsFileStream.good()) {
    loadAccounts(&accountsFileStream);
  } else {
    std::cout
      << MESSAGE_ERROR_CANNOT_OPEN_FILE_READ(accountsFilePath)
      << std::endl;
    failedToOpenFiles = true;
  }
}



void FrontEnd::loadAccounts(std::ifstream* accountsFileStream) {
  // NNNNN_AAAAAAAAAAAAAAAAAAAA_S_PPPPPPPP_Q
  std::string line;

  while (!accountsFileStream->eof()) {
    // Grab a line from the file.
    std::getline(*accountsFileStream, line);

    // Make sure it's the right length.
    if (line.length() != LENGTH_CURRENT_ACCOUNTS_LINE) {
      break;
    }

    // Parse the line into its components.
    int offset = 0;
    std::string accountNumberString, accountHolder, status, balanceDollars,
      balanceCents, plan;

    accountNumberString = line.substr(offset, 5);   offset += 1 + 5;
    accountHolder       = line.substr(offset, 20);  offset += 1 + 20;
    status              = line.substr(offset, 1);   offset += 1 + 1;
    balanceDollars      = line.substr(offset, 5);   offset += 1 + 5;
    balanceCents        = line.substr(offset, 2);   offset += 1 + 2;
    plan                = line.substr(offset, 1);   offset += 1 + 1;

    // While the last character of the name is whitespace, trim it off.
    while (accountHolder.length() > 0
        && accountHolder[accountHolder.length() - 1] == ' ') {
      accountHolder.resize(accountHolder.length() - 1);
    }

    // Parse the account number and balance into integers.
    unsigned int accountNumber = std::stoi(accountNumberString);
    unsigned int balance = std::stoi(balanceDollars) * 100
                           + std::stoi(balanceCents);

    // Create a new account and insert it into the accounts map.
    accounts.insert(
      std::pair<unsigned int, std::unique_ptr<Account> >(
        accountNumber, std::unique_ptr<Account>(
          new Account(accountNumber, accountHolder, balance,
                      plan == "S", status == "D"))));
  }
}
