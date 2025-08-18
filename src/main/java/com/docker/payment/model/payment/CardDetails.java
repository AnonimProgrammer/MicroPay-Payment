package com.docker.payment.model.payment;

public class CardDetails implements PaymentDetails {
    private String number;
    private String cvv;
    private String expirationMonth;
    private String expirationYear;

    public CardDetails() {
    }
    public CardDetails(String number, String cvv, String expirationMonth, String expirationYear) {
        this.number = number;
        this.cvv = cvv;
        this.expirationMonth = expirationMonth;
        this.expirationYear = expirationYear;
    }

    public String getNumber() {
        return number;
    }
    public void setNumber(String number) {
        this.number = number;
    }
    public String getCvv() {
        return cvv;
    }
    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
    public String getExpirationMonth() {
        return expirationMonth;
    }
    public void setExpirationMonth(String expirationMonth) {
        this.expirationMonth = expirationMonth;
    }
    public String getExpirationYear() {
        return expirationYear;
    }
    public void setExpirationYear(String expirationYear) {
        this.expirationYear = expirationYear;
    }

}
