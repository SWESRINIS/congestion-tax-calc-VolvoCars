package congestionTax.calculator.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Builder
public class TaxCalculatorResponse {

    private BigDecimal taxAmount;
    private Map<String, BigDecimal> chargesHistoryByDate;
}
