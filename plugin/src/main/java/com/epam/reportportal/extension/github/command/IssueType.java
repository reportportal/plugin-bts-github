package com.epam.reportportal.extension.github.command;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public enum IssueType {
  ISSUE("Issue");

  private final String name;

}
