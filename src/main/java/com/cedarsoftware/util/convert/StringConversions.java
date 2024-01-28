package com.cedarsoftware.util.convert;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.MonthDay;
import java.time.ZonedDateTime;
import java.time.temporal.TemporalAccessor;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.cedarsoftware.util.ClassUtilities;
import com.cedarsoftware.util.DateUtilities;
import com.cedarsoftware.util.StringUtilities;

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
public class StringConversions {
    private static final BigDecimal bigDecimalMinByte = BigDecimal.valueOf(Byte.MIN_VALUE);
    private static final BigDecimal bigDecimalMaxByte = BigDecimal.valueOf(Byte.MAX_VALUE);
    private static final BigDecimal bigDecimalMinShort = BigDecimal.valueOf(Short.MIN_VALUE);
    private static final BigDecimal bigDecimalMaxShort = BigDecimal.valueOf(Short.MAX_VALUE);
    private static final BigDecimal bigDecimalMinInteger = BigDecimal.valueOf(Integer.MIN_VALUE);
    private static final BigDecimal bigDecimalMaxInteger = BigDecimal.valueOf(Integer.MAX_VALUE);
    private static final BigDecimal bigDecimalMaxLong = BigDecimal.valueOf(Long.MAX_VALUE);
    private static final BigDecimal bigDecimalMinLong = BigDecimal.valueOf(Long.MIN_VALUE);

    static Byte toByte(Object from, Converter converter, ConverterOptions options) {
        return toByte((String)from);
    }

    private static Byte toByte(String s) {
        if (s.isEmpty()) {
            return CommonValues.BYTE_ZERO;
        }
        try {
            return Byte.valueOf(s);
        } catch (NumberFormatException e) {
            Long value = toLong(s, bigDecimalMinByte, bigDecimalMaxByte);
            if (value == null) {
                throw new IllegalArgumentException("Value: " + s + " not parseable as a byte value or outside " + Byte.MIN_VALUE + " to " + Byte.MAX_VALUE);
            }
            return value.byteValue();
        }
    }

    static Short toShort(Object from, Converter converter, ConverterOptions options) {
        return toShort(from);
    }

    private static Short toShort(Object o) {
        String str = StringUtilities.trimToEmpty((String)o);
        if (str.isEmpty()) {
            return CommonValues.SHORT_ZERO;
        }
        try {
            return Short.valueOf(str);
        } catch (NumberFormatException e) {
            Long value = toLong(str, bigDecimalMinShort, bigDecimalMaxShort);
            if (value == null) {
                throw new IllegalArgumentException("Value: " + o + " not parseable as a short value or outside " + Short.MIN_VALUE + " to " + Short.MAX_VALUE);
            }
            return value.shortValue();
        }
    }

    static Integer toInt(Object from, Converter converter, ConverterOptions options) {
        return toInt(from);
    }

    private static Integer toInt(Object from) {
        String str = StringUtilities.trimToEmpty((String)from);
        if (str.isEmpty()) {
            return CommonValues.INTEGER_ZERO;
        }
        try {
            return Integer.valueOf(str);
        } catch (NumberFormatException e) {
            Long value = toLong(str, bigDecimalMinInteger, bigDecimalMaxInteger);
            if (value == null) {
                throw new IllegalArgumentException("Value: " + from + " not parseable as an int value or outside " + Integer.MIN_VALUE + " to " + Integer.MAX_VALUE);
            }
            return value.intValue();
        }
    }

    static Long toLong(Object from, Converter converter, ConverterOptions options) {
        return toLong(from);
    }

    private static Long toLong(Object from) {
        String str = StringUtilities.trimToEmpty((String)from);
        if (str.isEmpty()) {
            return CommonValues.LONG_ZERO;
        }

        try {
            return Long.valueOf(str);
        } catch (NumberFormatException e) {
            Long value = toLong(str, bigDecimalMinLong, bigDecimalMaxLong);
            if (value == null) {
                throw new IllegalArgumentException("Value: " + from + " not parseable as a long value or outside " + Long.MIN_VALUE + " to " + Long.MAX_VALUE);
            }
            return value;
        }
    }

    private static Long toLong(String s, BigDecimal low, BigDecimal high) {
        try {
            BigDecimal big = new BigDecimal(s);
            big = big.setScale(0, RoundingMode.DOWN);
            if (big.compareTo(low) == -1 || big.compareTo(high) == 1) {
                return null;
            }
            return big.longValue();
        } catch (Exception e) {
            return null;
        }
    }

    static Float toFloat(Object from, Converter converter, ConverterOptions options) {
        String str = StringUtilities.trimToEmpty((String)from);
        if (str.isEmpty()) {
            return CommonValues.FLOAT_ZERO;
        }
        try {
            return Float.valueOf(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value: " + from + " not parseable as a float value");
        }
    }

    static Double toDouble(Object from, Converter converter, ConverterOptions options) {
        String str = StringUtilities.trimToEmpty((String)from);
        if (str.isEmpty()) {
            return CommonValues.DOUBLE_ZERO;
        }
        try {
            return Double.valueOf(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value: " + from + " not parseable as a double value");
        }
    }

    static AtomicBoolean toAtomicBoolean(Object from, Converter converter, ConverterOptions options) {
        return new AtomicBoolean(toBoolean((String)from));
    }

    static AtomicInteger toAtomicInteger(Object from, Converter converter, ConverterOptions options) {
        return new AtomicInteger(toInt(from));
    }

    static AtomicLong toAtomicLong(Object from, Converter converter, ConverterOptions options) {
        return new AtomicLong(toLong(from));
    }

    private static Boolean toBoolean(String from) {
        String str = StringUtilities.trimToEmpty(from);
        if (str.isEmpty()) {
            return false;
        }
        // faster equals check "true" and "false"
        if ("true".equals(str)) {
            return true;
        } else if ("false".equals(str)) {
            return false;
        }
        return "true".equalsIgnoreCase(str) || "t".equalsIgnoreCase(str) || "1".equals(str) || "y".equalsIgnoreCase(str);
    }

    static Boolean toBoolean(Object from, Converter converter, ConverterOptions options) {
        return toBoolean((String)from);
    }

    static char toCharacter(Object from, Converter converter, ConverterOptions options) {
        String str = StringUtilities.trimToNull((String)from);
        if (str == null) {
            return CommonValues.CHARACTER_ZERO;
        }
        if (str.length() == 1) {
            return str.charAt(0);
        }
        // Treat as a String number, like "65" = 'A'
        return (char) Integer.parseInt(str.trim());
    }

    static BigInteger toBigInteger(Object from, Converter converter, ConverterOptions options) {
        String str = StringUtilities.trimToNull((String)from);
        if (str == null) {
            return BigInteger.ZERO;
        }
        try {
            BigDecimal bigDec = new BigDecimal(str);
            return bigDec.toBigInteger();
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value: " + from + " not parseable as a BigInteger value.");
        }
    }

    static BigDecimal toBigDecimal(Object from, Converter converter, ConverterOptions options) {
        String str = StringUtilities.trimToEmpty((String)from);
        if (str.isEmpty()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(str);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Value: " + from + " not parseable as a BigDecimal value.");
        }
    }

    static String enumToString(Object from, Converter converter, ConverterOptions options) {
        return ((Enum<?>) from).name();
    }

    static UUID toUUID(Object from, Converter converter, ConverterOptions options) {
        return UUID.fromString(((String) from).trim());
    }

    static Duration toDuration(Object from, Converter converter, ConverterOptions options) {
        return Duration.parse((String) from);
    }

    static Class<?> toClass(Object from, Converter converter, ConverterOptions options) {
        String str = ((String) from).trim();
        Class<?> clazz = ClassUtilities.forName(str, options.getClassLoader());
        if (clazz != null) {
            return clazz;
        }
        throw new IllegalArgumentException("Cannot convert String '" + str + "' to class.  Class not found.");
    }

    static String classToString(Object from, Converter converter, ConverterOptions options) {
        return ((Class<?>) from).getName();
    }

    static MonthDay toMonthDay(Object from, Converter converter, ConverterOptions options) {
        String monthDay = (String) from;
        return MonthDay.parse(monthDay);
    }

    static Date toDate(Object from, Converter converter, ConverterOptions options) {
        Instant instant = getInstant((String) from, options);
        if (instant == null) {
            return null;
        }
        // Bring the zonedDateTime to a user-specifiable timezone
        return Date.from(instant);
    }

    static java.sql.Date toSqlDate(Object from, Converter converter, ConverterOptions options) {
        Instant instant = getInstant((String) from, options);
        if (instant == null) {
            return null;
        }
        Date date = Date.from(instant);
        // Bring the zonedDateTime to a user-specifiable timezone
        return new java.sql.Date(date.getTime());
    }

    static Timestamp toTimestamp(Object from, Converter converter, ConverterOptions options) {
        Instant instant = getInstant((String) from, options);
        if (instant == null) {
            return null;
        }
        // Bring the zonedDateTime to a user-specifiable timezone
        return Timestamp.from(instant);
    }

    static Calendar toCalendar(Object from, Converter converter, ConverterOptions options) {
        Instant instant = getInstant((String) from, options);
        if (instant == null) {
            return null;
        }
        Date date = Date.from(instant);
        return CalendarConversions.create(date.getTime(), options);
    }

    static LocalDate toLocalDate(Object from, Converter converter, ConverterOptions options) {
        Instant instant = getInstant((String) from, options);
        if (instant == null) {
            return null;
        }
        // Bring the LocalDate to a user-specifiable timezone
        return instant.atZone(options.getSourceZoneIdForLocalDates()).toLocalDate();
    }

    static LocalDateTime toLocalDateTime(Object from, Converter converter, ConverterOptions options) {
        Instant instant = getInstant((String) from, options);
        if (instant == null) {
            return null;
        }
        // Bring the LocalDateTime to a user-specifiable timezone
        return instant.atZone(options.getSourceZoneIdForLocalDates()).toLocalDateTime();
    }

    static LocalTime toLocalTime(Object from, Converter converter, ConverterOptions options) {
        String str = (String) from;
        if (StringUtilities.isEmpty(str)) {
            return null;
        }
        return LocalTime.parse(str);
    }

    static ZonedDateTime toZonedDateTime(Object from, Converter converter, ConverterOptions options) {
        Instant instant = getInstant((String) from, options);
        if (instant == null) {
            return null;
        }
        return ZonedDateTime.ofInstant(instant, options.getZoneId());
    }

    static Instant toInstant(Object from, Converter converter, ConverterOptions options) {
        String s = StringUtilities.trimToNull((String)from);
        if (s == null) {
            return null;
        }

        try {
            return Instant.parse(s);
        } catch (Exception e) {
            return getInstant(s, options);
        }
    }

    private static Instant getInstant(String from, ConverterOptions options) {
        String str = StringUtilities.trimToNull(from);
        if (str == null) {
            return null;
        }
        TemporalAccessor dateTime = DateUtilities.parseDate(str, options.getZoneId(), true);

        Instant instant;
        if (dateTime instanceof LocalDateTime) {
            LocalDateTime localDateTime = LocalDateTime.from(dateTime);
            instant = localDateTime.atZone(options.getZoneId()).toInstant();
        } else {
            instant = Instant.from(dateTime);
        }
        return instant;
    }

    static String toString(Object from, Converter converter, ConverterOptions options) {
        return from.toString();
    }
}
