import { Blocks, CircleDollarSign, Send, Wallet } from 'lucide-react'
import { motion } from 'framer-motion'
import Card from '../common/Card'
import { btc } from '../../utils/formatters'

const icons = [Blocks, CircleDollarSign, Send, Wallet]

export default function StatsCards({ status }) {
  const stats = [
    ['Total Blocks', status?.totalBlocks ?? 0],
    ['Chain Height', status?.chainHeight ?? 0],
    ['Pending Txs', status?.pendingTransactions ?? 0],
    ['Wallets', status?.totalWallets ?? 0],
    ['Coins', btc(status?.totalCoinsInCirculation ?? 0)],
  ]

  return (
    <div className="stats-grid">
      {stats.map(([label, value], index) => {
        const Icon = icons[index % icons.length]
        return (
          <motion.div key={label} initial={{ opacity: 0, y: 12 }} animate={{ opacity: 1, y: 0 }} transition={{ delay: index * 0.04 }}>
            <Card className="stat-card">
              <Icon size={22} />
              <span>{label}</span>
              <strong>{value}</strong>
            </Card>
          </motion.div>
        )
      })}
    </div>
  )
}
