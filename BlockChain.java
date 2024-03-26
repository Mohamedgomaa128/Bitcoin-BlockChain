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
    private static int BlockArrange = 0;
    
    
    
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
		}
		public BlockNode(Block theBlock) {
			this.myBlock = theBlock;
			//this.parent = null;
			this.children = new ArrayList<BlockNode>();
			this.level = 1;
			this.myNumber = 1;
		}

		
		public BlockNode(Block myBlock, BlockNode parent, ArrayList<BlockNode> children, int level) {
			this.myBlock = myBlock;
			//this.parent = parent;
			this.children = children;
			this.level = level;
			//parent.addChild(this);
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
        // IMPLEMENT THIS
    	//this.genesisBlock = new BlockNode(genesisBlock);
    	this.curLevelBlocks = new ArrayList<BlockNode>();
    	
    	BlockNode gen = new BlockNode(genesisBlock, null, new ArrayList<BlockNode>(), 1);
    	curLevelBlocks.add(gen);
    	this.currentLevel = 1;
    	this.maxHeight = 1;
    	gen.myNumber = ++BlockArrange;
    	
    	
    	
    	this.tPool = new TransactionPool();
    	map = new HashMap<>();
    	this.map.put(new ByteArrayWrapper(genesisBlock.getHash()), gen);
    }

    

    public void updateCurrentBlocks() {
    	if (maxHeight - currentLevel > CUT_OFF_AGE) {
    		ArrayList<BlockNode> cur = new ArrayList<BlockNode>();
    		
    		for (BlockNode b : curLevelBlocks) {
    			cur.addAll(b.getChildren());
    			map.remove(new ByteArrayWrapper(b.myBlock.getHash()));
    		}
    		
    		//System.out.println("maxHeight - currentLevel = " + (maxHeight - currentLevel));
    		System.out.println("cur level blocks " + curLevelBlocks);
    		
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
    	// take care of hash of the prev block
    	// STILL
        // IMPLEMENT THIS
    	//System.out.println("null parent");
    	//first block
    	if (block.getPrevBlockHash() == null)
    		return false;
    	
    	
    	// check for transactions first
    	BlockNode parentNode = map.get(new ByteArrayWrapper(block.getPrevBlockHash()));
    	//System.out.println("the parentNode me is " + parentNode);
    	
    	// parent is removed , you came late
    	if (parentNode == null)
    		return false;
    	
    	// i think we should repeat this step till we reach the first block in my chain so i can make use of all utxo
    	ArrayList<Transaction> trans = parentNode.myBlock.getTransactions();
    	UTXOPool utxoPool = new UTXOPool();
    	Transaction coinBase = parentNode.myBlock.getCoinbase();
    	if (!coinBase.isCoinbase())
    		return false;
    	if (coinBase.getInputs().size() != 0)
    		return false;
    	
    	// add coin base UTXO
    	ArrayList<Output> coinOutput = coinBase.getOutputs();
    	for (int i = 0; i < coinOutput.size(); i++)
			utxoPool.addUTXO(new UTXO(coinBase.getHash(), i), coinOutput.get(i));

    	
    	// add outputs of previous block as UTXO 
    	for (Transaction t : trans) {
    		ArrayList<Output> output = t.getOutputs();
    		
    		for (int i = 0; i < output.size(); i++)
    			utxoPool.addUTXO(new UTXO(t.getHash(), i), output.get(i));

    	}
    		
    	/*for (int i = 0; i < trans.size(); i++) {
    		Transaction t = trans.get(i);
    		for (int j = 0; j < t.getOutputs(); i++)
    			utxoPool.addUTXO(new UTXO(t.getHash(), i), t.getOutput(i));
    	}
    	*/
    	TxHandler hand = new TxHandler(utxoPool);
    	
    	for (int i = 0; i < trans.size(); i++) 
    		if (!hand.isValidTx(trans.get(i)))
    			return false;
    	
    	
    	
    	int blockLevel = parentNode.level + 1;
    	
    	
    	//System.out.println("max height  " + maxHeight);
    	//System.out.println("block level " + blockLevel);
    	//System.out.println("result " + (maxHeight - CUT_OFF_AGE));
    	if (blockLevel <= maxHeight - CUT_OFF_AGE)
    		return false;
    	
    	// may be used again
    	BlockNode newNode = new BlockNode(block, parentNode, new ArrayList<BlockNode>(), blockLevel);
    	
    	newNode.myNumber = ++BlockArrange;
    	parentNode.addChild(newNode);
    	maxHeight = Math.max(maxHeight, blockLevel);
    	
    	this.map.put(new ByteArrayWrapper(block.getHash()), newNode);

    	updateCurrentBlocks();
    	
    	//parentNode.addChild(new BlockNode(block));
    	
    	for (Transaction t : block.getTransactions())
    		tPool.removeTransaction(t.getHash());
    	// add the block UTXO to the transactoin
    	
    	System.out.println("added successfully " + newNode.myNumber + " at level " + newNode.level);
    	return true;
    }
    

    /** Add a transaction to the transaction pool */
    public void addTransaction(Transaction tx) {
    	tPool.addTransaction(tx);
    }
}