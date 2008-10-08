package org.hivedb.teamcity.plugin;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commit {
  Collection<NameAndStatus> changes = new ArrayList<NameAndStatus>();
  String hash, author, message;
  Date date;

  public Commit() {
    this.message = "";
  }

  public Commit(String hash, String author, Date date) {
    this.hash = hash;
    this.author = author;
    this.date = date;
    this.message = "";
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
    return "Commit<" + getHash() + ", " + getAuthor() + ", " + getDate() + ", NumberChanges=" + getChanges().size() + ", Version=" + getVersion() + ">";
  }

  public boolean isValid() {
    return this.author != null && this.date != null && this.hash != null;
  }

  public String getShortAuthor() {
    Pattern pattern = Pattern.compile("(\\S+)@\\S+");
    Matcher m = pattern.matcher(this.author);
    if (m.find()) {
      return m.group(1);
    }
    if (author.length() > 32) {
      return author.substring(0, 32);
    }
    return author;
  }
}
