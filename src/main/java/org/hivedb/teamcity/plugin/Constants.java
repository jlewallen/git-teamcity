package org.hivedb.teamcity.plugin;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constants {
  
  private static final String GIT_DATE_FORMAT = "EEE MMM dd HH:mm:ss yyyy Z";
  private static final String VERSION_DATE_FORMAT = "MM/dd/yyyy HH:mm:ss";
  public static final SimpleDateFormat GIT_DATE = new SimpleDateFormat(Constants.GIT_DATE_FORMAT, Locale.US);
  public static final SimpleDateFormat VERSION_DATE = new SimpleDateFormat(Constants.VERSION_DATE_FORMAT, Locale.US);

}
