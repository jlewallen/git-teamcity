package org.hivedb.teamcity.plugin;


import java.util.Date;
import java.text.SimpleDateFormat;

//TODO JavaDoc
public class Commit {
  public static final String VERSION_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
  private SimpleDateFormat format = new SimpleDateFormat(VERSION_DATE_FORMAT);

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

  public String getVersion() {
    return String.format("%s - %s", getId(), format.format(getDate()));
  }

  @Override
  public String toString() {
    StringBuilder s = new StringBuilder();
    s.append("commit ").append(this.getId()).append("\n");
    s.append("Author: ").append(this.getAuthor()).append("\n");
    s.append("Date: ").append(new SimpleDateFormat(Git.GIT_DATE_FORMAT).format(this.getDate())).append("\n");
    s.append("\n\t").append(this.getMessage());
    return s.toString();
  }

  public boolean isValid() {
    return this.author != null && this.date != null && this.id != null;
  }
  String id, author, message;
  Date date;
}
