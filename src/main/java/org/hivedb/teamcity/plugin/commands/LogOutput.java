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
        if (line == null || line.trim().equals(""))
          continue;
        else {
          if (line.startsWith("commit")) {
            if (current.isValid()) {
              commits.add(current);
              current = new Commit();
            }
            current.setHash(line.replaceAll("commit", "").trim());
          }
          else if (line.startsWith("Author"))
            current.setAuthor(line.replaceAll("Author:", "").trim());
          else if (line.startsWith("Date")) {
            String dateString = line.replaceAll("Date:", "").trim();
            current.setDate(Constants.GIT_DATE.parse(dateString));
          }
          else if (line.startsWith("\\t")) {
            current.setMessage(current.getMessage() + "\n" + line);
          }
          else {
            Matcher m = nameAndStatus.matcher(line);
            while (m.find()) {
              NameAndStatus change = new NameAndStatus(m.group(2), m.group(1));
              current.getChanges().add(change);
            }
          }
        }
      }
      if (current.isValid()) {
        commits.add(current);
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
