import { RefreshCcw } from 'lucide-react'
import Button from '../common/Button'
import { useBlockchain } from '../../hooks/useBlockchain'

export default function Navbar() {
  const { refreshAll, status } = useBlockchain()
  return (
    <header className="navbar">
      <div>
        <p className="eyebrow">Bitcoin Blockchain Web</p>
        <h1>In-memory chain console</h1>
      </div>
      <div className="navbar-actions">
        <span className="status-pill">{status?.chainHeight ?? 0} height</span>
        <Button icon={RefreshCcw} variant="secondary" onClick={refreshAll}>Refresh</Button>
      </div>
    </header>
  )
}
