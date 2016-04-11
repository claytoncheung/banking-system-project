#!/usr/bin/env bash

day=$1

# Move to the appropriate directory
pushd $day > /dev/null

function run_session() {
  command_file=$1
  counter=$2
  
  ../../bin/frontend/frontend CurrentBankAccounts.dat $day_$counter.txt < $command_file.input 2>&1
  
  if [ $? -ne 0 ]
  then
    echo -e "$day-$session_counter crashed the front end.\n"
    continue
  fi
  
  # Concatenate transaction logs to day$day.trans
  for trans_log in *.log
  do
    cat $trans_log.log > day-$day.trans
    #echo -e "Day $day transaction logs have been merged.\n"
  done
  
  # Remove the generated session transaction logs
  rm *.log
}

function run_backend() {
  pushd ../../ > /dev/null
  # Run the backend
  java bin.backend.main CurrentBankAccounts.dat day-$day.trans 2>&1
  
  if [ $? -ne 0 ]
  then
    echo -e "$day crashed the back end.\n"
    continue
  fi
  
  popd > /dev/null
}

session_counter=1

# For each list of transaction commands
for commands in *.input
do
  # Run the session
  run_session $commands $session_counter
  ((session_counter++))
done

run_backend

popd > /dev/null

echo -e "$day has completed.\n"
