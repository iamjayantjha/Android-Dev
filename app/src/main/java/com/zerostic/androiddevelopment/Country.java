package com.zerostic.androiddevelopment;

public class Country {
    String countryName, countryCurrency;

    public Country(String countryName, String countryCurrency) {
        this.countryName = countryName;
        this.countryCurrency = countryCurrency;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCurrency() {
        return countryCurrency;
    }

    public void setCountryCurrency(String countryCurrency) {
        this.countryCurrency = countryCurrency;
    }
}