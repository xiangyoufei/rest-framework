package com.example.demo.jdbc.dao.impl;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

import com.example.demo.jdbc.dao.DataAccessor;
import com.example.demo.jdbc.paging.Paging;
import com.example.demo.jdbc.paging.TotalCountHolder;

/**
 * 数据访问器接口默认实现（基于 MyBatis）
 *
 * @author huangyong
 * @since 1.0.0
 */
public class DefaultDataAccessor implements DataAccessor {

    private SqlSession sqlSession;

    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public <T> T selectOne(String statementId) {
        checkStatementId(statementId);
        return sqlSession.selectOne(statementId);
    }

    @Override
    public <T> T selectOne(String statementId, Object sqlParam) {
        checkStatementId(statementId);
        checkParameter(sqlParam);
        return sqlSession.selectOne(statementId, sqlParam);
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public <T> List<T> selectList(String statementId) {
        checkStatementId(statementId);
        return sqlSession.selectList(statementId);
    }

    @Override
    public <T> List<T> selectList(String statementId, Object sqlParam) {
        checkStatementId(statementId);
        checkParameter(sqlParam);
        return sqlSession.selectList(statementId, sqlParam);
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public <K, V> Map<K, V> selectMap(String statementId, String mapKey) {
        checkStatementId(statementId);
        checkMapKey(mapKey);
        return sqlSession.selectMap(statementId, mapKey);
    }

    @Override
    public <K, V> Map<K, V> selectMap(String statementId, Object sqlParam, String mapKey) {
        checkStatementId(statementId);
        checkParameter(sqlParam);
        checkMapKey(mapKey);
        return sqlSession.selectMap(statementId, sqlParam, mapKey);
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public <T> Paging<T> selectPaging(String statementId, int pageNumber, int pageSize) {
        checkStatementId(statementId);
        checkPageNumber(pageNumber);
        checkPageSize(pageNumber);
        return doSelectPaging(statementId, null, pageNumber, pageSize);
    }

    @Override
    public <T> Paging<T> selectPaging(String statementId, Object sqlParam, int pageNumber, int pageSize) {
        checkStatementId(statementId);
        checkParameter(sqlParam);
        checkPageNumber(pageNumber);
        checkPageSize(pageNumber);
        return doSelectPaging(statementId, sqlParam, pageNumber, pageSize);
    }

    private <T> Paging<T> doSelectPaging(String statementId, Object sqlParam, int pageNumber, int pageSize) {
        RowBounds rowBounds = createRowBounds(pageNumber, pageSize);
        List<T> itemList = sqlSession.selectList(statementId, sqlParam, rowBounds);
        try {
            int totalCount = TotalCountHolder.get();
            return new Paging<>(pageNumber, pageSize, totalCount, itemList);
        } finally {
            TotalCountHolder.remove();
        }
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public int insert(String statementId) {
        checkStatementId(statementId);
        return sqlSession.insert(statementId);
    }

    @Override
    public int insert(String statementId, Object sqlParam) {
        checkStatementId(statementId);
        checkParameter(sqlParam);
        return sqlSession.insert(statementId, sqlParam);
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public int update(String statementId) {
        checkStatementId(statementId);
        return sqlSession.update(statementId);
    }

    @Override
    public int update(String statementId, Object sqlParam) {
        checkStatementId(statementId);
        checkParameter(sqlParam);
        return sqlSession.update(statementId, sqlParam);
    }

    // ----------------------------------------------------------------------------------------------------

    @Override
    public int delete(String statementId) {
        checkStatementId(statementId);
        return sqlSession.delete(statementId);
    }

    @Override
    public int delete(String statementId, Object sqlParam) {
        checkStatementId(statementId);
        checkParameter(sqlParam);
        return sqlSession.delete(statementId, sqlParam);
    }

    // ----------------------------------------------------------------------------------------------------

    private void checkStatementId(String statementId) {
        if (StringUtils.isEmpty(statementId)) {
            throw new IllegalArgumentException("argument [statementId] is empty");
        }
    }

    private void checkParameter(Object sqlParam) {
        if (sqlParam == null) {
            throw new IllegalArgumentException("argument [sqlParam] is null");
        }
    }

    private void checkPageNumber(int pageNumber) {
        if (pageNumber < 1) {
            throw new IllegalArgumentException("argument [pageNumber] is invalid: " + pageNumber);
        }
    }

    private void checkPageSize(int pageSize) {
        if (pageSize <= 0) {
            throw new IllegalArgumentException("argument [pageSize] is invalid: " + pageSize);
        }
    }

    private void checkMapKey(String mapKey) {
        if (StringUtils.isEmpty(mapKey)) {
            throw new IllegalArgumentException("argument [mapKey] is empty");
        }
    }

    private RowBounds createRowBounds(int pageNumber, int pageSize) {
        int offset = (pageNumber - 1) * pageSize;
        return new RowBounds(offset, pageSize);
    }
}
