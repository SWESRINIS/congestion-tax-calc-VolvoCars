package congestionTax.calculator;

import congestionTax.calculator.entity.CityEntity;
import congestionTax.calculator.entity.NoTaxDaysEntity;
import congestionTax.calculator.entity.NoTaxMonthEntity;
import congestionTax.calculator.entity.TaxDaysEntity;
import org.apache.commons.lang3.time.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateTimeUtil {

    public static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat dateAndTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


    public static Boolean isWeekend(TaxDaysEntity taxDaysEntity, int day) {
        if(taxDaysEntity == null) return false;

        if(taxDaysEntity.isMonday() == false && day == Calendar.MONDAY) return false;
        if(taxDaysEntity.isTuesday() == false && day == Calendar.TUESDAY) return false;
        if(taxDaysEntity.isWednesday() == false && day == Calendar.WEDNESDAY) return false;
        if(taxDaysEntity.isThursday() == false && day == Calendar.THURSDAY) return false;
        if(taxDaysEntity.isFriday() == false && day == Calendar.FRIDAY) return false;
        if(taxDaysEntity.isSaturday() == false && day == Calendar.SATURDAY) return true;
        if(taxDaysEntity.isSunday() == false && day == Calendar.SUNDAY) return true;

        return false;
    }

    public static Boolean isHolidayMonth(NoTaxMonthEntity noTaxMonthEntity, int month) {
        if(noTaxMonthEntity == null) return false;

        if(noTaxMonthEntity.isJanuary() == true && month == (Calendar.JANUARY+1)) return false;
        if(noTaxMonthEntity.isFebruary() == true && month == (Calendar.FEBRUARY+1)) return false;
        if(noTaxMonthEntity.isMarch() == true && month == (Calendar.MARCH+1)) return false;
        if(noTaxMonthEntity.isApril() == true && month == (Calendar.APRIL+1)) return false;
        if(noTaxMonthEntity.isMay() == true && month == (Calendar.MAY+1)) return false;
        if(noTaxMonthEntity.isJune() == true && month == (Calendar.JUNE+1)) return false;
        if(noTaxMonthEntity.isJuly() == true && month == (Calendar.JULY+1)) return true;
        if(noTaxMonthEntity.isAugust() == true && month == (Calendar.AUGUST+1)) return false;
        if(noTaxMonthEntity.isSeptember() == true && month == (Calendar.SEPTEMBER+1)) return false;
        if(noTaxMonthEntity.isOctober() == true && month == (Calendar.OCTOBER+1)) return false;
        if(noTaxMonthEntity.isNovember() == true && month == (Calendar.NOVEMBER+1)) return false;
        if(noTaxMonthEntity.isDecember() == true && month == (Calendar.DECEMBER+1)) return false;

        return false;
    }

    public static Boolean isPerOrPostOrInPublicHoliday(Date date, CityEntity cityEntity) {
        Set<NoTaxDaysEntity> publicHolidays = cityEntity.getHolidayCalendarEntities();
        if(publicHolidays == null || publicHolidays.isEmpty()) return false;

        if (publicHolidays.stream().filter(holiday -> DateUtils.isSameDay(holiday.getDate(), date)).count() > 0 ) return true;

        if (publicHolidays.stream()
                .filter(holiday -> isDateInBetweenIncludingEndPoints(
                        holiday.getDate(),
                        DateUtils.addDays(holiday.getDate(), cityEntity.getTaxRuleEntity().getNumberOfTaxFreeDaysAfterHoliday()),
                        date
                ))
                .count() > 0 ) return true;

        if (publicHolidays.stream()
                .filter(holiday -> isDateInBetweenIncludingEndPoints(
                        DateUtils.addDays(holiday.getDate(), -(cityEntity.getTaxRuleEntity().getNumberOfTaxFreeDaysBeforeHoliday())),
                        holiday.getDate(),
                        date
                ))
                .count() > 0 ) return true;
        return false;
    }

    public static boolean isDateInBetweenIncludingEndPoints(final Date min, final Date max, final Date date){
        return !(date.before(min) || date.after(max));
    }

    public static String removeTime(Date date) {
        return dateFormat.format(date);
    }

    public static Date objectToDate(Object date) throws ParseException {
        if(date instanceof String)
            return dateAndTimeFormat.parse(date.toString());
        else
            return dateAndTimeFormat.parse(dateAndTimeFormat.format(((Date)date)));
    }

    public static void sortDateByAsc(List<Date> dates) {
        Collections.sort(dates, new Comparator<Date>() {
            @Override
            public int compare(Date object1, Date object2) {
                return object1.compareTo(object2);
            }
        });
    }
}
