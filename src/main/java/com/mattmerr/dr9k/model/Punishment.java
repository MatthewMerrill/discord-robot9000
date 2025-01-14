package com.mattmerr.dr9k.model;

import com.mattmerr.dr9k.service.PunishmentService;
import io.ebean.annotation.Length;
import io.ebean.annotation.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Entity
@Table(name = "punishments")
public class Punishment {

  @Id
  private Long id;

  @NotNull
  @Length(512)
  private String guildId;

  @NotNull
  @Length(512)
  private String authorId;

  @NotNull
  private int severityLevel;

  @NotNull
  private Instant punishmentStart;

  public Long getId() {
    return id;
  }

  public Punishment setId(Long id) {
    this.id = id;
    return this;
  }

  public String getGuildId() {
    return guildId;
  }

  public Punishment setGuildId(String guildId) {
    this.guildId = guildId;
    return this;
  }

  public String getAuthorId() {
    return authorId;
  }

  public Punishment setAuthorId(String authorId) {
    this.authorId = authorId;
    return this;
  }

  public int getSeverityLevel() {
    return severityLevel;
  }

  public Punishment setSeverityLevel(int severityLevel) {
    this.severityLevel = severityLevel;
    return this;
  }

  public Instant getPunishmentStart() {
    return punishmentStart;
  }

  public Punishment setPunishmentStart(Instant punishmentStart) {
    this.punishmentStart = punishmentStart;
    return this;
  }

  public boolean isOver() {
    Instant now = Instant.now();
    Duration timePassed = Duration.between(punishmentStart, now);
    long seconds = ((long) Math.pow(2, severityLevel));
    seconds -= timePassed.getSeconds();
    if (seconds <= 0) {
      return true;
    }
    return false;
  }

  public boolean getPunishmentDecayed() {
    int decay = getPunishmentDecay();

    return severityLevel <= decay;
  }

  public int getPunishmentDecay() {
    Instant now = Instant.now();
    Duration timePassed = Duration.between(punishmentStart, now);
    long secondsPassed = timePassed.getSeconds();
    return (int) secondsPassed / PunishmentService.getDecaySeconds();
  }

  public String getHumanTimeRemaining() {
    Instant now = Instant.now();
    Duration timePassed = Duration.between(punishmentStart, now);
    long seconds = ((long) Math.pow(2, severityLevel));
    seconds -= timePassed.getSeconds();
    if (seconds <= 0) {
      return "none";
    }

    long years = seconds / ChronoUnit.YEARS.getDuration().getSeconds();
    seconds %= ChronoUnit.YEARS.getDuration().getSeconds();
    long months = seconds / ChronoUnit.MONTHS.getDuration().getSeconds();
    seconds %= ChronoUnit.MONTHS.getDuration().getSeconds();
    long weeks = seconds / ChronoUnit.WEEKS.getDuration().getSeconds();
    seconds %= ChronoUnit.WEEKS.getDuration().getSeconds();
    long days = seconds / ChronoUnit.DAYS.getDuration().getSeconds();
    seconds %= ChronoUnit.DAYS.getDuration().getSeconds();
    long hours = seconds / ChronoUnit.HOURS.getDuration().getSeconds();
    seconds %= ChronoUnit.HOURS.getDuration().getSeconds();
    long minutes = seconds / ChronoUnit.MINUTES.getDuration().getSeconds();
    seconds %= ChronoUnit.MINUTES.getDuration().getSeconds();
    StringBuilder ret = new StringBuilder();
    if (years > 0) {
      ret.append(String.format("%d years, ", years));
    }
    if (months > 0) {
      ret.append(String.format("%d months, ", months));
    }
    if (weeks > 0) {
      ret.append(String.format("%d weeks, ", weeks));
    }
    if (days > 0) {
      ret.append(String.format("%d days, ", days));
    }
    if (hours > 0) {
      ret.append(String.format("%d hours, ", hours));
    }
    if (minutes > 0) {
      ret.append(String.format("%d minutes, ", minutes);
    }
    if (seconds > 0) {
      ret.append(String.format("%d seconds", seconds));
    }
    return ret.toString();
  }
}
