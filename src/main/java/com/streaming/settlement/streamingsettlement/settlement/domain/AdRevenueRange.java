package com.streaming.settlement.streamingsettlement.settlement.domain;

public enum AdRevenueRange {

    // 광고 단가
    AD_UNDER_100K(10, 0L, 99_999L),
    AD_UNDER_500K(12, 100_000L, 500_000L),
    AD_UNDER_1M(15, 500_000L, 1_000_000L),
    AD_OVER_1M(20, 1_000_000L, Long.MAX_VALUE);

    private final double pricePerView;
    private final long minViews;
    private final long maxViews;

    AdRevenueRange(double pricePerView, long minViews, long maxViews) {
        this.pricePerView = pricePerView;
        this.minViews = minViews;
        this.maxViews = maxViews;
    }

    public static Long calculateRevenueByViews(long totalViews) {
        long remainingViews = totalViews;
        long totalRevenue = 0;

        for (AdRevenueRange adRevenueRange : values()) {
            if (remainingViews <= 0) {
                break;
            }

            long range = adRevenueRange.maxViews - adRevenueRange.minViews;
            long views = Math.min(range, remainingViews);

            if (views > 0) {
                totalRevenue += (long) (views * adRevenueRange.pricePerView);
                remainingViews -= views;
            }
        }
        return totalRevenue;
    }
}
