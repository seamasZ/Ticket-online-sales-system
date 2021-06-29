package com.booking.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.RowSorter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import com.booking.constants.Constants;
import com.booking.entity.Flight;
import com.booking.entity.Ticket;
import com.booking.model.Booking;

import sun.swing.DefaultLookup;

public class FlightListView extends JFrame  {
  private JFrame frame = null;
  private JTextField flightTextField;
  private Ticket ticket;
  private JButton choose;
  

  public FlightListView() {
      this(null,null,null);
  }
  public FlightListView(JFrame frame) {
      this(frame,null,null);
  }

  public FlightListView(JFrame frame, JTextField flightTextField, Ticket ticket) {
      super("航班信息");
      this.frame = frame;
      this.flightTextField = flightTextField;
      this.ticket = ticket;
      this.init();
  }
	private void init() {
		//设置购票窗口为不可用
   // frame.setEnabled(false);
	  // 航班列表表格标题
		Vector columnNames = new Vector();
		columnNames.add("航班号");
		columnNames.add("始发地");
		columnNames.add("目的地");
		columnNames.add("始发时间");
		columnNames.add("终到时间");
		columnNames.add("航空公司");
		columnNames.add("机型");
		columnNames.add("票价");
		// 创建指定列名和数据的表格
		DefaultTableModel tableModel = new DefaultTableModel(getTableData(), columnNames);
		//单击JTable某行时背景色改变
		//方法：直接在new table后面写个匿名内部类，重写JTable类的prepareRenderer方法
		JTable table = new JTable(tableModel){
		    public Component prepareRenderer(TableCellRenderer renderer, int row, int column){
		        Component comp = super.prepareRenderer(renderer,row,column);
		        Point p = getMousePosition(); //getMousePosition是JTable的方法
		        if(p!=null){
		            int rowUnderMouse = rowAtPoint(p); //rowAtPoint是JTable的方法
		            if(rowUnderMouse == row){//选中的行，背景置橙色
		                  comp.setBackground(Color.ORANGE);
		           }else{ //没有选中的行，背景色还原
		                  comp.setBackground(DefaultLookup.getColor(this,ui,"Table.alternateRowColor"));
		           }
		        }
		        return comp;
		    }
		};
		//表格排序 ，单击每一列的标题，可以升序、降序排序
		RowSorter sorter = new TableRowSorter(tableModel);
		table.setRowSorter(sorter);
		
		table.setRowHeight(23);
		//将鼠标拖动到组件的边界外并且继续按下鼠标按键时，生成人为鼠标拖动事件
		table.setAutoscrolls(true);
		//设置表格是否填满窗口(垂直方向）
		table.setFillsViewportHeight(true);
		//设置控件可用
		table.setEnabled(false);
		// 设置表格只能单选行
		table.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
  	    table.addMouseListener(new MouseAdapter() {
  		private int rowUnderMouse = -1;
  		public void mouseClicked(MouseEvent e) {
  			// 获取所选中行号
  			int row =((JTable)e.getSource()).rowAtPoint(e.getPoint()); 
  			if(row == -1) { //没有选中有效的数据行
  				return;
  			}
  			flightTextField.setText(tableModel.getValueAt(row, 0).toString());
	      table.repaint();//重绘表格
    		}
    	});
		// 创建显示表格的滚动面板
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 0, 600, 300);
		
		// 实例化选好按钮
		choose = new CustomButton(490, 380, CustomButton.LEFT);
		choose.setText("确定");
		// 将滚动面板添加到边界布局的中间
		this.add(scrollPane, BorderLayout.NORTH);
		this.add(choose);
		choose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 判断是否选中行，没有选中则给予提示，否则打开选座页面
				if ("".equals(flightTextField.getText())) {
						JOptionPane.showMessageDialog(FlightListView.this, "您未选择航班！",
								"提示信息", JOptionPane.ERROR_MESSAGE);
					}else {
						setVisible(false);
						dispose();
						frame.setEnabled(true);
						frame.requestFocus();
					}
				}
			});
    }
	/**
	 * @Title: getTableData
	 * @Description: 获取航班信息table数据
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private Vector getTableData() {
		Vector data = new Vector();
		// 根据始发地和目的地读取相应航班信息
		Booking bookingService = new Booking();
		Map<String, Object> resultMap = bookingService.readFlightData(ticket.getStartAddress(),ticket.getEndAddress());
		List<Flight> flights = (List<Flight>)resultMap.get(Constants.RESULT_KEY_DATA);
		// 如果航班信息为空，直接返回空Vector
		if (flights == null || flights.isEmpty()) {
			return data;
		}
		//临时加的为了去掉航班的日期
		String s=null;
		for (int i = 0; i < flights.size(); i++) {
			Flight t = flights.get(i);
			Vector vector = new Vector();
			vector.add(t.getFlightNo());
			vector.add(t.getStartAddress());
			vector.add(t.getEndAddress());
			s=t.getStartTime();
			if(s.length()>10)s=s.substring(11,16);
			vector.add(s);
			s=t.getArrivedTime();
			if(s.length()>10)s=s.substring(11,16);
			vector.add(s);
			vector.add(t.getAirLineName());
			vector.add(t.getPlaneType());
			vector.add(t.getPrice());
			data.add(vector);
		}
		return data;
	}
}