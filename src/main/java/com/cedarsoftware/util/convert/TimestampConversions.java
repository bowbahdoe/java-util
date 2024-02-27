package com.cedarsoftware.util.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

/**
 * @author John DeRegnaucourt (jdereg@gmail.com)
 *         <br>
 *         Copyright (c) Cedar Software LLC
 *         <br><br>
 *         Licensed under the Apache License, Version 2.0 (the "License");
 *         you may not use this file except in compliance with the License.
 *         You may obtain a copy of the License at
 *         <br><br>
 *         <a href="http://www.apache.org/licenses/LICENSE-2.0">License</a>
 *         <br><br>
 *         Unless required by applicable law or agreed to in writing, software
 *         distributed under the License is distributed on an "AS IS" BASIS,
 *         WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *         See the License for the specific language governing permissions and
 *         limitations under the License.
 */
final class TimestampConversions {
    private TimestampConversions() {}

    static double toDouble(Object from, Converter converter) {
        Duration d = toDuration(from, converter);
        return d.getSeconds() + d.getNano() / 1_000_000_000d;
    }
    
    static BigDecimal toBigDecimal(Object from, Converter converter) {
        Timestamp timestamp = (Timestamp) from;
        Instant instant = timestamp.toInstant();
        return InstantConversions.toBigDecimal(instant, converter);
    }
    
    static BigInteger toBigInteger(Object from, Converter converter) {
        Duration duration = toDuration(from, converter);
        return DurationConversions.toBigInteger(duration, converter);
    }

    static Duration toDuration(Object from, Converter converter) {
        Timestamp timestamp = (Timestamp) from;
        Instant epoch = Instant.EPOCH;
        Instant timestampInstant = timestamp.toInstant();
        return Duration.between(epoch, timestampInstant);
    }

    static OffsetDateTime toOffsetDateTime(Object from, Converter converter) {
        Timestamp timestamp = (Timestamp) from;

        // Get the current date-time in the options ZoneId timezone
        ZonedDateTime zonedDateTime = ZonedDateTime.now(converter.getOptions().getZoneId());

        // Extract the ZoneOffset
        ZoneOffset zoneOffset = zonedDateTime.getOffset();

        return timestamp.toInstant().atOffset(zoneOffset);
    }
}