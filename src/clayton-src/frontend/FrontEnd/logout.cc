// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include "../FrontEnd.h"
#include <iostream>

void FrontEnd::handleLogout() {
  // Make sure the user is logged in.
  if (!isLoggedIn()) {
    std::cout << MESSAGE_ERROR_REQUIRES_LOGIN << std::endl << std::endl;
    return;
  }

  writeToTransactionFile(TRANSACTION_CODE_LOGOUT, currentUserName, 0, 0, "");

  std::cout
    << MESSAGE_SUCCESS_LOGOUT(currentUserIsAdmin ? "admin" : currentUserName)
    << std::endl << std::endl;

  currentUserName = "";
  currentUserIsAdmin = false;
}
