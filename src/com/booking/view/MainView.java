
package com.booking.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JRootPane;
import javax.swing.Timer;

import com.booking.util.ViewBackgroundUtil;


public class MainView extends BaseFrame implements ActionListener{

	/**
	 * @Fields serialVersionUID
	 */
	private static final long serialVersionUID = 8142213655847297255L;

	private ActionListener actionListener;
	/**
	 * @Fields bookingBtn : 购票按钮
	 */
	private JButton bookingBtn;
	/**
	 * @Fields checkinBtn : 退票/改签按钮
	 */
	private JButton alterBtn;
	/**
	 * @Fields checkinBtn : 值机按钮
	 */
	private JButton checkinBtn;
	/**
	 * @Fields exitBtn : 退出按钮
	 */
	private JButton exitBtn;

	public MainView() {
		
		init();
	}

	private void init() {
		// 设置背景图片
		ViewBackgroundUtil.setBG(this, "img/bg1.jpg");

		// 实例化购票按钮
		bookingBtn = new CustomButton(120, 460, CustomButton.LEFT);
		bookingBtn.setText("购票");
		bookingBtn.addActionListener(this);
		bookingBtn.setActionCommand("bookingBtn");
		
		// 实例退改签按钮
		alterBtn = new CustomButton(120, 520, CustomButton.LEFT);
		alterBtn.setText("退票/改签");
		alterBtn.addActionListener(this);
		alterBtn.setActionCommand("alterBtn");

		// 实例化值机按钮
		checkinBtn = new CustomButton(520, 460, CustomButton.RIGHT);
		checkinBtn.setText("值机");
		checkinBtn.addActionListener(this);
		checkinBtn.setActionCommand("checkinBtn");
		
		// 实例退出按钮
		exitBtn = new CustomButton(520, 520, CustomButton.RIGHT);
		exitBtn.setText("退出系统");
		exitBtn.addActionListener(this);
			
		
		
		JLabel welcomelabel = new JLabel("欢迎使用在线购票系统!");
		welcomelabel.setBounds(30, 560, 500, 30);

		// 转换日期显示格式
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JLabel time = new JLabel(df.format(new Date(System.currentTimeMillis())));
		time.setBounds(660, 10, 200, 30);

		Timer timeAction = new Timer(1000, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				long timemillis = System.currentTimeMillis();

				time.setText(df.format(new Date(timemillis)));
			}
		});
		timeAction.start();

		this.add(bookingBtn);
		this.add(alterBtn);
		this.add(checkinBtn);
		this.add(exitBtn);
		this.add(welcomelabel);
		this.add(time);
		// 设置窗口无标题栏
//		this.setUndecorated(true);
//		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		
		this.setTitle("在线购票系统");
		
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		String actionCommand = e.getActionCommand();
		// 根据不同按钮点击，显示不同窗口
		switch (actionCommand) {
		// 点击“购票”按钮
		case "bookingBtn":
			// 显示购票窗口
			BookingView bookingView = new BookingView();
			bookingView.setVisible(true);
			break;
			// 点击“退票/改签”按钮
		case "alterBtn":
			// 显示退票/改签窗口
			//RefundOrAlterView refundOrAlterView = new RefundOrAlterView();
			//refundOrAlterView.setVisible(true);
			break;
		// 点击“值机”按钮
		case "checkinBtn":
			// 显示值机窗口
			//CheckinView checkinView = new CheckinView();
			//checkinView.setVisible(true);
			break;
		case "退出系统":
			System.exit(0);
		}
}}
