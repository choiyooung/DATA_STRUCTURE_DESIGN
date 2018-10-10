package list.linkedlist.implementation;

 
public class LinkedList { 
    private Node head;
    private Node tail;
    private int size = 0;
    private class Node{
    	// 데이터값들
    	private boolean realtime; // 실시건 검색어이면 true, 아니면  false
        private String word; // 실시간 검색어의 단어
        private int rank; // 검색어의 순위
        private int timecount; // 1분동안 유지되면 +1해서 몇분동안했는지 알려주는 변수
        private boolean initial; // 처음으로 들어온 경우 
        // 다음 노드들
        private Node next; // 실시간 검색어 끼리 연결하는 노드
        private Node changerank; // 실시간 검색어의 순위가 바꼈을 때 보여주는 노드,
        private Node backward; // 해당 순위에 있었던 검색어들을 연결하는 노드
        public Node(String word,int rank,boolean initial) {
            this.word = word;
            this.realtime = true;
            this.initial = initial;
            this.timecount = 1;
            this.rank = rank;
            
            this.next = null;
            this.changerank = null;
            this.backward = null;
        }
        public Node(Node other)
        {
        	 this.word = other.word;
             this.realtime = other.realtime;
             this.initial = other.initial;
             this.timecount = other.timecount;
             this.rank = other.rank;
             
             this.next = other.next;
             this.changerank = other.changerank;
             this.backward = other.backward; 
        }
        public String toString(){
            return String.valueOf(this.word);
        }
    }
    public void addFirst(String word,int rank,boolean initial){
       //새로운 노드를 생성한다.
    	Node newNode = new Node(word,rank,initial);
       //원래있던 첫노드를 이 노드가 가르키게한다.
        newNode.next = head;
        //헤드를 이노드에 넣어논다.
        head = newNode;
        size++;
        if(head.next == null){
            tail = head;
        }
    }
    public void addLast(String word,int rank,boolean initial){
        // 첫 노드를 생성한다.
    	Node newNode = new Node(word,rank,initial);
        // 사이즈가 0이라면 아무것도 없기때문에 이 노드가 첫노드이다.
        if(size == 0){
            addFirst(word,rank,initial);
        } else {
            // 마지막 노드의 다음 노드를 새노드를 가르키게 한다.
            tail.next = newNode;
            // tail이 새노드를 가르키게 한다.
            tail = newNode;
            //사이즈를 1증가 시킨다.
            size++;
        }
    }
    Node node(int rank) {
        Node x = head;
        for (int i = 0; i < rank -1 ; i++)
            x = x.next;
        return x;
    }
    public void add(String word,int rank,boolean initial){
        if(rank == 1){
            addFirst(word,rank,initial);
        } else {
            Node temp1 = node(rank-1);
            Node temp2 = temp1.next;
            Node newNode = new Node(word,rank,initial);
            temp1.next = newNode;
            newNode.next = temp2;
            size++;
            if(newNode.next == null){
                tail = newNode;
            }
        }
    }
    public void addBackword(Node preWord,int rank)
    {
    	Node temp = node(rank);
    	if(temp.backward ==null) {
    		temp.backward = preWord;
    	}
    	while(temp.backward !=null)
    	{
    		if(temp.backward.timecount >= preWord.timecount)
    		{
    			temp = temp.backward;
    		}
    		else
    		{
    			preWord.backward = temp.backward;
    			temp.backward = preWord;
    			break;
    		}
    	}
    	size++;
    }
    public String toString() {
        if(head == null){
            return "[]";
        }       
        Node temp = head;
        String str = "[";
        while(temp.next != null){
            str += temp.word + ",";
            temp = temp.next;
        }
        str += temp.word;
        return str+"]";
    }
    public Node removeFirst(){
        Node temp = head;
        temp.realtime = false;
        temp.next =null;
        temp.backward =null;
        temp.changerank = null;
        head = temp.next;
        size--;
        return temp;
    }
    public Node remove(int rank){
        if(rank == 1)
            return removeFirst();
        Node temp = node(rank-1);
        Node todoDeleted = temp.next;
        todoDeleted.realtime = false;
        todoDeleted.backward = null;
        todoDeleted.changerank = null;
        todoDeleted.next = null;
        temp.next = temp.next.next;
        if(todoDeleted == tail){
            tail = temp;
        }
        size--;
        return todoDeleted;
    }
    public Object removeLast(){
        return remove(size-1);
    }
    public int size(){
        return size;
    }
    public Object get(int k){	
        Node temp = node(k);
        return temp.word;
    }
    public int indexOf(String word){

        Node temp = head;
        int index = 0;
        while(temp.word != word){
            temp = temp.next;
            index++;
            if(temp == null)
                return -1;
        }
        return index;
    }
    //위는 기본적인 함수,변수 정의
    //아래는 그래프 만드는 함수 정의
    public void makeInitialGraph(String[] words)
	{
		for(int i =  words.length ; i >0 ; i--)
		{
			addFirst(words[i-1], i, true);
		}
	}
    public void makeGraph(String[] words, String[] newWord) {
    	Node temp = head;
    	int j = 0;
    	while(temp != null)
    	{
    		if(words[temp.rank - 1] != null) //words가 null인경우 이미 바뀐 경우 이므로 다음 검색어를 비교해본다.
    		{
    			for(int i = 0; i<words.length ; i++)
    			{
	    			if(temp.word.equals(words[i])) //현 순위의 검색어가 새로 들어온 검색어에 있다면
	    			{
	    				if(temp.rank == (i+1))//현 순위 검색어가 똑같은 순위로 들어올경우
	    				{
	    					temp.timecount++;
	    					words[i] = null;
	    					break;
	    				}
	    				else // 다른 순위에 있을 경우
	    				{
	    					if(!words[i].equals(node(i+1).word)) //다른순위에 있더라도, 이미 다음검색어로 바뀐경우 다시 바꾸지않는다.
	    					{
		    					//해당 순위의 검색어를 다음 실시간 검색어로 바꿔준다.
		    					add(words[temp.rank-1],temp.rank,wordNew(words[temp.rank-1],newWord));
		    					temp = node(temp.rank);
		    					temp.backward = temp.next.backward;
		    					Node temp1 = remove(temp.rank+1);
		    					addBackword(temp1,temp1.rank);
		    					// 전에 있는  검색어가 어떤 랭크의 순위에 위치해 있는에 있는 
		    					add(words[i],i+1,false);
		    					Node temp3 = node(i+1);
		    					temp3.backward = temp3.next.backward;
		    					Node temp2 = remove(i+2);
		    					temp1.changerank = temp3;
		    					addBackword(temp2,temp2.rank);
		    					words[i] = null;
		    					while(true)
		    					{
		    						int t;
		    						for(t=0;t<words.length;t++)
		    						{
		    							if(temp2.word.equals(words[t]))
		    							{
		    								add(words[t],t+1,false);
		    								temp3 = node(t+1);
		    								temp3.backward = temp3.next.backward;
		    								temp2.changerank = temp3;
		    								temp2 = remove(t+2);
		    								addBackword(temp2,temp2.rank);
		    								words[t] = null;
		    								break;
		    							}
		    						}
		    						if(t == words.length)
		    						{
		    							break;
		    						}
		    					}
		    				}
	    					else
	    					{
	    						add(words[temp.rank-1],temp.rank,wordNew(words[temp.rank-1],newWord));
		    					temp = node(temp.rank);
		    					temp.backward = temp.next.backward;
		    					Node temp1 = remove(temp.rank+1);
		    					addBackword(temp1,temp1.rank);
		    					temp1.changerank = node(i+1);
		    					words[i] = null;
	    					}
	    				}
	    			}
	    			if(i==words.length)//해당 순위 검색어가 새로들어온 검색어들중 없을 경우
	    			{
	    				add(words[temp.rank-1],temp.rank,wordNew(words[temp.rank-1],newWord));
    					temp = node(temp.rank);
    					temp.backward = temp.next.backward;
    					temp.next.backward = null;
    					Node temp1 = remove(temp.rank+1);
    					addBackword(temp1,temp1.rank);	
	    			}
	    		}
    		}
    		temp = temp.next;
    	}
    }
	// 새로운 단어가 어니면 false, 맞으면 true
    public boolean wordNew(String word,String[] newWord)
    {
    	for(int i=0;i<newWord.length;i++)
    	{
    		if(word.contains(newWord[i]))
    		{
    			return true;
    		}
    			
    	}
    	return false;
    }
}