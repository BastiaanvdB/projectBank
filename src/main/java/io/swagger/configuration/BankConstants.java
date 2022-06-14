package io.swagger.configuration;

import java.math.BigDecimal;

public final class BankConstants {

    public static final BigDecimal DEFAULT_ACCOUNT_BALANCE = new BigDecimal(0);
    public static final BigDecimal DEFAULT_ACCOUNT_ABSOLUTE_LIMIT = new BigDecimal(20);
    public static final Boolean DEFAULT_ACCOUNT_ACTIVATION = true;
    public static final String IBAN_BANK = "NL01INHO0000000001";
    public static final String EMAIL_BANK = "bank@live.nl";
    public static final String IBAN_COUNTRY_PREFIX = "NL";
    public static final String IBAN_BANK_PREFIX = "INHO0";
    public static final String REGEX_NUMBERS_ONLY = "[0-9]+";

    private BankConstants() {

    }
}
