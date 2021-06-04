package com.anupam.employeemanager.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@AllArgsConstructor
public class CustomException extends RuntimeException {

  private State state;
  private List<String> errorMessages;
}
