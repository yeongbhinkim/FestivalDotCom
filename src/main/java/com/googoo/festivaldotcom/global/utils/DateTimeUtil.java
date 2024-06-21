package com.googoo.festivaldotcom.global.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.Date;

public class DateTimeUtil {

    // 폴더용 날짜 포맷 (년월일)
    private static final DateTimeFormatter folderFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");

    // 파일명용 시간 포맷 (년월일시분초)
    private static final DateTimeFormatter fileFormatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    // 문자열을 LocalDate로 변환하기 위한 포맷
    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 현재 시간을 폴더용 날짜 형식으로 포맷
     * @param now 현재 시간
     * @return 포맷된 폴더용 날짜 문자열
     */
    public static String formatDateForFolder(LocalDateTime now) {
        return now.format(folderFormatter);
    }

    /**
     * 현재 시간을 파일명용 시간 형식으로 포맷
     * @param now 현재 시간
     * @return 포맷된 파일명용 시간 문자열
     */
    public static String formatDateTimeForFileName(LocalDateTime now) {
        return now.format(fileFormatter);
    }

    /**
     * 문자열을 Date 객체로 변환하는 헬퍼 메서드입니다.
     *
     * @param dateStr 변환할 날짜 문자열
     * @return 변환된 Date 객체
     */
    public static Date convertToDate(String dateStr) {
        // DateTimeFormatter를 사용하여 문자열을 LocalDate로 변환합니다.
        LocalDate localDate = LocalDate.parse(dateStr, dateFormatter);
        // LocalDate를 Date로 변환하여 반환합니다.
        return Date.valueOf(localDate);
    }
}
