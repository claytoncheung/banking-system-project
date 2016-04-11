#!/usr/bin/env bash

# Build the frontend and backend.
pushd ../ > /dev/null
make bin/frontend/frontend
make bin/backend/main.class
popd > /dev/null

day=0

function run_day() {
  directory=day-$day
  session_counter=1
    
  ./run_day.sh $day
}

# For each directory in /transaction_sessions/
for directory in $(echo */)
do
  ((day++))
  # Move the account files to the current day
  pushd ../ > /dev/null
  mv CurrentBankAccounts.dat transaction_sessions/day$day/
  mv MasterBankAccounts.dat transaction_sessions/day$day/
  popd > /dev/null
  
  # And run the day's sessions
  run_day $day    
done
