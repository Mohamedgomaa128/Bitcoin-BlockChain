import { Copy } from 'lucide-react'
import Button from '../common/Button'
import Card from '../common/Card'
import BalanceDisplay from './BalanceDisplay'
import { shortHash } from '../../utils/formatters'

export default function WalletCard({ wallet }) {
  const copyKey = () => navigator.clipboard?.writeText(wallet.publicKey)
  return (
    <Card className="wallet-card">
      <div className="section-head">
        <h2>{wallet.name}</h2>
        <span>{wallet.utxos.length} UTXOs</span>
      </div>
      <BalanceDisplay balance={wallet.balance} />
      <div className="hash-line">
        <code>{shortHash(wallet.publicKey, 15)}</code>
        <Button icon={Copy} variant="ghost" onClick={copyKey}>Copy</Button>
      </div>
    </Card>
  )
}
