import Modal from '../common/Modal'
import { btc, shortHash } from '../../utils/formatters'

export default function BlockDetail({ block, onClose }) {
  return (
    <Modal open={Boolean(block)} title={block ? `Block ${block.height}` : ''} onClose={onClose}>
      {block && (
        <div className="detail-stack">
          <div className="kv"><span>Hash</span><code>{block.hash}</code></div>
          <div className="kv"><span>Previous</span><code>{block.prevBlockHash || 'genesis'}</code></div>
          <div className="kv"><span>Coinbase</span><strong>{btc(block.coinbaseValue)}</strong></div>
          <div className="kv"><span>Recipient</span><code>{shortHash(block.coinbaseRecipient, 18)}</code></div>
          <h3>Transactions</h3>
          {block.transactions.length === 0 && <p className="muted">Only the coinbase transaction is included.</p>}
          {block.transactions.map((tx) => (
            <div className="tx-chip" key={tx.hash}>
              <code>{shortHash(tx.hash, 14)}</code>
              <span>{tx.outputs.length} outputs</span>
            </div>
          ))}
        </div>
      )}
    </Modal>
  )
}
