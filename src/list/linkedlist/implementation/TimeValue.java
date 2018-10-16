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
		//������
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
		//�Լ�
		//�ð��� �Ķ���� ���·� �ٲ��ش�.
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
		//�ð��� ���� ũ�Ѹ��� �ð����� �ٲ��ش�. //30�ʾ� ����
		public void changeTime()
		{
			int tempHour = Integer.parseInt(hour);
			int tempMin = Integer.parseInt(min);
			int tempSec = Integer.parseInt(sec);
			int check  = 0;
			check = (tempSec+30) / 60; //�ʰ� 60�ʰ� �Ǹ� check�� ������ min�� ������Ų��.
			tempSec = (tempSec+30) % 60;
		
			if(check==0)   sec = "30"; else sec= "00"; //
			if(check>0) //���� 1������ ���
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
			if(check>0) //�ð��� 1 �����Ѱ��
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
				
			}else return; //�ð��� ��������������� ���̻� ���������ʴ´�.
			if(check>0) //���ڰ� 1 �����Ѱ��
			{
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date date = df.parse(year+ "-" + month+ "-" + day);
					//��¥ 1���ϱ�
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
					return false; // time1 ���� �� ũ�� false
				}
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return true; //time2���� ���ų� ũ��,true
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

