import { useState } from 'react'
import { Hammer } from 'lucide-react'
import Button from '../common/Button'
import Card from '../common/Card'
import MiningAnimation from './MiningAnimation'
import { useBlockchain } from '../../hooks/useBlockchain'

export default function MiningPanel() {
  const { wallets, mineBlock, pendingTxs } = useBlockchain()
  const [minerWalletId, setMinerWalletId] = useState('')
  const [mining, setMining] = useState(false)

  const submit = async (event) => {
    event.preventDefault()
    setMining(true)
    try {
      await mineBlock(minerWalletId)
    } finally {
      setTimeout(() => setMining(false), 450)
    }
  }

  return (
    <div className="two-column">
      <Card>
        <form className="form-grid" onSubmit={submit}>
          <label className="wide">
            Miner wallet
            <select value={minerWalletId} onChange={(event) => setMinerWalletId(event.target.value)} required>
              <option value="">Select wallet</option>
              {wallets.map((wallet) => <option value={wallet.id} key={wallet.id}>{wallet.name}</option>)}
            </select>
          </label>
          <Button icon={Hammer} disabled={mining}>{mining ? 'Mining...' : 'Mine Block'}</Button>
        </form>
      </Card>
      <Card>
        <div className="section-head">
          <h2>Block candidate</h2>
          <span>{pendingTxs.length} txs</span>
        </div>
        <MiningAnimation active={mining} />
      </Card>
    </div>
  )
}
