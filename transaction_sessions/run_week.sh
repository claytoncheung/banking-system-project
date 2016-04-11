#!/usr/bin/env bash

# Build the frontend and backend.
pushd ../
make bin/frontend/frontend
make bin/backend/main.class
popd

function run_day() {
  day=$1
  directory=day-$day
  session_counter=1
    
  ./run_day.sh $day
}

for days in $(echo */)
do
  run_day $days
  # Move the resulting account files to the next day and rename
  
done
