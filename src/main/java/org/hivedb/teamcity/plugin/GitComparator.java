package org.hivedb.teamcity.plugin;

import java.util.Comparator;
import java.util.Collection;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class GitComparator implements Comparator<String> {
  SimpleDateFormat format = new SimpleDateFormat(Commit.VERSION_DATE_FORMAT);

  public int compare(String o1, String o2) {
    if(o1.equals(o2))
      return 0;
    else
      return extractDate(o1).compareTo(extractDate(o2));
  }

  private Date extractDate(String version) {
    String[] s = version.split("-");
    try {
      return format.parse(s[s.length-1].trim());
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }
}
