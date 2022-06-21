package io.swagger.configuration;

import io.swagger.model.enumeration.Role;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public final class BankConstants {

    public static final BigDecimal DEFAULT_ACCOUNT_BALANCE = new BigDecimal(0);
    public static final BigDecimal DEFAULT_ACCOUNT_ABSOLUTE_LIMIT = new BigDecimal(20);
    public static final Boolean DEFAULT_ACCOUNT_ACTIVATION = true;
    public static final String IBAN_BANK = "NL01INHO0000000001";
    public static final String EMAIL_BANK = "bank@live.nl";
    public static final String IBAN_COUNTRY_PREFIX = "NL";
    public static final String IBAN_BANK_PREFIX = "INHO0";
    public static final String REGEX_NUMBERS_ONLY = "[0-9]+";

    public static final BigDecimal DEFAULT_DAY_LIMIT = new BigDecimal(200);

    public static final BigDecimal DEFAULT_TRANSACTION_LIMIT = new BigDecimal(1000);

    public static final List<Role> DEFAULT_ROLE = new ArrayList<>(List.of(Role.ROLE_USER));

    private BankConstants() {

    }
}
