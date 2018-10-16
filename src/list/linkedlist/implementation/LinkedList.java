package list.linkedlist.implementation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class LinkedList {
	private Node head;
	private Node tail;
	private int size = 0;
	private int edgeSize = 0;

	private class Node {
		// 데이터값들
		private String word; // 실시간 검색어의 단어
		private int rank; // 검색어의 순위
		private int timecount; // 1분동안 유지되면 +1해서 몇분동안했는지 알려주는 변수
		private boolean initial; // 처음으로 들어온 경우
		// 다음 노드들
		private Node next; // 실시간 검색어 끼리 연결하는 노드
		private Node changerank; // 실시간 검색어의 순위가 바꼈을 때 보여주는 노드,
		private Node backward; // 해당 순위에 있었던 검색어들을 연결하는 노드

		public Node(String word, int rank, boolean initial) {
			this.word = word;
			this.initial = initial;
			this.timecount = 1;
			this.rank = rank;

			this.next = null;
			this.changerank = null;
			this.backward = null;
		}

		public Node(Node other) {
			this.word = other.word;
			this.initial = other.initial;
			this.timecount = other.timecount;
			this.rank = other.rank;

			this.next = other.next;
			this.changerank = other.changerank;
			this.backward = other.backward;
		}

		public String toString() {
			return String.valueOf(this.word);
		}
	}

	public void addFirst(String word, int rank, boolean initial) {
		// 새로운 노드를 생성한다.
		Node newNode = new Node(word, rank, initial);
		// 원래있던 첫노드를 이 노드가 가르키게한다.
		newNode.next = head;
		// 헤드를 이노드에 넣어논다.
		head = newNode;
		size++;
		edgeSize++;
		if (head.next == null) {
			tail = head;
		}
	}

	public void addLast(String word, int rank, boolean initial) {
		// 첫 노드를 생성한다.
		Node newNode = new Node(word, rank, initial);
		// 사이즈가 0이라면 아무것도 없기때문에 이 노드가 첫노드이다.
		if (size == 0) {
			addFirst(word, rank, initial);
		} else {
			// 마지막 노드의 다음 노드를 새노드를 가르키게 한다.
			tail.next = newNode;
			// tail이 새노드를 가르키게 한다.
			tail = newNode;
			// 사이즈를 1증가 시킨다.
			size++;
			edgeSize++;
		}
	}

	Node node(int rank) {
		Node x = head;
		for (int i = 0; i < rank - 1; i++)
			x = x.next;
		return x;
	}

	public void add(String word, int rank, boolean initial) {
		if (rank == 1) {
			addFirst(word, rank, initial);
		} else {
			Node temp1 = node(rank - 1);
			Node temp2 = temp1.next;
			Node newNode = new Node(word, rank, initial);
			temp1.next = newNode;
			newNode.next = temp2;
			size++;
			edgeSize++;
			if (newNode.next == null) {
				tail = newNode;
			}
		}
	}

	public void addBackword(Node preWord, int rank) {
		Node temp = node(rank);
		if (temp.backward == null) {
			temp.backward = preWord;
		} else {
			while (temp.backward != null) {
				if (temp.backward.timecount >= preWord.timecount) {
					temp = temp.backward;
				} else {
					preWord.backward = temp.backward;
					temp.backward = preWord;
					size++;
					edgeSize++;
					return;
				}
			}
			temp.backward = preWord;
		}
		size++;
	}

	public String toString() {
		if (head == null) {
			return "[]";
		}
		Node temp = head;
		String str = "[";
		while (temp.next != null) {
			str += temp.word + ",";
			temp = temp.next;
		}
		str += temp.word;
		return str + "]";
	}

	public String toRankSetString(int rank) {
		Node temp = node(rank);
		String str = "[";
		while (temp.backward != null) {
			str += temp.timecount + "";
			str += temp.word + ",";
			temp = temp.backward;
		}
		str += temp.timecount + "";
		str += temp.word;

		return str + "]";
	}

	public Node removeFirst() {
		Node temp = head;
		head = temp.next;
		temp.next = null;
		temp.backward = null;
		size--;
		return temp;
	}

	public Node remove(int rank) {
		if (rank == 1)
			return removeFirst();
		Node temp = node(rank - 1);
		Node todoDeleted = temp.next;
		temp.next = temp.next.next;
		if (todoDeleted == tail) {
			tail = temp;
		}
		todoDeleted.backward = null;
		todoDeleted.next = null;
		size--;
		return todoDeleted;
	}

	public Object removeLast() {
		return remove(size - 1);
	}

	public int size() {
		return size;
	}

	public Object get(int k) {
		Node temp = node(k);
		return temp.word;
	}

	public int indexOf(String word) {

		Node temp = head;
		int index = 0;
		while (temp.word != word) {
			temp = temp.next;
			index++;
			if (temp == null)
				return -1;
		}
		return index;
	}

	// 위는 기본적인 함수,변수 정의
	// 아래는 그래프 만드는 함수 정의
	public void makeInitialGraph(String[] words) {
		for (int i = words.length; i > 0; i--) {
			addFirst(words[i - 1], i, true);
		}
	}

	public void makeGraph(String[] words, String[] newWord, String filename) {
		Node temp = head;
		int j = 0;
		while (temp != null) {
			if (words[temp.rank - 1] != null) // words가 null인경우 이미 바뀐 경우 이므로 다음 검색어를 비교해본다.
			{
				for (int i = 0; i < words.length; i++) {
					int t = -1;
					if (temp.word.equals(words[i])) // 현 순위의 검색어가 새로 들어온 검색어에 있다면
					{
						if (temp.rank == (i + 1))// 1.i위 검색어가 다음에 오는 String[]에 있고, 똑같은 순위로 들어올경우
						{
							temp.timecount++;
							words[i] = null;
							break;
						} else // 2.i 위 검색어가 다음에 오는 String[]에 있고, 다른 순위로 들어올경우
						{
							// 해당 순위의 검색어를 다음 실시간 검색어로 바꿔준다.
							if (!words[i].equals(node(i + 1).word)) {
								add(words[temp.rank - 1], temp.rank, wordNew(words[temp.rank - 1], newWord));
								temp = node(temp.rank);
								temp.backward = temp.next.backward;
								Node temp1 = remove(temp.rank + 1);
								addBackword(temp1, temp1.rank);
								// 전에 있는 검색어가 어떤 랭크의 순위에 위치해 있는에 있는
								add(words[i], i + 1, false);
								Node temp3 = node(i + 1);
								temp3.backward = temp3.next.backward;
								Node temp2 = remove(i + 2);
								temp1.changerank = temp3;
								edgeSize++;
								addBackword(temp2, temp2.rank);
								words[i] = null;

								// 위에있는걸 반복
								while (true) {
									for (t = 0; t < words.length; t++) {
										if (temp2.word.equals(words[t])) {
											if (t + 1 != temp.rank) {
												add(words[t], t + 1, false);
												temp3 = node(t + 1);
												temp3.backward = temp3.next.backward;
												temp2.changerank = temp3;
												edgeSize++;
												temp2 = remove(t + 2);
												addBackword(temp2, temp2.rank);
												words[t] = null;
												break;
											} else {
												temp2.changerank = temp;
												words[t] = null;
												edgeSize++;
												break;
											}
										}
									}
									if (t == words.length) {
										break;
									} else if (t + 1 == temp.rank) {
										break;
									}
								}
							} else {
								temp.changerank = node(i + 1);
								words[i] = null;
								break;
							}
							if (t == words.length) {
								break;
							} else if (t + 1 == temp.rank) {
								break;
							}
						}
					}
				}
			}
			// 3. i위 검색어가 다음에 오는 String[]에 없고, 다음에 오는 word가 현순위 검색어 들 사이에 있다면 무시하고, 다음 순위를
			// 탐색
			// 어쩌피 나중에 1.2번방법으로 바뀌게 되어있음.
			temp = temp.next;
		}
		// 4. i위 검색어가 다음에 오는 String[]에 없고, 다음에 오는 word가 현순위 검색어 들 사이에 없는 경우들은 String[]에
		// null로 안들어가있음.
		// 따라서 null이 아닌것들은 해당 순위에 다음에 올걸 넣어주고, 원래있던 검색어는 backward에 넣어주면 됨
		for (int i = 0; i < words.length; i++) {
			if (words[i] != (null)) {
				if (!words[i].equals(node(i + 1).word)) {
					add(words[i], i + 1, wordNew(words[i], newWord));
					Node temp5 = node(i + 1);
					temp5.backward = temp5.next.backward;
					Node temp6 = remove(i + 2);
					addBackword(temp6, i + 1);

					Node temp4 = searchLastChangePart(searchRankinFile(temp5.word, filename), temp5.word);
					if (temp4 != null) {
						connectChangePart(temp4, temp5);
					}
				} else {
					Node temp5 = node(i + 1);
					Node temp4 = searchLastChangePart(searchRankinFile(temp5.word, filename), temp5.word);
					if (temp4 != null) {
						connectChangePart(temp4, temp5);
					}
				}
			}
			words[i] = null;
		}
	}

	// 새로운 단어가 어니면 false, 맞으면 true
	public boolean wordNew(String word, String[] newWord) {
		for (int i = 0; i < newWord.length; i++) {
			String[] temp = newWord[i].split("/");
			if (word.equals(temp[1])) {
				return true;
			}
		}
		return false;
	}

	public void connectChangePart(Node beforeWord, Node afterWord) {
		if (beforeWord != null) {
			beforeWord.changerank = afterWord;
		}
	}

	public int searchRankinFile(String word, String filename) {
		try {
			File file = new File(filename);
			FileReader fileReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufReader.readLine()) != null) {
				String[] temp = line.split("/");
				if (temp[1].equals(word))
					return Integer.parseInt(temp[0]);
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
		return -1;
	}

	public Node searchLastChangePart(int rank, String word) {
		if (rank > 0) {
			Node temp = node(rank);
			while (temp != null) {
				if (temp.word.equals(word) && temp.initial == true) {
					while (temp.changerank != null) {
						temp = temp.changerank;
					}
					if (temp.initial == false) {
						return temp;
					}
				}
				temp = temp.backward;
			}
		}
		return null;
	}

	public Node searchFirstChangePart(int rank, String word) {
		if (rank > 0) {
			Node temp = node(rank);
			while (temp != null) {
				if (temp.word.equals(word) && temp.initial == true) {
					return temp;
				}
				temp = temp.backward;
			}
		}
		return null;
	}

	///////////////////////////////////////////////////////////////////////////////////////////////////////////
	// 그래프 만드는게 끝났으니, 여기부터는 데이터를 분석하는 함수들.
	/////////////////////////////////////////////////////////////////////////////////////////////////////////////
	public String toSizeString() {
		String str;
		str = "Node size :" + size + ", Edge size : " + edgeSize;
		return str;
	}

	public void showWordImpormation(String filename, String word) {
		File file = new File(filename);
		try {
			FileReader fileReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufReader.readLine()) != null) {
				String[] temp = line.split("/");
				if (temp[1].equals(word)) {
					Node temp2 = searchFirstChangePart(Integer.parseInt(temp[0]), word);
					System.out.println("검색어의 정보");
					System.out.println("검색어 : " +  word );
					int[] number = toWordUpperAndLowerLimit(temp2);
					System.out.println("가장 높았던 순위 : " +number[1]+ "위" + "가장 낮았던 순위 : " + number[0] + "위"  );
					String str = String.format("%.2f", toAverageRank(temp2));
					System.out.println("평균 순위 : " + str + "위" );
					System.out.println("단어의 변천사 ");
					System.out.println(toChangeSetString(temp2));
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
	}
	public float toAverageRank(Node temp)
	{
		int number = 0;
		int timecount =0;
		while (temp != null) {
			number += temp.timecount * temp.rank;
			timecount+= temp.timecount;
			temp = temp.changerank;
		}
		return (float)number/(float)timecount;
		
		
	}
	public int[] toWordUpperAndLowerLimit(Node temp)
	{
		int max = 1;
		int min = 20;
		int[] number = new int[2];
		while (temp != null) {
			if(min>temp.rank)
			{
				min = temp.rank;
			}
			if(max<temp.rank)
			{
				max = temp.rank;
			}
			temp = temp.changerank;
		}
		number[0] = max;
		number[1] = min;
		return number;
		
	}
	public String toChangeSetString(Node temp) {
		String str = "[";
		while (temp.changerank != null) {
			str += temp.rank + "위 -> ";
			temp = temp.changerank;
		}
		str += temp.rank + "위";
		return str + "]";
	}

	public String[][] showHotKeyWord(String filename)// 핫한 키워드 찾기
	{
		File file = new File(filename);
		FileReader fileReader;
		String[][] hotKeyWord = new String[2][20];
		float[] hotScore = new float[20];
		try {
			fileReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufReader.readLine()) != null) {
				String[] temp = line.split("/");
				float temp2 = calculateWordScore(Integer.parseInt(temp[0]), temp[1]);
				for (int i = 19; i >= 0; i--) {
					if (hotScore[i] < temp2) {
						if (i > 0) {
							hotScore[i] = hotScore[i - 1];
							hotKeyWord[1][i] = hotKeyWord[1][i - 1];
							hotKeyWord[0][i] = hotKeyWord[0][i - 1];
						} else {
							hotScore[i] = temp2;
							hotKeyWord[1][i] = temp2+"";
							hotKeyWord[0][i] = temp[1];
						}
					} else {
						if (i < 19) {
							hotScore[i + 1] = temp2;
							hotKeyWord[1][i + 1] = temp2+"";
							hotKeyWord[0][i + 1] = temp[1];
							break;
						} else {
							break;
						}
					}
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
		return hotKeyWord;
	}

	// 해당 단어가 list에서 얼마나 있는지 rank랑 비교해서 더하기,
	public float calculateWordScore(int rank, String word) {
		Node temp = node(rank);
		int HotScore = 0;
		while (temp != null) {
			if (temp.word.equals(word) && temp.initial == true) {
				while (temp.changerank != null) {
					HotScore += (105 - temp.rank * 5) * temp.timecount;
					temp = temp.changerank;
				}
				return (float)HotScore;
			}
			temp = temp.backward;
		}
		return 0;
	}

	public String[][] showCountTime(String filename) {
		File file = new File(filename);
		FileReader fileReader;
		String[][] muchTimeWord = new String[2][20];
		int[] timeCount = new int[20];
		try {
			fileReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufReader.readLine()) != null) {
				String[] temp = line.split("/");
				int temp2 = calculateWordTime(Integer.parseInt(temp[0]), temp[1]);
				for (int i = 19; i >= 0; i--) {
					if (timeCount[i] < temp2) {
						if (i > 0) {
							timeCount[i] = timeCount[i - 1];
							muchTimeWord[0][i] = muchTimeWord[0][i - 1];
						} else {
							timeCount[i] = temp2;
							muchTimeWord[0][i] = temp[1];
						}
					} else {
						if (i < 19) {
							timeCount[i + 1] = temp2;
							muchTimeWord[0][i + 1] = temp[1];
							break;
						} else {
							break;
						}
					}
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
		for(int j = 0;j<20;j++)
		{
			muchTimeWord[1][j] = timeCount[j] + "";
		}
		return muchTimeWord;
	}

	public int calculateWordTime(int rank, String word) {
		Node temp = node(rank);
		int timeCount = 0;
		while (temp != null) {
			if (temp.word.equals(word) && temp.initial == true) {
				while (temp.changerank != null) {
					timeCount += temp.timecount;
					temp = temp.changerank;
				}
				return timeCount;
			}
			temp = temp.backward;
		}
		return 0;
	}
	public String[][] showMyHotKeyWord(String filename,int crawlingNumber) {
		File file = new File(filename);
		FileReader fileReader;
		String[][] myHoyKeyWord = new String[2][20];
		float[] score = new float[20];
		try {
			fileReader = new FileReader(file);
			BufferedReader bufReader = new BufferedReader(fileReader);
			String line;
			while ((line = bufReader.readLine()) != null) {
				String[] temp = line.split("/");
				float temp2 = (float)calculateWordTime(Integer.parseInt(temp[0]), temp[1])/crawlingNumber*100 +calculateWordScore(Integer.parseInt(temp[0]), temp[1])/crawlingNumber;
				for (int i = 19; i >= 0; i--) {
					if (score[i] < temp2) {
						if (i > 0) {
							score[i] = score[i - 1];
							myHoyKeyWord[0][i] = myHoyKeyWord[0][i - 1];
						} else {
							score[i] = temp2;
							myHoyKeyWord[0][i] = temp[1];
						}
					} else {
						if (i < 19) {
							score[i + 1] = temp2;
							myHoyKeyWord[0][i + 1] = temp[1];
							break;
						} else {
							break;
						}
					}
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
		for(int j = 0;j<20;j++)
		{
			myHoyKeyWord[1][j] = score[j] + "";
		}
		return myHoyKeyWord;
	}

}