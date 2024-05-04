package com.googoo.festivaldotcom.global.utils;


import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.PrecisionModel;

public class GeometryUtils {

    // GeometryFactory의 단일 인스턴스를 생성합니다.
    // 이 인스턴스는 정밀 모델과 좌표계를 사용하여 공간 데이터를 처리하는 데 사용됩니다.
    // 여기서 사용하는 5179는 한국 중부 원점(Korean 2000 Central Belt) 좌표계를 나타냅니다.
    private static final GeometryFactory INSTANCE
            = new GeometryFactory(new PrecisionModel(), 5179);

    // 외부에서 인스턴스화를 방지하기 위해 생성자를 private로 선언합니다.
    private GeometryUtils() {
        /* no-op */
    }

    // GeometryFactory의 인스턴스를 반환하는 정적 메서드입니다.
    // 이 메서드를 통해 공간 데이터의 생성과 조작이 가능한 Factory 객체에 접근할 수 있습니다.
    public static GeometryFactory getInstance() {
        return INSTANCE;
    }

    // 주어진 미터 단위의 거리를 지구상의 대략적인 반경(도 단위)으로 변환합니다.
    // 이 메서드는 거리를 지구 반경을 나누어 라디안 단위로 변환한 후, 다시 도 단위로 변환합니다.
    public static double calculateApproximateRadius(int distanceInMeters) {
        // 지구의 대략적인 반경을 미터 단위로 설정합니다.
        double earthRadius = 6371000;

        // 거리를 지구의 반경으로 나누어 라디안으로 변환합니다.
        double radiusInRadians = distanceInMeters / earthRadius;

        // 라디안을 도 단위로 변환하여 반환합니다.
        return Math.toDegrees(radiusInRadians);
    }

}
