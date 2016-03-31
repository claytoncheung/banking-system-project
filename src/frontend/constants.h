// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#ifndef SRC_FRONTEND_CONSTANTS_H_
#define SRC_FRONTEND_CONSTANTS_H_

#include <string>

// The amount of companies the user can pay bills to.
#define MAX_COMPANIES 3

// The maximum characters in an account holder's name.
#define MAX_ACCOUNT_HOLDER_NAME_LEN 20

// The maximum characters in an account number.
#define MAX_ACCOUNT_NUMBER_LEN 5

// The maximum characters in an amount
//   (5 for whole part, 1 for decimal point, 2 for fractional part).
#define MAX_AMOUNT_LEN (5 + 1 + 2)

// The maximum characters that can be entered as a command.
#define MAX_COMMAND_LEN sizeof("withdrawal")

// The maximum characters that can be entered as a login type.
#define MAX_LOGIN_TYPE_LEN sizeof("standard")

// The length of a line in the current accounts file.
#define LENGTH_CURRENT_ACCOUNTS_LINE 39

// The maximum balance a user can have.
#define MAX_BALANCE 9999999

// The limit on transfer transactions per day.
#define DAILY_TRANSFER_LIMIT 100000

// The limit on withdrawals per day.
#define DAILY_WITHDRAWAL_LIMIT 50000

// The limit on bill payments per day.
#define DAILY_PAYBILL_LIMIT 200000

// The smallest amount that can be withdrawn.
#define MIN_WITHDRAWAL 500

// The transaction fee applied to normal users.
#define TRANSACTION_FEE_NORMAL 10

// The transaction fee applied to students.
#define TRANSACTION_FEE_STUDENT 5

// Macro to format cents into a string like XXXXX.YY.
#define FORMAT_AMOUNT(amount) \
  std::to_string((amount) / 100) + "." + \
  (((amount) % 100) < 10 ? "0" : "") + std::to_string((amount) % 100)

// Various messages.
#define MESSAGE_WELCOME "Welcome to Generic Bank.\nEnter \"login\" to begin."
#define MESSAGE_HELP    "The available commands are:"

// Various prompts for input.
#define MESSAGE_PROMPT_MAIN                         \
    "> "

#define MESSAGE_PROMPT_NAME                         \
    "Enter your name: "

#define MESSAGE_PROMPT_ACCOUNT_HOLDER               \
    "Enter the account holder's name: "

#define MESSAGE_PROMPT_ACCOUNT_NUMBER               \
    "Enter the account number: "

#define MESSAGE_PROMPT_AMOUNT                       \
    "Enter the amount ($0.01 - $99999.99): $"

#define MESSAGE_PROMPT_LOGIN_TYPE                   \
    "Enter login type (\"standard\" or \"admin\"): "

#define MESSAGE_PROMPT_ACCOUNT_TRANSFER_SOURCE      \
    "Enter the account number to withdraw funds from: "

#define MESSAGE_PROMPT_ACCOUNT_TRANSFER_DESTINATION \
    "Enter the account number to deposit funds to: "

#define MESSAGE_PROMPT_AMOUNT_WITHDRAWAL            \
    "Enter the amount ($5.00 - $99995.00): $"

#define MESSAGE_PROMPT_COMPANY                      \
    "Which company will you be paying? (EC, CQ, or TV): "

#define MESSAGE_PROMPT_INITIAL_BALANCE              \
    "Enter the initial balance of the account: $"

// Messages sent when a file cannot be opened.
#define MESSAGE_ERROR_CANNOT_OPEN_FILE_WRITE(fileName)  \
    std::string("") + "Error: Could not open \"" + fileName + "\" for writing."

#define MESSAGE_ERROR_CANNOT_OPEN_FILE_READ(fileName)   \
    std::string("") + "Error: Could not open \"" + fileName + "\" for reading."

// Messages sent when the user does something wrong.
#define MESSAGE_ERROR_TOO_MUCH_DATA(maxCharacters)  \
    "Too much data entered (max " +                 \
    std::to_string(maxCharacters) + " characters)."

#define MESSAGE_ERROR_INVALID_LOGIN_TYPE            \
    "Error: Must enter either \"standard\" or \"admin\"."

#define MESSAGE_ERROR_INVALID_NAME                  \
    "Error: Name entered is not valid."

#define MESSAGE_ERROR_INVALID_COMMAND               \
    "Error: The command entered is invalid. "       \
    "Enter \"help\" for list of commands."

#define MESSAGE_ERROR_INVALID_MONETARY_AMOUNT       \
    "Error: Monetary amount entered is not valid."

#define MESSAGE_ERROR_INVALID_ACCOUNT_NUMBER        \
    "Error: Account number entered is not valid."

#define MESSAGE_ERROR_MISSING_ACCOUNT               \
    "Error: Account not found."

#define MESSAGE_ERROR_MISSING_ACCOUNT_HOLDER        \
    "Error: Account holder not found."

#define MESSAGE_ERROR_NEED_TO_OWN_ACCOUNT           \
    "Error: Account must be one of your own accounts."

#define MESSAGE_ERROR_BALANCE_TOO_HIGH              \
    "Error: Balance is too high to make deposit."

#define MESSAGE_ERROR_BALANCE_TOO_LOW               \
    "Error: Insufficient funds."

#define MESSAGE_ERROR_REQUIRES_ADMIN                \
    "Error: Command requires admin access."

#define MESSAGE_ERROR_REQUIRES_LOGIN                \
    "Error: Please login first."

#define MESSAGE_ERROR_REQUIRES_ACTIVE_ACCOUNT       \
    "Error: Account is disabled."

#define MESSAGE_ERROR_MISSING_NAME                  \
    "Error: Name not found."

#define MESSAGE_ERROR_ALREADY_LOGGED_IN             \
    "Error: You are already logged in (use \"logout\" to log out)."

#define MESSAGE_ERROR_REQUIRES_MULTIPLES_OF_FIVE    \
    "Error: Amount must end in 0 or 5. ($350, $420, $55, etc.)"

#define MESSAGE_ERROR_HIT_WITHDRAWAL_LIMIT(amount)  \
    std::string("") + "Error: You can only withdraw up to $500 in a day. "\
    "($" + FORMAT_AMOUNT(amount) + " remaining)"

#define MESSAGE_ERROR_AT_WITHDRAWAL_LIMIT           \
    "Error: You have already withdrawn $500 today. "\
    "Please try again another day."

#define MESSAGE_ERROR_COMPANY_NOT_FOUND             \
    "Error: Company not found."

#define MESSAGE_ERROR_HIT_PAYBILL_LIMIT(amount)     \
    std::string("") + \
    "Error: You can only pay up to $2000 per day per company. "\
    "($" + FORMAT_AMOUNT(amount) + " remaining)"

#define MESSAGE_ERROR_AT_PAYBILL_LIMIT(company)     \
    std::string("") + "Error: You have already paid " + \
    (company) + " $2000 today. "\
    "Please try again another day."

#define MESSAGE_ERROR_ALREADY_DISABLED              \
    "Error: Account is already disabled."

#define MESSAGE_ERROR_ALREADY_ENABLED               \
    "Error: Account is already enabled."

#define MESSAGE_ERROR_HIT_TRANSFER_LIMIT(amount)    \
    std::string("") + \
    "Error: You can only transfer up to $1000 in a day. "\
    "($" + FORMAT_AMOUNT(amount) + " remaining)"

#define MESSAGE_ERROR_AT_TRANSFER_LIMIT             \
    "Error: You have already transferred $1000 today. "\
    "Please try again another day."

// Messages sent to the user when they successfully complete a transaction.
#define MESSAGE_SUCCESS_LOGIN(name)               \
    std::string("") + "Hello, " + (name) + "."

#define MESSAGE_SUCCESS_LOGOUT(name)              \
    std::string("") + "Goodbye, " + (name) + "."

#define MESSAGE_SUCCESS_WITHDRAWAL(amount)        \
    std::string("$") + FORMAT_AMOUNT(amount) + " withdrawn successfully."

#define MESSAGE_SUCCESS_TRANSFER(amount)          \
    std::string("$") + FORMAT_AMOUNT(amount) + " transferred successfully."

#define MESSAGE_SUCCESS_PAYBILL(amount, company)  \
    std::string("") + "Payment of $" + FORMAT_AMOUNT(amount) + " to " + \
    (company) + " made successfully."

#define MESSAGE_SUCCESS_DEPOSIT(amount)           \
    std::string("$") + FORMAT_AMOUNT(amount) + " deposited successfully.\n"\
    "Funds will be available tomorrow."

#define MESSAGE_SUCCESS_CREATE                    \
    "Account created successfully."

#define MESSAGE_SUCCESS_DELETE                    \
    "Account deleted successfully."

#define MESSAGE_SUCCESS_DISABLE                   \
    "Account disabled successfully."

#define MESSAGE_SUCCESS_ENABLE                    \
    "Account enabled successfully."

#define MESSAGE_SUCCESS_CHANGEPLAN(type)          \
    std::string("Plan changed successfully.\n") + \
    "Account is now a " + (type) + " account."

// Transaction codes for the transaction log.
enum TransactionCode {
  TRANSACTION_CODE_LOGOUT     = 0,
  TRANSACTION_CODE_WITHDRAWAL = 1,
  TRANSACTION_CODE_TRANSFER   = 2,
  TRANSACTION_CODE_PAYBILL    = 3,
  TRANSACTION_CODE_DEPOSIT    = 4,
  TRANSACTION_CODE_CREATE     = 5,
  TRANSACTION_CODE_DELETE     = 6,
  TRANSACTION_CODE_DISABLE    = 7,
  TRANSACTION_CODE_CHANGEPLAN = 8,
  TRANSACTION_CODE_ENABLE     = 9,
  TRANSACTION_CODE_LOGIN      = 10
};

// Codes for the different companies.
// Must be in range [0, MAX_COMPANIES).
enum Company {
  COMPANY_EC  = 0,
  COMPANY_CQ  = 1,
  COMPANY_TV  = 2
};

#endif  // SRC_FRONTEND_CONSTANTS_H_
