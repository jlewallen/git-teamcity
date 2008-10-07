package org.hivedb.teamcity.plugin;

import java.util.Comparator;
import java.util.Date;

public class GitComparator implements Comparator<String> {
  public int compare(String o1, String o2) {
    if (o1.equals(o2)) {
      return 0;
    }
    else {
      return extractDate(o1).compareTo(extractDate(o2));
    }
  }

  private Date extractDate(String version) {
    VersionNumber versionNumber = new VersionNumber(version);
    return versionNumber.getDate();
  }
}
