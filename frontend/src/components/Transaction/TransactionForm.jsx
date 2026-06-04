import { useMemo, useState } from 'react'
import { Send } from 'lucide-react'
import Button from '../common/Button'
import Card from '../common/Card'
import { useBlockchain } from '../../hooks/useBlockchain'

export default function TransactionForm() {
  const { wallets, createTransaction } = useBlockchain()
  const [senderWalletId, setSenderWalletId] = useState('')
  const [recipientWalletId, setRecipientWalletId] = useState('')
  const [recipientPublicKey, setRecipientPublicKey] = useState('')
  const [amount, setAmount] = useState('1')

  const sender = useMemo(() => wallets.find((wallet) => wallet.id === senderWalletId), [senderWalletId, wallets])
  const recipientKey = recipientWalletId
    ? wallets.find((wallet) => wallet.id === recipientWalletId)?.publicKey
    : recipientPublicKey

  const submit = async (event) => {
    event.preventDefault()
    await createTransaction({ senderWalletId, recipientPublicKey: recipientKey, amount: Number(amount) })
    setAmount('1')
  }

  return (
    <Card>
      <form className="form-grid" onSubmit={submit}>
        <label>
          Sender
          <select value={senderWalletId} onChange={(event) => setSenderWalletId(event.target.value)} required>
            <option value="">Select wallet</option>
            {wallets.map((wallet) => <option value={wallet.id} key={wallet.id}>{wallet.name} ({wallet.balance.toFixed(2)} BTC)</option>)}
          </select>
        </label>
        <label>
          Recipient wallet
          <select value={recipientWalletId} onChange={(event) => setRecipientWalletId(event.target.value)}>
            <option value="">Use public key input</option>
            {wallets.filter((wallet) => wallet.id !== senderWalletId).map((wallet) => <option value={wallet.id} key={wallet.id}>{wallet.name}</option>)}
          </select>
        </label>
        {!recipientWalletId && (
          <label className="wide">
            Recipient public key
            <input value={recipientPublicKey} onChange={(event) => setRecipientPublicKey(event.target.value)} required={!recipientWalletId} />
          </label>
        )}
        <label>
          Amount
          <input type="number" min="0.00000001" step="0.00000001" max={sender?.balance || undefined} value={amount} onChange={(event) => setAmount(event.target.value)} required />
        </label>
        <Button icon={Send} className="form-submit">Submit Transaction</Button>
      </form>
    </Card>
  )
}
