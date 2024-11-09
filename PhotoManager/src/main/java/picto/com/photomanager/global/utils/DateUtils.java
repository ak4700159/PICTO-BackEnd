package picto.com.photomanager.global.utils;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

public class DateUtils {

    /**
     * 날짜 계산에 사용할 수 있는 기간 타입을 정의하는 enum
     */
    public enum PeriodType {
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

    /**
     * 주어진 UTC timestamp에서 지정된 기간만큼 이전의 UTC timestamp를 반환합니다.
     *
     * @param utcTimestamp UTC timestamp (밀리초)
     * @param periodType 계산할 기간 타입 ("하루", "일주일", "한달", "일년")
     * @return 지정된 기간만큼 이전의 UTC timestamp
     */
    public static long getTimeAgo(long utcTimestamp, String periodType) {
        Instant instant = Instant.ofEpochMilli(utcTimestamp);
        PeriodType type = PeriodType.fromString(periodType);

        Instant resultTime = instant.atZone(ZoneOffset.UTC)
                .minus(getPeriodAmount(type), getPeriodUnit(type))
                .toInstant();

        return resultTime.toEpochMilli();
    }

    /**
     * 기간 타입에 따른 수량을 반환합니다.
     */
    private static long getPeriodAmount(PeriodType type) {
        return switch (type) {
            case day -> 1;
            case week -> 7;
            case month -> 1;
            case year -> 1;
            default -> throw new IllegalArgumentException("지원하지 않는 기간 타입입니다.");
        };
    }

    /**
     * 기간 타입에 따른 시간 단위를 반환합니다.
     */
    private static ChronoUnit getPeriodUnit(PeriodType type) {
        return switch (type) {
            case day -> ChronoUnit.DAYS;
            case week -> ChronoUnit.DAYS;
            case month -> ChronoUnit.MONTHS;
            case year -> ChronoUnit.YEARS;
            default -> throw new IllegalArgumentException("지원하지 않는 기간 타입입니다.");
        };
    }
}