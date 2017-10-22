package frequentflyer.com.domain;

import frequentflyer.com.entities.Airport;
import frequentflyer.com.entities.Combination;
import frequentflyer.com.entities.Rotation;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by sasaradovanovic on 10/17/17.
 */
@Slf4j
public class DomainMapper {

    private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm");


    public static CombinationDto combinationToCombinationDto(Combination combination) {
        CombinationDto combinationDto = new CombinationDto();
        combinationDto.setId(combination.getId());
        combinationDto.setName(combination.getCombinationName());
        combinationDto.setColor(combination.getCombinationColor());
        return combinationDto;
    }

    public static RotationDto rotationToRotationDto(Rotation rotation) {

        RotationDto rotationDto = new RotationDto();

        Airport origin = rotation.getOrigin();
        Airport destination = rotation.getDestination();

        log.info("Mapping to rotation to " + rotation.getOrigin().getIataCode() + " " + rotation.getDestination().getIataCode());

        rotationDto.setOriginIataCode(origin.getIataCode());
        rotationDto.setOriginIcaoCode(origin.getIcaoCode());
        rotationDto.setOriginName(origin.getAirportName());
        rotationDto.setOriginCityName(origin.getCity());
        rotationDto.setOriginCountry(origin.getCountry());
        rotationDto.setOriginTimezone(origin.getTimezone().getDisplayName());


        rotationDto.setDestinationIataCode(destination.getIataCode());
        rotationDto.setDestinationIcaoCode(destination.getIcaoCode());
        rotationDto.setDestinationName(destination.getAirportName());
        rotationDto.setDestinationCityName(destination.getCity());
        rotationDto.setDestinationCountry(destination.getCountry());
        rotationDto.setDestinationTimezone(destination.getTimezone().getDisplayName());

        // Set departure tine and length
        LocalTime lt = LocalTime.parse(rotation.getLocalDepartureTime());

        log.info("Local dep time " + lt);

        long tzDiff = origin.getTimezone().getOffset(new Date().getTime());

        LocalTime utcStandardized = lt.plusSeconds(tzDiff / 1000);

        log.info("UTC time " + utcStandardized);


        if (tzDiff > 0) {
            if (utcStandardized.getHour() < lt.getHour()) {
                // readapt days
                log.info("Timezone diff was > 0. Adapting frequencies to utc... ");
                rotationDto.setDayMap(formDayMap(rotation.getFrequency(), -1));
            } else {
                rotationDto.setDayMap(formDayMap(rotation.getFrequency(), 0));
            }
        } else if (tzDiff < 0) {
            if (utcStandardized.getHour() > lt.getHour()) {
                // readapt days
                log.info("Timezone diff was < 0. Adapting frequencies to utc... ");
                rotationDto.setDayMap(formDayMap(rotation.getFrequency(), 1));
            } else {
                rotationDto.setDayMap(formDayMap(rotation.getFrequency(), 0));
            }
        } else {
            rotationDto.setDayMap(formDayMap(rotation.getFrequency(), 0));
        }

        rotationDto.setUtcDepartureTime(utcStandardized.format(dtf));
        rotationDto.setFlightTime(rotation.getFlightLength());

        return rotationDto;
    }


    private static HashMap<String, Boolean> formDayMap (String frequencyString, int shift) {
        HashMap<String, Boolean> dayMap = new HashMap<>();
        for (int i=1; i<=7; i++) {
            if (frequencyString.contains(String.valueOf(i))) {
                dayMap.put(String.valueOf(getDayIndexWithShift(i, shift)), true);
            } else {
                dayMap.put(String.valueOf(getDayIndexWithShift(i, shift)), false);
            }
        }
        return dayMap;
    }


    private static int getDayIndexWithShift(int i, int shift) {
        if (shift > 0) {
            return (i + shift) <= 7 ? (i + shift) : 1;
        } else if (shift < 0) {
            return (i + shift) >= 1 ? (i + shift) : 7;
        } else {
            return i;
        }
    }

}