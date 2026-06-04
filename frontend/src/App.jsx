import { Routes, Route } from 'react-router-dom'
import Layout from './components/Layout/Layout'
import Dashboard from './components/Dashboard/Dashboard'
import BlockchainViewer from './components/Blockchain/BlockchainViewer'
import WalletManager from './components/Wallet/WalletManager'
import TransactionForm from './components/Transaction/TransactionForm'
import TransactionList from './components/Transaction/TransactionList'
import MiningPanel from './components/Mining/MiningPanel'
import Loader from './components/common/Loader'
import { useBlockchain } from './hooks/useBlockchain'

function TransactionsPage() {
  const { pendingTxs } = useBlockchain()
  return (
    <div className="page-grid">
      <TransactionForm />
      <TransactionList transactions={pendingTxs} />
    </div>
  )
}

export default function App() {
  const { loading } = useBlockchain()
  return (
    <Layout>
      {loading ? (
        <Loader />
      ) : (
        <Routes>
          <Route path="/" element={<Dashboard />} />
          <Route path="/blockchain" element={<BlockchainViewer />} />
          <Route path="/wallets" element={<WalletManager />} />
          <Route path="/transactions" element={<TransactionsPage />} />
          <Route path="/mining" element={<MiningPanel />} />
        </Routes>
      )}
    </Layout>
  )
}
