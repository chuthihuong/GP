package TS_P;

import Common.Const;


//Lop node
class node implements Const {
	String	name;
	node		sibling, children;
	double	value;			
	String	_sbuff;
	int		_curchar	= 0;
	
//Ham tao 1
	public node() {
		name = new String();
		sibling = null;
		children = null;
		value = 0;
	}
//Ham tao 2
	public node(String nname, double value) {
		this.name = nname;
		this.sibling = null;
		this.children = null;
		this.value = value;
	}
//phuong thuc xoa Node
//	void DeleteNode(){
//		node q, p,t;
//		t=this;
//		if(t!=null)
//		{
//		if(t.children==null)
//			t=null;
//		else
//		{
//			p=t.children;
//			while(p!=null)
//			{
//				q=p.sibling;
//				p.DeleteNode();
//				p=q;
//			}
//			t=null;
//		}
//		}
//	}
//	void DeleteNode(node t){
//		node q, p;		
//		if(t!=null)
//		{
//		if(t.children==null)
//			t=null;
//		else
//		{
//			p=t.children;
//			while(p!=null)
//			{
//				q=p.sibling;
//				p.DeleteNode(p);
//				p=q;
//			}
//			t=null;
//		}
//		}
//	}
	
//lay do cao cua cay
	int GetHeightNode(){
		if(this.children == null) return 0;
		else {
			int c, max = 0;
			node p = this.children;
			while(p != null) {
				c = p.GetHeightNode();
				if(c > max) max = c;
				p = p.sibling;
			}
			return max + 1;
		}
	}
	//-------------------------------------------
	node StringToTreeN(){
		String n = new String("");
		int i;
		node t;
//		while(_sbuff.charAt(_curchar) == ' ')
//			_curchar++;
		if(isalpha(_sbuff.charAt(_curchar))) {
			i = 0;
			while(isalnum(_sbuff.charAt(_curchar)) && (i < MAXNAME - 1)) {
				n += _sbuff.charAt(_curchar);
				i++;
				_curchar++;
			}
			// n[i]=0;//terminate the string
			t = new node(n, VOIDVALUE);
			while(_sbuff.charAt(_curchar) == ' ')
				_curchar++;
			switch(_sbuff.charAt(_curchar)) {
				case '(':
					_curchar++;
					t.children = StringToTree();
					_curchar--;
//					_curchar++;
					while(_sbuff.charAt(_curchar) == ' ')
						_curchar++;
					// finding for the next sibling
					if(_sbuff.charAt(_curchar) == ',') {
						_curchar++;
						t.sibling = StringToTree();
					}
					return t;
				case ')':
					// _curchar++;
					return t;
				case ',':
					_curchar++;
					t.sibling = StringToTree();
					return t;
				case '\0':
					return t;// new part
				default:
					System.out.print("Error in the string");
					System.exit(1);
			}
		} else {
			System.out.print("Error in the string");
			System.exit(1);
		}
		return null;
	}
//phuong thuc bien doi xau thanh cay	
	node StringToTree(){
		String n = new String("");
		int i;
		node t;
//		while(_sbuff.charAt(_curchar) == ' ')
//			_curchar++;
		if(isalpha(_sbuff.charAt(_curchar))) {
			i = 0;
			while(isalnum(_sbuff.charAt(_curchar)) && (i < MAXNAME - 1)) {
				StringBuilder builderString = new StringBuilder(n);
//				builderString.setCharAt(i, _sbuff.charAt(_curchar));
				builderString.append(_sbuff.charAt(_curchar));				
				n = builderString.toString();
				// n[i]=_sbuff.charAt(_curchar);
				i++;
				_curchar++;
			}
			// n[i]=0;//terminate the string
			StringBuilder builderString = new StringBuilder(n);
//			builderString.setCharAt(i, '\0');
			//builderString.append("\0");
			n = builderString.toString();
			t = new node(n, VOIDVALUE);
			while(_sbuff.charAt(_curchar) == ' ')
				_curchar++;
			switch(_sbuff.charAt(_curchar)) {
				case '(':
					_curchar++;
					t.children = StringToTree();
					_curchar++;
					while(_sbuff.charAt(_curchar) == ' ')
						_curchar++;
					// finding for the next sibling
					if(_sbuff.charAt(_curchar) == ',') {
						_curchar++;
						t.sibling = StringToTree();
					}
					return t;
				case ')':
					// _curchar++;
					return t;
				case ',':
					_curchar++;
					t.sibling = StringToTree();
					return t;
				case 0:
					return t;// new part
				default:
					System.out.print("Error in the string");
					System.exit(1);
			}
		} else {
			System.out.print("Error in the string");
			System.exit(1);
		}
		return null;
	}
//phuong thuc bien doi xau thanh cay
	node StringToTreeComplete(String s)
	{
		node t;
		_sbuff=s;
		t=new node();
		t=StringToTreeN();
		return t;
		
	}
//phuong thuc bien doi cay thanh xau
	void TreeToString(node r){
		node p;
		String value;
		if(r != null) {
			if((r.name.charAt(0) == 'E') && (r.name.charAt(1) == 'R')) // Ephemeral constants
			{
				value = "";
				value = String.format("%.6f", r.value);
				_sbuff += value;
			} else _sbuff += r.name;
			if(r.children != null) {
				_sbuff += "(";
				p = r.children;
				while(p != null) {
					TreeToString(p);
					p = p.sibling;
					if(p != null) _sbuff += ",";
				}
				_sbuff += ")";
			}
		}
	}
//phuong thuc hien thi xau
	void DisplaySTree(node t){
		_sbuff = "";
		 TreeToString(t);
		 System.out.println(_sbuff);		
	}
	String TreeToStringN(node t)
	{
		_sbuff = "";
		 TreeToString(t);
		 return _sbuff;
	}
//phuong thuc copy node
	node CopyNode(node s){
		node t, p, q;
		if(s != null) {
			t = new node(s.name, s.value);
			q = s.children;
			t.children = CopyNode(q);
			p = t.children;
			while(q != null) {
				p.sibling = CopyNode(q.sibling);
				q = q.sibling;
				p = p.sibling;
			}
			return t;
		}
		return null;
	}

	byte	status;
//phuong thuc duyet Preoder
	void Preorder(node t){
		node p;
		if(t != null) {
			if(status == 0) NodeCount(t);
			else NodeBranchCount(t);
			p = t.children;
			while(p != null) {
				Preorder(p);
				p = p.sibling;
			}
		}
	}

	int	_nodecount;
	byte	_countleave;
//phuong thuc dem node
	void NodeCount(node t){
		_nodecount++;
		if(_countleave == FALSE && (t.children == null)) // if don't count the leaves
		_nodecount--;
	}
//phuong thuc lay kich thuoc cay
	int GetSizeNode(node t, byte countleave){
		_countleave = countleave;
		_nodecount = 0;
		status = 0;
		Preorder(t);
		return _nodecount;
	}

//phuong thuc dem so nhanh cua cay
	int	_nodebranch;
	void NodeBranchCount(node t){
		node p;
		byte i;
		if(t != null) {
			if(t.children != null) {
				i = 0;
				p = t.children;
				while(p != null) {
					i++;
					p = p.sibling;
				}
				_nodebranch += i;
			}
		}
	}
//phuong thuc tinh gia tri trung binh
	double GetAVGNode(node t){
		int size;
		if(t.children == null) return 1.0;
		size = GetSizeNode(t, FALSE);
		_nodebranch = 0;
		status = 1;
		Preorder(t);
		return ((double) _nodebranch) / size;
	}

	static node	_idnode			= new node();
	static node	_idnodeparent	= new node();
	static int	_indexnode;
//phuong thuc tim node
	void FindNode(node t, node parent){
		if(_indexnode > 0) {
			if(t != null) {
				node p;
				_indexnode--;
				if(_indexnode == 0) {
					_idnode = t;
					_idnodeparent = parent;
				} else {
					p = t.children;
					while(p != null) {
						FindNode(p, t);
						p = p.sibling;
					}
				}
			}
		}
	}
//dua den mot node trong cay
	void LocateNode(int i, node t, node parent){
		_indexnode = i;
		_idnode = null;
		_idnodeparent = null;
		FindNode(t, parent);
	}	
//phuong thuc kiem tra co phai ky tu hay khong
	boolean isalnum(char ch){
		return Character.isLetter(ch);
	}
//phuong thuc kiem tra co phai la ky tu va so khong
	boolean isalpha(char ch){
		return (Character.isLetter(ch) || Character.isDigit(ch));
	}
};
