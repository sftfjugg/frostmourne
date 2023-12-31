package com.autohome.frostmourne.monitor.service.core.query.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.joda.time.DateTime;
import org.springframework.stereotype.Service;

import com.autohome.frostmourne.common.contract.ProtocolException;
import com.autohome.frostmourne.monitor.model.contract.DataNameContract;
import com.autohome.frostmourne.monitor.model.contract.DataSourceContract;
import com.autohome.frostmourne.monitor.model.contract.ElasticsearchDataResult;
import com.autohome.frostmourne.monitor.service.admin.IDataAdminService;
import com.autohome.frostmourne.monitor.service.core.query.IElasticsearchDataQuery;
import com.autohome.frostmourne.monitor.service.core.query.IQueryService;
import com.google.common.base.Splitter;
import com.google.common.collect.Range;
import com.google.common.collect.RangeMap;
import com.google.common.collect.TreeRangeMap;

import au.com.bytecode.opencsv.CSVWriter;

@Service
public class QueryService implements IQueryService {

    private static RangeMap<Long, Integer> rangeMap = TreeRangeMap.create();

    static {
        rangeMap.put(Range.closed(0L, 3600 * 1000L), 5 * 60);
        rangeMap.put(Range.closed(3600 * 1000L + 1, 3 * 3600 * 1000L), 10 * 60);
        rangeMap.put(Range.closed(3 * 3600 * 1000L + 1, 6 * 3600 * 1000L), 15 * 60);
        rangeMap.put(Range.closed(6 * 3600 * 1000L + 1, 12 * 3600 * 1000L), 30 * 60);
        rangeMap.put(Range.closed(12 * 3600 * 1000L + 1, 24 * 3600 * 1000L), 60 * 60);
        rangeMap.put(Range.closed(24 * 3600 * 1000L + 1, 3 * 24 * 3600 * 1000L), 2 * 60 * 60);
        rangeMap.put(Range.closed(3 * 24 * 3600 * 1000L + 1, 5 * 24 * 3600 * 1000L), 6 * 60 * 60);
        rangeMap.put(Range.closed(5 * 24 * 3600 * 1000L + 1, 7 * 24 * 3600 * 1000L), 24 * 60 * 60);
        rangeMap.put(Range.greaterThan(7 * 24 * 3600 * 1000L), 24 * 60 * 60);
    }

    @Resource
    private IElasticsearchDataQuery elasticsearchDataQuery;

    @Resource
    private IDataAdminService dataAdminService;

    @Override
    public ElasticsearchDataResult elasticsearchQuery(String dataName, Date startTime, Date endTime, String esQuery, String scrollId, String sortOrder,
        Integer intervalInSeconds) {
        if (intervalInSeconds == null || intervalInSeconds == 0) {
            Long timeGap = endTime.getTime() - startTime.getTime();
            intervalInSeconds = rangeMap.get(timeGap);
        }
        DataNameContract dataNameContract = dataAdminService.findDataNameByName(dataName);
        DataSourceContract dataSourceContract = dataAdminService.findDatasourceById(dataNameContract.getDataSourceId());
        return elasticsearchDataQuery.query(dataNameContract, dataSourceContract, new DateTime(startTime), new DateTime(endTime), esQuery, scrollId, sortOrder,
            intervalInSeconds);
    }

    @Override
    public List<String> elasticsearchFields(String dataName) {
        DataNameContract dataNameContract = dataAdminService.findDataNameByName(dataName);
        DataSourceContract dataSourceContract = dataAdminService.findDatasourceById(dataNameContract.getDataSourceId());
        return elasticsearchDataQuery.queryMappingFileds(dataNameContract, dataSourceContract);
    }

    @Override
    public void exportToCsv(CSVWriter csvWriter, String dataName, DateTime startTime, DateTime endTime, String esQuery, String scrollId, String sortOrder) {
        DataNameContract dataNameContract = dataAdminService.findDataNameByName(dataName);
        DataSourceContract dataSourceContract = dataAdminService.findDatasourceById(dataNameContract.getDataSourceId());
        ElasticsearchDataResult elasticsearchDataResult =
            elasticsearchDataQuery.query(dataNameContract, dataSourceContract, startTime, endTime, esQuery, scrollId, sortOrder, null);
        String[] heads = elasticsearchDataResult.getFlatFields().toArray(new String[0]);
        csvWriter.writeNext(heads);
        while (true) {
            if (elasticsearchDataResult.getTotal() > 10 * 10000) {
                throw new ProtocolException(500, "数量总数超过10万,无法下载");
            }
            if (elasticsearchDataResult.getLogs().size() == 0) {
                break;
            }
            for (Map<String, Object> log : elasticsearchDataResult.getLogs()) {
                String[] data = Arrays.stream(heads).map(h -> {
                    Object value = findFieldValue(log, h);
                    return value == null ? null : value.toString();
                }).toArray(String[]::new);
                csvWriter.writeNext(data);
            }
            scrollId = elasticsearchDataResult.getScrollId();
            elasticsearchDataResult =
                elasticsearchDataQuery.query(dataNameContract, dataSourceContract, startTime, endTime, esQuery, scrollId, sortOrder, null);
        }
    }

    private Object findFieldValue(Map<String, Object> sourceMap, String flatFieldName) {
        List<String> fields = Splitter.on(".").splitToList(flatFieldName);
        Map<String, Object> currentMap = sourceMap;
        int size = fields.size();
        for (int i = 0; i < size; i++) {
            if (i == size - 1) {
                return currentMap.get(fields.get(i));
            }
            currentMap = (Map<String, Object>)currentMap.get(fields.get(i));
        }
        return null;
    }
}
