package com.i0dev.plugin.technobladememorial.object;

import lombok.Data;

import java.util.UUID;

@Data
public class QueuedDonation {

    DonationTier tier;
    UUID donorUUID;
    UUID donationUUID;
    long timestamp;
    double amountUSD;


}
