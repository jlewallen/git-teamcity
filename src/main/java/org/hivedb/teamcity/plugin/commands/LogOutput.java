package org.hivedb.teamcity.plugin.commands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.hivedb.teamcity.plugin.Commit;
import org.hivedb.teamcity.plugin.Constants;
import org.hivedb.teamcity.plugin.NameAndStatus;

public class LogOutput {

  public static Collection<Commit> parse(String log) {
    Pattern nameAndStatus = Pattern.compile("^(\\w)\\s+(.+)$");
    
    Collection<Commit> commits = new ArrayList<Commit>();
    BufferedReader r = new BufferedReader(new StringReader(log));
    String line;
    try {
      Commit current = new Commit();
      while ((line = r.readLine()) != null) {
        String s = line.trim();
        if (s == null || "".equals(s))
          continue;
        else {
          if (s.startsWith("commit")) {
            if (current.isValid()) {
              commits.add(current);
              current = new Commit();
            }
            current.setHash(s.replaceAll("commit", "").trim());
          } else if (s.startsWith("Author"))
            current.setAuthor(s.replaceAll("Author:","").trim());
          else if (s.startsWith("Date")) {
            String dateString = s.replaceAll("Date:","").trim();
            current.setDate(Constants.GIT_DATE.parse(dateString));
          } else {
            current.setMessage(s);
            commits.add(current);
            current = new Commit();
          }
          
          Matcher m = nameAndStatus.matcher(line);
          while (m.find()) {
            NameAndStatus change = new NameAndStatus(m.group(2), m.group(1));
            current.getChanges().add(change);
          }
        }
      }
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
    return commits;
  }

}
