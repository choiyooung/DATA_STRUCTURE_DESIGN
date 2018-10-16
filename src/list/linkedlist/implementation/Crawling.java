package list.linkedlist.implementation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

class Crawling {
	
	private static String URL = "https://datalab.naver.com/keyword/realtimeList.naver?datetime=";
	public final int flag = 20; //30초*20 , 즉 10분마다 크롤링한다. 값을 바꿔주면 몇분마다 크롤링할지 결정할수 있다.
	public Crawling(){}
	
	public void startCrawling(TimeValue time, TimeValue startTime, TimeValue endTime) throws IOException {	
		int count =0;
		
		// 정보를 가지고올 페이지 지정
		//이미 크롤링한 것이 있는지 확인
//		System.out.println("시작 : " +startTime.year+startTime.month+startTime.day+startTime.hour+startTime.min+startTime.sec);
//		System.out.println("끝    : " +endTime.year+endTime.month+endTime.day+endTime.hour+endTime.min+endTime.sec);
//		System.out.println("음   : " +time.year+time.month+time.day+time.hour+time.min+time.sec);
		if(fileExists(startTime,endTime)== false) //같은 파일이 없는경우 크롤링을 해야한다.
		{
			while(time.timeCompare(endTime))
			{
				
				if(count % flag == 0)
				{
					String tempURL = null;
					tempURL = URL + time.getParameter();
					System.out.println(tempURL);
					// 해당 페이지 파싱작업
					Document doc = Jsoup.connect(tempURL).get();
					// 실시간 검색어랑를 가지고온다.
					Elements elements= doc.select(".keyword_rank.select_date .rank_inner .rank_scroll .rank_list .list");
					fileWrite(elements.toString(),startTime, endTime);
				}
				count++;
				time.changeTime();
				//key_word = getArrayKeyword(elements.toString());
			}
		}
		
		
	}
	// 실시간 검색어들을 배열에 저장한다.
	public String[] getArrayKeyword(String Line) //크롤링한 유의미한 데이터를 배열에 저장한다.(실시가 검색어들)
	{ 
		String[] memory = Line.split("</li>");
		String[] Key_word = new String[memory.length];
		for(int i=0; i<memory.length; i++)
		{
		String temp = memory[i];
		String[] rank_temp = temp.split(">");
		temp = rank_temp[5];
		String[] key_word = temp.split("<");
		Key_word[i] = key_word[0];
	    }
		return Key_word;
	}
	public boolean fileExists(TimeValue startTime, TimeValue endTime) //그 파일이 존재하는지 확인하는 함수, 있으면 굳이 크롤링할 필요가 없다.
	{
		File file = new File("C:\\Users\\YungJae\\eclipse-workspace\\DS_Design\\" + startTime.year+startTime.month+startTime.day+startTime.hour+startTime.min+startTime.sec + "_"+ endTime.year+endTime.month+endTime.day+endTime.hour+endTime.min+endTime.sec +".txt");
		boolean isExists = file.exists();
		if(isExists)
		{
			return true;
		}
		return false;
	}
	public void fileWrite(String Line,TimeValue startTime, TimeValue endTime) //크롤링한 데이터를 저장하는 함수.
	{
		try {
				File file = new File("C:\\Users\\YungJae\\eclipse-workspace\\DS_Design\\" + startTime.year+startTime.month+startTime.day+startTime.hour+startTime.min+startTime.sec + "_"+ endTime.year+endTime.month+endTime.day+endTime.hour+endTime.min+endTime.sec +".txt");
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
			
			if(file.isFile()&&file.canWrite())
			{
				String[] key_word = getArrayKeyword(Line);
				//쓰기
				for(int i=0; i<key_word.length;i++)
				{
					if(i!=key_word.length-1)
					{
						bufferedWriter.write(key_word[i] + "/");				
					}else{
						bufferedWriter.write(key_word[i]);
						bufferedWriter.newLine();
					}
				}
				bufferedWriter.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}