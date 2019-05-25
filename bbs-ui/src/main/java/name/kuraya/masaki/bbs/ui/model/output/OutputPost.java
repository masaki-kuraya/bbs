package name.kuraya.masaki.bbs.ui.model.output;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class OutputPost {

    private String name;
    private Instant time;
    private String comment;

    public OutputPost() {
    }

    public OutputPost(String name, Instant time, String comment) {
        this.name = name;
        this.time = time;
        this.comment = comment;
    }

    public String getTimeDisplay(String fmtSec, String fmtMin, String fmtHour, String fmtDay, String fmtMonth, String fmt) {
        ZoneId zone = ZoneId.of("UTC");
        ZonedDateTime currentTime = ZonedDateTime.now(zone);
        ZonedDateTime postedTime = time.atZone(zone);
        long durationSec = ChronoUnit.SECONDS.between(postedTime, currentTime);
        if (durationSec < 60) {
            return postedTime.format(DateTimeFormatter.ofPattern(fmtSec.replace("{0}", String.valueOf(durationSec))));
        }
        long durationMin = ChronoUnit.MINUTES.between(postedTime, currentTime);
        if (durationMin < 60) {
            return postedTime.format(DateTimeFormatter.ofPattern(fmtMin.replace("{0}", String.valueOf(durationMin))));
        }
        long durationHour = ChronoUnit.HOURS.between(postedTime, currentTime);
        if (durationHour < 24) {
            return postedTime.format(DateTimeFormatter.ofPattern(fmtHour.replace("{0}", String.valueOf(durationHour))));
        }
        long durationDay = ChronoUnit.DAYS.between(postedTime, currentTime);
        long durationMonth = ChronoUnit.MONTHS.between(postedTime, currentTime);
        if (durationMonth < 1) {
            return postedTime.format(DateTimeFormatter.ofPattern(fmtDay.replace("{0}", String.valueOf(durationDay))));
        }
        long durationYear = ChronoUnit.YEARS.between(postedTime, currentTime);
        if (durationYear < 1) {
            return postedTime.format(DateTimeFormatter.ofPattern(fmtMonth.replace("{0}", String.valueOf(durationMonth))));
        }
        return postedTime.format(DateTimeFormatter.ofPattern(fmt));
    }

    public String getTimeIso8601() {
        OffsetDateTime offsetTime = time.atOffset(ZoneOffset.UTC);
        return offsetTime.format(DateTimeFormatter.ISO_INSTANT);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Instant getTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}