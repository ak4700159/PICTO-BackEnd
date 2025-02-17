package picto.com.photomanager.global.utils;

import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Component
public class DateUtils {
    public enum PeriodType {
        all("전체"),
        day("하루"),
        week("일주일"),
        month("한달"),
        year("일년");

        private final String value;

        PeriodType(String value) {
            this.value = value;
        }

        public static PeriodType fromString(String text) {
            for (PeriodType type : PeriodType.values()) {
                if (type.value.equalsIgnoreCase(text)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("지원하지 않는 기간 타입입니다: " + text);
        }
    }

    // periodType 전만큼의 UTC 시간 반환.
    public long getTimeAgo(long startUTCtime, String periodType) {
        PeriodType type = PeriodType.fromString(periodType);

        if (type == PeriodType.all) {
            return 0L; // 1970년 1월 1일의 UTC timestamp
        }

        Instant instant = Instant.ofEpochMilli(startUTCtime);
        Instant resultTime = instant.atZone(ZoneOffset.UTC)
                // 지정된 기간 만큼 시간을 뺀다.
                // getPeriodAmount : 양의 단위는 일, 월, 년
                // getPeriodUnit : ChronoUnit enum 형식으로 기간 선택
                .minus(getPeriodAmount(type), getPeriodUnit(type))
                .toInstant();

        return resultTime.toEpochMilli();
    }

    private long getPeriodAmount(PeriodType type) {
        return switch (type) {
            case day -> 1;
            case week -> 7;
            case month -> 1;
            case year -> 1;
            case all -> 0;
            default -> throw new IllegalArgumentException("지원하지 않는 기간 타입입니다.");
        };
    }

    private ChronoUnit getPeriodUnit(PeriodType type) {
        return switch (type) {
            case day -> ChronoUnit.DAYS;
            case week -> ChronoUnit.DAYS;
            case month -> ChronoUnit.MONTHS;
            case year -> ChronoUnit.YEARS;
            case all -> ChronoUnit.FOREVER;
            default -> throw new IllegalArgumentException("지원하지 않는 기간 타입입니다.");
        };
    }
}