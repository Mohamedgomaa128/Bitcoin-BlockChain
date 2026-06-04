import { useState } from 'react'
import Card from '../common/Card'
import TransactionDetail from './TransactionDetail'
import { btc, shortHash } from '../../utils/formatters'

export default function TransactionList({ transactions }) {
  const [selected, setSelected] = useState(null)
  return (
    <Card>
      <div className="section-head">
        <h2>Pending transactions</h2>
        <span>{transactions.length}</span>
      </div>
      <div className="table-wrap">
        <table>
          <thead>
            <tr>
              <th>Hash</th>
              <th>Inputs</th>
              <th>Outputs</th>
              <th>Value</th>
            </tr>
          </thead>
          <tbody>
            {transactions.map((tx) => (
              <tr key={tx.hash} onClick={() => setSelected(tx)}>
                <td><code>{shortHash(tx.hash, 12)}</code></td>
                <td>{tx.inputs.length}</td>
                <td>{tx.outputs.length}</td>
                <td>{btc(tx.outputs.reduce((sum, out) => sum + out.value, 0))}</td>
              </tr>
            ))}
          </tbody>
        </table>
        {transactions.length === 0 && <p className="muted empty">No pending transactions.</p>}
      </div>
      <TransactionDetail tx={selected} onClose={() => setSelected(null)} />
    </Card>
  )
}
