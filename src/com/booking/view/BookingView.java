/**  
 * @Title: BookingView.java
 * @Package com.booking.view
 * @author 姜向阳
 * @date 2018年7月3日
 * @version V1.0  
 */
package com.booking.view;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.booking.constants.Constants;
import com.booking.entity.Ticket;
import com.booking.model.Booking;
import com.booking.util.CalendarPanel;
import com.booking.util.DateFormatUtil;
import com.booking.util.ViewBackgroundUtil;


public class BookingView extends BaseFrame implements ActionListener{

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 2870945371327830406L;


	/**
	 * @Fields startPlace : 始发地
	 */
	private JComboBox<String> startComboBox;
	/**
	 * @Fields endPlace : 目的地
	 */
	private JComboBox<String> endComboBox;
	/**
	 * @Fields idNumber : 身份证号码
	 */
	private JTextField idNumber;
	/**
	 * @Fields name : 姓名
	 */
	private JTextField name;
	/**
	 * @Fields date : 出行日期
	 */
	private JTextField startDate;
	/**
	 * @Fields flightTextField : 航班号
	 */
	private JTextField flightTextField;
	/**
	 * @Fields confirmBtn : 确认按钮
	 */
	private JButton confirmBtn;
	/**
	 * @Fields backBtn : 返回按钮
	 */
	private JButton backBtn;
	/**
	 * @Fields flightListDialog : 航班选择对话窗口
	 */
	private FlightListView flightListDialog;
	String[] startComboData;
	String[] endComboData;

	public JTextField getFlightNo() {
		return flightTextField;
	}

	public BookingView() {
		init();
	}

	private void init() {
		// 设置背景图片
		ViewBackgroundUtil.setBG(this, "img/bg2.jpg");
		// 定义下拉列表的条目
        startComboData = new String[]{"大连","北京","上海","广州","深圳","成都","昆明","哈尔滨","长春","沈阳","天津","西安","乌鲁木齐","郑州","武汉","无锡","南京","温州","重庆","三亚","厦门","长沙","青岛","杭州"};
        endComboData = new String[]{"大连","北京","上海","广州","深圳","乌鲁木齐","郑州","武汉","成都","三亚","厦门","长沙","昆明","哈尔滨","长春","沈阳","天津","西安","无锡","南京","温州","重庆","青岛","杭州"};
		// 始发地标签
		JLabel startLabel = new JLabel("始发地:");
		startLabel.setBounds(180, 160, 90, 33);
		// 文字右对齐
		startLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		// 始发地下拉列表
		startComboBox = new JComboBox<>(startComboData);
		startComboBox.setBounds(380, 160, 200, 33);
        // 设置默认选中的条目
		startComboBox.setSelectedIndex(0);
		// 目的地标签
		JLabel endLabel = new JLabel("目的地:");
		endLabel.setBounds(180, 200, 90, 33);
		// 文字右对齐
		endLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		// 目的地下拉列表
		endComboBox = new JComboBox<>(endComboData);
		endComboBox.setBounds(380, 200, 200, 33);
		endComboBox.setToolTipText("请输入目的地");
		
		// 身份证号码
		JLabel idLabel = new JLabel("身份证号:");
		idLabel.setBounds(180, 240, 90, 33);
		// 文字右对齐
		idLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		// 身份证文本框
		idNumber = new JTextField();
		idNumber.setBounds(380, 240, 200, 33);
		idNumber.setToolTipText("请输入身份证号码");

		// 姓名
		JLabel nameLabel = new JLabel("姓名:");
		nameLabel.setBounds(180, 280, 90, 33);
		// 文字右对齐
		nameLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		// 姓名文本框
		name = new JTextField();
		name.setBounds(380, 280, 200, 33);
		name.setToolTipText("请输入姓名");
		
		// 出行日期
		JLabel dateLabel = new JLabel("出行日期:");
		dateLabel.setBounds(180, 320, 90, 33);
//		dateLabel.setForeground(Color.ORANGE);
		// 文字右对齐
		dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		// 出行日期文本框
		startDate = new JTextField();
		startDate.setBounds(380, 320, 200, 33);
		startDate.setToolTipText("请输入出行日期");
		// 定义日历控件面板类
		//CalendarPanel calendar = new CalendarPanel(startDate, "yyyy-MM-dd HH:mm:ss");
		CalendarPanel calendar = new CalendarPanel(startDate, "yyyy-MM-dd");
		calendar.initCalendarPanel();
//		calendar.add(date);
		
		// 航班号码
		JLabel flightLabel = new JLabel("选择航班:");
		flightLabel.setBounds(180, 360, 90, 33);
		// 文字右对齐
		flightLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		// 航班号码文本框
		flightTextField = new JTextField();
		flightTextField.setBounds(380, 360, 200, 33);
		flightTextField.setToolTipText("请点击选择航班");

		flightTextField.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				Ticket ticket = new Ticket();
				ticket.setStartAddress(startComboData[startComboBox.getSelectedIndex()]);
				ticket.setEndAddress(endComboData[endComboBox.getSelectedIndex()]);
				//BookingView.this 代表BookingView类的对象
				flightTextField.setText("");
				flightListDialog = new FlightListView(BookingView.this,flightTextField, ticket);
//				flightListDialog = new FlightListView(BookingView.this,true,"航班信息", flightTextField, ticket);
				flightListDialog.setSize(600,520);
				flightListDialog.setVisible(true);
				flightListDialog.setLocationRelativeTo(null);
			}
		});
		
		// 实例化确认按钮
		confirmBtn = new CustomButton(460, 480, CustomButton.RIGHT);
		confirmBtn.setText("确认");
		confirmBtn.addActionListener(this);
		
		// 实例化返回按钮
		backBtn = new CustomButton(160, 480, CustomButton.LEFT);
		backBtn.setText("返回");
		backBtn.addActionListener(this);
		backBtn.setActionCommand("backToMainView");


		this.add(startLabel);
		this.add(startComboBox);
		this.add(endLabel);
		this.add(endComboBox);
		this.add(idLabel);
		this.add(idNumber);
		this.add(nameLabel);
		this.add(name);
		this.add(dateLabel);
		this.add(startDate);
		this.add(calendar);
		this.add(flightLabel);
		this.add(flightTextField);
		this.add(backBtn);
		this.add(confirmBtn);
		this.setTitle("购票窗口");
		// 设置窗口无标题栏
//		this.setUndecorated(true);
//		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		
	}
		@Override
		public void actionPerformed(ActionEvent e) {
		
			String actionCommand = e.getActionCommand();
			switch (actionCommand) {

			// 购票窗口点击“返回”按钮
			case "backToMainView":
				//JButton btn = (JButton) e.getSource();
				//JFrame f = (JFrame) btn.getParent().getParent().getParent().getParent().getParent();
				//f.setVisible(false);
				//f.dispose();
				this.dispose();
				break;
			
			
			case "确认":
			
			String startAddress = startComboData[startComboBox.getSelectedIndex()];
			String endAddress = endComboData[endComboBox.getSelectedIndex()];
			String identityNo = idNumber.getText();
			String username = name.getText();
			String startdate = startDate.getText();
			String airNo = flightTextField.getText();
			if (startAddress.equals(endAddress)) {
				JOptionPane.showMessageDialog(BookingView.this, "始发地和目的地不能相同！",
						"提示信息", JOptionPane.ERROR_MESSAGE);
				return;
			}
			if ("".equals(identityNo)) {
				JOptionPane.showMessageDialog(BookingView.this, "身份证号不能为空，请填写身份证号！",
						"提示信息", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if ("".equals(username)) {
				JOptionPane.showMessageDialog(BookingView.this, "姓名不能为空，请填写姓名！",
						"提示信息", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if ("".equals(startdate)) {
				JOptionPane.showMessageDialog(BookingView.this, "出行时间不能为空，请填写出行时间！",
						"提示信息", JOptionPane.ERROR_MESSAGE);
				return;
			}
//			else {
//				// 获取当前时间
//				String nowDate = DateFormatUtil.getDateFormatUtil().formatStrByDate(new Date());
//				// 判断出行时间不能早于当前时间
//				if(startdate.compareToIgnoreCase(nowDate) < 0){
//					JOptionPane.showMessageDialog(BookingView.this, "出行时间不能早于当前时间！",
//							"提示信息", JOptionPane.ERROR_MESSAGE);
//					return;
//				}
//			}
			if ("".equals(airNo)) {
				JOptionPane.showMessageDialog(BookingView.this, "请选择航班！",
						"提示信息", JOptionPane.ERROR_MESSAGE);
				return;
			}
			Ticket ticket = new Ticket();
			ticket.setStartAddress(startAddress);
			ticket.setEndAddress(endAddress);
			ticket.setIdentityNo(identityNo);
			ticket.setUserName(username);				
			ticket.setTravelDate(startdate);
			
			Booking bookService = new Booking();
			String resultCode =bookService.createBookingInfo(ticket);
			// 验证身份证号输入是否合法
			//if (Constants.MSG_ERR_IDENTITYNO.equals(resultCode)) {
				//填入代码，完成身份证号错误信息提示
				// ........
				// ........
			
			
			//}		
			if (JOptionPane.showConfirmDialog(null, "确定去支付？", "提示", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
				BookingView.this.setVisible(false);
				PayView payView = new PayView(identityNo);
				payView.setVisible(true);
			}
		}
		}}

