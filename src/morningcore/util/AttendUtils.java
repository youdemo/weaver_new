package morningcore.util;

import weaver.general.Util;
import weaver.integration.logging.Logger;
import weaver.integration.logging.LoggerFactory;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AttendUtils{
    private final static Logger logger = LoggerFactory.getLogger(Util.class);

    /**
     * 获取某个月份所有日期
     *
     * @param year  所在年份
     * @param month 所在月份
     */
    public static List<String> getMonthFullDay(int year,int month){
        SimpleDateFormat dateFormatYYYYMMDD = new SimpleDateFormat("yyyy-MM-dd");
        List<String> fullDayList = new ArrayList<String>(32);
        // 获得当前日期对象
        Calendar cal = Calendar.getInstance();
        cal.clear();// 清除信息
        cal.set(Calendar.YEAR,year);
        // 1月从0开始
        cal.set(Calendar.MONTH,month - 1);
        // 当月1号
        cal.set(Calendar.DAY_OF_MONTH,1);
        int count = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        for(int j = 1;j <= count;j++){
            fullDayList.add(dateFormatYYYYMMDD.format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
        return fullDayList;
    }

    /**
     * 获取某日期区间的所有日期  日期倒序
     *
     * @param startDate  开始日期
     * @param endDate    结束日期
     * @param dateFormat 日期格式
     * @return 区间内所有日期
     */
    public static List<String> getPerDaysByStartAndEndDate(String startDate,String endDate,String dateFormat){
        DateFormat format = new SimpleDateFormat(dateFormat);
        List<String> res = new ArrayList<String>();
        try{
            Date sDate = format.parse(startDate);
            Date eDate = format.parse(endDate);
            long start = sDate.getTime();
            long end = eDate.getTime();
            if(start > end){
                return null;
            }
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(eDate);

            while(end >= start){
                res.add(format.format(calendar.getTime()));
                calendar.add(Calendar.DAY_OF_MONTH,-1);
                end = calendar.getTimeInMillis();
            }
            return res;
        }catch(ParseException e){
            logger.error(e.getMessage(),e);
        }

        return res;
    }

    public static int compareDate(String date1,String date2){
        DateFormat df = new SimpleDateFormat("hh:mm");
        try{
            Date dt1 = df.parse(date1);
            Date dt2 = df.parse(date2);
            if(dt1.getTime() > dt2.getTime()){
                System.out.println("dt1 在dt2前");
                return 1;
            }else if(dt1.getTime() < dt2.getTime()){
                System.out.println("dt1在dt2后");
                return -1;
            }else{
                return 0;
            }
        }catch(Exception exception){
            exception.printStackTrace();
        }
        return 0;
    }
}
