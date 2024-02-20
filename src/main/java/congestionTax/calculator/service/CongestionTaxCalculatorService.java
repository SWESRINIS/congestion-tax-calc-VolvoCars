package congestionTax.calculator.service;

import congestionTax.calculator.DateTimeUtil;
import congestionTax.calculator.entity.CityEntity;
import congestionTax.calculator.entity.TariffEntity;
import congestionTax.calculator.entity.VehicleEntity;
import congestionTax.calculator.exception.GlobalException;
import congestionTax.calculator.model.TaxCalculatorResponse;
import congestionTax.calculator.model.Vehicle;
import congestionTax.calculator.repository.CityRepository;
import congestionTax.calculator.repository.VehicleRepository;
import lombok.SneakyThrows;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class CongestionTaxCalculatorService {

    private CityRepository cityRepository;
    private VehicleRepository vehicleRepository;


    public CongestionTaxCalculatorService(CityRepository cityRepository, VehicleRepository vehicleRepository) {
        this.cityRepository = cityRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public TaxCalculatorResponse getTax(Vehicle vehicle, List<Date> dates, String city)
    {
        Map<String, BigDecimal> chargerHistoryPerDay = new HashMap<>();
        CityEntity cityEntity = cityRepository.findByName(city).get();

        /** check for tax exempt vehicle */
        if(isTollFreeVehicle(cityEntity.getTaxExemptVehicles(), vehicle)) return TaxCalculatorResponse.builder().taxAmount(new BigDecimal(0)).build();
        if(dates == null || dates.isEmpty()) return TaxCalculatorResponse.builder().taxAmount(new BigDecimal(0)).build();

        DateTimeUtil.sortDateByAsc(dates);

        /** remove weekends, public holidays, days before or after a public holiday as per configuration and during the holiday month*/
        dates.removeIf(date -> IsTollFreeDate(date, cityEntity));

        /** calculate by single charge rule */
        Map<String, List<BigDecimal>> chargesPerDay = getSingleChargeRule(dates, cityEntity);

        /** calculate for total charge */
        BigDecimal totalFee = calculateTotalTaxBySingleChargeRule(chargerHistoryPerDay, cityEntity, chargesPerDay);

        return TaxCalculatorResponse.builder().taxAmount(totalFee).chargesHistoryByDate(chargerHistoryPerDay).build();
    }

    private BigDecimal calculateTotalTaxBySingleChargeRule(Map<String, BigDecimal> chargerHistoryPerDay, CityEntity cityEntity, Map<String, List<BigDecimal>> chargesPerDay) {
        BigDecimal totalFee = new BigDecimal(0);
        for (Map.Entry<String, List<BigDecimal>> entry : chargesPerDay.entrySet()) {
            BigDecimal totalChargePerDay = entry.getValue().stream().reduce(BigDecimal.ZERO, BigDecimal::add);
            if(cityEntity.getTaxRuleEntity() != null &&
                    cityEntity.getTaxRuleEntity().getMaxTaxPerDay() != null &&
                    totalChargePerDay.compareTo(cityEntity.getTaxRuleEntity().getMaxTaxPerDay()) == 1)
                totalChargePerDay = cityEntity.getTaxRuleEntity().getMaxTaxPerDay();
            chargerHistoryPerDay.put(entry.getKey(), totalChargePerDay);
            totalFee = totalFee.add(totalChargePerDay);
        }
        return totalFee;
    }

    /*

     */

    @SneakyThrows
    private Map<String, List<BigDecimal>> getSingleChargeRule(List<Date> dates, CityEntity cityEntity) {
        List<Date> visitedSlots = new ArrayList<>();
        Map<String, List<BigDecimal>> result = new HashMap<>();
        for(int start = 0; start< dates.size(); start++) {
            if(visitedSlots.contains(dates.get(start))) continue;
            BigDecimal charge = getTollFeeByTariffAndDate(dates.get(start), cityEntity.getTariffEntities());
            for (int end = start + 1; end < dates.size(); end++) {
                long duration  = dates.get(end).getTime() - dates.get(start).getTime();
                long diffInMinutes = TimeUnit.MILLISECONDS.toMinutes(duration);
                if(diffInMinutes <= cityEntity.getTaxRuleEntity().getSingleChargeIntervalInMin()) {
                    visitedSlots.add(dates.get(end));
                    BigDecimal temp = getTollFeeByTariffAndDate(dates.get(end), cityEntity.getTariffEntities());
                    if(temp.compareTo(charge) == 1) charge = temp;
                } else break;
            }
            ConstructChargesByDate(dates, result, start, charge);
        }
        return result;
    }

    private void ConstructChargesByDate(List<Date> dates, Map<String, List<BigDecimal>> result, int start, BigDecimal charge) {
        String dateString = DateTimeUtil.removeTime(dates.get(start));
        List<BigDecimal> chargeLists;
        if(result.containsKey(dateString)) {
            chargeLists = result.get(dateString);
        } else {
            chargeLists = new ArrayList<>();
        }
        chargeLists.add(charge);
        result.put(dateString, chargeLists);
    }

    private BigDecimal getTollFeeByTariffAndDate(Date date, Set<TariffEntity> tariffs) {
        BigDecimal totalFee = new BigDecimal(0);
        if(tariffs == null || tariffs.isEmpty()) return totalFee;

        for (TariffEntity tariffEntity : tariffs) {
            LocalTime fromTime = tariffEntity.getFromTime();
            LocalTime toTime = tariffEntity.getToTime();
            LocalTime source = date.toInstant().atZone(ZoneId.systemDefault()).toLocalTime();
            if(!source.isBefore(fromTime) && source.isBefore(toTime)) {
                return totalFee.add(tariffEntity.getCharge());
            }
        }

        return totalFee;
    }

    private boolean isTollFreeVehicle(Set<VehicleEntity> taxExemptVehicles, Vehicle vehicle) {
        if (taxExemptVehicles == null) return false;
        if(taxExemptVehicles.stream()
                .filter(taxExemptVehicle -> taxExemptVehicle.getName().equalsIgnoreCase(vehicle.getType())).count() > 0) return true;
        return false;
    }

    private Boolean IsTollFreeDate(Date date, CityEntity cityEntity)
    {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH)+1;
        int day = cal.get(Calendar.DAY_OF_WEEK);

        if (DateTimeUtil.isWeekend(cityEntity.getTaxDaysEntity(), day)) return true;
        if (DateTimeUtil.isHolidayMonth(cityEntity.getNoTaxMonthEntity(), month)) return true;
        if(DateTimeUtil.isPerOrPostOrInPublicHoliday(date, cityEntity)) return true;

        return false;
    }

    public void isValidCity(String city) throws GlobalException {


        if(cityRepository.findByName(city).isEmpty()) {
            throw new GlobalException("City not found in our records. Please enter a valid City!", HttpStatus.NOT_FOUND);
        }
    }

    public void isValidVehicle(Vehicle vehicle) throws GlobalException {
        if(vehicleRepository.findByName(vehicle.getType()).isEmpty()) {
            throw new GlobalException("Vehicle type not found in our records. Please enter a valid Vehicle!", HttpStatus.NOT_FOUND);
        }
    }
}