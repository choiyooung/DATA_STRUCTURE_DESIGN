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
	public final int flag = 20; //30��*20 , �� 10�и��� ũ�Ѹ��Ѵ�. ���� �ٲ��ָ� ��и��� ũ�Ѹ����� �����Ҽ� �ִ�.
	public Crawling(){}
	
	public void startCrawling(TimeValue time, TimeValue startTime, TimeValue endTime) throws IOException {	
		int count =0;
		
		// ������ ������� ������ ����
		//�̹� ũ�Ѹ��� ���� �ִ��� Ȯ��
//		System.out.println("���� : " +startTime.year+startTime.month+startTime.day+startTime.hour+startTime.min+startTime.sec);
//		System.out.println("��    : " +endTime.year+endTime.month+endTime.day+endTime.hour+endTime.min+endTime.sec);
//		System.out.println("��   : " +time.year+time.month+time.day+time.hour+time.min+time.sec);
		if(fileExists(startTime,endTime)== false) //���� ������ ���°�� ũ�Ѹ��� �ؾ��Ѵ�.
		{
			while(time.timeCompare(endTime))
			{
				
				if(count % flag == 0)
				{
					String tempURL = null;
					tempURL = URL + time.getParameter();
					System.out.println(tempURL);
					// �ش� ������ �Ľ��۾�
					Document doc = Jsoup.connect(tempURL).get();
					// �ǽð� �˻������ ������´�.
					Elements elements= doc.select(".keyword_rank.select_date .rank_inner .rank_scroll .rank_list .list");
					fileWrite(elements.toString(),startTime, endTime);
				}
				count++;
				time.changeTime();
				//key_word = getArrayKeyword(elements.toString());
			}
		}
		
		
	}
	// �ǽð� �˻������ �迭�� �����Ѵ�.
	public String[] getArrayKeyword(String Line) //ũ�Ѹ��� ���ǹ��� �����͸� �迭�� �����Ѵ�.(�ǽð� �˻����)
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
	public boolean fileExists(TimeValue startTime, TimeValue endTime) //�� ������ �����ϴ��� Ȯ���ϴ� �Լ�, ������ ���� ũ�Ѹ��� �ʿ䰡 ����.
	{
		File file = new File("C:\\Users\\YungJae\\eclipse-workspace\\DS_Design\\" + startTime.year+startTime.month+startTime.day+startTime.hour+startTime.min+startTime.sec + "_"+ endTime.year+endTime.month+endTime.day+endTime.hour+endTime.min+endTime.sec +".txt");
		boolean isExists = file.exists();
		if(isExists)
		{
			return true;
		}
		return false;
	}
	public void fileWrite(String Line,TimeValue startTime, TimeValue endTime) //ũ�Ѹ��� �����͸� �����ϴ� �Լ�.
	{
		try {
				File file = new File("C:\\Users\\YungJae\\eclipse-workspace\\DS_Design\\" + startTime.year+startTime.month+startTime.day+startTime.hour+startTime.min+startTime.sec + "_"+ endTime.year+endTime.month+endTime.day+endTime.hour+endTime.min+endTime.sec +".txt");
				BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file,true));
			
			if(file.isFile()&&file.canWrite())
			{
				String[] key_word = getArrayKeyword(Line);
				//����
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