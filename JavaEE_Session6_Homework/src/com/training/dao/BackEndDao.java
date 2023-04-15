package com.training.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.training.model.Goods;
import com.training.vo.SalesReport;
import com.training.dao.DBConnectionFactory;

public class BackEndDao {
	
	private static BackEndDao backendDao = new BackEndDao();
	
	private BackEndDao(){ }

	public static BackEndDao getInstance(){
		return backendDao;
	}
	
	/**
	 * 後臺管理商品列表
	 * @return Set(Goods)
	 */
	public Set<Goods> queryGoods() {
		Set<Goods> goods = new LinkedHashSet<>(); 
		String querySQL = "SELECT * FROM BEVERAGE_GOODS";
		try(Connection conn=DBConnectionFactory.getOracleDBConnection();
			Statement stmt=conn.createStatement();
			ResultSet rs=stmt.executeQuery(querySQL)){
				while(rs.next()){
					Goods good= new Goods();
					good.setGoodsID(rs.getInt("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setGoodsImageName(rs.getString("IMAGE_NAME"));
					good.setStatus(rs.getString("STATUS"));
					goods.add(good);					
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return goods;
		
		
	}
	
	public List<Goods> queryGoodById(String TotalID){
		List<Goods> Goods = new ArrayList<Goods>();		
		//SQL 語法 : select * from BEVERAGE_GOODS where GOODS_NAME = 'coke_original';
		String querySQL = "SELECT GOODS_ID, GOODS_NAME, PRICE, QUANTITY, STATUS FROM BEVERAGE_GOODS WHERE GOODS_ID IN ";
		//補查詢字串
		querySQL += TotalID;
		// Step1:取得Connection
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
		    // Step2:Create prepareStatement For SQL
			PreparedStatement stmt = conn.prepareStatement(querySQL)){
			
			try(ResultSet rs = stmt.executeQuery()){
				while(rs.next()){
					Goods good = new Goods();
					good.setGoodsID(rs.getInt("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setStatus(rs.getString("STATUS"));
					Goods.add(good);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return Goods;
	}
	
	/**
	 * 後臺管理新增商品
	 * @param goods
	 * @return int(商品編號)
	 */
	public boolean createGoods(Goods goods){
		boolean createSuccess = false;
		try(Connection conn = DBConnectionFactory.getOracleDBConnection()){
			conn.setAutoCommit(false);
			String insertSQL="INSERT INTO BEVERAGE_GOODS (GOODS_ID,GOODS_NAME,PRICE,QUANTITY,IMAGE_NAME,STATUS) VALUES (BEVERAGE_GOODS_SEQ.NEXTVAL,?,?,?,?,?)";
			
			try(PreparedStatement pstmt = conn.prepareStatement(insertSQL)){
				
				pstmt.setString(1, goods.getGoodsName());
				pstmt.setInt(2, goods.getGoodsPrice());
				pstmt.setInt(3, goods.getGoodsQuantity());
				pstmt.setString(4, goods.getGoodsImageName());
				pstmt.setString(5, goods.getStatus());			
				int count=pstmt.executeUpdate();
				
				createSuccess = (count>0) ? true : false;
				conn.commit();
								
			}catch(SQLException e){
				conn.rollback();
				throw e;
			}
			conn.commit();
		} catch (SQLException e) {
			createSuccess = false;
			e.printStackTrace();
		}
		return createSuccess ;
	}
	
	/**
	 * 後臺管理更新商品
	 * @param goods
	 * @return boolean
	 */
	public boolean updateGoods(Goods goods) {
		boolean updateSuccess = false;
		try (Connection conn= DBConnectionFactory.getOracleDBConnection()){
			conn.setAutoCommit(false);
			String updateSql="UPDATE BEVERAGE_GOODS SET PRICE=?,QUANTITY=QUANTITY+?,STATUS=? WHERE GOODS_ID=?";
			
			try(PreparedStatement stmt=conn.prepareStatement(updateSql)){
//				stmt.setString(1, goods.getGoodsName());
				stmt.setInt(1, goods.getGoodsPrice());
				stmt.setInt(2, goods.getGoodsQuantity());
//				stmt.setString(4, goods.getGoodsImageName());
				stmt.setString(3, goods.getStatus());
				stmt.setInt(4, goods.getGoodsID());				
				int count=stmt.executeUpdate();
				
				if(count==0){
					updateSuccess=false;
				}else{
					updateSuccess=true;
				}
									
			}catch(SQLException e){
				conn.rollback();
				throw e;				
			}
			conn.commit();
		} catch (SQLException e) {		
			e.printStackTrace();
		}
		
		return updateSuccess;
	}
	
	/**
	 * 後臺管理刪除商品
	 * @param goodsID
	 * @return boolean
	 */
	public boolean deleteGoods(BigDecimal goodsID) {
		boolean deleteSuccess = false;
		
		try(Connection conn=DBConnectionFactory.getOracleDBConnection()){
			conn.setAutoCommit(false);
			String deleteSql="DELETE FROM BEVERAGE_GOODS WHERE GOODS_ID=?";
			
			try(PreparedStatement stmt=conn.prepareStatement(deleteSql)){
				stmt.setBigDecimal(1, goodsID);
				int count=stmt.executeUpdate();
				
				if(count==0){
					deleteSuccess=false;
				}else{
					deleteSuccess=true;
				}
								
			}catch(SQLException e){
				conn.rollback();
				throw e;
			}
			conn.commit();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return deleteSuccess;
	}
	
	/**
	 * 後臺管理顧客訂單查詢
	 * @param queryStartDate
	 * @param queryEndDate
	 * @return Set(SalesReport)
	 */
	public Set<SalesReport> queryOrderBetweenDate(String queryStartDate, String queryEndDate) {
		Set<SalesReport> reports = new LinkedHashSet<>();
		String querySQL = "select O.ORDER_ID, O.ORDER_DATE, O.BUY_QUANTITY, O.GOODS_BUY_PRICE," 
				 + " M.CUSTOMER_NAME, G.GOODS_NAME,"
				 + " O.BUY_QUANTITY*O.GOODS_BUY_PRICE AS BUY_AMOUNT"
				 + " from BEVERAGE_ORDER O"
				 + " inner JOIN BEVERAGE_MEMBER M"
				 + " on O.CUSTOMER_ID = M.IDENTIFICATION_NO" 
				 + " inner JOIN BEVERAGE_GOODS G"
				 + " on O.GOODS_ID = G.GOODS_ID"
				 + " where O.ORDER_DATE BETWEEN TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')"
				 + " AND TO_TIMESTAMP(?, 'YYYY-MM-DD HH24:MI:SS')"
				 + " ORDER BY O.ORDER_ID";

		try(Connection conn=DBConnectionFactory.getOracleDBConnection();
			PreparedStatement stmt=conn.prepareStatement(querySQL)){
			queryStartDate += " 00:00:00";
			queryEndDate += " 23:59:59";
			stmt.setString(1, queryStartDate);
			stmt.setString(2, queryEndDate);
			
			try(ResultSet rs=stmt.executeQuery()){
				while(rs.next()){
					SalesReport report= new SalesReport();
					report.setOrderID(rs.getInt("ORDER_ID"));
					report.setCustomerName(rs.getString("CUSTOMER_NAME"));
					report.setOrderDate(rs.getTimestamp("ORDER_DATE"));
					report.setGoodsName(rs.getString("GOODS_NAME"));
					report.setGoodsBuyPrice(rs.getInt("GOODS_BUY_PRICE"));
					report.setBuyQuantity(rs.getInt("BUY_QUANTITY"));
					report.setBuyAmount(rs.getInt("BUY_AMOUNT"));
										
					reports.add(report);					
					}
				}catch(SQLException e){
					throw e;
				}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		return reports;
	}	
	
}
