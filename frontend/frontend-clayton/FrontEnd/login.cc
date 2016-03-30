// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

void FrontEnd::handleLogin() {
  // Check that they aren't already logged in.
  if (isLoggedIn()) {
    std::cout << MESSAGE_ERROR_ALREADY_LOGGED_IN << std::endl << std::endl;
    return;
  }

  // Get the login type.
  for (;;) {
    std::string loginType;

    std::cout << MESSAGE_PROMPT_LOGIN_TYPE;
    std::getline(std::cin, loginType);
    std::cout << std::endl;

    // If we're at the end of input, stop the program.
    if (std::cin.eof()) {
      return;
    }

    // If some sort of error is encountered, panic.
    if (std::cin.fail()) {
      exit(1);
    }

    if (loginType == "standard") {
      // If standard, continue to the next prompt.
      break;

    } else if (loginType == "admin") {
      // If admin, nothing else to do. Return early.
      std::cout << MESSAGE_SUCCESS_LOGIN("admin") << std::endl << std::endl;
      writeToTransactionFile(TRANSACTION_CODE_LOGIN, "", 0, 0, "A");
      currentUserIsAdmin = true;
      return;

    } else if (loginType == "cancel") {
      std::cout << std::endl;
      return;

    } else {
      std::cout << MESSAGE_ERROR_INVALID_LOGIN_TYPE << std::endl << std::endl;
      // In case they entered too many characters, clear the fail bit.
      std::cin.clear();
      continue;
    }
  }

  // Get the user's name.
  for (;;) {
    std::string name;

    std::cout << MESSAGE_PROMPT_NAME;
    std::getline(std::cin, name);
    std::cout << std::endl;

    // If we're at the end of input, stop the program.
    if (std::cin.eof()) {
      return;
    }

    // If an input error is encountered, panic.
    if (std::cin.fail()) {
      exit(1);
    }

    if (name == "cancel") {
      std::cout << std::endl;
      return;
    }

    if (checkAccountHolderName(name)) {
      std::cout << MESSAGE_SUCCESS_LOGIN(name) << std::endl << std::endl;
      writeToTransactionFile(TRANSACTION_CODE_LOGIN, name, 0, 0, "S");
      currentUserName = name;
      break;
    }

    std::cout << MESSAGE_ERROR_MISSING_NAME << std::endl << std::endl;
    // In case they entered too many characters, clear the fail bit.
    std::cin.clear();
    continue;
  }
}
