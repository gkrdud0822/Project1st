package com.movie.UI;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.movie.DAO.BookingDAO;
import com.movie.VO.BookedseatVO;
import com.movie.VO.BookingVO;
import com.movie.VO.CinemaVO;
import com.movie.VO.DayseatVO;
import com.movie.VO.MovieVO;
import com.movie.VO.MovietimeVO;
import com.movie.VO.SeatVO;
import com.movie.main.AppManager;

public class ReservationPanel extends JPanel implements ActionListener,ListSelectionListener{
	BookingDAO bdao=AppManager.getInstance().getDAOManager().getBookingDAO();
	BookingVO bvo=AppManager.getInstance().getDataManager().getBookingVO();
	BookedseatVO bsvo=AppManager.getInstance().getDataManager().getBookedseatVO();
	DayseatVO dvo=AppManager.getInstance().getDataManager().getDayseatVO();

	// --- 카드레이아웃 설정

	protected final CardLayout CARD=new CardLayout();

	// ------------------------------------- 시간 버튼 설정용 리스트, 멤버변수

	private List<String> time=new ArrayList<>();
	private String screenNum; // 상영관별 시간, 날짜 가져올 멤버변수
	private Date screendate;

	// ------------------------------------- 캘린더 컴포넌트 메서드

	Calendar cal=Calendar.getInstance(); // 캘린더 인스턴스 생성
	private List<String> dateList=new ArrayList<>();
	private JPanel calP=new JPanel(new GridLayout(7,7));
	private JPanel monthCalP=new JPanel(new BorderLayout());
	private JPanel monthButtonP=new JPanel(new FlowLayout(FlowLayout.CENTER,20,0));
	private String[] calWeek= {"일","월","화","수","목","금","토"};

	private JLabel secretDate=new JLabel(); // 이벤트 이후 저장용 라벨
	private JLabel[] calWeekL=new JLabel[7];
	protected JToggleButton[] calDate=new JToggleButton[32];
	protected ButtonGroup calDateBG=new ButtonGroup();
	protected JButton nextMonth=new JButton("▶");
	protected JButton preMonth=new JButton("◀");
	protected JLabel calMonth=new JLabel("");
	protected JLabel calYear=new JLabel("2019");

	private int startDay; // 각 월 시작 일
	private int lastDay; // 각 월 마지막 일

	private int year; // 현재 년도
	private int month; // 월
	private int date; // 일
	private int sunday=1;
	private int indexDay=1;
	String time_code;

	// ------------------------------------- 버튼그룹 메서드

	private JPanel adultButtonP=new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel childButtonP=new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel adultP=new JPanel(new BorderLayout(0,5));
	private JPanel childP=new JPanel(new BorderLayout(0,5));

	protected List<JToggleButton> timeBList=new ArrayList<>();
	private int timeCount; // 시간버튼 선택시 반환인덱스 설정용
	private int adultCount; // 성인버튼 선택시 반환 카운트 설정용 ( 좌석 갯수 )
	private int childCount; // 청소년버튼 선택시 반환 카운트 설정용 ( 좌석 갯수 )
	private JPanel timeGroup=new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JPanel peopleGroup=new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel adultInfoP=new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JPanel childInfoP=new JPanel(new FlowLayout(FlowLayout.LEFT));
	private JLabel timeChoiceLabel=new JLabel("시간");
	private JLabel peopleChoiceLabel=new JLabel("인원");
	protected JLabel adultL=new JLabel("성인");
	protected JLabel adultPrice=new JLabel("  ※ 1인당 9000원");
	protected JLabel childL=new JLabel("청소년");
	protected JLabel childPrice=new JLabel("  ※ 1인당 6000원");
	protected JToggleButton[] adultB=new JToggleButton[9];
	protected JToggleButton[] childB=new JToggleButton[9];
	protected ButtonGroup adultBG=new ButtonGroup();
	protected ButtonGroup childBG=new ButtonGroup();
	protected ButtonGroup timeBG=new ButtonGroup();

	// ------------------------------------- 홈패널

	/* 카드레이아웃 패널 */
	protected JPanel reservCard=new JPanel(CARD);
	private JPanel homeCard=new JPanel(new BorderLayout()); // 홈 카드패널
	private JPanel seatCard=new JPanel(new FlowLayout(FlowLayout.CENTER,0,70)); // 좌석 카드패널

	/* 홈 패널 */
	private JPanel topPanel=new JPanel(new FlowLayout());
	private JPanel bottomPanel=new JPanel(new FlowLayout(FlowLayout.CENTER,50,0));
	private JPanel calendar=new JPanel(new BorderLayout());
	private JPanel timeChoice=new JPanel();
	private JPanel peopleChoice=new JPanel(); 

	private JPanel info=new JPanel(new BorderLayout()); // 카드레이아웃 미포함
	private JPanel infoTop=new JPanel();
	private JPanel infoName=new JPanel(new FlowLayout(FlowLayout.CENTER));
	private JPanel infoLabel=new JPanel(new BorderLayout(20,0));
	private JPanel infoWestLP=new JPanel(new GridLayout(0,1,0,15));
	private JPanel infoCenterLP=new JPanel(new GridLayout(0,1,0,10));
	private JPanel infoCenter=new JPanel(new BorderLayout(0,30));
	private JPanel infoBottom=new JPanel();
	private JPanel dayGroupLP=new JPanel(new FlowLayout(FlowLayout.LEFT,0,2));
	private JPanel peopleGroupLP=new JPanel(new FlowLayout(FlowLayout.LEFT,0,2));
	private JPanel movieListPanel=new JPanel(new BorderLayout(0,3));
	private JPanel cinemaListPanel=new JPanel(new BorderLayout(0,3));
	protected JPanel calendarPanel=new JPanel(new BorderLayout(0,3));

	// --- 서브라벨

	private JLabel movieChoiceLabel=new JLabel("영화");
	private JLabel cinemaChoiceLabel=new JLabel("상영관");
	private JLabel dayChoiceLabel=new JLabel("날짜");

	// --------------------------------- 좌석 패널

	private List<String> seat_Num=new ArrayList<>();
	private List<String> seatNum=new ArrayList<>(); 
	private List<Integer> seat_status=new ArrayList<>();
	private JPanel seatPanel=new JPanel(new BorderLayout(10,0)); // 좌석 구역 패널
	private JPanel leftArea=new JPanel(new GridLayout(12,5,1,1)); // 왼쪽 구역 좌석
	private JPanel centerArea=new JPanel(new GridLayout(12,10,1,1)); // 중앙 구역 좌석
	private JPanel rightArea=new JPanel(new GridLayout(12,5,1,1)); // 오른쪽 구역 좌석
	private JPanel areaLine=new JPanel(new GridLayout(12,1,1,7)); // 좌석 열표시
	private JPanel seatChoice=new JPanel(new BorderLayout());
	private JPanel seatChoiceP=new JPanel(new GridLayout(4,2,3,2));
	private JPanel lineSeatP=new JPanel(new BorderLayout(20,30));

	protected List<JButton> seatButton=new ArrayList<>();	
	protected JButton seatReset=new JButton("좌석초기화");


	private int index=0; // 좌석라벨 인덱스
	private int seatIndex=0; // 좌석버튼 액션 이벤트 등록 위한 인덱스
	private List<JLabel> seatList=new ArrayList<>(); // 좌석 리스트
	private JLabel screen=new JLabel("SCREEN");
	private JLabel secretSeatNum=new JLabel(); // 선택한 좌석번호 저장용 라벨
	protected JLabel[] seatChoiceL=new JLabel[8];
	protected JLabel[] seatLine=new JLabel[12]; // 좌석 열 번호

	/* 결제 패널 */

	private JDialog paymentD=new JDialog(); // 다이얼로그 프레임
	private JPanel paymentMainP=new JPanel(new BorderLayout());

	// --- 상단 패널

	private JPanel paymentCenterP=new JPanel(new BorderLayout());
	private JPanel paymentPriceP=new JPanel();
	private JPanel paymentP=new JPanel(new BorderLayout());
	private JPanel paymentLayout=new JPanel(new GridLayout(0,1));
	private JPanel paymentChoice=new JPanel();
	private JPanel paymentChoiceCard=new JPanel(CARD);
	private JPanel paymentCashP=new JPanel(new GridLayout(0,1));
	private JPanel paymentCardP=new JPanel();
	private JPanel paymentOptionP=new JPanel(new GridLayout(1,0));

	private JLabel paymentPriceinfo=new JLabel("결제금액");
	private JLabel paymentOption=new JLabel();
	private JLabel paymentOptionPrice=new JLabel();
	private JTextField paymentPrice=new JTextField();

	private JLabel paymentChoiceInfo=new JLabel("※ 매장 사정상 현장결제만 가능합니다");
	protected ButtonGroup paymentBG=new ButtonGroup();
	protected JRadioButton paymentCash=new JRadioButton("현장결제");
	protected JRadioButton paymentCard=new JRadioButton("온라인결제");

	protected ButtonGroup paymentSiteBG=new ButtonGroup();
	protected JRadioButton paymentSiteCash=new JRadioButton("현금결제");
	protected JRadioButton paymentSiteCard=new JRadioButton("카드결제");
	private JLabel paymentInfoL=new JLabel("※ 현장결제 선택 시 예매 내역을 가지고 오프라인 카운터에서 결제를 진행해주시기 바랍니다.");

	// --- 하단 패널
	private JPanel paymentSouthP=new JPanel(new BorderLayout());
	private JPanel paymentSCenterP=new JPanel();
	private JPanel paymentPosName=new JPanel(new FlowLayout(FlowLayout.LEFT,15,0));
	private JPanel paymentNameAge=new JPanel(new GridLayout(0,1,0,50));
	private JPanel paymentLP=new JPanel(new GridLayout(0,1,0,5));
	private JPanel paymentInfoLP=new JPanel(new GridLayout(0,1,0,5));
	private JPanel paymentInfoBLP=new JPanel();

	protected JButton seatChoicePrev=new JButton("좌석선택");
	protected JButton paymentB=new JButton("결제하기");

	private JLabel paymentPoster=new JLabel();
	private JLabel paymentAge=new JLabel();
	private JLabel paymentName=new JLabel("영화 이름");
	private JLabel paymentDate=new JLabel("일시");
	private JLabel setPaymentDate=new JLabel();
	private JLabel paymentCinema=new JLabel("상영관");
	private JLabel setPaymentCinema=new JLabel("");
	private JLabel paymentPeople=new JLabel("인원");
	private JLabel setPaymentPeople=new JLabel("");
	private JLabel paymentSeat=new JLabel("좌석번호");
	private JLabel setPaymentSeat=new JLabel("");

	// ----------------------------------- 리스트

	private int movie_code; // 무비 코드 저장용 멤버변수
	private List<Integer> booking_code=new ArrayList<>();
	private Vector<Integer> codeVector=new Vector<>(); // 각 영화 이름에 대한 코드를 저장할 벡터, 무비코드로 영화클릭시 해당하는 상영관 항목 생성
	private Vector<String> movieVector=new Vector<>(); // 각 리스트에 들어갈 벡터 생성
	private Vector<String> cinemaVector=new Vector<>();
	protected JList movieList=new JList(movieVector); // J리스트 생성
	protected JList cinemaList=new JList();

	// ---------------------------------- 정보라벨

	protected JLabel movieNameL=new JLabel("영화제목");
	protected JLabel daysL=new JLabel("날짜");
	protected JLabel setDaysL=new JLabel();
	protected JLabel setTimesL=new JLabel();
	protected JLabel cinemaL=new JLabel("상영관");
	protected JLabel setCinemaL=new JLabel("");
	protected JLabel seatL=new JLabel("좌석");
	protected JLabel setSeatL=new JLabel(""); 
	protected JLabel peopleL=new JLabel("인원");
	protected JLabel setAdultL=new JLabel();
	protected JLabel setChildL=new JLabel();
	protected JLabel priceL=new JLabel("가격");
	protected JLabel setPriceL=new JLabel("0");

	// ---------------------------------- 정보패널 버튼

	private Vector<String> movieImg=new Vector<>(); // 포스터 삽입용 벡터생성
	private JLabel secretImgL=new JLabel();
	private Vector<String> movieAge=new Vector<>();
	private JLabel secretAgeL=new JLabel();
	protected JButton moviePosterB=new JButton();
	protected JButton infoNextB=new JButton("다음단계");
	protected JButton seatPaymentB=new JButton("결제단계");
	protected JButton seatBackB=new JButton("이전단계");

	public ReservationPanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createEmptyBorder(50,110,200,170));			

		/* 정보패널 구축 */
		Font setLabelFont=new Font("맑은 고딕",Font.BOLD,12);
		Font labelFont=new Font("맑은 고딕",Font.PLAIN,12);
		Font priceFont=new Font("맑은 고딕",Font.BOLD,16);
		info.setPreferredSize(new Dimension(300,700));
		moviePosterB.setPreferredSize(new Dimension(265,300));
		moviePosterB.setMargin(new Insets(0,0,0,0));
		moviePosterB.setContentAreaFilled(false);
		moviePosterB.setBorderPainted(false);
		moviePosterB.addActionListener(this);
		moviePosterB.setFocusable(false);

		infoTop.add(moviePosterB); // 정보패널 상단 

		infoCenter.setBorder(BorderFactory.createEmptyBorder(0,0,50,0));
		infoLabel.setBorder(BorderFactory.createEmptyBorder(0,30,0,0));

		infoName.add(movieNameL); // 영화명 라벨
		dayGroupLP.add(setDaysL); dayGroupLP.add(setTimesL);
		peopleGroupLP.add(setAdultL); peopleGroupLP.add(setChildL);

		infoWestLP.add(daysL);    infoCenterLP.add(dayGroupLP);
		infoWestLP.add(cinemaL);  infoCenterLP.add(setCinemaL);
		infoWestLP.add(seatL);    infoCenterLP.add(setSeatL);
		infoWestLP.add(peopleL);  infoCenterLP.add(peopleGroupLP);
		infoWestLP.add(priceL);   infoCenterLP.add(setPriceL); // 2줄라벨 모음

		infoLabel.add(infoWestLP,"West"); infoLabel.add(infoCenterLP,"Center");
		infoCenter.add(infoName,"North"); infoCenter.add(infoLabel,"Center");

		movieNameL.setFont(setLabelFont);
		daysL.setFont(labelFont);   setDaysL.setFont(setLabelFont); setTimesL.setFont(setLabelFont);
		cinemaL.setFont(labelFont); setCinemaL.setFont(setLabelFont);
		seatL.setFont(labelFont);   setSeatL.setFont(new Font("맑은 고딕",Font.BOLD,11));
		peopleL.setFont(labelFont); setAdultL.setFont(setLabelFont); setChildL.setFont(setLabelFont);
		priceL.setFont(labelFont);	setPriceL.setFont(priceFont);

		daysL.setForeground(Color.LIGHT_GRAY);
		cinemaL.setForeground(Color.LIGHT_GRAY);
		seatL.setForeground(Color.LIGHT_GRAY);
		peopleL.setForeground(Color.LIGHT_GRAY);
		priceL.setForeground(Color.LIGHT_GRAY);

		movieNameL.setForeground(Color.WHITE);
		setDaysL.setForeground(Color.WHITE);
		setTimesL.setForeground(Color.WHITE);
		setCinemaL.setForeground(Color.WHITE);
		setSeatL.setForeground(Color.WHITE);
		setAdultL.setForeground(Color.WHITE);
		setChildL.setForeground(Color.WHITE);
		setPriceL.setForeground(Color.WHITE);

		infoNextB.setPreferredSize(new Dimension(280,40)); // 다음버튼 크기설정
		infoNextB.setEnabled(false);
		infoNextB.addActionListener(this);
		infoBottom.add(infoNextB); // 정보패널 하단
		seatBackB.setVisible(false); seatBackB.setPreferredSize(new Dimension(130,40)); 
		seatPaymentB.setVisible(false); seatPaymentB.setPreferredSize(new Dimension(130,40)); seatPaymentB.setEnabled(false);
		seatBackB.addActionListener(this);
		seatPaymentB.addActionListener(this);
		infoBottom.add(seatBackB); infoBottom.add(seatPaymentB);


		infoNextB.setBackground(Color.RED);
		infoNextB.setForeground(Color.WHITE);
		infoNextB.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		seatBackB.setBackground(Color.RED);
		seatBackB.setForeground(Color.WHITE);
		seatBackB.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		seatPaymentB.setBackground(Color.RED);
		seatPaymentB.setForeground(Color.WHITE);
		seatPaymentB.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));

		info.add(infoTop,"North");
		info.add(infoCenter,"Center"); 
		info.add(infoBottom,"South");

		infoTop.setOpaque(false);
		infoTop.setBorder(BorderFactory.createEmptyBorder(9,0,0,0));

		infoCenter.setOpaque(false);
		infoName.setOpaque(false);
		infoLabel.setOpaque(false);
		infoWestLP.setOpaque(false);
		dayGroupLP.setOpaque(false);
		peopleGroupLP.setOpaque(false);
		infoCenterLP.setOpaque(false);

		infoBottom.setOpaque(false);

		// ------------------ 카드패널에 패널, 키설정

		reservCard.add(homeCard,"home"); // 예매 카드패널
		reservCard.add(seatCard,"seat"); // 좌석 카드패널 

		homeCard.setBackground(Color.WHITE);
		seatCard.setBackground(Color.WHITE);
		reservCard.setBackground(Color.WHITE);

		homeCard.setBorder(BorderFactory.createEmptyBorder(10,10,13,10));

		// ------------------ 예매 카드패널

		// ------ 상단 패널 구축

		cinemaList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // 리스트 설정 단일선택 모델
		//		cinemaList.setPreferredSize(new Dimension(200,650)); // 리스트 크기 지정
		movieList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); //
		movieList.setPreferredSize(new Dimension(200,650));
		calendar.setPreferredSize(new Dimension(400,380)); // 달력 패널 크기지정
		calendar.setBackground(Color.ORANGE);

		movieList.setFont(new Font("맑은 고딕",Font.PLAIN,20));
		cinemaList.setFont(new Font("맑은 고딕",Font.PLAIN,20));

		/* 리스트 항목 작성 */		
		Vector<MovieVO> mV=bdao.movieNameList();
		if((mV != null) && mV.size()>0) {
			for(int i=0;i<mV.size();i++) {
				MovieVO mvo=mV.get(i);
				movieVector.add(mvo.getMovie_nameK());
				movieImg.add(mvo.getMovie_img());
				codeVector.add(mvo.getMovie_code());
				movieAge.add(mvo.getAge());
			}// for
		}// if

		// --- 리스트 스크롤생성

		JScrollPane movieListSp=new JScrollPane(movieList,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); // 스크롤, 수직바 항상, 수평바 필요할때
		movieListSp.setPreferredSize(new Dimension(240,380)); // 스크롤판 사이즈 설정
		JScrollPane cinemaListSp=new JScrollPane(cinemaList,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		cinemaListSp.setPreferredSize(new Dimension(240,380));

		Font choiceLabelFont=new Font("맑은 고딕",Font.BOLD,20);

		// --- 각 리스트 선택이벤트 등록

		movieList.addListSelectionListener(this);
		cinemaList.addListSelectionListener(this);

		// --- 다이얼로그 버튼이벤트 등록

		paymentB.addActionListener(this);
		seatChoicePrev.addActionListener(this);
		paymentCash.setSelected(true);
		paymentCash.addActionListener(this);
		paymentCard.addActionListener(this);
		paymentSiteCash.addActionListener(this);
		paymentSiteCard.addActionListener(this);

		/* --- 라벨, 리스트 추가한 패널 구성 */
		movieListPanel.add(movieChoiceLabel,"North");   movieListPanel.add(movieListSp,"Center");
		cinemaListPanel.add(cinemaChoiceLabel,"North"); cinemaListPanel.add(cinemaListSp,"Center");
		calendarPanel.add(dayChoiceLabel,"North");      setCalendar();


		movieChoiceLabel.setFont(choiceLabelFont);      cinemaChoiceLabel.setFont(choiceLabelFont);
		dayChoiceLabel.setFont(choiceLabelFont);

		movieChoiceLabel.setHorizontalAlignment(JLabel.CENTER);
		cinemaChoiceLabel.setHorizontalAlignment(JLabel.CENTER);
		dayChoiceLabel.setHorizontalAlignment(JLabel.CENTER);

		movieListPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		cinemaListPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		calendarPanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		topPanel.add(movieListPanel); topPanel.add(cinemaListPanel); topPanel.add(calendarPanel);

		movieListPanel.setOpaque(false);
		cinemaListPanel.setOpaque(false);

		topPanel.setOpaque(false);

		// ------ 하단 패널 구축
		Font bottomLabelFont=new Font("맑은 고딕",Font.BOLD,13);
		bottomPanel.setOpaque(false);

		bottomPanel.setPreferredSize(new Dimension(1000,180));
		timeChoice.setBackground(Color.WHITE);
		timeChoice.setPreferredSize(new Dimension(550,160));
		timeChoice.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		peopleChoice.setBackground(Color.WHITE);
		peopleChoice.setPreferredSize(new Dimension(230,160));
		peopleChoice.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		timeChoiceLabel.setFont(bottomLabelFont); peopleChoiceLabel.setFont(bottomLabelFont);

		timeChoice.add(timeChoiceLabel);	    timeChoice.add(setTimeButton());
		peopleChoice.add(peopleChoiceLabel);	peopleChoice.add(setPeopleButton());

		bottomPanel.add(timeChoice);            bottomPanel.add(peopleChoice); // 하단패널에 들어갈 패널 삽입

		homeCard.add(topPanel,BorderLayout.CENTER); homeCard.add(bottomPanel,BorderLayout.SOUTH);
		// 홈 카드레이아웃 설정된 패널에 상단패널과 하단패널 삽입

		// ------------------ 좌석 카드패널

		setCinemaSeat();		

		/* 메인패널 구축 */
		add(reservCard,BorderLayout.CENTER); add(info,BorderLayout.EAST);

		reservCard.setBackground(Color.BLACK);
		reservCard.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(2,2,2,0,Color.GRAY),
				BorderFactory.createEmptyBorder(20,20,20,20)));
		info.setBackground(Color.BLACK);
		info.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createMatteBorder(2,0,2,2,Color.GRAY), 
				BorderFactory.createEmptyBorder(5,0,3,0)));
	}//cons

	// --- 시간선택 버튼 컴포넌트 메서드	
	public Component setTimeButton() {
		timeGroup.setPreferredSize(new Dimension(550,160));
		timeGroup.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		timeGroup.setOpaque(false);

		return timeGroup;
	}//setTimeButton()

	// --- 인원선택 버튼 컴포넌트 메서드
	public Component setPeopleButton() {
		UIManager.put("ToggleButton.select", Color.RED); // 토글버튼 선택모델 색상 변경
		Font ageLabelFont=new Font("맑은 고딕",Font.BOLD,11);

		peopleGroup.setPreferredSize(new Dimension(230,150));
		peopleGroup.setOpaque(false);
		peopleGroup.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		adultInfoP.setPreferredSize(new Dimension(230,20));
		childInfoP.setPreferredSize(new Dimension(230,20));
		adultButtonP.setPreferredSize(new Dimension(230,30));
		childButtonP.setPreferredSize(new Dimension(230,30));

		adultL.setFont(ageLabelFont); childL.setFont(ageLabelFont);
		adultPrice.setFont(ageLabelFont); childPrice.setFont(ageLabelFont);

		adultInfoP.add(adultL);       adultInfoP.add(adultPrice);
		adultInfoP.setOpaque(false);  childInfoP.setOpaque(false);

		adultP.add(adultInfoP,"North");
		adultP.add(adultButtonP,"Center");
		for(int i=0;i<adultB.length;i++) {
			adultB[i]=new JToggleButton(i+"");
			adultBG.add(adultB[i]);
			adultB[i].setFont(new Font("맑은 고딕",Font.BOLD,10));
			adultB[i].setMargin(new Insets(0,3,0,3));
			adultB[i].setBackground(Color.BLACK);
			adultB[i].setForeground(Color.WHITE);
			adultB[i].addActionListener(this);
			adultB[i].setEnabled(false);
			adultButtonP.add(adultB[i]);
			SwingUtilities.updateComponentTreeUI(adultB[i]); // 토글버튼 선택모델 적용
		}//for
		childInfoP.add(childL);		 childInfoP.add(childPrice);
		childP.add(childInfoP,"North");
		childP.add(childButtonP,"Center");
		for(int i=0;i<childB.length;i++) {
			childB[i]=new JToggleButton(i+"");
			childBG.add(childB[i]);
			childB[i].setFont(new Font("맑은 고딕",Font.BOLD,10));
			childB[i].setMargin(new Insets(0,3,0,3));
			childB[i].setBackground(Color.BLACK);
			childB[i].setForeground(Color.WHITE);
			childB[i].addActionListener(this);
			childB[i].setEnabled(false);
			childButtonP.add(childB[i]);
		}//for
		peopleGroup.add(adultP);
		peopleGroup.add(childP);

		adultP.setOpaque(false);
		adultButtonP.setOpaque(false);
		childP.setOpaque(false);
		childButtonP.setOpaque(false);	

		return peopleGroup;
	}//setPeopleButton()

	// --- 날짜선택 캘린더 컴포넌트 메서드
	public void setCalendar() {
		calMonth.setText("08");
		calYear.setHorizontalAlignment(JLabel.CENTER); // 라벨 중앙설정
		calMonth.setHorizontalAlignment(JLabel.CENTER);

		// --- 년 / 월 설정 버튼

		monthButtonP.add(preMonth);
		monthButtonP.add(calMonth);
		monthButtonP.add(nextMonth);

		preMonth.setContentAreaFilled(false);
		preMonth.setBorderPainted(false);
		nextMonth.setContentAreaFilled(false);
		nextMonth.setBorderPainted(false);

		nextMonth.addActionListener(this);

		// --- 캘린더 날짜 구성
		year=2019; 
		month=Integer.parseInt(calMonth.getText()); // 컴포넌트위 문자열을 정수로 반환

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, month-1);
		cal.set(Calendar.DATE, 1);

		startDay=cal.get(Calendar.DAY_OF_WEEK); // 현재 월 시작 요일
		lastDay=cal.getActualMaximum(Calendar.DATE); // 월 마지막 날짜
		// 버튼 설정
		for(int i=0;i<calDate.length;i++) {
			String sday="";
			if(i<10) {
				sday=" "+i;
			}else {
				sday=""+i;
			}
			calDate[i]=new JToggleButton(sday);
			calDate[i].setEnabled(false);
			calDateBG.add(calDate[i]);
		}//for
		for(int i=0;i<calWeek.length;i++) {
			calWeekL[i]=new JLabel(calWeek[i]);
			calWeekL[i].setHorizontalAlignment(JLabel.CENTER);
			calP.add(calWeekL[i]);
		}//for
		for(int i=1;i<startDay;i++) {
			calP.add(new JLabel(""));
			sunday++;
		}//for
		for(int j=0;j<lastDay;j++) {
			if(sunday%7==1) calDate[indexDay].setForeground(Color.RED);
			else if(sunday%7==0) calDate[indexDay].setForeground(Color.BLUE);
			else calDate[indexDay].setForeground(Color.BLACK);
			sunday++;
			calP.add(calDate[indexDay++]);
		}//for
		while(calP.getComponentCount()<49) {
			calP.add(new JLabel(""));
		}//while
		for(int i=0;i<calDate.length;i++) {
			calDate[i].setBackground(Color.WHITE);
			calDate[i].addActionListener(this);
		}//for

		calYear.setForeground(Color.WHITE);

		calP.setBackground(Color.WHITE);
		monthButtonP.setBackground(Color.WHITE);
		monthCalP.setOpaque(false);
		calendar.setBackground(Color.BLACK);
		calendar.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		calendarPanel.setOpaque(false);

		monthCalP.add(monthButtonP,"North"); monthCalP.add(calP,"Center");		
		calendar.add(calYear,"North");	     calendar.add(monthCalP,"Center");
		calendarPanel.add(calendar,"Center");
	}//setCalendar()

	public void setCinemaSeat() {
		char lineName='A'; // 열 이름
		// --- 열 이름
		for(int i=0;i<12;i++) {
			seatLine[i]=new JLabel(lineName+"");
			lineName++;
			areaLine.add(seatLine[i]);
		}//for
		// --- 각 상영관 좌석 배치

		seatReset.addActionListener(this);
		seatReset.setBackground(Color.BLACK);
		seatReset.setForeground(Color.WHITE);
		seatReset.setBorder(BorderFactory.createMatteBorder(1,0,0,0,Color.GRAY));
		seatReset.setFont(new Font("맑은 고딕",Font.BOLD,13));
		seatChoice.add(seatChoiceP,"Center"); seatChoice.add(seatReset,"South"); 

		seatChoice.setPreferredSize(new Dimension(120,0));
		seatChoice.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		screen.setPreferredSize(new Dimension(0,30));
		screen.setHorizontalAlignment(JLabel.CENTER);
		screen.setOpaque(true);
		screen.setBackground(new Color(225,225,225));
		screen.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		lineSeatP.add(areaLine,"West"); lineSeatP.add(seatPanel,"Center");
		lineSeatP.add(screen,"North"); lineSeatP.add(seatChoice,"East");

		areaLine.setOpaque(false);
		seatPanel.setOpaque(false);
		seatChoiceP.setOpaque(false);
		seatChoice.setOpaque(false);
		lineSeatP.setOpaque(false);
		lineSeatP.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.GRAY),
				BorderFactory.createEmptyBorder(20,20,20,20)));

		seatCard.add(lineSeatP);
	}//setCinemaSeat()

	// --- 결제 다이얼로그 구축
	public Component setPaymentDialog() {
		DecimalFormat commas = new DecimalFormat("##,###");

		paymentD.setSize(980,350);
		paymentD.setLocationRelativeTo(null);

		Font paymentSetLF=new Font("맑은 고딕",Font.BOLD,13);
		Font paymentLF=new Font("맑은 고딕",Font.PLAIN,13);
		Font paymentBF=new Font("맑은 고딕",Font.BOLD,20);

		// --- 포스터 라벨
		ImageIcon mainposter = new ImageIcon("pic/"+secretImgL.getText());
		Image originImg = mainposter.getImage();
		Image changeImg = originImg.getScaledInstance(100,120,Image.SCALE_SMOOTH);		
		ImageIcon paymentImg = new ImageIcon(changeImg);

		paymentPoster.setIcon(paymentImg);

		// --- 하단패널 라벨

		paymentName.setText(movieNameL.getText()); 							paymentName.setFont(paymentSetLF);
		paymentAge.setText(secretAgeL.getText()); 							paymentAge.setFont(paymentSetLF);
		setPaymentDate.setText(setDaysL.getText()+setTimesL.getText());		setPaymentDate.setFont(paymentSetLF);
		setPaymentCinema.setText(setCinemaL.getText());						setPaymentCinema.setFont(paymentSetLF);
		setPaymentPeople.setText(setAdultL.getText()+setChildL.getText());	setPaymentPeople.setFont(paymentSetLF);
		setPaymentSeat.setText(setSeatL.getText());							setPaymentSeat.setFont(paymentSetLF);

		paymentCinema.setFont(paymentLF);
		paymentDate.setFont(paymentLF);
		paymentPeople.setFont(paymentLF);
		paymentSeat.setFont(paymentLF);

		paymentPoster.setForeground(Color.WHITE);
		paymentAge.setForeground(Color.WHITE);
		paymentName.setForeground(Color.WHITE);
		paymentDate.setForeground(Color.WHITE);
		setPaymentDate.setForeground(Color.WHITE);
		paymentCinema.setForeground(Color.WHITE);
		setPaymentCinema.setForeground(Color.WHITE);
		paymentPeople.setForeground(Color.WHITE);
		setPaymentPeople.setForeground(Color.WHITE);
		paymentSeat.setForeground(Color.WHITE);
		setPaymentSeat.setForeground(Color.WHITE);

		paymentLP.add(paymentCinema);
		paymentLP.add(paymentDate);
		paymentLP.add(paymentPeople);
		paymentLP.add(paymentSeat);
		paymentInfoLP.add(setPaymentCinema);
		paymentInfoLP.add(setPaymentDate);
		paymentInfoLP.add(setPaymentPeople);
		paymentInfoLP.add(setPaymentSeat);

		// --- 하단패널 버튼

		seatChoicePrev.setPreferredSize(new Dimension(140,0));
		seatChoicePrev.setForeground(Color.WHITE);
		seatChoicePrev.setBackground(Color.GRAY);
		seatChoicePrev.setFont(paymentBF);		
		paymentB.setPreferredSize(new Dimension(200,0));
		paymentB.setEnabled(false);
		paymentB.setForeground(Color.WHITE);
		paymentB.setBackground(Color.RED);
		paymentB.setFont(paymentBF);


		// --- 결제창 하단패널 구성

		paymentPosName.setPreferredSize(new Dimension(300,130));
		paymentPosName.setBorder(BorderFactory.createCompoundBorder(
				BorderFactory.createEmptyBorder(-1,-1,-1,0),
				BorderFactory.createLineBorder(Color.GRAY)));
		paymentLP.setBorder(BorderFactory.createEmptyBorder(0,5,0,0));

		paymentPosName.setOpaque(false);
		paymentNameAge.setOpaque(false);
		paymentLP.setOpaque(false);
		paymentInfoLP.setOpaque(false);
		paymentInfoBLP.setOpaque(false);
		paymentSCenterP.setOpaque(false);
		paymentSouthP.setBackground(Color.BLACK);

		paymentInfoBLP.add(paymentInfoLP);

		paymentNameAge.add(paymentName); 			  	paymentNameAge.add(paymentAge);

		paymentPosName.add(paymentPoster);				paymentPosName.add(paymentNameAge);

		paymentSCenterP.add(paymentPosName); 			paymentSCenterP.add(paymentLP);
		paymentSCenterP.add(paymentInfoBLP); 			

		paymentSouthP.add(seatChoicePrev,"West");   	
		paymentSouthP.add(paymentB,"East");				paymentSouthP.add(paymentSCenterP,"Center");

		// --- 결제창 상단패널 구성

		Font priceFont=new Font("맑은 고딕",Font.BOLD,20);
		int formatNum=Integer.parseInt(setPriceL.getText().trim());

		// --- 결제창 금액부 패널 구성

		paymentPriceP.setPreferredSize(new Dimension(200,0));

		paymentPriceinfo.setHorizontalAlignment(JLabel.CENTER);
		paymentPriceinfo.setFont(priceFont);
		paymentPriceinfo.setBorder(BorderFactory.createLineBorder(Color.GRAY));

		paymentOption.setHorizontalAlignment(JLabel.CENTER);
		paymentOption.setFont(new Font("맑은 고딕",Font.PLAIN,13));

		paymentOptionPrice.setHorizontalAlignment(JLabel.CENTER);
		paymentOptionPrice.setFont(new Font("맑은 고딕",Font.PLAIN,13));

		paymentPrice.setFont(priceFont);
		paymentPrice.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		paymentPrice.setText(commas.format(formatNum)+"원");
		paymentPrice.setHorizontalAlignment(JTextField.RIGHT);
		paymentPrice.setPreferredSize(new Dimension(190,0));
		paymentPrice.setBackground(Color.BLACK);
		paymentPrice.setForeground(Color.WHITE);
		paymentPrice.setEditable(false);

		// --- 결제창 수단선택부 패널 구성

		paymentCard.setEnabled(false);

		paymentCash.setFont(paymentLF);
		paymentCard.setFont(paymentLF);

		paymentSiteCash.setFont(paymentLF);
		paymentSiteCard.setFont(paymentLF);

		paymentChoiceInfo.setFont(new Font("맑은 고딕",Font.PLAIN,13));
		paymentChoiceInfo.setForeground(Color.RED);
		paymentInfoL.setFont(new Font("맑은 고딕",Font.PLAIN,13));
		paymentInfoL.setForeground(Color.RED);

		paymentChoiceCard.setBorder(BorderFactory.createEmptyBorder(0,20,0,0));
		paymentChoice.setBorder(BorderFactory.createMatteBorder(0,0,1,0,Color.GRAY));	

		paymentBG.add(paymentCash); 						paymentBG.add(paymentCard); 
		paymentSiteBG.add(paymentSiteCash); 				paymentSiteBG.add(paymentSiteCard);

		paymentChoice.add(paymentCash); paymentChoice.add(paymentCard); paymentChoice.add(paymentChoiceInfo);

		paymentCashP.add(paymentSiteCash); paymentCashP.add(paymentSiteCard); paymentCashP.add(paymentInfoL);

		paymentChoiceCard.add(paymentCashP,"cash"); 		paymentChoiceCard.add(paymentCardP,"card");

		paymentP.add(paymentChoice,"North"); 				paymentP.add(paymentChoiceCard,"Center");

		paymentP.setBorder(BorderFactory.createMatteBorder(1,1,1,1,Color.GRAY));
		paymentPriceP.setBorder(BorderFactory.createMatteBorder(1,0,1,1,Color.GRAY));

		paymentOptionP.add(paymentOption);					paymentOptionP.add(paymentOptionPrice);

		paymentLayout.add(paymentPriceinfo); 				paymentLayout.add(paymentPrice);
		paymentLayout.add(paymentOptionP);

		paymentPriceP.add(paymentLayout);

		paymentCenterP.add(paymentP,"Center");				paymentCenterP.add(paymentPriceP,"East");

		// -- 결제창 전체 패널 구성

		paymentMainP.add(paymentCenterP,"Center");
		paymentMainP.add(paymentSouthP,"South");

		paymentP.setOpaque(false);		
		paymentCenterP.setOpaque(false);
		paymentChoice.setOpaque(false);
		paymentChoiceCard.setOpaque(false);
		paymentCashP.setOpaque(false);
		paymentCash.setOpaque(false);
		paymentSiteCard.setOpaque(false);
		paymentSiteCash.setOpaque(false);
		paymentCard.setOpaque(false);

		paymentPriceP.setOpaque(false);
		paymentOptionP.setOpaque(false);
		paymentLayout.setOpaque(false);

		paymentMainP.setBackground(Color.WHITE);
		paymentD.setModal(true);
		paymentD.add(paymentMainP);
		paymentD.setResizable(false);

		return paymentD;
	}// setPaymentDialog()

	@Override
	public void actionPerformed(ActionEvent e) {
		int i=0; // 반복문 매개변수
		Object obj=e.getSource();
		if(obj==moviePosterB) {
			if(movie_code != 0) {
				new MovieInfoPlus(movie_code);
			}// if
		}// if
		// --- 시간 버튼
		for(i=0;i<timeBList.size();i++) {
			if(obj==timeBList.get(i)) {
				setTimesL.setText(" "+timeBList.get(i).getText());				
				for(int j=0;j<adultB.length;j++) {
					adultB[j].setEnabled(true);
					childB[j].setEnabled(true);
				}// for
				adultBG.clearSelection();
				childBG.clearSelection();
				setAdultL.setText("");
				setChildL.setText(""); 
				setPriceL.setText("");
				// 각 시간버튼 카운트 설정
				if(obj==timeBList.get(0)) {
					timeCount=1;
				}else if(obj==timeBList.get(1)) {
					timeCount=2;
				}else if(obj==timeBList.get(2)) {
					timeCount=3;
				}else if(obj==timeBList.get(3)) {
					timeCount=4;
				}else if(obj==timeBList.get(4)) {
					timeCount=5;
				}// if else if
			}// if 시간버튼 눌렀을때 == > 타임버튼 배치와 함께 타임버튼 클릭시 액션
		}// for
		// --- 성인, 청소년 버튼 이벤트		
		for(i=0;i<adultB.length;i++) {
			if(obj==adultB[i]){
				setAdultL.setText(adultL.getText()+adultB[i].getText()+"명");
				adultCount=Integer.parseInt(adultB[i].getText());
				setPriceL.setText((adultCount*9000)+(childCount*6000)+" ");
			}// if else
			// 0선택 => 초기화
			if(obj==adultB[0]) {
				setAdultL.setText("");
				childB[1].setEnabled(true);
				childB[2].setEnabled(true);
				childB[3].setEnabled(true);
				childB[4].setEnabled(true);
				childB[5].setEnabled(true);
				childB[6].setEnabled(true);
				childB[7].setEnabled(true);
				childB[8].setEnabled(true);
			}// if
			// 성인 1명 버튼 눌렀을 때			
			if(obj==adultB[1]) {
				childB[1].setEnabled(true);
				childB[2].setEnabled(true);
				childB[3].setEnabled(true);
				childB[4].setEnabled(true);
				childB[5].setEnabled(true);
				childB[6].setEnabled(true);
				childB[7].setEnabled(true);
				childB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			// 성인 2명 버튼 눌렀을 때
			if(obj==adultB[2]) {
				childB[1].setEnabled(true);
				childB[2].setEnabled(true);
				childB[3].setEnabled(true);
				childB[4].setEnabled(true);
				childB[5].setEnabled(true);
				childB[6].setEnabled(true);
				childB[7].setEnabled(false);
				childB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			};// if
			// 성인 3명 버튼 눌렀을 때
			if(obj==adultB[3]) {
				childB[1].setEnabled(true);
				childB[2].setEnabled(true);
				childB[3].setEnabled(true);
				childB[4].setEnabled(true);
				childB[5].setEnabled(true);
				childB[6].setEnabled(false);
				childB[7].setEnabled(false);
				childB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if			
			// 성인 4명 버튼 눌렀을 때
			if(obj==adultB[4]) {
				childB[1].setEnabled(true);
				childB[2].setEnabled(true);
				childB[3].setEnabled(true);
				childB[4].setEnabled(true);
				childB[5].setEnabled(false);
				childB[6].setEnabled(false);
				childB[7].setEnabled(false);
				childB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			// 성인 5
			if(obj==adultB[5]) {
				childB[1].setEnabled(true);
				childB[2].setEnabled(true);
				childB[3].setEnabled(true);
				childB[4].setEnabled(false);
				childB[5].setEnabled(false);
				childB[6].setEnabled(false);
				childB[7].setEnabled(false);
				childB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			// 성인 6
			if(obj==adultB[6]) {
				childB[1].setEnabled(true);
				childB[2].setEnabled(true);
				childB[3].setEnabled(false);
				childB[4].setEnabled(false);
				childB[5].setEnabled(false);
				childB[6].setEnabled(false);
				childB[7].setEnabled(false);
				childB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			// 성인 7
			if(obj==adultB[7]) {
				childB[1].setEnabled(true);
				childB[2].setEnabled(false);
				childB[3].setEnabled(false);
				childB[4].setEnabled(false);
				childB[5].setEnabled(false);
				childB[6].setEnabled(false);
				childB[7].setEnabled(false);
				childB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			// 성인 8
			if(obj==adultB[8]) {
				childB[1].setEnabled(false);
				childB[2].setEnabled(false);
				childB[3].setEnabled(false);
				childB[4].setEnabled(false);
				childB[5].setEnabled(false);
				childB[6].setEnabled(false);
				childB[7].setEnabled(false);
				childB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
		}// for
		// 청소년 버튼
		for(int j=0;j<childB.length;j++) {
			if(obj==childB[j]){
				setChildL.setText(" "+childL.getText()+childB[j].getText()+"명");
				childCount=Integer.parseInt(childB[j].getText());
				setPriceL.setText((adultCount*9000)+(childCount*6000)+"");
			}// if
			// 0 선택 => 초기화
			if(obj==childB[0]) {
				setChildL.setText("");
				adultB[1].setEnabled(true);
				adultB[2].setEnabled(true);
				adultB[3].setEnabled(true);
				adultB[4].setEnabled(true);
				adultB[5].setEnabled(true);
				adultB[6].setEnabled(true);
				adultB[7].setEnabled(true);
				adultB[8].setEnabled(true);
			}// if
			if(obj==childB[1]) {
				adultB[1].setEnabled(true);
				adultB[2].setEnabled(true);
				adultB[3].setEnabled(true);
				adultB[4].setEnabled(true);
				adultB[5].setEnabled(true);
				adultB[6].setEnabled(true);
				adultB[7].setEnabled(true);
				adultB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			if(obj==childB[2]) {
				adultB[1].setEnabled(true);
				adultB[2].setEnabled(true);
				adultB[3].setEnabled(true);
				adultB[4].setEnabled(true);
				adultB[5].setEnabled(true);
				adultB[6].setEnabled(true);
				adultB[7].setEnabled(false);
				adultB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			if(obj==childB[3]) {
				adultB[1].setEnabled(true);
				adultB[2].setEnabled(true);
				adultB[3].setEnabled(true);
				adultB[4].setEnabled(true);
				adultB[5].setEnabled(true);
				adultB[6].setEnabled(false);
				adultB[7].setEnabled(false);
				adultB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			if(obj==childB[4]) {
				adultB[1].setEnabled(true);
				adultB[2].setEnabled(true);
				adultB[3].setEnabled(true);
				adultB[4].setEnabled(true);
				adultB[5].setEnabled(false);
				adultB[6].setEnabled(false);
				adultB[7].setEnabled(false);
				adultB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			if(obj==childB[5]) {
				adultB[1].setEnabled(true);
				adultB[2].setEnabled(true);
				adultB[3].setEnabled(true);
				adultB[4].setEnabled(false);
				adultB[5].setEnabled(false);
				adultB[6].setEnabled(false);
				adultB[7].setEnabled(false);
				adultB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			if(obj==childB[6]) {
				adultB[1].setEnabled(true);
				adultB[2].setEnabled(true);
				adultB[3].setEnabled(false);
				adultB[4].setEnabled(false);
				adultB[5].setEnabled(false);
				adultB[6].setEnabled(false);
				adultB[7].setEnabled(false);
				adultB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			if(obj==childB[7]) {
				adultB[1].setEnabled(true);
				adultB[2].setEnabled(false);
				adultB[3].setEnabled(false);
				adultB[4].setEnabled(false);
				adultB[5].setEnabled(false);
				adultB[6].setEnabled(false);
				adultB[7].setEnabled(false);
				adultB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
			// 청소년 8
			if(obj==childB[8]) {
				adultB[1].setEnabled(false);
				adultB[2].setEnabled(false);
				adultB[3].setEnabled(false);
				adultB[4].setEnabled(false);
				adultB[5].setEnabled(false);
				adultB[6].setEnabled(false);
				adultB[7].setEnabled(false);
				adultB[8].setEnabled(false);
				infoNextB.setEnabled(true);
			}// if
		}// for
		// --- 캘린더 버튼 이벤트
		for(i=0;i<calDate.length;i++) {
			// --- 캘린더 버튼 클릭시
			if(obj==calDate[i]) {
				java.util.Date nowCal = new java.util.Date();		
				setDaysL.setText(calYear.getText()+"년"+calMonth.getText()+"월 "+calDate[i].getText()+"일");
				secretDate.setText(calDate[i].getText());
				// 날짜 시간 리스트 생성
				screendate=Date.valueOf(calYear.getText()+"-"+calMonth.getText()+"-"+calDate[i].getText());
				screenNum=(String)cinemaList.getSelectedValue();
				timeBG.clearSelection();
				setTimesL.setText("");
				List<MovietimeVO> mL=bdao.dayTimeList(screendate,screenNum);
				if((mL != null) && mL.size()>0) {
					for(int j=0;j<mL.size();j++) {
						MovietimeVO mtvo=mL.get(j);
						time.add(mtvo.getScreentime());
					}// for
				}// if
				for(int j=0;j<time.size();j++) {
					String movieTime = calYear.getText()+calMonth.getText()+calDate[i].getText()+time.get(j).replaceAll("[^0-9]","");
					try {
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmm");
						java.util.Date movieStart = dateFormat.parse(movieTime);
						if(timeGroup.getComponentCount()<=5) {
							timeBList.add(new JToggleButton());
						}// if
						timeBList.get(j).setBackground(Color.BLACK);
						timeBList.get(j).setForeground(Color.WHITE);
						timeBList.get(j).setText(time.get(j));
						timeBList.get(j).addActionListener(this);
						timeBG.add(timeBList.get(j));
						timeGroup.add(timeBList.get(j));
						if(j==4) {
							time.clear();
						}// if
						if(nowCal.getTime() >= movieStart.getTime()) {
							timeBList.get(j).setEnabled(false);
						}else if(nowCal.getTime() < movieStart.getTime()){
							timeBList.get(j).setEnabled(true);
						}// if else
						timeBList.get(j).setVisible(true);
					}catch(Exception de) {de.printStackTrace();}
				}// for
			}// if
		}// for	

		// --- 다음, 이전단계 버튼 이벤트
		if(obj==infoNextB) {
			seatPaymentB.setEnabled(false);
			if(setAdultL.getText().trim().equals("") && setChildL.getText().trim().equals("")) {
				JOptionPane.showMessageDialog(this,"인원수를 선택해주세요!");
			}else {
				infoNextB.setVisible(false);
				seatBackB.setVisible(true);
				seatPaymentB.setVisible(true);
				CARD.next(reservCard);
				time_code=cinemaList.getSelectedValue()+calMonth.getText()+secretDate.getText()+timeCount;
				List<DayseatVO> sS=bdao.getBookingSeat(time_code);
				if((sS != null) && sS.size()>0) {
					for(i=0;i<sS.size();i++) {
						DayseatVO dvo=sS.get(i);
						seat_status.add(dvo.getSeat_status());
						seat_Num.add(dvo.getSeat_Num());
					}// for
				}// if				
				List<SeatVO> sV=bdao.setScreenSeat(screenNum);
				if((sV != null) && sV.size()>0) {
					for(i=0;i<sV.size();i++) {
						SeatVO svo=sV.get(i);
						seatButton.add(new JButton());
						seatButton.get(i).setText(svo.getSeatrow()+svo.getSeatcol());
						seatButton.get(i).setMargin(new Insets(2,0,2,0));
						seatButton.get(i).setBackground(Color.BLACK);
						seatButton.get(i).setForeground(Color.WHITE);			
						if(seatIndex<sV.size()) {
							seatButton.get(i).addActionListener(this);
						}// if
						seatIndex++;
						int seatPlace=Integer.parseInt(seatButton.get(i).getText().replaceAll(("[^0-9]"), ""));
						if(seatPlace<=5) {
							seatButton.get(i).setMargin(new Insets(2,3,2,3));
							seatButton.get(i).setText(svo.getSeatrow()+svo.getSeatcol());
							leftArea.add(seatButton.get(i));
							seatPanel.add(leftArea,"West");			
						}else if(5<seatPlace && seatPlace<=15) {
							seatButton.get(i).setText(svo.getSeatrow()+svo.getSeatcol());
							centerArea.add(seatButton.get(i));
							seatPanel.add(centerArea,"Center");
						}else if(seatPlace<=20) {
							seatButton.get(i).setText(svo.getSeatrow()+svo.getSeatcol());
							rightArea.add(seatButton.get(i));
							seatPanel.add(rightArea,"East");
						}// if else if
						if(seat_status.get(i)==1 && seat_Num.get(i).equals(cinemaList.getSelectedValue()+seatButton.get(i).getText())) {
							seatButton.get(i).setEnabled(false);
						}else if(seat_status.get(i)==0){
							seatButton.get(i).setEnabled(true);
						}// if else
					} // for
				}// if				
			}// if else
		}// if
		// 이전단계 버튼
		if(obj==seatBackB) {
			infoNextB.setVisible(true);
			seatPaymentB.setVisible(false);
			seatBackB.setVisible(false);
			CARD.previous(reservCard);
			
			seat_status.clear();
			seat_Num.clear();
			seatButtonReset();
		}// if
		// 결제단계 버튼
		if(obj==seatPaymentB) {
			setPaymentDialog().setVisible(true);
			paymentSiteBG.clearSelection();
		}// if
		// --- 좌석 선택 버튼
		for(i=0;i<seatButton.size();i++) {
			if(obj==seatButton.get(i)) {
				if(seatButton.get(i).getBackground()==Color.BLACK) {
					if(0<=index && index<(adultCount+childCount)) {
						seatButton.get(i).setBackground(Color.RED);
						seatList.add(new JLabel());
						seatChoiceP.add(seatList.get(index),"Center");
						seatList.get(index).setText(seatButton.get(i).getText());
						seatList.get(index).setHorizontalAlignment(JLabel.CENTER);
						setSeatL.setText(setSeatL.getText().concat(seatList.get(index).getText()+" "));
						secretSeatNum.setText(seatButton.get(i).getText());
						seatPaymentB.setEnabled(false);
						index++;
						if(index == adultCount+childCount) {
							seatPaymentB.setEnabled(true);
						}// if
					}else {
						JOptionPane.showMessageDialog(this,"모두 선택되었습니다!");
					}// if else if
					// --- 재선택된 좌석 지우기
				}else if(seatButton.get(i).getBackground()==Color.RED) {
					seatButton.get(i).setBackground(Color.BLACK);
					for(int k=0;k<seatList.size();k++) {						
						if(seatList.get(k).getText().equals(seatButton.get(i).getText())){
							setSeatL.setText(setSeatL.getText().replace(" "+seatList.get(k).getText(),"")
									.replace(seatList.get(k).getText(),""));
							seatChoiceP.remove(seatList.get(k));
							seatList.remove(k);
							seatChoiceP.revalidate();
							seatChoiceP.repaint();
							index--;
						}// if						
					}// outer for
				}// if else
			}// 좌석버튼 클릭 시
		}// outer for
		// 좌석 초기화 버튼
		if(obj==seatReset) {
			seatButtonReset();
			seatPaymentB.setEnabled(false);
		}// if
		// 결제 다이얼로그 닫기 버튼
		if(obj==seatChoicePrev) {
			paymentD.dispose();
		}// if
		// --- 결제하기 버튼 눌렀을 때
		if(obj==paymentB) {
			// --- Booking 저장
			bvo.setPrice(Integer.parseInt(setPriceL.getText().trim()));
			bvo.setSeatcount(adultCount+childCount);
			bvo.setMovie_code(movie_code);
			bvo.setMember_id(AppManager.getInstance().getMainUi().myPageC.idCL.getText());
			bvo.setTime_code(cinemaList.getSelectedValue()+calMonth.getText()+secretDate.getText()+timeCount);		
			int insertRe=bdao.setBooking(bvo);			

			// --- day_seat 예약좌석 변경
			String[] seatArr=setSeatL.getText().split("\\s");
			String time_code=cinemaList.getSelectedValue()+calMonth.getText()+secretDate.getText()+timeCount;
			dvo.setTime_code(time_code);
			dvo.setScreen((String)cinemaList.getSelectedValue());
			int updateRe=bdao.setBookingSeat(seatArr,dvo);
			for(int j=0;j<900000;j++) {}
			if(updateRe==1) {
			// --- booking_code 저장한 리스트 불러오기
			List<BookingVO> b_code=bdao.getBookingCode();
			if((b_code != null) && b_code.size()>0) {
				for(i=0;i<b_code.size();i++) {
					BookingVO bvo=b_code.get(i);
					booking_code.add(bvo.getBooking_code());
				}// for
			}// if

			// --- booking_code 마지막 인덱스값을 가져옴
			if(!(booking_code.isEmpty())) {
				int lastBooking_code=booking_code.get(booking_code.size()-1);
				bsvo.setBooking_code(lastBooking_code);
			}else {
				bsvo.setBooking_code(1);
			}// if else
			bsvo.setScreen((String)cinemaList.getSelectedValue());
			int insertBookedRe=bdao.setBookedSeat(bsvo,seatArr);

			if(updateRe==1 && insertBookedRe==1) {
				JOptionPane.showMessageDialog(this,"결제가 완료되었습니다.");
				// --- 초기화
				listReset();
				calendarButtonReset();
				timeButtonReset();
				peopleButtonReset();
				seatButtonReset();
				paymentSiteBG.clearSelection();
				CARD.show(reservCard,"home");
				paymentD.dispose(); // 결제창 종료
			}
			}else {
				JOptionPane.showMessageDialog(this,"결제에 실패했습니다.");
			}// if else

			// --- 버튼 초기화
			infoNextB.setEnabled(false);
			infoNextB.setVisible(true);
			seatPaymentB.setVisible(false);
			seatBackB.setVisible(false);			
		}// if
		if(obj==paymentCash) {
			CARD.show(paymentChoiceCard,"cash");
		}else if(obj==paymentCard) {
			CARD.show(paymentChoiceCard,"card");
		}// if else
		if(obj==paymentSiteCash) {
			paymentOption.setText(paymentSiteCash.getText());
			paymentOptionPrice.setText(paymentPrice.getText());
			paymentB.setEnabled(true);
		}else if(obj==paymentSiteCard) {
			paymentOption.setText(paymentSiteCard.getText());
			paymentOptionPrice.setText(paymentPrice.getText());
			paymentB.setEnabled(true);
		}

	}//aP()

	@Override
	public void valueChanged(ListSelectionEvent e) {
		Object obj=e.getSource();
		if(!e.getValueIsAdjusting()) {
			// 영화 리스트 선택 했을 시
			if(obj==movieList) {
				if(movieList.getSelectedValue()==null) {
					return;
				}
				movieNameL.setText((String)movieList.getSelectedValue()); // 영화이름제목 라벨 작성
				for(int i=0;i<movieVector.size();i++) {
					if(movieNameL.getText().equals(movieVector.get(i))){
						movie_code=codeVector.get(i);
						// --- 포스터 버튼에 해당하는 이미지 삽입
						secretImgL.setText(movieImg.get(i));
						secretAgeL.setText(movieAge.get(i));
						ImageIcon mainposter = new ImageIcon("pic/"+movieImg.get(i));		
						Image originImg = mainposter.getImage();
						Image changeImg = originImg.getScaledInstance(263,300,Image.SCALE_SMOOTH);		
						ImageIcon poster = new ImageIcon(changeImg);

						moviePosterB.setIcon(poster);
					}// if
				}// for
				// 상영관 리스트 출력
				Vector<CinemaVO> cV=bdao.cinemaList(movie_code);
				if((cV != null) && cV.size()>0) {
					for(int i=0;i<cV.size();i++) {
						CinemaVO cvo=cV.get(i);
						cinemaVector.clear();
						cinemaVector.add(cvo.getScreen());
					}// for
					cinemaList.setListData(cinemaVector);

					peopleButtonReset();
					calendarButtonReset();
					if(!timeBList.isEmpty()) {
						timeButtonReset();
					}//if
					cinemaList.clearSelection();					
					infoNextB.setEnabled(false);
				}// if
				// 상영관리스트 선택했을 시
			}else if(obj==cinemaList) {
				java.util.Date nowTime=new java.util.Date();
				setCinemaL.setText((String)cinemaList.getSelectedValue());
				// 좌석 배치

				screenNum=cinemaList.getSelectedValue()+"";				
				Vector<MovietimeVO> mL=bdao.dayList(screenNum);
				if((mL != null) && mL.size()>0) {
					for(int i=0;i<mL.size();i++) {
						MovietimeVO mtvo=mL.get(i);
						dateList.add(mtvo.getScreendate());
					}// for
				}// if ==> 날짜리스트 if
				for(int i=0;i<dateList.size();i++) {
					try {
						String movieTime=dateList.get(i).replaceAll("[^0-9]","").substring(0,8)+235959;
						SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMddHHmmss");
						java.util.Date movieStart = dateFormat.parse(movieTime);
						for(int j=0;j<calDate.length;j++) {
							if((nowTime.getTime() < movieStart.getTime()) && (dateList.get(i).substring(8,10).equals(calDate[j].getText()))) {
								calDate[j].setEnabled(true);
							}// if
						}// inner for
					}catch(Exception de) {de.printStackTrace();}
				}// outer for
			}// if else => 상영관 리스트 선택 시
		}// 리스트 항목에서 버튼을 뗐을 때
	}//vC()
	// 인원선택 버튼 리셋
	public void peopleButtonReset() {
		// 성인 버튼
		adultButtonP.removeAll();
		for(int i=0;i<adultB.length;i++) {
			adultB[i]=new JToggleButton(i+"");
			adultBG.add(adultB[i]);
			adultB[i].setFont(new Font("맑은 고딕",Font.BOLD,10));
			adultB[i].setMargin(new Insets(0,3,0,3));
			adultB[i].setBackground(Color.BLACK);
			adultB[i].setForeground(Color.WHITE);
			adultB[i].addActionListener(this);
			adultB[i].setEnabled(false);
			adultButtonP.add(adultB[i]);
			SwingUtilities.updateComponentTreeUI(adultB[i]); // 토글버튼 선택모델 적용
		}//for
		adultP.revalidate();
		adultP.repaint();
		// 청소년 버튼
		childButtonP.removeAll();
		for(int i=0;i<childB.length;i++) {
			childB[i]=new JToggleButton(i+"");
			childBG.add(childB[i]);
			childB[i].setFont(new Font("맑은 고딕",Font.BOLD,10));
			childB[i].setMargin(new Insets(0,3,0,3));
			childB[i].setBackground(Color.BLACK);
			childB[i].setForeground(Color.WHITE);
			childB[i].addActionListener(this);
			childB[i].setEnabled(false);
			childButtonP.add(childB[i]);
		}//for
		childP.revalidate();
		childP.repaint();

		setPriceL.setText("");
		setAdultL.setText("");
		setChildL.setText("");
		adultCount=0;
		childCount=0;
	}//peopleButtonReset()

	// 좌석선택 버튼 리셋
	public void seatButtonReset() {
		for(int i=0;i<seatButton.size();i++) {
			seatButton.get(i).setBackground(Color.BLACK);
		}// outer for
		for(int k=0;k<seatList.size();k++) {
			seatChoiceP.removeAll();
			seatList.remove(k);
			seatChoiceP.revalidate();
			seatChoiceP.repaint();
		}//for
		setSeatL.setText("");
		seat_status.clear();
		index=0;
	}// seatButtonReset()

	public void timeButtonReset() {
		for(int i=0;i<5;i++) {
			timeBList.get(i).setVisible(false);
		}// for
		setTimesL.setText("");
		timeBG.clearSelection();
	}// timeButtonReset();
	// 달력 버튼 초기화
	public void calendarButtonReset() {
		indexDay=1;

		calP.removeAll();
		for(int i=0;i<calDate.length;i++) {
			String sday="";
			if(i<10) {
				sday=" "+i;
			}else {
				sday=""+i;
			}
			calDate[i]=new JToggleButton(sday);
			calDate[i].setEnabled(false);
			calDateBG.add(calDate[i]);
		}//for
		for(int i=0;i<calWeek.length;i++) {
			calWeekL[i]=new JLabel(calWeek[i]);
			calWeekL[i].setHorizontalAlignment(JLabel.CENTER);
			calP.add(calWeekL[i]);
		}//for
		for(int i=1;i<startDay;i++) {
			calP.add(new JLabel(""));
			sunday++;
		}//for
		for(int j=0;j<lastDay;j++) {
			if(sunday%7==1) calDate[indexDay].setForeground(Color.RED);
			else if(sunday%7==0) calDate[indexDay].setForeground(Color.BLUE);
			else calDate[indexDay].setForeground(Color.BLACK);
			sunday++;
			calP.add(calDate[indexDay++]);
		}//for
		while(calP.getComponentCount()<49) {
			calP.add(new JLabel(""));
		}//while
		for(int i=0;i<calDate.length;i++) {
			calDate[i].setBackground(Color.WHITE);
			calDate[i].addActionListener(this);
		}//for
		monthCalP.add(calP);
		monthCalP.revalidate();
		monthCalP.repaint();
		setDaysL.setText("");
	}// calendarButtonReset();
	public void listReset() {
		cinemaList.clearSelection();
		movieList.clearSelection();
		movieNameL.setText("영화 제목");
		moviePosterB.setIcon(null);
		moviePosterB.revalidate();
		moviePosterB.repaint();
	}// listReset()
}//Reservation class
