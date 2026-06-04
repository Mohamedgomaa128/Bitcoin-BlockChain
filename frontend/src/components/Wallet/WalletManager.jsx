import { useState } from 'react'
import { Plus } from 'lucide-react'
import Button from '../common/Button'
import Card from '../common/Card'
import WalletCard from './WalletCard'
import { useBlockchain } from '../../hooks/useBlockchain'

export default function WalletManager() {
  const [name, setName] = useState('')
  const { wallets, createWallet } = useBlockchain()

  const submit = async (event) => {
    event.preventDefault()
    await createWallet(name)
    setName('')
  }

  return (
    <div className="page-grid">
      <Card>
        <form className="inline-form" onSubmit={submit}>
          <label>
            Wallet name
            <input value={name} onChange={(event) => setName(event.target.value)} placeholder="Trading wallet" />
          </label>
          <Button icon={Plus}>Create Wallet</Button>
        </form>
      </Card>
      <div className="wallet-grid">
        {wallets.map((wallet) => <WalletCard wallet={wallet} key={wallet.id} />)}
      </div>
    </div>
  )
}
