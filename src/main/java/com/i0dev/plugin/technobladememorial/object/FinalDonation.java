package com.i0dev.plugin.technobladememorial.object;

import lombok.Data;

import java.util.UUID;

@Data
public class FinalDonation {


    DonationTier tier;
    UUID donationUUID;
    UUID donorUUID;
    long timestamp;
    double amountUSD;
    String message;



}
