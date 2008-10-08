package org.hivedb.teamcity.plugin;

import java.text.ParseException;
import java.util.Date;

public class VersionNumber {
  private String hash;
  private Date date;

  public VersionNumber(String version) {
    String[] fields = version.split("-");
    try {
      if (fields.length != 2) {
        throw new RuntimeException("Malformed Git Version: " + version);
      }
      date = Constants.VERSION_DATE.parse(fields[1].trim());
      hash = fields[0].trim();
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public VersionNumber(String hash, Date date) {
    this.hash = hash;
    this.date = date;
  }

  public String getHash() {
    return this.hash;
  }

  public Date getDate() {
    return this.date;
  }
    
  @Override
  public int hashCode() {
    return hash.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    VersionNumber other = (VersionNumber) obj;
    return getHash().equals(other.getHash());
  }

  @Override
  public String toString() {
    return String.format("%s - %s", hash, Constants.VERSION_DATE.format(date));
  }
}
