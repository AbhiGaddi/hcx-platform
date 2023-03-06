package org.swasth.apigateway.utils;

import lombok.experimental.UtilityClass;
import org.swasth.apigateway.exception.ClientException;
import org.swasth.apigateway.exception.ErrorCodes;
import org.joda.time.DateTime;

import java.text.MessageFormat;
import static org.swasth.common.response.ResponseMessage.INVALID_TIMESTAMP_MSG;

@UtilityClass
public class DateTimeUtils {

    public static boolean validTimestamp(int range, String timestamp) throws ClientException {
        try {
            DateTime requestTime = new DateTime(timestamp);
            DateTime currentTime = DateTime.now();
            System.out.println("request time------------- "  +  requestTime);
            System.out.println("current time--------------" + currentTime);
            System.out.println("is before" + requestTime.isBefore(currentTime.minusHours(range)));
            System.out.println("isAfter" + requestTime.isAfter(currentTime));
            System.out.println(!requestTime.isBefore(currentTime.minusHours(range)) && !requestTime.isAfter(currentTime));
            return (!requestTime.isBefore(currentTime.minusHours(range)) && !requestTime.isAfter(currentTime));
        } catch (Exception e) {
            throw new ClientException(ErrorCodes.ERR_INVALID_TIMESTAMP, MessageFormat.format(INVALID_TIMESTAMP_MSG, e.getMessage()));
        }
    }

}
