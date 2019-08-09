package it.blank517.realtimeworld;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class TimeZoneAndOffSet {

    private static Map<String, String> getAllZoneIdsAndItsOffSet() {

        Map<String, String> result = new HashMap<>();

        LocalDateTime localDateTime = LocalDateTime.now();

        for (String zoneId : ZoneId.getAvailableZoneIds()) {

            ZoneId id = ZoneId.of(zoneId);

            // LocalDateTime -> ZonedDateTime
            ZonedDateTime zonedDateTime = localDateTime.atZone(id);

            // ZonedDateTime -> ZoneOffset
            ZoneOffset zoneOffset = zonedDateTime.getOffset();

            //replace Z to +00:00
            String offset = zoneOffset.getId().replaceAll("Z", "+00:00");

            result.put(id.toString(), offset);

        }

        return result;

    }

    ArrayList<String> genTimeZoneList() {
        ArrayList<String> timezonesList = new ArrayList<>();

        Map<String, String> allZoneIdsAndItsOffSet = getAllZoneIdsAndItsOffSet();

        allZoneIdsAndItsOffSet.forEach((k, v) -> timezonesList.add(String.format("%s-->(UTC%s)", k, v)));
        return timezonesList;
    }

}