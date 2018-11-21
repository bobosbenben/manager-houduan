package com.duobi.manager.khjf.controller;

import com.duobi.manager.khjf.entity.Statistics;
import com.duobi.manager.khjf.service.StatisticsService;
import com.duobi.manager.sys.utils.crud.CrudController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/khjf/statistics")
public class StatisticsController extends CrudController<StatisticsService,Statistics,Long> {


}
