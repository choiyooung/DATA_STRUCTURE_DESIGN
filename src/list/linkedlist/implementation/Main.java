package list.linkedlist.implementation;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

class Main {
	
	public static void main(String[] args) throws IOException {
	LinkedList Link = new LinkedList();
	TimeValue time = new TimeValue();
	TimeValue startTime = new TimeValue();
	TimeValue endTime = new TimeValue();
	//파일을 열고, 처음 이면  기본형태를 만든다.
	inputDate(time,startTime,endTime);
	while(startTime.timeCompare(endTime) == false)
	{
		System.out.println("끝나는 날짜보다 시작날짜의 값이 큽니다. 다시입력해주세요.");
		inputDate(time,startTime,endTime);
	}
	Crawling craw = new Crawling();
	craw.startCrawling(time,startTime,endTime);
	
	readFile(startTime,endTime,Link);
}
	//파일을 열어서 한라인씩 읽고, makeGraph로 그래프를 만든다.
	public static String readFile(TimeValue startTime, TimeValue endTime,LinkedList Link) 
	{
		try {
			File file = new File("C:\\Users\\YungJae\\eclipse-workspace\\DS_Design\\" + startTime.year+startTime.month+startTime.day+startTime.hour+startTime.min+startTime.sec + "_"+ endTime.year+endTime.month+endTime.day+endTime.hour+endTime.min+endTime.sec +".txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fileReader);
			String line = "";
			String[] newWord;
			while((line =bufReader.readLine())!=null)
			{
				String[] words = line.split("/");
				Link.makeInitialGraph(words);
				newWord = wordExists(words,startTime,endTime);
				while((line =bufReader.readLine())!=null)
				{
					words = line.split("/");
					newWord = wordExists(words,startTime,endTime);
					Link.makeGraph(words, newWord);
				}
			}

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	//검색어중에서 해당가 처음 나온 단어라면, 텍스트에 저장한다.
	public static String[] wordExists(String[] word,TimeValue startTime, TimeValue endTime)
	{
		String[] word_exists = (String[])word.clone();
		int  index=0;
		try {
			File infile = new File("C:\\Users\\YungJae\\eclipse-workspace\\DS_Design\\" + startTime.year+startTime.month+startTime.day+startTime.hour+startTime.min+startTime.sec + "_"+ endTime.year+endTime.month+endTime.day+endTime.hour+endTime.min+endTime.sec +"wordExisits.txt");
			if(infile.exists())
			{
				BufferedReader bufferedReader = new BufferedReader(new FileReader(infile));
				String line = null;
				while((line = bufferedReader.readLine()) != null)
				{
					String[] rankAndWord = line.split("/");
					for(int i= 0; i<word.length-index; i++)
					{
						if(word_exists[i].contains(rankAndWord[1]))
						{
							if(word_exists[word.length -(index+1)].indexOf('/')!=-1)
							{
								word_exists[i] = word_exists[word.length -(index+1)];
								word_exists[word.length -(index+1)] = null;
								index++;
							}
							else
							{
								word_exists[i] = (word.length -index) +"/"+ word_exists[word.length -(index+1)] +"";
								word_exists[word.length -(index+1)] = null;
								index++;
							}
						}
					}
					if(index==20)break;
				}
			bufferedReader.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//txt 파일에 쓰기
		int j =0;
		String[] temp = new String[word_exists.length -index];
		try {
			File  outfile = new File("C:\\Users\\YungJae\\eclipse-workspace\\DS_Design\\" + startTime.year+startTime.month+startTime.day+startTime.hour+startTime.min+startTime.sec + "_"+ endTime.year+endTime.month+endTime.day+endTime.hour+endTime.min+endTime.sec +"wordExisits.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outfile,true));
			while(j<word_exists.length - index) 
			{
				if(word_exists[j].indexOf("/")!=-1)
				{
					temp[j] = word_exists[j];
					bufferedWriter.write(word_exists[j]);
					bufferedWriter.newLine();
				}
				else
				{
					temp[j] = word_exists[j];
					bufferedWriter.write((j+1) + "/"+ word_exists[j]);
					bufferedWriter.newLine();

				}
				j++;
				
			}
			bufferedWriter.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return temp;
		
	}
	public static void inputDate(TimeValue time, TimeValue startTime, TimeValue endTime) //시작 일자와 끝 일자를 입력받는 함수.
	{
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);
		
		System.out.println("실시간검색어 시작시간");
		System.out.println("년도?");
		startTime.year = sc.next();
		time.year = startTime.year;
		System.out.println("월?");
		startTime.month = sc.next();
		time.month = startTime.month;
		System.out.println("일?");
		startTime.day = sc.next();
		time.day = startTime.day;
		System.out.println("시");
		startTime.hour  = sc.next();
		time.hour = startTime.hour;
		System.out.println("분");
		startTime.min  = sc.next();
		time.min = startTime.min;
		System.out.println("초");
		startTime.sec  = sc.next();
		time.sec = startTime.sec;
		System.out.println("실시간검색어 끝나는시간");
		System.out.println("년도?");
		endTime.year = sc.next();
		System.out.println("월?");
		endTime.month = sc.next();
		System.out.println("일?");
		endTime.day = sc.next();
		System.out.println("시");
		endTime.hour  = sc.next();
		System.out.println("분");
		endTime.min  = sc.next();
		System.out.println("초");
		endTime.sec  = sc.next();
		
	}
}
