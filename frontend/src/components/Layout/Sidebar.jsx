import { Blocks, Hammer, LayoutDashboard, Send, Wallet } from 'lucide-react'
import { NavLink } from 'react-router-dom'

const items = [
  { to: '/', label: 'Dashboard', icon: LayoutDashboard },
  { to: '/blockchain', label: 'Blockchain', icon: Blocks },
  { to: '/wallets', label: 'Wallets', icon: Wallet },
  { to: '/transactions', label: 'Transactions', icon: Send },
  { to: '/mining', label: 'Mining', icon: Hammer },
]

export default function Sidebar() {
  return (
    <aside className="sidebar">
      <div className="brand">₿</div>
      <nav>
        {items.map(({ to, label, icon: Icon }) => (
          <NavLink key={to} to={to} className={({ isActive }) => (isActive ? 'nav-item active' : 'nav-item')}>
            <Icon size={19} />
            <span>{label}</span>
          </NavLink>
        ))}
      </nav>
    </aside>
  )
}
