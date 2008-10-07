package org.hivedb.teamcity.plugin;

public class VersionNumber {
  private String number;

  public VersionNumber(String number) {
    this.number = number;
  }

  public String getHash() {
    return this.number.split(" - ")[0];
  }

  public String getDate() {
    return this.number.split(" - ")[1];
  }

  @Override
  public String toString() {
    return this.number;
  }
}
