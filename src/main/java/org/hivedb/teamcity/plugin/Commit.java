package org.hivedb.teamcity.plugin;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

public class Commit {
  Collection<NameAndStatus> changes = new ArrayList<NameAndStatus>();
  String hash, author, message;
  Date date;

  public Commit() {
  }

  public Commit(String hash, String author, Date date) {
    this.hash = hash;
    this.author = author;
    this.date = date;
  }

  public Collection<NameAndStatus> getChanges() {
    return changes;
  }

  public String getHash() {
    return hash;
  }

  public String getAuthor() {
    return author;
  }

  public Date getDate() {
    return date;
  }

  public String getMessage() {
    return message;
  }

  public void setHash(String hash) {
    this.hash = hash;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  public VersionNumber getVersion() { 
    return new VersionNumber(getHash(), getDate());
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("commit ").append(getHash()).append("\n");
    s.append("Author: ").append(getAuthor()).append("\n");
    s.append("Date: ").append(Constants.GIT_DATE.format(getDate())).append("\n");
    s.append("\n\t").append(getMessage());
    return s.toString();
  }

  public boolean isValid() {
    return this.author != null && this.date != null && this.hash != null;
  }
}
