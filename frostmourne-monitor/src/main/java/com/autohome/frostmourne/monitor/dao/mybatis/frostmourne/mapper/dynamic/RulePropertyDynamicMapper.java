package com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.mapper.dynamic;

import static com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.mapper.dynamic.RulePropertyDynamicSqlSupport.*;
import static org.mybatis.dynamic.sql.SqlBuilder.*;

import com.autohome.frostmourne.monitor.dao.mybatis.frostmourne.domain.generate.RuleProperty;
import java.util.List;
import java.util.Optional;
import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.apache.ibatis.type.JdbcType;
import org.mybatis.dynamic.sql.BasicColumn;
import org.mybatis.dynamic.sql.delete.DeleteDSLCompleter;
import org.mybatis.dynamic.sql.delete.render.DeleteStatementProvider;
import org.mybatis.dynamic.sql.insert.render.InsertStatementProvider;
import org.mybatis.dynamic.sql.select.CountDSLCompleter;
import org.mybatis.dynamic.sql.select.SelectDSLCompleter;
import org.mybatis.dynamic.sql.select.render.SelectStatementProvider;
import org.mybatis.dynamic.sql.update.UpdateDSL;
import org.mybatis.dynamic.sql.update.UpdateDSLCompleter;
import org.mybatis.dynamic.sql.update.UpdateModel;
import org.mybatis.dynamic.sql.update.render.UpdateStatementProvider;
import org.mybatis.dynamic.sql.util.SqlProviderAdapter;
import org.mybatis.dynamic.sql.util.mybatis3.MyBatis3Utils;

@Mapper
public interface RulePropertyDynamicMapper {
    BasicColumn[] selectList = BasicColumn.columnList(id, alarmId, ruleId, propKey, propValue, creator, createAt);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    long count(SelectStatementProvider selectStatement);

    @DeleteProvider(type=SqlProviderAdapter.class, method="delete")
    int delete(DeleteStatementProvider deleteStatement);

    @InsertProvider(type=SqlProviderAdapter.class, method="insert")
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="record.id", before=false, resultType=Long.class)
    int insert(InsertStatementProvider<RuleProperty> insertStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @ResultMap("RulePropertyResult")
    Optional<RuleProperty> selectOne(SelectStatementProvider selectStatement);

    @SelectProvider(type=SqlProviderAdapter.class, method="select")
    @Results(id="RulePropertyResult", value = {
        @Result(column="id", property="id", jdbcType=JdbcType.BIGINT, id=true),
        @Result(column="alarm_id", property="alarmId", jdbcType=JdbcType.BIGINT),
        @Result(column="rule_id", property="ruleId", jdbcType=JdbcType.BIGINT),
        @Result(column="prop_key", property="propKey", jdbcType=JdbcType.VARCHAR),
        @Result(column="prop_value", property="propValue", jdbcType=JdbcType.VARCHAR),
        @Result(column="creator", property="creator", jdbcType=JdbcType.VARCHAR),
        @Result(column="create_at", property="createAt", jdbcType=JdbcType.TIMESTAMP)
    })
    List<RuleProperty> selectMany(SelectStatementProvider selectStatement);

    @UpdateProvider(type=SqlProviderAdapter.class, method="update")
    int update(UpdateStatementProvider updateStatement);

    default long count(CountDSLCompleter completer) {
        return MyBatis3Utils.countFrom(this::count, ruleProperty, completer);
    }

    default int delete(DeleteDSLCompleter completer) {
        return MyBatis3Utils.deleteFrom(this::delete, ruleProperty, completer);
    }

    default int deleteByPrimaryKey(Long id_) {
        return delete(c -> 
            c.where(id, isEqualTo(id_))
        );
    }

    default int insert(RuleProperty record) {
        return MyBatis3Utils.insert(this::insert, record, ruleProperty, c ->
            c.map(alarmId).toProperty("alarmId")
            .map(ruleId).toProperty("ruleId")
            .map(propKey).toProperty("propKey")
            .map(propValue).toProperty("propValue")
            .map(creator).toProperty("creator")
            .map(createAt).toProperty("createAt")
        );
    }

    default int insertSelective(RuleProperty record) {
        return MyBatis3Utils.insert(this::insert, record, ruleProperty, c ->
            c.map(alarmId).toPropertyWhenPresent("alarmId", record::getAlarmId)
            .map(ruleId).toPropertyWhenPresent("ruleId", record::getRuleId)
            .map(propKey).toPropertyWhenPresent("propKey", record::getPropKey)
            .map(propValue).toPropertyWhenPresent("propValue", record::getPropValue)
            .map(creator).toPropertyWhenPresent("creator", record::getCreator)
            .map(createAt).toPropertyWhenPresent("createAt", record::getCreateAt)
        );
    }

    default Optional<RuleProperty> selectOne(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectOne(this::selectOne, selectList, ruleProperty, completer);
    }

    default List<RuleProperty> select(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectList(this::selectMany, selectList, ruleProperty, completer);
    }

    default List<RuleProperty> selectDistinct(SelectDSLCompleter completer) {
        return MyBatis3Utils.selectDistinct(this::selectMany, selectList, ruleProperty, completer);
    }

    default Optional<RuleProperty> selectByPrimaryKey(Long id_) {
        return selectOne(c ->
            c.where(id, isEqualTo(id_))
        );
    }

    default int update(UpdateDSLCompleter completer) {
        return MyBatis3Utils.update(this::update, ruleProperty, completer);
    }

    static UpdateDSL<UpdateModel> updateAllColumns(RuleProperty record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(alarmId).equalTo(record::getAlarmId)
                .set(ruleId).equalTo(record::getRuleId)
                .set(propKey).equalTo(record::getPropKey)
                .set(propValue).equalTo(record::getPropValue)
                .set(creator).equalTo(record::getCreator)
                .set(createAt).equalTo(record::getCreateAt);
    }

    static UpdateDSL<UpdateModel> updateSelectiveColumns(RuleProperty record, UpdateDSL<UpdateModel> dsl) {
        return dsl.set(alarmId).equalToWhenPresent(record::getAlarmId)
                .set(ruleId).equalToWhenPresent(record::getRuleId)
                .set(propKey).equalToWhenPresent(record::getPropKey)
                .set(propValue).equalToWhenPresent(record::getPropValue)
                .set(creator).equalToWhenPresent(record::getCreator)
                .set(createAt).equalToWhenPresent(record::getCreateAt);
    }

    default int updateByPrimaryKey(RuleProperty record) {
        return update(c ->
            c.set(alarmId).equalTo(record::getAlarmId)
            .set(ruleId).equalTo(record::getRuleId)
            .set(propKey).equalTo(record::getPropKey)
            .set(propValue).equalTo(record::getPropValue)
            .set(creator).equalTo(record::getCreator)
            .set(createAt).equalTo(record::getCreateAt)
            .where(id, isEqualTo(record::getId))
        );
    }

    default int updateByPrimaryKeySelective(RuleProperty record) {
        return update(c ->
            c.set(alarmId).equalToWhenPresent(record::getAlarmId)
            .set(ruleId).equalToWhenPresent(record::getRuleId)
            .set(propKey).equalToWhenPresent(record::getPropKey)
            .set(propValue).equalToWhenPresent(record::getPropValue)
            .set(creator).equalToWhenPresent(record::getCreator)
            .set(createAt).equalToWhenPresent(record::getCreateAt)
            .where(id, isEqualTo(record::getId))
        );
    }
}