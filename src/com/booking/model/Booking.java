
package com.booking.model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.booking.constants.Constants;
import com.booking.entity.Flight;
import com.booking.entity.Seat;
import com.booking.entity.Ticket;
import com.booking.util.DataOperateUtil;
import com.booking.util.DateFormatUtil;
import com.booking.util.ValidateDataUtil;


public class Booking {
	
	/*
	 * (非 Javadoc)
	 * 
	 * @param ticket
	 * 
	 * @return
	 * 
	 * @see com.booking.service#createBookingInfo(java.lang.String)
	 */
	
	public String createBookingInfo(Ticket ticket) {
		// 获取当前时间
		Calendar calendar = Calendar.getInstance();
		// 毫秒值
		long date = calendar.getTimeInMillis();
		// 毫秒值转化为字符串
		String orderNo = String.valueOf(date);
		// 结果集
//		Map<String,Object> resultMap = new HashMap<>();
		// 工具类
		DataOperateUtil dataOperateUtil = DataOperateUtil.getDataOperateUtil();
		// 数据结果集
		List<Ticket> tic =  new ArrayList<>();
		//验证身份证和姓名的代码注释掉了，免了测试的时候要输入18位。
		// 验证身份证是否正确
		//ValidateDataUtil validateDataUtil = ValidateDataUtil.getValidateDataUtil();
		// 验证身份证号
		//boolean result = validateDataUtil.validateIdentityNo(ticket.getIdentityNo());
		// 判断身份证号是否正确，
		//if (!result) 
		//	return Constants.MSG_ERR_IDENTITYNO;
		// 验证姓名
		//result = validateDataUtil.validateName(ticket.getUserName());
		// 判断姓名是否正确，
		//if (!result) 
		//	return Constants.MSG_ERR_USERNAME;
		// 生成订单编码
		ticket.setOrderNo(orderNo);
		// 调用写入数据方法
		String operateResult = null;
		// 读取本地磁盘数据
		List<Ticket> ticketList = dataOperateUtil.readTicketData(Constants.FILE_NAME);
		if (ticketList == null){ //机票文件中没有订购的机票，则把当前的机票写入文件
			tic.add(ticket);
			operateResult = dataOperateUtil.writeTicketData(tic);
		}else {//机票文件中有订购的机票，把当前的机票写入其它机票的后面
			ticketList.add(ticket);
			operateResult = dataOperateUtil.writeTicketData(ticketList);
		}
		return operateResult;//Constants.MSG_SUCCESS,MSG_ERR_SAVE_DATA
	}
	
	/*
	 * (非 Javadoc)
	 * 
	 * 
	 * @param orderNoOrIdentityNo
	 * 
	 * @return
	 * 
	 * @see com.booking.service#readData(java.lang.String)
	 */
	
	public Map<String, Object> readData(String orderNoOrIdentityNo) {

		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Ticket> resultList = new ArrayList<>();
		boolean result;
		// 调用工具类
		DataOperateUtil dataOperateUtil = DataOperateUtil.getDataOperateUtil();
		// 查询文件是否存在
		List<Ticket> ticketList = dataOperateUtil.readTicketData(Constants.FILE_NAME);
		// 判断文件是否为空
		if (ticketList == null) {
			resultMap.put(Constants.RESULT_KEY_CODE, Constants.MSG_ERR_GET_DATA);
			return resultMap;
		}else {
			// 循环结果集
			for(Ticket tick : ticketList){
				// 获取身份证号
				String identityNo = tick.getIdentityNo();
				// 获取订单编号
				String orderNo = tick.getOrderNo();
				// 判断参数身份证号与文件中的数据是否相等，并且日期不小于当前日期
				if (tick.getIdentityNo().equals(orderNoOrIdentityNo)){
					resultList.add(tick);
				}
				// 判断参数订单号与文件中的数据是否相等
				if (tick.getOrderNo().equals(orderNoOrIdentityNo)){
					resultList.add(tick);
				}
			}
			resultMap.put(Constants.RESULT_KEY_DATA, resultList);
		}
		return resultMap;
	}
	
	/*
	 * (非 Javadoc)
	 * 
	 * 
	 * @param orderNo
	 * 
	 * @return
	 * 
	 * @see com.booking.service#readSeatData(java.lang.String)
	 */

	public Map<String, Object> readSeatData(String orderNo) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> resultList = new ArrayList<>();
		
		List<String> seatNoList = new ArrayList<>();
		// 调用工具类
		DataOperateUtil dataOperateUtil = DataOperateUtil.getDataOperateUtil();
		// 查询文件是否存在
		List<Map<String, Object>> seatList = dataOperateUtil.readSeatData(Constants.FILE_SEAT_NAME);
		// 判断文件是否为空
		if (seatList == null) {
			resultMap.put(Constants.RESULT_KEY_CODE, Constants.MSG_ERR_GET_DATA);
			return resultMap;
		}else {
			// 循环结果集
			for(Map<String, Object> paramMap : seatList){
				String seatNo = String.valueOf(paramMap.get("seatNo"));
				seatNoList.add(seatNo);
				// 获取身份证号
				String order = String.valueOf(paramMap.get("orderNo"));
				// 判断订单号
				if (orderNo.equals(order)){
					resultList.add(paramMap);
				}
			}
			// 返回结果集
			resultMap.put(Constants.RESULT_KEY_CODE, Constants.MSG_SUCCESS);
			resultMap.put(Constants.RESULT_KEY_DATA, resultList);
			resultMap.put("seatNoList", seatNoList);
		}
		return resultMap;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * 
	 * @param seat、orderList
	 * 
	 * @return
	 * 
	 * @see com.booking.service#createSeatInfo(java.lang.String)
	 */

	public String createSeatInfo(Seat seat,List<String> orderList) {
		// 结果集
		Map<String,Object> resultMap = new HashMap<>();
		
		Map<String,Object> paramMap = new HashMap<>();
		// 工具类
		DataOperateUtil dataOperateUtil = DataOperateUtil.getDataOperateUtil();
		// 数据结果集
		List<Map<String,Object>> seatList =  new ArrayList<>();
		
		// 调用写入数据方法
		String operateResult = null;
		String date = orderList.get(5);
		String order = orderList.get(0);
		String seatNo = seat.getSeatNo();
		paramMap.put("seatNo", seat.getSeatNo());
		paramMap.put("status", seat.getStatus());
		paramMap.put("orderNo", orderList.get(0));
		paramMap.put("start", orderList.get(1));
		paramMap.put("end", orderList.get(2));
		paramMap.put("identityNo", orderList.get(4));
		paramMap.put("date", date);
		DateFormatUtil dateFormatUtil = DateFormatUtil.getDateFormatUtil();
		String nowDate = dateFormatUtil.formatStrByDate(new Date());
		// 判断当前时间是否已经大于飞机起飞时间，如果大于，则提示值机失败
		if (date.compareTo(nowDate) < 0) 
			return Constants.MSG_ON_DUTY;
		// 读取本地磁盘数据
		List<Map<String,Object>> seatListResult = dataOperateUtil.readSeatData(Constants.FILE_SEAT_NAME);
		if (seatListResult == null){
			seatList.add(paramMap);
			operateResult = dataOperateUtil.writeSeatData(seatList);
		}else {
			List<String> orderNoList =  new ArrayList<>();
			for (Map<String,Object> map : seatListResult) {
				String orderNo = String.valueOf(map.get("orderNo"));
				// 判断如果订单编号相等，则将座位号修改为最新数据
				if (orderNo.equals(order)) {
					map.replace("seatNo", map.get("seatNo"), seatNo);
				}
				orderNoList.add(orderNo);
			}
			
			if (!orderNoList.contains(order)) {
				seatListResult.add(paramMap);
			}
			operateResult = dataOperateUtil.writeSeatData(seatListResult);
		}
		return operateResult;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * 
	 * @param orderNo、travelDate
	 * 
	 * @return
	 * 
	 * @see com.booking.service#updateTravelDateInfo(java.lang.String)
	 */

	public String  updateTravelDateInfo(String orderNo,String travelDate) {
		// 调用写入数据方法
		String operateResult = null;
		// 工具类
		DataOperateUtil dataOperateUtil = DataOperateUtil.getDataOperateUtil();
		// 判断参数不为空则执行读取文件
		if (!"".equals(orderNo) && !"".equals(travelDate)) {
			// 读取本地磁盘数据
			List<Ticket> ticketList = dataOperateUtil.readTicketData(Constants.FILE_NAME);
			// 读取本地磁盘数据
			List<Map<String,Object>> seatList = dataOperateUtil.readSeatData(Constants.FILE_SEAT_NAME);
			// 判断数据是否为空
			if  (ticketList != null) {
				// 循环获取购票信息
				for (Ticket ticket : ticketList) {
					// 获取订单编号
					String order = ticket.getOrderNo();
					// 判断获取的订单编号与参数是否相等，相等则修改该订单出行时间
					if (order.equals(orderNo)) {
						ticket.setTravelDate(travelDate);
					}
				}
			}
			if (seatList.size() > 0 && seatList != null) {
				// 循环获取订单信息
				for (Map<String,Object> seatMap : seatList) {
					// 获取订单编号
					String order = String.valueOf(seatMap.get("orderNo"));
					String date = String.valueOf(seatMap.get("date"));
					// 判断获取的订单编号与参数是否相等，相等则删除该条数据
					if (order.equals(orderNo)) {
						seatMap.replace("date", date, travelDate);
					}
				}
				// 执行写入数据
				operateResult = dataOperateUtil.writeSeatData(seatList);
			}
			// 执行写入数据
			operateResult = dataOperateUtil.writeTicketData(ticketList);
		}
		return operateResult;
	}
	
	/*
	 * (非 Javadoc)
	 * 
	 * 
	 * @param orderNo
	 * 
	 * @return
	 * 
	 * @see com.booking.service#deleteOrderInfo(java.lang.String)
	 */

	public String deleteOrderInfo(String orderNo) {
		// 调用写入数据方法
		String operateResult = null;
		// 工具类
		DataOperateUtil dataOperateUtil = DataOperateUtil.getDataOperateUtil();
		// 判断参数订单编号是否为空
		if (!"".equals(orderNo)) {
			// 读取本地磁盘数据
			List<Ticket> ticketList = dataOperateUtil.readTicketData(Constants.FILE_NAME);
			List<Map<String,Object>> seatList = dataOperateUtil.readSeatData(Constants.FILE_SEAT_NAME);
			// 判断数据是否为空
			if  (ticketList != null) {
				// 循环获取订单信息
				for (int i = 0; i < ticketList.size();i++) {
					// 获取订单编号
					String order = ticketList.get(i).getOrderNo();
					// 判断获取的订单编号与参数是否相等，相等则删除该条数据
					if (order.equals(orderNo)) {
						ticketList.remove(i);
					}
				}
			}
			
			if (seatList.size() > 0 && seatList != null) {
				// 循环获取订单信息
				for (int j = 0; j < seatList.size();j++) {
					// 获取订单编号
					String order = String.valueOf(seatList.get(j).get("orderNo"));
					// 判断获取的订单编号与参数是否相等，相等则删除该条数据
					if (order.equals(orderNo)) {
						seatList.remove(j);
					}
				}
				// 执行写入数据
				operateResult = dataOperateUtil.writeSeatData(seatList);
			}
			
			// 执行写入数据
			operateResult = dataOperateUtil.writeTicketData(ticketList);
		}
		return operateResult;//Constants.MSG_SUCCESS,MSG_ERR_SAVE_DATA
	}
	
	
	/*
	 * (非 Javadoc)
	 * 
	 * 
	 * @param startAddress
	 * @param endAddress
	 * 
	 * @return
	 * 
	 * @see com.booking.service#readFlightData(java.lang.String,java.lang.String)
	 */

	public Map<String, Object> readFlightData(String startAddress, String endAddress) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Flight> resultList = new ArrayList<>();
		// 调用工具类
		DataOperateUtil dataOperateUtil = DataOperateUtil.getDataOperateUtil();
		// 查询文件是否存在
		List<Flight> ticketList = dataOperateUtil.readFlightData(Constants.FILE_FLIGHTS_NAME);
		// 判断文件是否为空
		if (ticketList == null) {
			resultMap.put(Constants.RESULT_KEY_CODE, Constants.MSG_ERR_GET_DATA);
			return resultMap;
		}else {
			// 循环结果集
			for(Flight tick : ticketList){
				// 筛选所选始发地、目的地的航班信息
				if (tick.getStartAddress().equals(startAddress) && tick.getEndAddress().equals(endAddress)){
					resultList.add(tick);
				}
			}
			// 返回结果集
			resultMap.put(Constants.RESULT_KEY_CODE, Constants.MSG_SUCCESS);
			resultMap.put(Constants.RESULT_KEY_DATA, resultList);
		}
		return resultMap;
	}

}
