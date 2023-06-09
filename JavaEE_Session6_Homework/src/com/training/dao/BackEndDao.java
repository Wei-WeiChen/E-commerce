package com.training.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.training.model.Goods;
import com.training.service.FrontEndService;
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
	public int createGoods(Goods goods){
		int goodsID = 0;
		String insert_stmt = "INSERT INTO BEVERAGE_GOODS "
				+ "(GOODS_ID,GOODS_NAME,PRICE,QUANTITY,IMAGE_NAME,STATUS) "
				+ "VALUES (BEVERAGE_GOODS_seq.NEXTVAL, ?, ?, ?, ?, ?)";
		String cols[] = { "GOODS_ID" };
		
		try (Connection con = DBConnectionFactory.getOracleDBConnection();
				PreparedStatement pstmt = con.prepareStatement(insert_stmt, cols)) {
			pstmt.setString(1, goods.getGoodsName());
			pstmt.setInt(2, goods.getGoodsPrice());
			pstmt.setInt(3, goods.getGoodsQuantity());
			pstmt.setString(4, goods.getGoodsImageName());
			pstmt.setString(5, goods.getStatus());
			pstmt.executeUpdate();
			
			ResultSet rsKeys = pstmt.getGeneratedKeys();
			ResultSetMetaData rsmd = rsKeys.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (rsKeys.next()) {
				for (int i = 1; i <= columnCount; i++) {
					rsKeys.getString(i);					
					goodsID=rsKeys.getInt(i);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return goodsID;
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
			String updateSql="UPDATE BEVERAGE_GOODS SET PRICE=?,QUANTITY=?,STATUS=? WHERE GOODS_ID=?";
			
			try(PreparedStatement stmt=conn.prepareStatement(updateSql)){
//				stmt.setString(1, goods.getGoodsName());
				stmt.setInt(1, goods.getGoodsPrice());
				stmt.setInt(2, goods.getGoodsQuantity());
//				stmt.setString(4, goods.getGoodsImageName());
				stmt.setString(3, goods.getStatus());
				stmt.setInt(4, goods.getGoodsID());				
				
				int count=stmt.executeUpdate();
				updateSuccess = (count>0) ? true : false;
									
			}catch(SQLException e){
				conn.rollback();
				throw e;				
			}
			conn.commit();
		} catch (SQLException e) {
			updateSuccess=false;
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
				deleteSuccess = (count > 0) ? true : false;
								
			}catch(SQLException e){
				conn.rollback();
				throw e;
			}
			conn.commit();
		}catch(SQLException e){
			deleteSuccess=false;
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

	public Set<Goods> queryAllGoods() {
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

	public List<Goods> returnPage(int page) {
		List<Goods> Goods = new ArrayList<Goods>();
		String returnSQL = "SELECT * FROM" 
						+" (SELECT ROWNUM ROW_NUM,GOODS_ID,GOODS_NAME,PRICE,QUANTITY,IMAGE_NAME,STATUS"
						+" FROM BEVERAGE_GOODS"
						+" WHERE GOODS_ID IS NOT NULL)"
						+" WHERE ROW_NUM BETWEEN ? AND ?";
		try(Connection conn=DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt=conn.prepareStatement(returnSQL)	){
				stmt.setInt(1, (page-1)*10+1);
				stmt.setInt(2, page*10);
				try(ResultSet rs=stmt.executeQuery()){
					while(rs.next()){
						Goods good =new Goods();
						good.setGoodsID(rs.getInt("GOODS_ID"));
						good.setGoodsName(rs.getString("GOODS_NAME"));
						good.setGoodsPrice(rs.getInt("PRICE"));
						good.setGoodsQuantity(rs.getInt("QUANTITY"));
						good.setGoodsImage("DrinksImage/"+rs.getString("IMAGE_NAME"));
						good.setStatus(rs.getString("STATUS"));
						Goods.add(good);
					}
				}catch(SQLException e){
					throw e;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return Goods;
	}

	public int goodsIcon() {
		String countSQL = "SELECT COUNT(*)COUNT FROM BEVERAGE_GOODS G WHERE G.GOODS_ID IS NOT NULL";
		int count = 0;
		try(Connection conn=DBConnectionFactory.getOracleDBConnection();
				PreparedStatement stmt=conn.prepareStatement(countSQL)	){
				
				try(ResultSet rs=stmt.executeQuery()){
					while(rs.next()){
						count = rs.getInt("count");
					}
				}catch(SQLException e){
					throw e;
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		return count;
	}

	//AJAX
	public Goods queryGoodsById(String id) {
		Goods good = null;		
		// querySQL SQL
		String querySQL = "SELECT GOODS_ID, GOODS_NAME, PRICE, QUANTITY, STATUS FROM BEVERAGE_GOODS WHERE GOODS_ID = ?";		
		// Step1:取得Connection
		try (Connection conn = DBConnectionFactory.getOracleDBConnection();
		    // Step2:Create prepareStatement For SQL
			PreparedStatement stmt = conn.prepareStatement(querySQL)){
			stmt.setString(1, id);
			try(ResultSet rs = stmt.executeQuery()){
				if(rs.next()){
					good = new Goods();
					good.setGoodsID(rs.getInt("GOODS_ID"));
					good.setGoodsName(rs.getString("GOODS_NAME"));
					good.setGoodsPrice(rs.getInt("PRICE"));
					good.setGoodsQuantity(rs.getInt("QUANTITY"));
					good.setStatus(rs.getString("STATUS"));
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return good;
	}
	
	
}
