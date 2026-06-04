import { Hammer, Plus, Send } from 'lucide-react'
import { useNavigate } from 'react-router-dom'
import Button from '../common/Button'
import Card from '../common/Card'
import StatsCards from './StatsCards'
import RecentActivity from './RecentActivity'
import { useBlockchain } from '../../hooks/useBlockchain'

export default function Dashboard() {
  const navigate = useNavigate()
  const { status, activity, pendingTxs, blocks } = useBlockchain()

  return (
    <div className="page-grid">
      <StatsCards status={status} />
      <div className="two-column">
        <Card>
          <div className="section-head">
            <h2>Quick actions</h2>
          </div>
          <div className="action-grid">
            <Button icon={Plus} onClick={() => navigate('/wallets')}>Create Wallet</Button>
            <Button icon={Send} variant="secondary" onClick={() => navigate('/transactions')}>Send BTC</Button>
            <Button icon={Hammer} variant="secondary" onClick={() => navigate('/mining')}>Mine Block</Button>
          </div>
        </Card>
        <RecentActivity activity={activity} pendingTxs={pendingTxs} blocks={blocks} />
      </div>
    </div>
  )
}
