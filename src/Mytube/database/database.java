package Mytube.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.corba.se.impl.orbutil.RepositoryIdUtility;



public class database {
	
	
	/**
	 * 동영상을 볼수있는 등록
	 * @param category 동영상의 주제
	 * @param title 동영상 제목
	 * @param url 동영상의 유튜브 url 주소
	 */
	public void insertTube(int category, String title, String url){
		String sql = "insert into tube values(seq_num.nextval,?,?,?)";
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection con = cm.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, category);
			pstmt.setString(2, title);
			pstmt.setString(3, url);
			pstmt.executeUpdate();
			System.out.println("삽입 완료");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cm.close(con);
		}
	}
	/**
	 * DB에서 제목을 통한 삭제를 위한 메소드
	 * @param title : 동영상 제목
	 */
	public void deleteTube(String title){
		String sql = "delete from tube where title = ?";
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection con = cm.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.executeUpdate();
			System.out.println("삭제 성공");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cm.close(con);
		}
	}
	/**
	 * 검색어를 통한 검색 메소드 제목들을 검색
	 * @param text : 검색어
	 */
	public ArrayList<String> searchTube(String text){
		ArrayList<String> result=null;

		String sql = "select * from tube where title = '%?%'";
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection con = cm.getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, text);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				String title = rs.getString("title");
				result.add(title);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cm.close(con);
		}
		return result;
	}
	/**
	 * 제목을 검색하여 url 정보를 가지고 옴.
	 * @param title
	 * @return
	 */
	public String showTube(String title){
		String address=null;
		String sql = "select * from tube where title = ?";
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection con = cm.getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title);
			ResultSet rs = pstmt.executeQuery();
			address = rs.getString("url");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cm.close(con);
		}
		return address;
	}
	
}