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
		// ������ ����, ó�� �̸� �⺻���¸� �����.
		inputDate(time, startTime, endTime);
		while (startTime.timeCompare(endTime) == false) {
			System.out.println("������ ��¥���� ���۳�¥�� ���� Ů�ϴ�. �ٽ��Է����ּ���.");
			inputDate(time, startTime, endTime);
		}
		String filename = "C:\\Users\\YungJae\\eclipse-workspace\\DS_Design\\" + startTime.year + startTime.month
				+ startTime.day + startTime.hour + startTime.min + startTime.sec + "_" + endTime.year
				+ endTime.month + endTime.day + endTime.hour + endTime.min + endTime.sec;
		String filename2 = filename + "wordExisits.txt";
		Crawling craw = new Crawling();
		craw.startCrawling(time, startTime, endTime);

		readFile(startTime, endTime, Link,filename);
		System.out.println(Link.toSizeString());
		
		String[][] hotKeyWord = Link.showHotKeyWord(filename2);
		System.out.println("~~���� ���� �˻����~~");
		for(int i = 0 ;i<20;i++)
		{
			System.out.println((i+1) + " �� :" + hotKeyWord[0][i]+ ", Score : "+ hotKeyWord[1][i]);
		}
		String[][] muchTimeWord = Link.showCountTime(filename2);
		System.out.println("~~���� ���� ���� �˻����~~");
		for(int i = 0 ;i<20;i++)
		{
			System.out.println((i+1) + " �� :" + muchTimeWord[0][i] + ", timeCount : " + muchTimeWord[1][i] );
		}
		String[][] myHotKeyWord = Link.showMyHotKeyWord(filename2,startTime.timeDifference(endTime)/(30*craw.flag));
		System.out.println("~~������ ���̽� �˻����~~");
		for(int i = 0 ;i<20;i++)
		{
			System.out.println((i+1) + " �� :" + myHotKeyWord[0][i] + ", Score : " +myHotKeyWord[1][i] );
		}
		Link.showWordImpormation(filename2, "��ȣ");
	}

	// ������ ��� �Ѷ��ξ� �а�, makeGraph�� �׷����� �����.
	public static String readFile(TimeValue startTime, TimeValue endTime, LinkedList Link,String filename) {
		try {
			String filename2 = filename + "wordExisits.txt";
			File file = new File(filename + ".txt");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fileReader);
			String line = "";
			String[] newWord;
			while ((line = bufReader.readLine()) != null) {
				String[] words = line.split("/");
				Link.makeInitialGraph(words);
				newWord = wordExists(words, startTime, endTime);
				while ((line = bufReader.readLine()) != null) {
					words = line.split("/");
					newWord = wordExists(words, startTime, endTime);
					Link.makeGraph(words, newWord, filename2);
				}
			}
			bufReader.close();
			fileReader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	// �˻����߿��� �ش簡 ó�� ���� �ܾ���, �ؽ�Ʈ�� �����Ѵ�.
	public static String[] wordExists(String[] word, TimeValue startTime, TimeValue endTime) {
		String[] word_exists = (String[]) word.clone();
		int index = 0;
		try {
			File infile = new File("C:\\Users\\YungJae\\eclipse-workspace\\DS_Design\\" + startTime.year
					+ startTime.month + startTime.day + startTime.hour + startTime.min + startTime.sec + "_"
					+ endTime.year + endTime.month + endTime.day + endTime.hour + endTime.min + endTime.sec
					+ "wordExisits.txt");
			if (infile.exists()) {
				BufferedReader bufferedReader = new BufferedReader(new FileReader(infile));
				String line = null;
				while ((line = bufferedReader.readLine()) != null) {
					String[] rankAndWord = line.split("/");
					for (int i = 0; i < word.length - index; i++) {
						if (word_exists[i].indexOf('/') != -1) {
							String temp[] = word_exists[i].split("/");
							if (temp[1].equals(rankAndWord[1])) {
								if (word_exists[word.length - (index + 1)].indexOf('/') != -1) {
									word_exists[i] = word_exists[word.length - (index + 1)];
									word_exists[word.length - (index + 1)] = null;
									index++;
								} else {
									word_exists[i] = (word.length - index) + "/"
											+ word_exists[word.length - (index + 1)] + "";
									word_exists[word.length - (index + 1)] = null;
									index++;
								}
								break;
							}
						} else {
							if (word_exists[i].equals(rankAndWord[1])) {
								if (word_exists[word.length - (index + 1)].indexOf('/') != -1) {
									word_exists[i] = word_exists[word.length - (index + 1)];
									word_exists[word.length - (index + 1)] = null;
									index++;
								} else {
									word_exists[i] = (word.length - index) + "/"
											+ word_exists[word.length - (index + 1)] + "";
									word_exists[word.length - (index + 1)] = null;
									index++;
								}
								break;
							}
						}
					}
					if (index == 20)
						break;
				}
				bufferedReader.close();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// txt ���Ͽ� ����
		int j = 0;
		String[] temp = new String[word_exists.length - index];
		try {
			File outfile = new File("C:\\Users\\YungJae\\eclipse-workspace\\DS_Design\\" + startTime.year
					+ startTime.month + startTime.day + startTime.hour + startTime.min + startTime.sec + "_"
					+ endTime.year + endTime.month + endTime.day + endTime.hour + endTime.min + endTime.sec
					+ "wordExisits.txt");
			BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(outfile, true));
			while (j < word_exists.length - index) {
				if (word_exists[j].indexOf('/') != -1) {
					temp[j] = word_exists[j];
					bufferedWriter.write(word_exists[j]);
					bufferedWriter.newLine();
				} else {
					temp[j] = (j + 1)+"" + "/" + word_exists[j];
					bufferedWriter.write((j + 1) + "/" + word_exists[j]);
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
	public static void inputDate(TimeValue time, TimeValue startTime, TimeValue endTime) // ���� ���ڿ� �� ���ڸ� �Է¹޴� �Լ�.
	{
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);

		System.out.println("�ǽð��˻��� ���۽ð�");
		System.out.println("�⵵?");
		startTime.year = sc.next();
		time.year = startTime.year;
		System.out.println("��?");
		startTime.month = sc.next();
		time.month = startTime.month;
		System.out.println("��?");
		startTime.day = sc.next();
		time.day = startTime.day;
		System.out.println("��");
		startTime.hour = sc.next();
		time.hour = startTime.hour;
		System.out.println("��");
		startTime.min = sc.next();
		time.min = startTime.min;
		System.out.println("��");
		startTime.sec = sc.next();
		time.sec = startTime.sec;
		System.out.println("�ǽð��˻��� �����½ð�");
		System.out.println("�⵵?");
		endTime.year = sc.next();
		System.out.println("��?");
		endTime.month = sc.next();
		System.out.println("��?");
		endTime.day = sc.next();
		System.out.println("��");
		endTime.hour = sc.next();
		System.out.println("��");
		endTime.min = sc.next();
		System.out.println("��");
		endTime.sec = sc.next();

	}
}
