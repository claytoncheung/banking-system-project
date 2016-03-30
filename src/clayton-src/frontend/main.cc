// Copyright 2016 Pat Smuk, Clayton Cheung, Dennis Pacewicz
#include <iostream>
#include <string>
#include "./FrontEnd.h"

int main(int argc, char *argv[]) {
  if (argc < 3) {
    std::cout
      << "Usage: frontend <current accounts file> <transactions file>"
      << std::endl;
    return 0;
  }

  FrontEnd frontEnd(argv[1], argv[2]);
  bool ranSuccessfully = frontEnd.run();
  return ranSuccessfully ? 0 : 1;
}
