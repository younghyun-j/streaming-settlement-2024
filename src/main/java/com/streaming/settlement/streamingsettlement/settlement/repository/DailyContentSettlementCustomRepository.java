package com.streaming.settlement.streamingsettlement.settlement.repository;


import com.streaming.settlement.streamingsettlement.settlement.entity.DailyContentSettlement;

import java.util.List;

public interface DailyContentSettlementCustomRepository{
    void bulkInsertDailySettlement(List<DailyContentSettlement> dailyContentSettlements);
}
