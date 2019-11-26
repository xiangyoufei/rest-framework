package com.example.demo.core.mybatis.sqlHandler;

import org.apache.ibatis.executor.result.ResultMapException;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ListTypeHanlder implements TypeHandler<List<?>> {
    private static final String TYPE_NAME_VARCHAR = "varchar";
    private static final String TYPE_NAME_INTEGER = "integer";
    private static final String TYPE_NAME_BOOLEAN = "boolean";
    private static final String TYPE_NAME_NUMERIC = "numeric";

    @Override
    public void setParameter(PreparedStatement ps, int i, List<?> parameter, JdbcType jdbcType) throws SQLException {
        if (parameter == null) {
            try {
                ps.setNull(i, JdbcType.ARRAY.TYPE_CODE);
            } catch (SQLException e) {
                throw new TypeException("Error setting null for parameter #" + i + " with JdbcType " + jdbcType + " . "
                        + "Try setting a different JdbcType for this parameter or a different jdbcTypeForNull configuration property. "
                        + "Cause: " + e, e);
            }
        } else {
            try {
                Array array = ps.getConnection().createArrayOf(jdbcType.name(), parameter.toArray());
                ps.setArray(i, array);
            } catch (Exception e) {
                throw new TypeException("Error setting non null for parameter #" + i + " with JdbcType " + jdbcType
                        + " . "
                        + "Try setting a different JdbcType for this parameter or a different configuration property. "
                        + "Cause: " + e, e);
            }
        }

       /* String typeName = null;
        if (parameter instanceof Integer[]) {
            typeName = TYPE_NAME_INTEGER;
        } else if (parameter instanceof String[]) {
            typeName = TYPE_NAME_VARCHAR;
        } else if (parameter instanceof Boolean[]) {
            typeName = TYPE_NAME_BOOLEAN;
        } else if (parameter instanceof Double[]) {
            typeName = TYPE_NAME_NUMERIC;
        }

        if (typeName == null) {
            throw new TypeException("ArrayTypeHandler parameter typeName error, your type is " + parameter.getClass().getName());
        }
        Connection conn = ps.getConnection();
// 这3行是关键的代码，创建Array，然后ps.setArray(i, array)就可以了        Connection conn = ps.getConnection();
        Array array = conn.createArrayOf(typeName, parameter);
        ps.setArray(i, array);
    }*/

}

    @Override
    public List<?> getResult(ResultSet rs, String columnName) throws SQLException {
        List<?> result;
        try {
            Array array = rs.getArray(columnName);
            result = array == null ? null : new ArrayList<>(Arrays.asList((Object[]) array.getArray()));
        } catch (Exception e) {
            throw new ResultMapException(
                    "Error attempting to get column '" + columnName + "' from result list.  Cause: " + e, e);
        }
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public List<?> getResult(ResultSet rs, int columnIndex) throws SQLException {
        List<?> result;
        try {
            Array array = rs.getArray(columnIndex);
            result = array == null ? null : new ArrayList<>(Arrays.asList((Object[]) array.getArray()));
        } catch (Exception e) {
            throw new ResultMapException(
                    "Error attempting to get column #" + columnIndex + " from result list.  Cause: " + e, e);
        }
        if (rs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    public List<?> getResult(CallableStatement cs, int columnIndex) throws SQLException {
        List<?> result;
        try {
            Array array = cs.getArray(columnIndex);
            result = array == null ? null : new ArrayList<>(Arrays.asList((Object[]) array.getArray()));
        } catch (Exception e) {
            throw new ResultMapException(
                    "Error attempting to get column #" + columnIndex + " from callable statement.  Cause: " + e, e);
        }
        if (cs.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

}