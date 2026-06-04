import Navbar from './Navbar'
import Sidebar from './Sidebar'
import Toast from '../common/Toast'
import { useBlockchain } from '../../hooks/useBlockchain'

export default function Layout({ children }) {
  const { error } = useBlockchain()
  return (
    <div className="app-shell">
      <Sidebar />
      <main className="main-panel">
        <Navbar />
        {children}
      </main>
      <Toast message={error} />
    </div>
  )
}
