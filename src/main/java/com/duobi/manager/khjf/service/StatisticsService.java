package com.duobi.manager.khjf.service;

import com.duobi.manager.khjf.dao.StatisticsDao;
import com.duobi.manager.khjf.entity.Statistics;
import com.duobi.manager.sys.utils.DataScopeUtils;
import com.duobi.manager.sys.utils.Global;
import com.duobi.manager.sys.utils.Page;
import com.duobi.manager.sys.utils.crud.CrudService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(value = "secondTransactionManager",rollbackFor=Exception.class)
public class StatisticsService extends CrudService<StatisticsDao,Statistics,Long> {

    public Page<Statistics> findPage(Page<Statistics> page, Statistics statistics){

        statistics.getSqlMap().put("dsf", DataScopeUtils.dataScopeFilter(Global.getKhjfModuleId(), statistics.getCurrentUserOrganization(), "o",null));

        page.setAutoPage(false);
        statistics.setPage(page);
        page.setCount(dao.findCount(statistics));
        if (page.getCount() > 0) {
            //获取到网点的以下属性：全部积分、客户数、账户数
            List<Statistics> statisticsList = dao.findList(statistics);
            //获取每个网点已经兑换了的积分数
            List<Statistics> orgConsumedPointsValueList = dao.getOrgConsumedPointsValue();
            for (Statistics s:statisticsList){
                if ("78001".equals(s.getOrgCode())) s.setOrgName("全行汇总");
                Double consumedPointsValue = 0D;
                for (Statistics cs : orgConsumedPointsValueList){
                    if (s.getOrgCode().equals(cs.getOrgCode())) {
                        consumedPointsValue = cs.getConsumedPointsValue();
                        break;
                    }
                }
                s.setConsumedPointsValue(consumedPointsValue);
            }

            page.setList(statisticsList);
        }
        return page;
    }

}
