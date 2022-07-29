package com.se2.alert.constant;

import lombok.Getter;
import java.util.HashMap;
import java.util.Map;

@Getter
public enum AlertGeoRiskThreatLevel {
    CRITICAL(1),
    MODERATE(2),
    MINOR(3);

    int value;

    private static Map<Integer, AlertGeoRiskThreatLevel> map = new HashMap<>();

    static {
        for (AlertGeoRiskThreatLevel threatLevel : AlertGeoRiskThreatLevel.values())
            map.put(threatLevel.value, threatLevel);
    }

    AlertGeoRiskThreatLevel(int value) {
        this.value = value;
    }

    public static AlertGeoRiskThreatLevel getThreatLevelByValue(int threatLevel) {
        return map.get(threatLevel);
    }

}
