package org.hivedb.teamcity.plugin;

public class NameAndStatus {
  private String name;
  private String status;
  
  public String getName() {
    return name;
  }
  
  public String getStatus() {
    return status;
  }
  
  public NameAndStatus(String name, String status) {
    super();
    this.name = name;
    this.status = status;
  }
  
  public String toString() {
    return String.format("%s: %s", status, name);
  }
}
