package org.hivedb.teamcity.plugin;

import java.text.ParseException;
import java.util.Date;

public class VersionNumber {
  private String number;

  public VersionNumber(String number) {
    this.number = number;
  }

  public VersionNumber(String hash, Date date) {
    this.number = String.format("%s - %s", hash, Constants.VERSION_DATE.format(date));
  }

  public String getHash() {
    return this.number.split(" - ")[0];
  }

  public Date getDate() {
    String[] s = number.split("-");
    try {
      return Constants.VERSION_DATE.parse(s[s.length - 1].trim());
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public String toString() {
    return this.number;
  }
}
