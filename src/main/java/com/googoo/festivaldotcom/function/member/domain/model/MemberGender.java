package com.googoo.festivaldotcom.function.member.domain.model;

public enum MemberGender {
  MALE("남"), FEMALE("여");

  private final String description;

  MemberGender(String description) {
    this.description = description;
  }

  public String getDescription() {
    return description;
  }
}
