package org.hivedb.teamcity.plugin;

import org.jetbrains.annotations.NotNull;

public class NameAndStatus {
  public static enum Status {
    ADDED("A"),
    COPIED("C"),
    DELETED("D"),
    MODIFIED("M"),
    RENAMED("R"),
    UNMERGED("U"),
    UNKNOWN("X"),
    BROKEN("B");

    private final String myName;

    Status(@NotNull String name) {
        myName = name;
    }

    @NotNull
    public String getName() {
        return myName;
    }
  }

  private final String name;
  private final Status status;

  public String getName() {
    return name;
  }

  public Status getStatus() {
    return status;
  }

  public NameAndStatus(String name, String status) {
    super();
    this.name = name;
    switch (status.charAt(0)) {
      case 'A': {
        this.status = Status.ADDED;
        break;
      }
      case 'C': {
        this.status = Status.COPIED;
        break;
      }
      case 'D': {
        this.status = Status.DELETED;
        break;
      }
      case 'M': {
        this.status = Status.MODIFIED;
        break;
      }
      case 'R': {
        this.status = Status.RENAMED;
        break;
      }
      case 'U': {
        this.status = Status.UNMERGED;
        break;
      }
      case 'X': {
        this.status = Status.UNKNOWN;
        break;
      }
      case 'B': {
        this.status = Status.BROKEN;
        break;
      }
      default: {
        assert false : "unknown status: " + status;
        this.status = Status.UNKNOWN;
      }
    }
  }

  public String toString() {
    return String.format("%s: %s", status, name);
  }
}
