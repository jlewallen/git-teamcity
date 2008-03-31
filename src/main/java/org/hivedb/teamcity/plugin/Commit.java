package org.hivedb.teamcity.plugin;


import java.util.Date;

public class Commit {
  public Commit(){}
  public Commit(String id, String author, Date date) {
    this.id = id;
    this.author = author;
    this.date = date;
  }

  public String getId() {
    return id;
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

  public void setId(String id) {
    this.id = id;
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

  public boolean isValid() {
    return this.author != null && this.date != null && this.id != null;
  }
  String id, author, message;
  Date date;
}
