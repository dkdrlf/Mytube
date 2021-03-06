package Mytube.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.sun.corba.se.impl.orbutil.RepositoryIdUtility;

import Mytube.vo.User;
import Mytube.vo.myLibrary;



public class database {
	
	
	/**
	 * 동영상을 볼수있는 등록
	 * @param category 동영상의 주제
	 * @param title 동영상 제목
	 * @param url 동영상의 유튜브 url 주소
	 */
	public boolean insertTube(int category, String title, String url, User user){
		boolean result =false;
		String sql = "insert into tube values(seq_num.nextval,?,?,?,?)";
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection con = cm.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, category);
			pstmt.setString(2, title);
			pstmt.setString(3, url);
			pstmt.setString(4, user.getName());
			pstmt.executeUpdate();
			System.out.println("삽입 완료");
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cm.close(con);
		}
		return result;
	}
	/**
	 * DB에서 제목을 통한 삭제를 위한 메소드
	 * @param title : 동영상 제목
	 */
	public boolean deleteTube(String title, User user){
		boolean result = false;
		String sql = "delete from tube where title = ? and name=?";
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection con = cm.getConnection();
		try {
			PreparedStatement pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, user.getName());
			pstmt.executeUpdate();
			result = true;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			cm.close(con);
		}
		return result;
	}
	/**
	 * 검색어를 통한 검색 메소드 제목들을 검색
	 * @param text : 검색어
	 * @return 검색결과 리스트를 보내주기
	 */
	public ArrayList<myLibrary> searchTube(String text, User user){
		ArrayList<myLibrary> result=new ArrayList<>();

		String sql = "select * from tube where name = ? and title like '%'||?||'%'";
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection con = cm.getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getName());
			pstmt.setString(2, text);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String title = rs.getString("title");
				int category = rs.getInt("category");
				result.add(new myLibrary(category,title));
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
	 * @return url 정보를 넘겨줌
	 */
	public String showTube(String title,User user){
		String address=null;
		System.out.println(user.getName());
		String sql = "select * from tube where title = ? and name = ?";
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection con = cm.getConnection();
		PreparedStatement pstmt;
		System.out.println("여기");
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, title);
			pstmt.setString(2, user.getName());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next())
			{
				address = rs.getString("url");
				System.out.println("데이터베이스어드레스"+address);
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cm.close(con);
		}
		return address;
	}
	/**
	 * DB에 있는 모든 객체들을 가지고옴.
	 * @return myLibrary가 담겨있는 arraylist를 보내기.
	 */
	public ArrayList<myLibrary> showAllTube(User user){
		ArrayList<myLibrary> allList = new ArrayList<>();
		String sql = "select * from tube where name = ?";
		ConnectionManager cm = ConnectionManager.getInstance();
		Connection con = cm.getConnection();
		PreparedStatement pstmt;
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, user.getName());
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				int category = rs.getInt("category");
				String title = rs.getString("title");
				allList.add(new myLibrary(category, title));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			cm.close(con);
		}
		return allList;
	}
	public boolean checkUser(String name){
	      boolean result = false;
	      String sql = "select * from tube_user where name = ?";
	      ConnectionManager cm = ConnectionManager.getInstance();
	      Connection con = cm.getConnection();
	      PreparedStatement pstmt;
	      try {
	         pstmt = con.prepareStatement(sql);
	         pstmt.setString(1, name);
	         pstmt.executeQuery();
	         ResultSet rs = pstmt.getResultSet();
	         if(rs.getRow()==0)
	         {
	        	 result=true;
	         }
	      } catch (SQLException e) {
	         e.printStackTrace();
	      } finally {
	         cm.close(con);
	      }
	      return result;
	   }
	
	public boolean login(User user)
	{
		boolean result =false;
		boolean b=checkUser(user.getName());
		if(b)
		{
			String sql="select password from tube_user where name=?";
			ConnectionManager cm= ConnectionManager.getInstance();
			Connection con=cm.getConnection();
			PreparedStatement pstmt;
			try {
				pstmt=con.prepareStatement(sql);
				pstmt.setString(1, user.getName());
				pstmt.executeQuery();
				ResultSet rs=pstmt.getResultSet();
				if(rs.next())
				{
					result=true;
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				cm.close(con);
			}
			
		}
		return result;
	}
	public boolean join(User user)
	{
		boolean result =false;
		boolean b=checkUser(user.getName());
		if(b)
		{
			String sql = "insert into tube_user values(?,?)";
			ConnectionManager cm = ConnectionManager.getInstance();
			Connection con = cm.getConnection();
			try {
				PreparedStatement pstmt = con.prepareStatement(sql);
				pstmt.setString(1, user.getName());
				pstmt.setString(2, user.getPassword());
				pstmt.executeUpdate();
				System.out.println("삽입 완료");
				result = true;
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				cm.close(con);
			}
		}
		return result;
	}
	
}
