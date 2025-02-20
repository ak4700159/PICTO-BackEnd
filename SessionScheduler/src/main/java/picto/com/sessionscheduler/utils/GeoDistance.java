package picto.com.sessionscheduler.utils;

public class GeoDistance {
    // 지구의 반지름 (단위: km)
    private static final double EARTH_RADIUS = 6371;

    /**
     * 두 지점 간의 거리를 계산하는 함수
     * @param lat1 시작점의 위도
     * @param lon1 시작점의 경도
     * @param lat2 도착점의 위도
     * @param lon2 도착점의 경도
     * @return 두 지점 간의 거리 (단위: km)
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 위도와 경도를 라디안으로 변환
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // 위도와 경도의 차이
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Haversine 공식
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double distance = EARTH_RADIUS * c;

        // 소수점 둘째 자리까지 반올림
        return Math.round(distance * 100.0) / 100.0;
    }
}