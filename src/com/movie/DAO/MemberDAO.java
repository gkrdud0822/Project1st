package com.movie.DAO;

import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import com.movie.VO.MemberVO;
import com.movie.main.AppManager;

public class MemberDAO {
	DAOManager daoManager = AppManager.getInstance().getDAOManager();				//데이터를 가져오는 클래스
	MemberVO memberVO = AppManager.getInstance().getDataManager().getMemberVO();	//데이터를 보관해줄 VO

	int result;
	ResultSet rs;

	public MemberDAO() {
		AppManager.getInstance().getDAOManager().setMemberDAO(this);
	}
	//------------------------------회원가입------------------------------//
	boolean Customer() {
		daoManager.connectDB();
		String sql= "INSERT INTO ( memberVOber_id, memberVOber_pw, memberVOber_name, memberVOber_sex, memberVOber_birthday, memberVOber_email) VALUES(?, ?, ?, ?, ?, ?)";
		try {
			daoManager.pt = daoManager.con.prepareStatement(sql);
			daoManager.pt.setString(1, memberVO.getId());
			daoManager.pt.setString(2, memberVO.getPwd());
			daoManager.pt.setString(3, memberVO.getName());
			daoManager.pt.setString(4, memberVO.getSex());
			daoManager.pt.setString(5, memberVO.getBirth());
			daoManager.pt.setString(6, memberVO.getEmail());

		}catch(Exception e) {e.printStackTrace();}
		//		result값이 참일 때
		if(result > 0) {
			return true;
		}else {
			return false;
		}//if else
	}

	//------------------------------로그인 기능------------------------------//
	public boolean login() {
		daoManager =AppManager.getInstance().getDAOManager(); 
		memberVO = AppManager.getInstance().getDataManager().getMemberVO();

		try {
			daoManager.connectDB();
			String sql= "SELECT * FROM member WHERE member_id = ?";
			daoManager.pt = daoManager.con.prepareStatement(sql);
			daoManager.pt.setString(1, memberVO.getId());
			rs=daoManager.pt.executeQuery();

			//--------------------아이디와 비밀번호가 같다면 회원정보를 불러와 저장-------------------//
			if(rs.next()) {
				if(memberVO.getPwd().equals(rs.getString("member_pwd"))) {
					memberVO.setId(rs.getString("member_id"));
					memberVO.setPwd(rs.getString("member_pwd"));
					memberVO.setName(rs.getString("member_name"));
					memberVO.setSex(rs.getString("member_sex"));
					memberVO.setBirth(rs.getString("member_birth"));
					memberVO.setEmail(rs.getString("member_email"));
					return true;
				}//if
			}//if
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs !=null) rs.close();
				daoManager.closeDB();			
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	//------------------------------중복검사------------------------------//
	boolean idCheck() {
		daoManager =AppManager.getInstance().getDAOManager(); 
		memberVO = AppManager.getInstance().getDataManager().getMemberVO();


		daoManager.connectDB();
		String sql="SELECT memberVOber_id FROM memberVOber";

		try {
			daoManager.pt=daoManager.con.prepareStatement(sql);
			rs= daoManager.pt.executeQuery();
			while(rs.next()) {
				if(memberVO.getId().equals(rs.getString(1)))
					return false;
			}
			rs.close();
			daoManager.closeDB();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	//------------------------------정보수정------------------------------//
	public int updateInfo() {
		int re = -1;
		daoManager =AppManager.getInstance().getDAOManager(); 
		memberVO = AppManager.getInstance().getDataManager().getMemberVO();

		daoManager.connectDB();
		String sql = "update member set member_pwd=?,member_sex=?,member_birth=?,member_email=? where member_id=?";
		try {
			daoManager.pt = daoManager.con.prepareStatement(sql);
			daoManager.pt.setString(1,memberVO.getPwd());
			daoManager.pt.setString(2,memberVO.getSex());
			daoManager.pt.setString(3,memberVO.getBirth());
			daoManager.pt.setString(4,memberVO.getEmail());
			daoManager.pt.setString(5,memberVO.getId());
			re = daoManager.pt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				daoManager.closeDB();
			}catch(Exception e) {
				e.printStackTrace();
			}//try~catch
		}//finally	
		return re;
	}//updateInfo()
	//------------------------------탈퇴를 위한 비밀번호 정보가저오기------------------------------//
	public String getPass(String id) {
		daoManager = AppManager.getInstance().getDAOManager();
		String pwd = null;

		daoManager.connectDB();
		String sql = "select member_pwd from member where member_id=?";
		try {
			daoManager.pt = daoManager.con.prepareStatement(sql);
			daoManager.pt.setString(1,id);
			rs = daoManager.pt.executeQuery();
			if(rs.next()) {
				pwd = rs.getString("member_pwd");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(rs!=null) rs.close();
				daoManager.closeDB();
			}catch(Exception e) {
				e.printStackTrace();
			}//try~catch
		}//finally
		return pwd;
	}//showInfo()
	//------------------------------회원탈퇴------------------------------//
	public int deleteMember(String id) {
		int re = -1;
		daoManager = AppManager.getInstance().getDAOManager();

		daoManager.connectDB();
		String sql = "delete from member where member_id=?";
		try {
			daoManager.pt = daoManager.con.prepareStatement(sql);
			daoManager.pt.setString(1,id);	
			re=daoManager.pt.executeUpdate();
		}catch(Exception e) {
			e.printStackTrace();
		}finally {
			try {
				daoManager.closeDB();
			}catch(Exception e) {
				e.printStackTrace();
			}//try~catch
		}//finally
		return re;
	}
	
	/* 사용전!
	 * LIB 폴터에 추가되어있는 mail-1.4.7 jar를 연결 시킨 후 구동해야됨
	 * 프로젝트폴더 우측버튼 클릭 -> properties -> java build path
	 * -> (상단)Libraries ->(우측)add external jars -> 해당 LIB 폴더안에 파일 선택
	 * -> Apply and close
	 */
	public String randomAuthNum() {		//인증번호생성
		String security="";
		for(int i=0;i<6;i++){
			char p01=(char)(Math.random()*26+97);
			if(i%2==0){
				security+=(int)(Math.random()*10);
			}else{
				security+=p01;
			}//if
		}//for	      
		return security;
	}//randomAuthNum()

	public void Auth_Email(String email,String security){	//인증번호 보내기
		String id = "cine190823@gmail.com";
		String pw = "swing190823!";
		
		//STMP 서버 정보를 설정
		Properties p = System.getProperties();
		p.put("mail.smtp.host", "smtp.gmail.com"); //"mail.smtp.host" : 이메일 발송을 처리해줄 STMP 서버
		p.put("mail.smtp.port", 465);         	   //"mail.smtp.port "은 SMTP서버와 통신하는 포트를 말하는데 gmail일 경우 465
		p.put("mail.smtp.auth","true");            
		p.put("mail.smtp.ssl.enable", "true"); 
		p.put("mail.smtp.ssl.trust", "smtp.gmail.com");

		Session session = Session.getDefaultInstance(p, new Authenticator() {
			public PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(id, pw);
			}
		});

		/* 텍스트필드에서 이메일 가져오기 =>매게변수 email */
		String charSet = "UTF-8" ;
		try{
			MimeMessage msg = new MimeMessage(session);
			msg.setSentDate(new Date());
			String fromName = id ;
			InternetAddress from = new InternetAddress() ;
			try{
				from = new InternetAddress(new String(fromName.getBytes(charSet), "8859_1") + "<cine190823@gmail.com>");
				//from = new InternetAddress("<ksu4675447@gmail.com>","8859_1");
			}catch(UnsupportedEncodingException uee){
				uee.printStackTrace();
			}
			msg.setFrom(from);
			InternetAddress to = new InternetAddress(email);
			msg.setRecipient(Message.RecipientType.TO, to);
			msg.setSubject("안녕하세요 CineBro 입니다.", charSet);
			msg.setText("고객님이 입력하실 보안코드는\n"+security+"\n입니다.\n정확하게 입력하시기 바랍니다.\n 감사합니다.", "euc-kr");
			//본문이 깨져서 국내용 유니코드로 변경하였음
			msg.setHeader("content-Type", "text/html"); //이메일 헤더가 본문내용으로 발송

			Transport.send(msg);

		}catch (AddressException addr_e) {
			addr_e.printStackTrace();
		}catch (MessagingException msg_e) {
			msg_e.printStackTrace();
		}//try~catch
	}//
}