package org.hivedb.teamcity.plugin;

import java.util.Comparator;

public class GitComparator implements Comparator<String> {
  public int compare(String o1, String o2) {
    if (o1.equals(o2)) {
      return 0;
    }
    else {
      VersionNumber v1 = new VersionNumber(o1);
      VersionNumber v2 = new VersionNumber(o2);
      return v1.getDate().compareTo(v2.getDate());
    }
  }
}
