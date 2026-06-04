import Modal from '../common/Modal'
import { btc, shortHash } from '../../utils/formatters'

export default function TransactionDetail({ tx, onClose }) {
  return (
    <Modal open={Boolean(tx)} title="Transaction" onClose={onClose}>
      {tx && (
        <div className="detail-stack">
          <div className="kv"><span>Hash</span><code>{tx.hash}</code></div>
          <h3>Inputs</h3>
          {tx.inputs.length === 0 && <p className="muted">Coinbase transaction.</p>}
          {tx.inputs.map((input, index) => (
            <div className="tx-chip" key={`${input.prevTxHash}-${index}`}>
              <code>{shortHash(input.prevTxHash, 12)}</code>
              <span>#{input.outputIndex}</span>
            </div>
          ))}
          <h3>Outputs</h3>
          {tx.outputs.map((output, index) => (
            <div className="tx-chip" key={`${output.address}-${index}`}>
              <code>{shortHash(output.address, 12)}</code>
              <span>{btc(output.value)}</span>
            </div>
          ))}
        </div>
      )}
    </Modal>
  )
}
