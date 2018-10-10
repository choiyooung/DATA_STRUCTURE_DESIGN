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
            Node temp1 = node(rank);
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
    	while(temp == null)
    	{
    		for(int i = j; i<words.length ; i++)
    		{
    			if(temp.word.equals(words[i]))
    			{
    				if(temp.rank == (i+1))
    				{
    					temp.timecount++;
    					break;
    				}
    				else
    				{
    					add(words[i], i+1,wordNew(words[i],newWord));
    					Node tmep2 = remove(i+2);
    				}
  
    		
    			}else
    			{
    				
    			}
    			i++;
    		}
    		j++;
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