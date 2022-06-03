package io.swagger.model.ResponseDTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class SpendResponseDTO {
    @JsonProperty("spend" )
    private BigDecimal spendMoney;

    public SpendResponseDTO(BigDecimal SUM) {
        this.spendMoney = SUM;
    }

    public BigDecimal getSpendMoney() {
        return spendMoney;
    }

    public void setSpendMoney(BigDecimal spendMoney) {
        this.spendMoney = spendMoney;
    }
}
