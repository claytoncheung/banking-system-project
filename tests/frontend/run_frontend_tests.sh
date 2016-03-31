#!/usr/bin/env bash

# Build the frontend.
pushd ../../ > /dev/null
make bin/frontend/frontend > /dev/null
popd > /dev/null

# Make sure it was built successfully.
if [ $? -ne 0 ]
then
  echo "Failed to build frontend."
  exit
fi

# These variables are used to count the number of tests that pass/fail.
tests_passed=0
tests_failed=0

# Define a function that runs a single test.
function run_single_test () {
  test=$1

  # Enter the test directory and run the frontend.
  pushd $test > /dev/null
  ../../../../bin/frontend/frontend current_accounts.txt actual_transactions.txt < commands.txt > actual_feedback.txt 2>&1

  # If the frontend returned non-zero exit code, it crashed.
  if [ $? -ne 0 ]
  then
    echo -e "$directory$test: Test failed. Frontend crashed.\n"
    echo -e "================================================================================\n"

    tests_failed=`expr $tests_failed + 1`
    rm actual_transactions.txt actual_feedback.txt
    popd > /dev/null
    continue
  fi

  # Get the diffs of the feedback and transactions file.
  feedback_diff="$(diff -u expected_feedback.txt actual_feedback.txt)"
  transactions_diff="$(diff -u expected_transactions.txt actual_transactions.txt)"

  # If either diff is not an empty string, the test failed.
  if [ -n "$feedback_diff" ] || [ -n "$transactions_diff" ]
  then
    echo -e "$directory$test: Test failed.\n"
    tests_failed=`expr $tests_failed + 1`

    if [ -n "$feedback_diff" ]
    then
      echo -e "Feedback difference:\n\n$feedback_diff\n\n"
    fi

    if [ -n "$transactions_diff" ]
    then
      echo -e "Transactions difference:\n\n$transactions_diff\n"
    fi

    echo -e "================================================================================\n"
  else
    tests_passed=`expr $tests_passed + 1`
  fi

  # Remove generated data.
  rm actual_transactions.txt actual_feedback.txt

  popd > /dev/null
}

# Define a function that runs all tests in a directory.
function run_tests () {
  directory=$1
  pushd $directory > /dev/null

  # For each test...
  for test in $(echo */)
  do
    # Run the test.
    run_single_test $test
  done

  popd > /dev/null
}

# Run the login tests first.
run_tests login/

# If any of the login tests failed, stop here.
if [ $tests_failed -gt 0 ]
then
  echo -e "Login test suite failed. Other test suites skipped.\n"
else
  # Run the remaining commands tests.
  for directory in $(echo */)
  do
    if [ $directory != "login/" ]
    then
      run_tests $directory
    fi
  done
fi

echo -e "RESULTS: $tests_passed tests passed, $tests_failed tests failed."
