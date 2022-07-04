package com.i0dev.plugin.technobladememorial.object;

public enum DonationTier {

    CUSTOM(-1),
    DIAMOND(20),
    GOLD(5),
    IRON(1);

    final int amount;

    DonationTier(int amount) {
        this.amount = amount;
    }

    public int getAmount() {
        return amount;
    }
}
