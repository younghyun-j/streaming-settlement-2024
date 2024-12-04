package com.streaming.settlement.streamingsettlement.settlement.domain;

public enum ContentRevenueRange {

    // 영상 단가
    CONTENT_UNDER_100K(1, 0L, 99_999L),
    CONTENT_UNDER_500K(1.1, 100_000L, 500_000L),
    CONTENT_UNDER_1M(1.3, 500_000L, 1_000_000L),
    CONTENT_OVER_1M(1.5, 1_000_000L, Long.MAX_VALUE);

    private final double pricePerView;
    private final long minViews;
    private final long maxViews;

    ContentRevenueRange(double pricePerView, long minViews, long maxViews) {
        this.pricePerView = pricePerView;
        this.minViews = minViews;
        this.maxViews = maxViews;
    }

    public static Long calculateRevenueByViews(long totalViews) {
        long remainingViews = totalViews;
        long totalRevenue = 0;

        for (ContentRevenueRange contentRevenueRange : values()) {
            if (remainingViews <= 0) {
                break;
            }

            long range = contentRevenueRange.maxViews - contentRevenueRange.minViews;
            long views = Math.min(range, remainingViews);

            if (views > 0) {
                totalRevenue += (long) (views * contentRevenueRange.pricePerView);
                remainingViews -= views;
            }
        }
        return totalRevenue;
    }


}
