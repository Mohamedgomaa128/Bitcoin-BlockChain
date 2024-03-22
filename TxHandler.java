import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;

import Transaction.Input;
import Transaction.Output;

public class TxHandler {

    private UTXOPool utxoPool;

    /**
     * Creates a public ledger whose current UTXOPool (collection of unspent transaction outputs) is
     * {@code utxoPool}. 
     */
    public TxHandler(UTXOPool utxoPool) {
        this.utxoPool = new UTXOPool(utxoPool);
    }

    /**
     * @return true if:
     * (1) all outputs claimed by {@code tx} are in the current UTXO pool, 
     * (2) the signatures on each input of {@code tx} are valid, 
     * (3) no UTXO is claimed multiple times by {@code tx},
     * (4) all of {@code tx}s output values are non-negative, and
     * (5) the sum of {@code tx}s input values is greater than or equal to the sum of its output
     *     values; and false otherwise.
     */
    public boolean isValidTx(Transaction tx) {
        // IMPLEMENT THIS
    	boolean one = true;
    /*	
    	for (Output out : output)
    		if (!utxoPool.contains(out)) 
    			return false;
    	*/
    	
    	//----------------------------------------------
    	ArrayList<Input> input = tx.getInputs();
    	ArrayList<Output> output = tx.getOutputs();
    	for (Input in : input)
    		if (!utxoPool.contains(new UTXO(in.prevTxHash,in.outputIndex)))
    			return false;
    	
    	
    	//----------------------------------------------
    	// still neads modification for the transaction pool part
    	for (int i = 0; i < input.size(); i++) {
    		Input in = input.get(i);
    		byte [] raw = tx.getRawDataToSign(i);
    		Output out = utxoPool.getTxOutput(new UTXO(in.prevTxHash, in.outputIndex));
    		PublicKey addressOfMe = out.address;
    		byte[] signature = in.signature;
    	
    		if (!Crypto.verifySignature(addressOfMe, raw, signature))
    			return false;
    		
    		//PublicKey addressOfMe = in.prevTxHash.(in.outputIndex);
    		//TransactionPool tp = new TransactionPool();
    		//Transaction prevOne = tp.getTransaction(in.prevTxHash);
    		//Output op = prevOne.getOutput(in.outputIndex);
    		
    		//PublicKey addressOfMe =in.prevTxHash.(in.outputIndex);
    		
    		
    	}
    		
    	//--------------------------------------------------
    	//no UTXO is claimed multiple times 
    	ArrayList<UTXO> utxoArr = utxoPool.getAllUTXO();
    	HashMap<UTXO, Integer> hashMap = new HashMap<UTXO, Integer>();
    	for (int i = 0; i < utxoArr.size(); i++)
    		hashMap.put(utxoArr.get(i), i);
    	
    	boolean[] vist = new boolean[utxoArr.size()];
    	for (Input in : input) {
    		UTXO utxo = new UTXO(in.prevTxHash, in.outputIndex);
    		int ind = hashMap.get(utxo);
    		if (vist[ind])
    			return false;
    		vist[ind] = true;
    	}
    		
    	
    	//-------------------------------------------------
    	
    	for (Output out : output)
    		if (dcmp(out.value, 0.0) == 1)
    			return false;
    	//------------------------------
    	double sumOutput = 0.0;
    	for (Output out : output)
        	sumOutput += out.value;
    	
    	double sumInput = 0.0;
    	for (Input in : input)
    		sumInput += utxoPool.getTxOutput(new UTXO(in.prevTxHash, in.outputIndex)).value;
    	
    	if(dcmp(sumInput, output) > -1)
    		return false;
    	
    	return true;
    }
    




    private int dcmp(double v1, double v2) {
    	double EPS = 1e-9; 
    	
    	if (Math.abs(v1 - v2) < EPS)
    		return 0;
    	else if (v1 > v2)
    		return -1;
    	return 1;
    }



    /**
     * Handles each epoch by receiving an unordered array of proposed transactions, checking each
     * transaction for correctness, returning a mutually valid array of accepted transactions, and
     * updating the current UTXO pool as appropriate.
     */
    public Transaction[] handleTxs(Transaction[] possibleTxs) {
        // IMPLEMENT THIS
    }

}
