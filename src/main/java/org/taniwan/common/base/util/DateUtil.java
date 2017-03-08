package org.taniwan.common.base.util;

import org.taniwan.common.base.emus.DateUnitEnum;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.util.ObjectUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 日期操作工具类
 * 
 * @author 张超
 * @date 2016年4月15日-下午5:49:59
 */
public class DateUtil {

    public static final ThreadLocal<SimpleDateFormat> yyyyMMddHHmmss = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyyMMddHHmmss");
		};
	};
	
	public static final ThreadLocal<SimpleDateFormat> yyyymmddhhmmss = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		};
	};
	
	public static final ThreadLocal<SimpleDateFormat> yyyymmddhhmm = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd HH:mm");
		};
	};
	
	public static final ThreadLocal<SimpleDateFormat> yyyymmdd = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("yyyy-MM-dd");
		};
	};
	
	public static final ThreadLocal<SimpleDateFormat> hhmmss = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm:ss");
		};
	};
	
	public static final ThreadLocal<SimpleDateFormat> hhmm = new ThreadLocal<SimpleDateFormat>() {
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat("HH:mm");
		};
	};

	/**
	 * 比较date2减去date1的分钟差是否大于等于给定的minute
	 * 
	 * @param date1
	 * @param date2
	 * @param minute
	 * @return
	 */
	public static boolean compareGet(Date date1, Date date2, int minute) {
		long between = (date2.getTime() - date1.getTime()) / 1000;// 除以1000是为了转换成秒
//		long day1 = between / (24 * 3600);
//		long hour1 = between % (24 * 3600) / 3600;
		long minute1 = between % 3600 / 60;
//		long second1 = between % 60 / 60;
		if (minute1 >= minute) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 比较date2减去date1的分钟差是否大于给定的minute
	 * @author 张超
	 * @date 2016年6月27日-下午6:54:05
	 * @param date1
	 * @param date2
	 * @param minute
	 * @return
	 */
	public static boolean compareGt(Date date1, Date date2, int minute) {
		long between = (date2.getTime() - date1.getTime()) / 1000;// 除以1000是为了转换成秒
//		long day1 = between / (24 * 3600);
//		long hour1 = between % (24 * 3600) / 3600;
		long minute1 = between % 3600 / 60;
//		long second1 = between % 60 / 60;
		if (minute1 > minute) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 计算当前时间与凌晨的时间差(毫秒)
	 * 
	 * @return
	 */
	public static long getRemainingTime() {
		Date now = new Date();
		Date last = new Date();
		last = DateUtils.setHours(last, 23);
		last = DateUtils.setMinutes(last, 59);
		last = DateUtils.setMilliseconds(last, 59);
		return last.getTime() - now.getTime();
	}

	/**
	 * 比较date减去当前系统时间的分钟差是否大于给定的minute
	 * 
	 * @param date
	 * @param minute
	 * @return
	 */
	public static boolean compare(Date date, int minute) {
		long between = (date.getTime() - new Date().getTime()) / 1000;// 除以1000是为了转换成�?
		long minute1 = between % 3600 / 60;
		if (minute1 > minute) {
			return true;
		} else {
			return false;
		}
	}

	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static Date parseyyyyMMddHHmmss(String time) {
		try {
			return yyyymmddhhmmss.get().parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		throw new BizException(BizErrorConst.SYS_PARAM_ERROR);
		throw new RuntimeException();
	}

	/**
	 * yyyy-MM-dd HH:mm
	 */
	public static Date parseyyyyMMddHHmm(String time) {
		try {
			return yyyymmddhhmm.get().parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		throw new BizException(BizErrorConst.SYS_PARAM_ERROR);
		throw new RuntimeException();
	}
	
	/**
	 * yyyy-MM-dd HH:mm:ss
	 */
	public static String formatyyyyMMddHHmmss(Date time){
		return yyyymmddhhmmss.get().format(time);
	}
	
	/**
	 * HH:mm
	 * 
	 * @author 张超
	 * @date 2016年6月24日-上午11:46:48
	 * @param time
	 * @return
	 */
	public static Date parseHHmm(String time){
		try {
			return hhmm.get().parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
//		throw new BizException(BizErrorConst.SYS_PARAM_ERROR);
		throw new RuntimeException();
	}
	
	/**
	 * HH:mm
	 * 
	 * @author 张超
	 * @date 2016年6月24日-上午11:47:12
	 * @param time
	 * @return
	 */
	public static String formatHHmm(Date time){
		return hhmm.get().format(time);
	}
	
	/**
	 * yyyy-MM-dd
	 * 
	 * @author 张超
	 * @date 2016年6月24日-上午11:46:04
	 * @param time
	 * @return
	 */
	public static String formatyyyyMMdd(Date time){
		return yyyymmdd.get().format(time);
	}
	
	/**
	 * yyyy-MM-dd
	 */
	public static Date parseyyyyMMdd(String time){
		try {
			return yyyymmdd.get().parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * yyyyMMddHHmmss
	 */
	public static String format(Date time){
		return yyyyMMddHHmmss.get().format(time);
	}
	
	/**
	 * yyyyMMddHHmmss
	 */
	public static Date parse(String time){
		try {
			return yyyyMMddHHmmss.get().parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * MM.dd
	 * @author 张超
	 * @date 2016年7月27日-下午7:50:37
	 * @param time
	 * @return
	 */
	public static String formatPointMMdd(Date time){
		SimpleDateFormat sf = new SimpleDateFormat("MM.dd");
		return sf.format(time);
	}
	
	/**
	 * 根据生日计算年龄
	 */
	public static Integer getAgeByBirthday(Date birthday) {
		if(birthday == null){
			return null;
		}
		Calendar cal = Calendar.getInstance();

		if (cal.before(birthday)) {
			throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
		}

		int yearNow = cal.get(Calendar.YEAR);
		int monthNow = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

		cal.setTime(birthday);
		int yearBirth = cal.get(Calendar.YEAR);
		int monthBirth = cal.get(Calendar.MONTH) + 1;
		int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

		int age = yearNow - yearBirth;

		if (monthNow <= monthBirth) {
			if (monthNow == monthBirth) {
				// monthNow==monthBirth
				if (dayOfMonthNow < dayOfMonthBirth) {
					age--;
				}
			} else {
				// monthNow>monthBirth
				age--;
			}
		}
		return age;
	}

	/**
	 * 获取当天或指定日期的开始时间
	 * 
	 * @return
	 */
	public static Date getDayStartTime(Date... day) {
		Date startTime = handlerVariableParam(day);
		startTime = DateUtils.setHours(startTime, 0);
		startTime = DateUtils.setMinutes(startTime, 0);
		startTime = DateUtils.setSeconds(startTime, 0);
		startTime = DateUtils.setMilliseconds(startTime, 0);
		return startTime;
	}

	/**
	 * 获取当天或指定日期的结束时间
	 * 
	 * @return
	 */
	public static Date getDayEndTime(Date... day) {
		Date endTime = handlerVariableParam(day);
		endTime = DateUtils.setHours(endTime, 23);
		endTime = DateUtils.setMinutes(endTime, 59);
		endTime = DateUtils.setSeconds(endTime, 59);
		endTime = DateUtils.setMilliseconds(endTime, 0);
		return endTime;
	}

	private static Date handlerVariableParam(Date... day) {
		Date tempTime = null;
		if (!ObjectUtils.isEmpty(day)) {
			if (day[0] == null) {
				tempTime = new Date();
			} else {
				tempTime = day[0];
			}
		} else {
			tempTime = new Date();
		}
		return tempTime;
	}

	/**
	 * 获取当月或指定月份的第一天
	 * 
	 * @param day
	 * @return
	 */
	public static Date getMonthFirstDay(Date... day) {
		Date firstDay = handlerVariableParam(day);
		firstDay = DateUtils.setDays(firstDay, 1); // 月的第一天
		firstDay = getDayStartTime(firstDay);
		return firstDay;
	}

	/**
	 * 获取当月或指定月份的最后一天
	 * 
	 * @param day
	 * @return
	 */
	public static Date getMonthEndDay(Date... day) {
		Date endDay = handlerVariableParam(day);
		// 先取得下个月的第一天再减一天，得到当前月份的最后一天。
		endDay = DateUtils.addMonths(endDay, 1);
		endDay = DateUtils.setDays(endDay, 1);
		endDay = DateUtils.addDays(endDay, -1);
		endDay = getDayEndTime(endDay);
		return endDay;
	}

	/**
	 * 获取指定日期加上指定天数（负数代表减）后的零点日期。 如：2015-11-17 11:30:00 加上 -10 后 得到：2015-11-07
	 * 00:00:00
	 *
	 * @param date
	 *            日期
	 * @param day
	 *            相加的天数
	 * @return 相加后的零点的日期。
	 */
	public static Date getZeroDateAfterAddDays(Date date, int day) {
		Date zeroDate = convertToZeroDate(date);
		return DateUtils.addDays(zeroDate, day);
	}

	/**
	 * 将日期转化为零点的日期。 比如：2015-11-16 11:30:00 转化为 2015-11-16 00:00:00
	 *
	 * @param date
	 *            转化前的日期
	 * @return 转化后的日期
	 */
	public static Date convertToZeroDate(Date date) {
		if (date == null) {
			return null;
		}

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd 00:00:00");
		try {
			date = format.parse(format.format(date));
		} catch (Exception e) {
			// 字符串写死，解析永远不会失败。
		}

		return date;
	}

	/**
	 * 将日期列表转化为零点的日期列表。 比如：2015-11-16 11:30:00 转化为 2015-11-16 00:00:00
	 *
	 * @param dateList
	 *            转化前的日期列表
	 * @return 转化后的日期列表
	 */
	public static List<Date> convertToZeroDate(List<Date> dateList) {
		List<Date> zeroDates = new ArrayList<Date>();
		for (Date date : dateList) {
			zeroDates.add(convertToZeroDate(date));
		}

		return zeroDates;
	}

	/**
	 * 获取从指定日期开始算，最近的连续天数。
	 * 
	 * 测试代码： List<Date> dates = new ArrayList<Date>();
	 * dates.add(DateUtils.parseDate("20151115134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151115134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151113124700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151113114700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151112124700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151109124700", "yyyyMMddHHmmss"));
	 * 
	 * @date 2015年11月17日 上午11:37:02
	 *
	 * @param date 从指定天数开始
	 * @param dateList 日期列表
	 * @return 连续的天数
	 */
	public static int getContinuousDayNum(Date date, List<Date> dateList) {
		List<Date> zeroDates = convertToZeroDate(dateList);
		int num = 0; // 0表示比较当天日期  1表示不比较当天日期（当天总是会算上一次）
		boolean isToday = true;
		// 倒序排列
		Collections.sort(zeroDates);
		Collections.reverse(zeroDates);
		for (Date zeroDate : zeroDates) {
			// 日期为空时，过滤
			if (zeroDate == null) {
				continue;
			}
			// 如果满足连续提前num天，则连续天数加1
			if (getZeroDateAfterAddDays(date, -num).compareTo(zeroDate) == 0) {
				isToday = false;
				num++;
			}
		}
		// 解决list最大日期比参考date少1一天（list不包含当天）的问题，num不能自增的问题
		if(isToday && zeroDates.size() > 0 && num == 0){
			num++;
			for (Date zeroDate : zeroDates) {
				// 日期为空时，过滤
				if (zeroDate == null) {
					continue;
				}
				// 如果满足连续提前num天，则连续天数加1
				if (getZeroDateAfterAddDays(date, -num).compareTo(zeroDate) == 0) {
					isToday = false;
					num++;
				}
			}
			// 参考日期和list最大日期有间隔
			if(isToday){
				num = 0;
			}
			else{
				num--; //减掉辅助自增的
			}
		}

		return num;
	}

	/**
	 * 获取指定日期列表中最大的连续天数。
	 * 
	 * 测试代码： List<Date> dates = new ArrayList<Date>();
	 * dates.add(DateUtils.parseDate("20160101134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151231134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151230134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151229134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151228134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151227134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151226134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151120134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151119134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151118134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151117134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151116134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151115134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151115134700", "yyyyMMddHHmmss"));
	 * dates.add(DateUtils.parseDate("20151113124700", "yyyyMMddHHmmss"));
	 *
	 * @param dateList
	 *            按照日期倒序排列的日期列表，最新的日期在最前面
	 * @return 连续的天数
	 */
	public static int getContinuousDayNum(List<Date> dateList) {
		int resultNum = 0;
		for (Date date : dateList) {
			int num = getContinuousDayNum(date, dateList);
			if (resultNum < num) {
				resultNum = num;
			}
		}
		return resultNum;
	}

	/**
	 * 计算两个日期之间相差的天数
	 * 
	 * @param smdate
	 *            较小的时间
	 * @param bdate
	 *            较大的时间
	 * @return 相差天数
	 * @throws ParseException
	 */
	public static int daysBetween(Date smdate, Date bdate) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		smdate = sdf.parse(sdf.format(smdate));
		bdate = sdf.parse(sdf.format(bdate));
		Calendar cal = Calendar.getInstance();
		cal.setTime(smdate);
		long time1 = cal.getTimeInMillis();
		cal.setTime(bdate);
		long time2 = cal.getTimeInMillis();
		long between_days = (time2 - time1) / (1000 * 3600 * 24);

		return Integer.parseInt(String.valueOf(between_days));
	}
	
	/**
	 * 返回当前或指定日期修改后的日期
	 * @param beginDate
	 * @param calenderUnit Calender类日历字段
	 * @param amount 要添加到该字段的日期或时间的量。
	 * @return
	 */
	public static Date addCalender(Date beginDate, int calenderUnit, int amount){
		if(beginDate == null) {
			beginDate = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);
		calendar.add(calenderUnit, amount);
		return calendar.getTime();
	}
	
	public static Date addCalender(Date beginDate, DateUnitEnum calenderUnit, int amount){
		if(beginDate == null) {
			beginDate = new Date();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(beginDate);
		calendar.add(calenderUnit.getUnit(), amount);
		return calendar.getTime();
	}
	
   /**
    * 根据开始时间和结束时间返回时间段内的时间集合
    * @param beginDate 开始日期
    * @param endDate 结束日期
    * @return List<Date>
    */
	public static List<Date> getDatesBetweenTwoDate(Date beginDate, Date endDate) {
		if(beginDate == null)
			return new ArrayList<Date>();
		List<Date> lDate = new ArrayList<Date>();
		lDate.add(beginDate);//把开始时间加入集合
		Calendar cal = Calendar.getInstance();
		//使用给定的 Date设置此 Calendar的时间
		cal.setTime(beginDate);
		try {
			int between_days = daysBetween(beginDate, endDate);
			if(between_days > 0){
				boolean bContinue = true;
				while (bContinue) {
					// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
					cal.add(Calendar.DAY_OF_MONTH, 1);
					// 测试此日期是否在指定日期之后
					if (endDate.after(cal.getTime())) {
						lDate.add(cal.getTime());
					} else {
						break;
					}
				}
				lDate.add(endDate);// 把结束时间加入集合
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return lDate;
	}
}