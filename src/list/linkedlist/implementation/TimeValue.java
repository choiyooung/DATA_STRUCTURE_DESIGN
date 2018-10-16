package list.linkedlist.implementation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeValue {
		public String year;
		public String month;
		public String day;
		public String hour;
		public String min;
		public String sec;
		//생성자
		public TimeValue()
		{
			year = null;
			month= null;
			day = null; 
			hour = null;
			min = null;
			sec = null;
		}
		public TimeValue(String year, String month, String day, String hour, String min, String sec)
		{
			this.year = year;
			this.month= month;
			this.day = day; 
			this.hour = hour;
			this.min = min;
			this.sec = sec;
			
		}
		//함수
		//시간을 파라미터 형태로 바꿔준다.
		public String getParameter()
		{
		String time = this.year + "-" + 
					this.month + "-" + 
					this.day + "T" + 
					this.hour + ":"  +
					this.min + ":"  +
					this.sec +  ""; 
		return time;
		}
		//시간을 다음 크롤링할 시간으로 바꿔준다. //30초씩 증가
		public void changeTime()
		{
			int tempHour = Integer.parseInt(hour);
			int tempMin = Integer.parseInt(min);
			int tempSec = Integer.parseInt(sec);
			int check  = 0;
			check = (tempSec+30) / 60; //초가 60초가 되면 check를 증가해 min을 증가시킨다.
			tempSec = (tempSec+30) % 60;
		
			if(check==0)   sec = "30"; else sec= "00"; //
			if(check>0) //분이 1증가한 경우
			{
				tempMin++;
				if(tempMin>=60)
				{
					tempMin = tempMin%60;
				}else
				{
					check = 0;
				}
				if(tempMin<10) min = "0" + tempMin; else min = tempMin + "";
			}
			else return;
			if(check>0) //시간이 1 증가한경우
			{
				tempHour++;
				if(tempHour>=24)
				{
					tempHour = tempHour%24;
				}else
				{
					check = 0;
				}
				if(tempHour<10) hour = "0" + tempHour; else hour = tempHour + "";
				
			}else return; //시간이 증가하지않은경우 더이상 수행하지않는다.
			if(check>0) //일자가 1 증가한경우
			{
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = df.parse(year+ "-" + month+ "-" + day);
					//날짜 1더하기
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					cal.add(Calendar.DATE,1);
					String[] tempDate = df.format(cal.getTime()).split("-");
					year = tempDate[0];
					month = tempDate[1];
					day = tempDate[2];
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}else return; 
		}
		public boolean timeCompare(TimeValue thing)
		{
			String temp1 = this.year + this.month + this.day + this.hour + this.min + this.sec;
			String temp2 = thing.year + thing.month + thing.day + thing.hour + thing.min + thing.sec;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				Date date1 = dateFormat.parse(temp1);
				Date date2 = dateFormat.parse(temp2);
				long time1 = date1.getTime();
				long time2 = date2.getTime();
				//System.out.println(time1 - time2);
				if((time1 - time2)>0.00)
				{
					return false; // time1 값이 더 크면 false
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true; //time2값이 같거나 크면,true
		}
		public int timeDifference(TimeValue thing)
		{
			String temp1 = this.year + this.month + this.day + this.hour + this.min + this.sec;
			String temp2 = thing.year + thing.month + thing.day + thing.hour + thing.min + thing.sec;
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
			try {
				Date date1 = dateFormat.parse(temp1);
				Date date2 = dateFormat.parse(temp2);
				long time1 = date1.getTime();
				long time2 = date2.getTime();
				//System.out.println(time1 - time2);
				if((time1 - time2)>0.00)
				{
					return (int)((time1-time2)/1000); 
				}
				return (int)((time2-time1)/1000); 
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				return 0;	
		}
	}

