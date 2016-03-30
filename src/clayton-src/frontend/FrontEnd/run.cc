// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

bool FrontEnd::run() {
  if (failedToOpenFiles) {
    return false;
  }

  std::string input;

  // Print the welcome message.
  std::cout << MESSAGE_WELCOME << std::endl << std::endl;

  // Keep going until we reach the end of input.
  for (;;) {
    // Print the prompt and get a line.
    std::cout << MESSAGE_PROMPT_MAIN;
    std::getline(std::cin, input);
    std::cout << std::endl;

    // If we're at the end of input, stop the program.
    if (std::cin.eof()) {
      break;
    }

    // If some sort of error was encountered, panic.
    if (std::cin.fail()) {
      exit(1);
    }

    if (input == "help") {
      std::cout << MESSAGE_HELP << std::endl;

      if (isLoggedIn()) {
        std::cout << "  logout"   << std::endl
                  << "  deposit"  << std::endl
                  << "  withdrawal" << std::endl
                  << "  transfer"   << std::endl
                  << "  paybill"  << std::endl;

        if (currentUserIsAdmin) {
          std::cout << "  create"   << std::endl
                    << "  delete"   << std::endl
                    << "  enable"   << std::endl
                    << "  disable"  << std::endl
                    << "  changeplan" << std::endl;
        }
      } else {
        std::cout << "  login" << std::endl;
      }
      std::cout << std::endl;

    } else if (input == "login") {
      handleLogin();
    } else if (input == "logout") {
      handleLogout();
    } else if (input == "deposit") {
      handleDeposit();
    } else if (input == "withdrawal") {
      handleWithdrawal();
    } else if (input == "transfer") {
      handleTransfer();
    } else if (input == "paybill") {
      handlePayBill();
    } else if (input == "create") {
      handleCreate();
    } else if (input == "delete") {
      handleDelete();
    } else if (input == "enable") {
      handleEnable();
    } else if (input == "disable") {
      handleDisable();
    } else if (input == "changeplan") {
      handleChangePlan();
    } else if (input == "") {
      // Do nothing.
    } else {
      std::cout << MESSAGE_ERROR_INVALID_COMMAND << std::endl << std::endl;
    }

    // If we're at the end of input, stop the program.
    if (std::cin.eof()) {
      break;
    }
  }

  return true;
}
