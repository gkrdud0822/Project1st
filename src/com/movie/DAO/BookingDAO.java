package com.movie.DAO;

import java.sql.Date;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.movie.VO.BookedseatVO;
import com.movie.VO.BookingVO;
import com.movie.VO.CinemaVO;
import com.movie.VO.DayseatVO;
import com.movie.VO.MovieVO;
import com.movie.VO.MovietimeVO;
import com.movie.VO.SeatVO;
import com.movie.main.AppManager;

public class BookingDAO {
	DAOManager daoManager = AppManager.getInstance().getDAOManager();

	String sql=null;
	ResultSet rs=null;

	public BookingDAO() {
		AppManager.getInstance().getDAOManager().setBookingDAO(this);
	}
	// 영화이름 리스트
	public Vector<MovieVO> movieNameList(){
		daoManager.connectDB();

		Vector<MovieVO> nameList=new Vector<MovieVO>();
		String sql="SELECT movie_code,movie_nameK,movie_img,age FROM MovieData ORDER BY movie_code";

		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);
			rs=daoManager.pt.executeQuery();
			while(rs.next()) {
				MovieVO m=new MovieVO();
				m.setMovie_code(rs.getInt("movie_code"));
				m.setMovie_nameK(rs.getString("movie_nameK"));
				m.setMovie_img(rs.getString("movie_img"));
				m.setAge(rs.getString("age"));
				nameList.add(m);
			} // while
			if(rs != null) rs.close();
			daoManager.closeDB();
		}catch(Exception e) {e.printStackTrace();}// try-catch		

		return nameList;
	}// movieNameList()
	// 영화관 리스트
	public Vector<CinemaVO> cinemaList(int movie_code){
		daoManager.connectDB();

		Vector<CinemaVO> cinemaList=new Vector<>();
		String sql="SELECT * FROM cinema WHERE movie_code=?";
		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);
			daoManager.pt.setInt(1,movie_code);
			rs=daoManager.pt.executeQuery();
			while(rs.next()) {
				CinemaVO m=new CinemaVO();
				m.setScreen(rs.getString("screen"));
				cinemaList.add(m);
			} // while
			if(rs != null) rs.close();
			daoManager.closeDB();
		}catch(Exception e) { e.printStackTrace(); }

		return cinemaList;
	}// cinemaList()

	// 영화 날짜, 영화 시간 리스트
	public List<MovietimeVO> dayTimeList(Date screendate,String screenNum){
		daoManager.connectDB();

		List<MovietimeVO> dayTimeList=new ArrayList<>();
		String sql="SELECT * FROM movietime WHERE screendate=? AND screen=?";
		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);
			daoManager.pt.setDate(1,screendate);
			daoManager.pt.setString(2,screenNum);
			rs=daoManager.pt.executeQuery();
			while(rs.next()) {
				MovietimeVO mt=new MovietimeVO();
				mt.setScreendate(rs.getString("screendate"));
				mt.setScreentime(rs.getString("screentime"));
				dayTimeList.add(mt);
			}// while
			if(rs != null) rs.close();
			daoManager.closeDB();
		}catch(Exception e) {e.printStackTrace();}

		return dayTimeList;
	}// DayTimeList()	

	// 영화 날짜'만' 구함
	public Vector<MovietimeVO> dayList(String screenNum){
		daoManager.connectDB();

		Vector<MovietimeVO> dayList=new Vector<>();
		String sql="SELECT screendate FROM movietime WHERE screen=?";
		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);
			daoManager.pt.setString(1,screenNum);
			rs=daoManager.pt.executeQuery();
			while(rs.next()) {
				MovietimeVO mt=new MovietimeVO();
				mt.setScreendate(rs.getString("screendate"));
				dayList.add(mt);
			}// while
		}catch(Exception e) {e.printStackTrace();}

		return dayList;
	}// dayList()

	// 상영관별 좌석 세팅
	public List<SeatVO> setScreenSeat(String screenNum){
		daoManager.connectDB();

		List<SeatVO> screenSeat=new ArrayList<>();
		String sql="SELECT * FROM seat WHERE screen=? ORDER BY seatrow";
		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);
			daoManager.pt.setString(1,screenNum);
			rs=daoManager.pt.executeQuery();
			while(rs.next()) {
				SeatVO seat=new SeatVO();
				seat.setSeat_Num(rs.getString("seat_Num"));
				seat.setSeatcol(rs.getString("seatcol"));
				seat.setSeatrow(rs.getString("seatrow"));
				screenSeat.add(seat);
			}// while
			if(rs != null) rs.close();
			daoManager.closeDB();
		}catch(Exception e) {e.printStackTrace();}

		return screenSeat;
	}// setScreenSeat

	// 예매 저장
	public int setBooking(BookingVO b) {
		daoManager.connectDB();

		int re=-1;
		String sql="INSERT INTO Booking VALUES(booked_seq.nextval,?,?,?,?,?)";
		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);
			daoManager.pt.setInt(1,b.getPrice());
			daoManager.pt.setInt(2,b.getSeatcount());
			daoManager.pt.setInt(3,b.getMovie_code());
			daoManager.pt.setString(4,b.getMember_id());
			daoManager.pt.setString(5,b.getTime_code());

			re=daoManager.pt.executeUpdate();

			daoManager.closeDB();
		}catch(Exception e) {e.printStackTrace();}

		return re;
	}// setBooking()
	// 예약코드 가져올 리스트
	public List<BookingVO> getBookingCode() {
		daoManager.connectDB();
		
		List<BookingVO> b_code=new ArrayList<>();
		String sql="SELECT booking_code FROM Booking ORDER BY booking_code";
		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);
			rs=daoManager.pt.executeQuery();
			while(rs.next()) {
				BookingVO bvo=new BookingVO();
				bvo.setBooking_code(rs.getInt("booking_code"));
				b_code.add(bvo);
			}// while
			
			if(rs != null) rs.close();
			daoManager.closeDB();
		}catch(Exception e) {e.printStackTrace();}
		
		return b_code;
	}// getBookingCode()

	// 예약된 좌석 저장
	public int setBookedSeat(BookedseatVO b,String[] seatArr) {
		daoManager.connectDB();

		int re=-1;
		String sql="INSERT INTO booked_seat VALUES(?,?,?)";
		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);

			for(int i=0;i<seatArr.length;i++) {
				b.setSeat_Num(b.getScreen()+seatArr[i]);

				daoManager.pt.setInt(1,b.getBooking_code());
				daoManager.pt.setString(2,b.getScreen());
				daoManager.pt.setString(3,b.getSeat_Num());

				re=daoManager.pt.executeUpdate();
			}// for

			daoManager.closeDB();
		}catch(Exception e) {e.printStackTrace();}

		return re;
	}// setBookedSeat()

	// seat_Num 가져올 셀렉트 리스트
	public List<BookedseatVO> bookingList(){
		daoManager.connectDB();

		List<BookedseatVO> seatNum=new ArrayList<>();
		String sql="SELECT * FROM booked_seat ORDER BY seat_Num";
		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);
			rs=daoManager.pt.executeQuery();
			while(rs.next()) {
				BookedseatVO bvo=new BookedseatVO();
				bvo.setSeat_Num(rs.getString("seat_Num"));
				seatNum.add(bvo);
			}// while

			if(rs != null) rs.close();
			daoManager.closeDB();
		}catch(Exception e) {e.printStackTrace();}

		return seatNum;
	}

	// 좌석 예매
	public int setBookingSeat(String[] seatArr,DayseatVO d) {
		daoManager.connectDB();

		int re=-1;
		String sql="UPDATE day_seat SET seat_status=1 WHERE seat_Num=? AND time_code=?";
		try {			
			daoManager.pt=daoManager.con.prepareStatement(sql);
			for(int i=0;i<seatArr.length;i++) {
				d.setSeat_Num(d.getScreen()+seatArr[i]);

				daoManager.pt.setString(1,d.getSeat_Num());
				daoManager.pt.setString(2,d.getTime_code());

				re=daoManager.pt.executeUpdate();
			}// for

			daoManager.closeDB();
		}catch(Exception e) {e.printStackTrace();}

		return re;
	}// setBookingSeat()	

	public List<DayseatVO> getBookingSeat(String time_code) {
		daoManager.connectDB();

		List<DayseatVO> booking = new ArrayList<>();
		String sql="SELECT * FROM day_seat WHERE time_code=?";
		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);
				daoManager.pt.setString(1,time_code);
				rs=daoManager.pt.executeQuery();
				while(rs.next()) {
					DayseatVO dvo=new DayseatVO();
					dvo.setSeat_status(rs.getInt("seat_status"));
					dvo.setSeat_Num(rs.getString("seat_Num"));
					booking.add(dvo);
				}// if
			if(rs != null) rs.close();
			daoManager.closeDB();
		}catch(Exception e) {e.printStackTrace();}

		return booking;
	}// getBookingSeat()
}// BookingDAO class
