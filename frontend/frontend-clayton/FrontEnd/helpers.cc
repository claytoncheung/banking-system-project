// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>
#include <iomanip>
#include <regex>

static const std::regex accountNumberRegex("\\d{1,5}");
static const std::regex amountRegex("(\\d{0,5})\\.(\\d{2})");

void FrontEnd::writeToTransactionFile(unsigned int code,
                                      const std::string& accountHolderName,
                                      unsigned int accountNumber,
                                      unsigned int transactionAmount,
                                      const std::string& miscellaneousInfo) {
  transactionFileStream
    << std::setw(2) << std::right << std::setfill('0')
    << code
    << " "
    << std::setw(20) << std::left << std::setfill(' ')
    << accountHolderName
    << " "
    << std::setw(5) << std::right << std::setfill('0')
    << accountNumber
    << " "
    << std::setw(5) << std::right << std::setfill('0')
    << transactionAmount/100
    << "."
    << std::setw(2) << std::right << std::setfill('0')
    << transactionAmount%100
    << " "
    << std::setw(2) << std::left << std::setfill(' ')
    << miscellaneousInfo
    << std::endl;
}

bool FrontEnd::checkAccountHolderName(
    const std::string& accountHolderName) const {
  // Go through all the accounts in the accounts map.
  for (auto it = accounts.begin(); it != accounts.end(); ++it) {
    // If the names match, accountHolderName is valid.
    if (it->second->accountHolderName == accountHolderName) {
      return true;
    }
  }

  // accountHolderName is not the holder of any accounts.
  return false;
}

bool FrontEnd::promptForAmount(unsigned int* amount,
                               const std::string& prompt,
                               bool isWithdrawal) {
  std::string amountString;
  unsigned int amountTemp;

  for (;;) {


    std::cout << prompt;
    std::getline(std::cin, amountString);
    std::cout << std::endl;

    // If we're at the end of input, stop the program.
    if (std::cin.eof()) {
      return true;
    }

    // If an input error is encountered, panic.
    if (std::cin.fail()) {
      exit(1);
    }

    if (amountString == "cancel") {
      std::cout << std::endl;
      return true;
    }

    if (amountString.length() == 0) {
      continue;
    }

    std::smatch matches;
    bool matched = std::regex_match(amountString, matches, amountRegex);

    if (!matched) {
      std::cout
        << MESSAGE_ERROR_INVALID_MONETARY_AMOUNT
        << std::endl << std::endl;

      continue;
    }

    // Should never throw because of the regex validation.
    amountTemp = (matches[1] == "" ? 0 : std::stoi(matches[1])) * 100 +
                 std::stoi(matches[2]);

    // If this is for a withdrawal, ennsure that the amount is a multiple of $5.
    if (isWithdrawal && amountTemp % 500 != 0) {
      std::cout
        << MESSAGE_ERROR_REQUIRES_MULTIPLES_OF_FIVE
        << std::endl << std::endl;
      continue;
    }
    
    //Check if the amount is greater than $0.00
    if (amountTemp == 0) {
      std::cout << MESSAGE_ERROR_INVALID_MONETARY_AMOUNT
      << std::endl << std::endl;
      continue;
    }

    break;
  }

  *amount = amountTemp;
  return false;
}

bool FrontEnd::promptForAccountHolder(std::string* accountHolder,
                                      bool mustAlreadyExist) {
  std::string accountHolderTemp;

  for (;;) {
    std::cout << MESSAGE_PROMPT_ACCOUNT_HOLDER;
    std::getline(std::cin, accountHolderTemp);
    std::cout << std::endl;

    // If we're at the end of input, stop the program.
    if (std::cin.eof()) {
      return true;
    }

    // If an input error occurred, panic.
    if (std::cin.fail()) {
      exit(1);
    }

    if (accountHolderTemp == "cancel") {
      std::cout << std::endl;
      return true;
    }

    if (accountHolderTemp.length() == 0) {
      continue;
    }

    if (accountHolderTemp.length() > MAX_ACCOUNT_HOLDER_NAME_LEN) {
      std::cout << MESSAGE_ERROR_INVALID_NAME << std::endl << std::endl;
      continue;
    }

    if (mustAlreadyExist && !checkAccountHolderName(accountHolderTemp)) {
      std::cout << MESSAGE_ERROR_MISSING_ACCOUNT_HOLDER
                << std::endl << std::endl;
      continue;
    }

    break;
  }

  *accountHolder = accountHolderTemp;
  return false;
}

bool FrontEnd::promptForAccount(Account** account, const std::string& prompt,
                                const std::string* accountHolder,
                                bool mustBeEnabled) {
  for (;;) {
    std::string accountNumberString;

    std::cout << prompt;
    std::getline(std::cin, accountNumberString);
    std::cout << std::endl;

    // If we're at the end of input, stop the program.
    if (std::cin.eof()) {
      return true;
    }

    // If an input error occurred, panic.
    if (std::cin.fail()) {
      exit(1);
    }

    if (accountNumberString == "cancel") {
      std::cout << std::endl;
      return true;
    }

    if (accountNumberString.length() == 0) {
      continue;
    }

    // Match the input against the account number regex.
    std::smatch matches;
    bool matched = std::regex_match(accountNumberString, matches,
                                    accountNumberRegex);

    if (!matched) {
      std::cout
        << MESSAGE_ERROR_INVALID_ACCOUNT_NUMBER
        << std::endl << std::endl;

      continue;
    }

    // This should never throw because of the regex.
    unsigned int accountNumber = std::stoi(matches[0]);

    try {
      *account = accounts.at(accountNumber).get();

      if (mustBeEnabled && (*account)->isDisabled) {
        std::cout
          << MESSAGE_ERROR_REQUIRES_ACTIVE_ACCOUNT
          << std::endl << std::endl;

        continue;
      }

      if (accountHolder != NULL
          && (*account)->accountHolderName != *accountHolder) {
        std::string error(currentUserIsAdmin
          ? MESSAGE_ERROR_MISSING_ACCOUNT
          : MESSAGE_ERROR_NEED_TO_OWN_ACCOUNT);
        std::cout << error << std::endl << std::endl;

        continue;
      }

      break;

    } catch (const std::out_of_range&) {
      std::cout << MESSAGE_ERROR_MISSING_ACCOUNT << std::endl << std::endl;

      continue;
    }
  }

  return false;
}

bool FrontEnd::promptForCompany(Company* company) {
  for (;;) {
    std::string companyCode;

    std::cout << MESSAGE_PROMPT_COMPANY;
    std::getline(std::cin, companyCode);
    std::cout << std::endl;

    // If we're at the end of input, stop the program.
    if (std::cin.eof()) {
      return true;
    }

    // If an input error occurred, panic.
    if (std::cin.fail()) {
      exit(1);
    }

    if (companyCode == "cancel") {
      std::cout << std::endl;
      return true;
    }
    if (companyCode == "EC") {
      *company = COMPANY_EC;
      break;
    }
    if (companyCode == "CQ") {
      *company = COMPANY_CQ;
      break;
    }
    if (companyCode == "TV") {
      *company = COMPANY_TV;
      break;
    }

    std::cout << MESSAGE_ERROR_COMPANY_NOT_FOUND << std::endl << std::endl;
  }

  return false;
}
