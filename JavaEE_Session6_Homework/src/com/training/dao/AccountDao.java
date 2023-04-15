package com.training.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.training.model.Account;

public class AccountDao {
	
	private static AccountDao accountDao = new AccountDao();
	private AccountDao(){}
	public static AccountDao getInstance(){
		return accountDao;
	}
	
	public Account queryAccountById(String id){
		Account account = null;
		String memberSQL = "SELECT IDENTIFICATION_NO, PASSWORD, CUSTOMER_NAME FROM BEVERAGE_MEMBER WHERE IDENTIFICATION_NO = ?";
		try(Connection conn = DBConnectionFactory.getOracleDBConnection();	
			PreparedStatement stmt = conn.prepareStatement(memberSQL)){
			stmt.setString(1, id);
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()){
					account = new Account();
					account.setId(rs.getString("IDENTIFICATION_NO"));
					account.setPwd(rs.getString("PASSWORD"));
					account.setName(rs.getString("CUSTOMER_NAME"));
				}
			}catch(SQLException e){
				e.printStackTrace();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return account;
	}
}
