#!/usr/bin/env bash

day=$1

# Move to the appropriate directory
pushd day$day > /dev/null

function run_session() {
  command_file=$1
  counter=$2
  
  ../../bin/frontend/frontend CurrentBankAccounts.dat $day-$counter.log < $command_file > /dev/null 2>&1
  
  if [ $? -ne 0 ]
  then
    echo -e "Day $day-$counter crashed the front end.\n"
  fi
}

function run_backend() {
  pushd ../../ > /dev/null
  # Run the backend
  java -cp bin/ backend.main transaction_sessions/day$day/MasterBankAccounts.dat transaction_sessions/day$day/merged-day$day.trans > /dev/null 2>&1
  
  if [ $? -ne 0 ]
  then
    echo -e "Day $day crashed the back end.\n"
  fi
  
  popd > /dev/null
}

session_counter=1

rm merged-day*.trans > /dev/null

# For each list of transaction commands
for commands in *.txt
do
  # Run the session
  run_session $commands $session_counter
  ((session_counter++))
done

# Concatenate transaction logs to day$day.trans
for trans_log in *.log
do
  cat $trans_log >> merged-day$day.trans
  #echo -e "Day $day transaction logs have been merged.\n"
done

# Remove the generated session transaction logs
rm *.log

run_backend

popd > /dev/null

echo -e "Day $day has completed.\n"
