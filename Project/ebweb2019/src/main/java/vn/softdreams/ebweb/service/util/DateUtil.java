package vn.softdreams.ebweb.service.util;

import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.util.Locale;
import java.util.Objects;

public class DateUtil {

    public static final String C_YYYY = "yyyy";
    public static final String C_MM = "MM";
    public static final String C_DD = "dd";
    public static final String C_DDMMYYYY = "ddMMyyyy";
    public static final String C_DD_MM_YYYY = "dd/MM/yyyy";
    public static final String C_D_MM_YYYY = "d/MM/yyyy";
    public static final String C_DD_M_YYYY = "dd/M/yyyy";
    public static final String C_D_M_YYYY = "d/M/yyyy";
    public static final String C_DDaMMaYYYY = "dd-MM-yyyy";
    public static final String C_DaMMaYYYY = "d-MM-yyyy";
    public static final String C_DDaMaYYYY = "dd-M-yyyy";
    public static final String C_DaMaYYYY = "d-M-yyyy";
    public static final String C_MMDDYYYY = "yyyyMMdd";
    public static final String C_YYYYMM = "yyyyMM";
    public static final String C_YYYYMMDD_HHMMSS = "yyyyMMddHHmmss";
    public static final String C_YYYY_MM_DD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
    public static final String C_YYYY_MM_DD_HHMM = "yyyy-MM-dd HH:mm";
    public static final String C_DD_MM_YYYY_HHMM = "dd/MM/yyyy HH:mm";
    public static final String C_YYYY_MM_DD = "YYYY-MM-DD";

    /**
     * @author haivv
     *
     * Chuyển đổi định dạng ngày tháng từ LocalDate sang String
     *
     * @param date ngày tháng kiểu LocalDate cần đổi
     * @param pattern định dạng ngày tháng muốn chuyển
     * @return ngày tháng kiểu String theo định dạng
     */
    public static String getStrByLocalDate(LocalDate date, String pattern) {
        if (Objects.isNull(date)) {
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(dateTimeFormatter);
    }

    /**
     * @author haivv
     *
     * Chuyển đổi định dạng ngày tháng từ LocalDateTime sang String
     *
     * @param date ngày tháng kiểu LocalDateTime cần đổi
     * @param pattern định dạng ngày tháng muốn chuyển
     * @return ngày tháng kiểu String theo định dạng
     */
    public static String getStrByLocalDateTime(LocalDateTime date, String pattern) {
        if (Objects.isNull(date)) {
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
        return date.format(dateTimeFormatter);
    }

    /**
     * @author haivv
     *
     * Chuyển đổi ngày tháng từ String sang LocalDateTime theo pattern
     *
     * @param date ngày tháng kiểu String
     * @param pattern định dạng ngày tháng truyển vào
     * @return ngày tháng theo kiểu LocalDateTime hoặc null nếu ngày truyền vào null
     */
    public static LocalDateTime getLocalDateTimeFromString(String date, String pattern) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            return LocalDateTime.parse(date, formatter);
        } catch (Exception ex) {

        }
        return null;
    }

    /**
     * @author haivv
     *
     * Chuyển đổi ngày tháng từ String sang LocalDate theo pattern
     *
     * @param date ngày tháng kiểu String
     * @param pattern định dạng ngày tháng truyển vào
     * @return ngày tháng theo kiểu LocalDate hoặc null nếu ngày truyền vào null
     */
    public static LocalDate getLocalDateFromString(String date, String pattern) {
        if (StringUtils.isBlank(date)) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        try {
            return LocalDate.parse(date, formatter);
        } catch (Exception ex) {

        }
        return null;
    }

    /**
     * @author haivv
     *
     * Chuyển đổi ngày tháng từ String sang Instant theo pattern
     *
     * @param date ngày tháng kiểu String
     * @param pattern định dạng ngày tháng truyền vào
     * @return ngày tháng theo kiểu Instant hoặc null nếu date truyền vào không đúng
     */
    public static Instant getInstantFromString(String date, String pattern) {
        if (StringUtils.isNotBlank(date)) {
            LocalDateTime localDateTime = getLocalDateTimeFromString(date, pattern);
            if (Objects.nonNull(localDateTime)) {
                return localDateTime.toInstant(ZoneOffset.UTC);
            }
            return null;
        }
        return null;
    }

    /**
     * @author haivv
     *
     * Chuyển đổi ngày tháng từ String sang chuẩn dd/MM/yyyy theo pattern
     *
     * @param date ngày tháng kiểu String
     * @param pattern định dạng ngày tháng truyền vào
     * @return ngày tháng theo chuẩn dd/MM/yyyy hoặc null nếu date truyền vào không đúng
     */
    public static String formatStandardDateFromString(String date, String pattern, String toPattern) {
        if (StringUtils.isNotBlank(date)) {
            LocalDate localDate = getLocalDateFromString(date, pattern);
            if (Objects.nonNull(localDate)) {
                if (Strings.isNullOrEmpty(toPattern)) {
                    return getStrByLocalDate(localDate, C_DDMMYYYY);
                } else {
                    return getStrByLocalDate(localDate, toPattern);
                }

            }
            return null;
        }
        return null;
    }

    /**
     * @author haivv
     *
     * Chuyển đổi định dạng ngày tháng từ Instant sang String
     *
     * @param date ngày tháng kiểu LocalDate cần đổi
     * @param pattern định dạng ngày tháng muốn chuyển
     * @return ngày tháng kiểu String theo định dạng
     */
    public static String getStrByInstant(Instant date, String pattern) {
        if (Objects.isNull(date)) {
            return "";
        }
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern)
            .withLocale(Locale.getDefault())
            .withZone(ZoneId.systemDefault());
        return dateTimeFormatter.format(date);
    }

    public static String getQuarterOfYear(String date) {
        if (Strings.isNullOrEmpty(date)) {
            return "";
        }
        if (date.length() == 6) {
            date += "01";
        }
        return date.substring(0, 4) + LocalDate.parse(date, DateTimeFormatter.ofPattern(C_DDMMYYYY)).get(IsoFields.QUARTER_OF_YEAR);
    }

    public static String plusQuarter(String date) {
        if (Strings.isNullOrEmpty(date) && date.length() != 5) {
            return "";
        }
        String month = (date.substring(4).equalsIgnoreCase("4") ? "" : "0") + Integer.parseInt(date.substring(4)) * 3;
        date = date.substring(0, 4) + month + "01";
        LocalDate newDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(C_DDMMYYYY)).plusMonths(3);
        return newDate.format(DateTimeFormatter.ofPattern(C_YYYY)) + newDate.get(IsoFields.QUARTER_OF_YEAR);
    }
}
