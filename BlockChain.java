package proj;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;

import proj.Transaction.Output;

// The BlockChain class should maintain only limited block nodes to satisfy the functionality.
// You should not have all the blocks added to the block chain in memory 
// as it would cause a memory overflow.

public class BlockChain {
    public static final int CUT_OFF_AGE = 10;
    private ArrayList<BlockNode> curLevelBlocks;
    private int currentLevel;
    private TransactionPool tPool;
    private int maxHeight; // for looping to get the max height block
    private HashMap<ByteArrayWrapper, BlockNode> map;
    private int BlockArrange = 0;
    private HashMap<UTXO, Boolean> doubleSpendingOffMap; // utxo, spentOrNot
    
    
    static class BlockNode {
    	
		private Block myBlock;
		//private BlockNode parent; // null if root // not important now 
		private ArrayList<BlockNode> children;
		private int level;
		private int myNumber;
		private UTXOPool myUtxoPool;
		
		@Override
		public String toString() {
			return "level " + level + " myNumber " + myNumber;
		}
		
		public BlockNode() {
			this.children = new ArrayList<BlockNode>();
			this.myUtxoPool = new UTXOPool();
		}
		public BlockNode(Block theBlock) {
			this.myBlock = theBlock;
			//this.parent = null;
			this.children = new ArrayList<BlockNode>();
			this.level = 1;
			this.myNumber = 1;
			this.myUtxoPool = new UTXOPool();
		}

		
		public BlockNode(Block myBlock, BlockNode parent, ArrayList<BlockNode> children, int level) {
			this.myBlock = myBlock;
			//this.parent = parent;
			this.children = children;
			this.level = level;
			//parent.addChild(this);
			this.myUtxoPool = new UTXOPool();
		}
		 

		public int getLevel() {
			return level;
		}


		public void setLevel(int level) {
			this.level = level;
		}


		public Block getMyBlock() {
			return myBlock;
		}

		public void setMyBlock(Block myBlock) {
			this.myBlock = myBlock;
		}
/*
		public BlockNode getParent() {
			return parent;
		}

		public void setParent(BlockNode parent) {
			this.parent = parent;
		}
*/
		public ArrayList<BlockNode> getChildren() {
			return children;
		}

		public void setChildren(ArrayList<BlockNode> children) {
			this.children = children;
		}
		
		public void addChild(BlockNode child) {
			this.children.add(child);
		}
		
		
	}
	
    
    
    /**
     * create an empty blockchain with just a genesis block. Assume {@code genesisBlock} is a valid
     * block
     */
    public BlockChain(Block genesisBlock) {
        this.curLevelBlocks = new ArrayList<BlockNode>();
    	
        
    	BlockNode gen = new BlockNode(genesisBlock, null, new ArrayList<BlockNode>(), 1);
    	curLevelBlocks.add(gen);
    	this.currentLevel = 1;
    	this.maxHeight = 1;
    	//System.out.println(BlockArrange);
    	gen.myNumber = ++BlockArrange;
    	
    	
    	
    	
    	this.tPool = new TransactionPool();
    	map = new HashMap<>();
    	this.map.put(new ByteArrayWrapper(genesisBlock.getHash()), gen);
    	this.doubleSpendingOffMap = new HashMap<>();
    	
    	
    	
    	//coin base >> 0, output(0)
    	UTXO u = new UTXO(genesisBlock.getCoinbase().getHash(), 0);
    	this.doubleSpendingOffMap.put(u, false);
    	gen.myUtxoPool.addUTXO(u, genesisBlock.getCoinbase().getOutput(0));
    	
    	
    	//trans , loop, i, output(i)
    	// add the transactions UTXOs if contain
    	ArrayList<Transaction> list = genesisBlock.getTransactions();
    	for (int i = 0; i < list.size(); i++) {
    		Transaction t = list.get(i);
    		UTXO uu = new UTXO(t.getHash(), i);
        	gen.myUtxoPool.addUTXO(uu, t.getOutput(i));
        	doubleSpendingOffMap.put(uu, false);
    	}
    	
    }

    

    public void updateCurrentBlocks() {
    	if (maxHeight - currentLevel > CUT_OFF_AGE) {
    		ArrayList<BlockNode> cur = new ArrayList<BlockNode>();
    		
    		for (BlockNode b : curLevelBlocks) {
    			// add all my children
    			cur.addAll(b.getChildren());
    			
    			// pass my UTXOs to my Children when the node removed , because the possible nodes that can benifit from my UTXOs are my grandchildrens
    			for (BlockNode bb : b.getChildren())
    				for (UTXO u : b.myUtxoPool.getAllUTXO())
    					bb.myUtxoPool.addUTXO(new UTXO(u.getTxHash(), u.getIndex()), b.myUtxoPool.getTxOutput(u));
    			
    			//remove my block
    			map.remove(new ByteArrayWrapper(b.myBlock.getHash()));
    		}
    		
    		//System.out.println("maxHeight - currentLevel = " + (maxHeight - currentLevel));
    		//System.out.println("cur level blocks " + curLevelBlocks);
    		
    		curLevelBlocks = cur;
    		currentLevel++;
    		System.gc();
    	}
    	
    }
    /** Get the maximum height block */
    public Block getMaxHeightBlock() {
    	// we need to get the oldest one of them 
    	
    	//System.out.println("cur Level Blocks " + curLevelBlocks);
    	
    	//BFS or according to ds that may be changed
    	// we may take the level from the lastNode , to be updated by the last Node
    	// to be tested 
    	Queue<BlockNode> q = new ArrayDeque<BlockNode>();   
    	for (int i = 0; i < curLevelBlocks.size(); i++)
    		q.add(curLevelBlocks.get(i));
    	
    	int level = currentLevel;
    	int sz = q.size();
    	BlockNode cur = null;
    	BlockNode needed = null;
    	
    			
    	while(!q.isEmpty()) {
    		int lastOne = (int) 1e9;
    		System.out.println(q);
    		for (int i = 0; i < sz; i++) {
    			cur = q.poll();
    			if (cur.myNumber < lastOne) {
    				needed = cur;
    				lastOne = cur.myNumber;
    				//System.out.println(lastOne);
    			}
    			
    			ArrayList<BlockNode> children = cur.children;
    			System.out.println("children of " + cur + " are " + children);
    			if (children != null)
	    			for (BlockNode b: children)
	    				q.add(b);
    			
    			
    		}
    		sz = q.size();
    		
    		if (!q.isEmpty())
    			level++;
    	}
    	
    	maxHeight = level;
    	System.out.println("maxHeight " + level + " //// from node " + cur.myNumber);
    	
    	return needed.myBlock;
    }

    /** Get the UTXOPool for mining a new block on top of max height block */
    public UTXOPool getMaxHeightUTXOPool() {
    	Block b = getMaxHeightBlock();
    	UTXOPool pool = new UTXOPool();
    	
    	for (Transaction t :  b.getTransactions()) {
    		ArrayList<Output> output = t.getOutputs();
    		
    		for (int i = 0; i < output.size(); i++) 
    			pool.addUTXO(new UTXO(t.getHash(), i), output.get(i));
    	}
    	
    	return pool;
    }

    /** Get the transaction pool to mine a new block */
    public TransactionPool getTransactionPool() {
    	return tPool;
    }

    /**
     * Add {@code block} to the blockchain if it is valid. For validity, all transactions should be
     * valid and block should be at {@code height > (maxHeight - CUT_OFF_AGE)}, where maxHeight is 
     * the current height of the blockchain.
	 * <p>
	 * Assume the Genesis block is at height 1.
     * For example, you can try creating a new block over the genesis block (i.e. create a block at 
	 * height 2) if the current blockchain height is less than or equal to CUT_OFF_AGE + 1. As soon as
	 * the current blockchain height exceeds CUT_OFF_AGE + 1, you cannot create a new block at height 2.
     * 
     * @return true if block is successfully added
     */
    public boolean addBlock(Block block) {

    	
    	/* put all possible UTXOs
    	 * loop on transactions and check validity
    	 * check for height 
    	 * if ok >> add the block and update the block chain and add the UTXOs
    	 * 
    	 */
    	
    	//---------------------------------------------------
    	//if pass genis
    	if (block.getPrevBlockHash() == null)
    		return false;
    	//----------------------------------------------------
    	
    	
    	// check for transactions first
    	BlockNode parentNode = map.get(new ByteArrayWrapper(block.getPrevBlockHash()));
    	
    	//--------------------------------------------------------------
    	// parent is removed , you came late
    	if (parentNode == null)
    		return false;
    	//-------------------------------------------------------------------
    	
    	// i think we should repeat this step till we reach the first block in my chain so i can make use of all utxo
    	
    	UTXOPool utxoPool = new UTXOPool();
    	
    	// all UTXOs of grandparents 
    	BlockNode bb = parentNode;
    	
    	while (bb != null) {
    		// Recursively add the remaining transaction in each Block Node to choose from them 
    		
    		for (UTXO u : bb.myUtxoPool.getAllUTXO()) {
    			Boolean bool = doubleSpendingOffMap.get(u);
    			
    			if (bool == false)
    				utxoPool.addUTXO(u, bb.myUtxoPool.getTxOutput(u));
    		}
    		
    		if (bb.myBlock.getPrevBlockHash() != null)
    			bb = map.get(new ByteArrayWrapper(bb.myBlock.getPrevBlockHash()));
    		else
    			bb = null; // break;
    	}
	
    	//---------------------------------------------------------------------
    	
    	TxHandler hand = new TxHandler(utxoPool);
    	
    	for (int i = 0; i < block.getTransactions().size(); i++) 
    		if (!hand.isValidTx(block.getTransactions().get(i)))
    			return false;
    	
    	// transactions are correct    	
    	int blockLevel = parentNode.level + 1;
    	
    	//height check
    	if (blockLevel <= maxHeight - CUT_OFF_AGE)
    		return false;
    	
    	//height is correct 
    	
    	//now add the node and update the blockchain
    	BlockNode newNode = new BlockNode(block, parentNode, new ArrayList<BlockNode>(), blockLevel);
    	
    	newNode.myNumber = ++BlockArrange;
    	parentNode.addChild(newNode);
    	maxHeight = Math.max(maxHeight, blockLevel);
    	
    	this.map.put(new ByteArrayWrapper(block.getHash()), newNode);

    	
    	// update current level of blocks
    	updateCurrentBlocks();
    	
    	
    	// add my UTXOS to my block Node
    	ArrayList<Transaction> newNodeTrans = newNode.myBlock.getTransactions();
    	for (Transaction t : newNodeTrans) {
    		ArrayList<Output> output = t.getOutputs();
    		
    		for (int i = 0; i < output.size(); i++) {
    			UTXO u = new UTXO(t.getHash(), i);
    			newNode.myUtxoPool.addUTXO(u, output.get(i));
    			doubleSpendingOffMap.put(u, false);
    		}
    	}    	
    	
    	// add my coinbase transaction to UTXOPool
    	Transaction coTrans = newNode.myBlock.getCoinbase();
    	UTXO uu = new UTXO(coTrans.getHash(), 0);
    	newNode.myUtxoPool.addUTXO(uu, coTrans.getOutput(0));
    	doubleSpendingOffMap.put(uu, false);
    	
    	
    	//remove the transactions form the transaction pool
    	for (Transaction t : block.getTransactions())
    		tPool.removeTransaction(t.getHash());
    	
    	
    	//System.out.println("added successfully " + newNode.myNumber + " at level " + newNode.level);
    	return true;
    }
    

    /** Add a transaction to the transaction pool */
    public void addTransaction(Transaction tx) {
    	tPool.addTransaction(tx);
    }
}