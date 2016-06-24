package com.sample;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;


public class JdbcGenSelectDao implements GenSelectDao {
	
	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private class InsertSqlRowMapper<T> implements RowMapper<T> {

		public final String COMMA_SEP = ", ";
		@SuppressWarnings("unchecked")
		public  T mapRow(ResultSet rs, int arg1) throws SQLException {
			ResultSetMetaData rsMetaData =  rs.getMetaData();
			int numberOfColoumns = rsMetaData.getColumnCount();
			StringBuffer sbColNames = new StringBuffer();
			StringBuffer sbColValues = new StringBuffer();
			StringBuffer sbPrefix = new StringBuffer();
			StringBuffer sbInsert = new StringBuffer();
			boolean prefixPrepared = false;
			
			//insert into table_name(col1_name, col2_name, ...) values(val1, val2,...)
			for(int columnIndex=1; columnIndex <= numberOfColoumns; columnIndex++)
			{
				String tableName = rsMetaData.getTableName(columnIndex);
				if(!prefixPrepared){
					sbPrefix.append("insert into ").append(tableName).append(" (");
					prefixPrepared = true;
				}
				
				String columnName = rsMetaData.getColumnName(columnIndex);
				sbColNames.append(columnName).append(COMMA_SEP);
				
				int coloumnType = rsMetaData.getColumnType(columnIndex);
				Object value = rs.getObject(columnIndex);
				
				//sbColValues
				if(java.sql.Types.VARCHAR == coloumnType || 
						java.sql.Types.CHAR == coloumnType ||
						java.sql.Types.DATE == coloumnType ||
						java.sql.Types.TIMESTAMP == coloumnType)
				{
					String strValue = null;
					if(value != null){
						strValue = value.toString();
						sbColValues.append("'").append(strValue).append("'").append(COMMA_SEP);
					}else{
						sbColValues.append(value).append(COMMA_SEP);
					}
					
					
				} else {
					sbColValues.append(value).append(COMMA_SEP);
				}
			}
			
			if(sbColNames.length() > 0 ){
				sbColNames.setLength(sbColNames.length() - COMMA_SEP.length());
			}
			if(sbColValues.length() > 0 ){
				sbColValues.setLength(sbColValues.length() - COMMA_SEP.length());
			}
			
			sbInsert.append(sbPrefix).append(sbColNames).append(") VALUES ( ").append(sbColValues).append(" )");
			
			return (T) sbInsert.toString();
		}
		
	}
	
	public List<String> getAllSqlQueries(String inputSelectSQL) {
		
		List<String> insertSQLs = jdbcTemplate.query(inputSelectSQL, new InsertSqlRowMapper<String>());
		return insertSQLs;
	}


	
}
