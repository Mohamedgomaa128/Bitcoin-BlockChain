import Card from '../common/Card'
import { shortHash } from '../../utils/formatters'

export default function RecentActivity({ activity, pendingTxs, blocks }) {
  const fallback = [
    ...pendingTxs.map((tx) => ({ type: 'transaction', title: tx.hash })),
    ...blocks.slice(-4).reverse().map((block) => ({ type: 'block', title: block.hash })),
  ]
  const items = activity.length ? activity : fallback

  return (
    <Card>
      <div className="section-head">
        <h2>Recent activity</h2>
        <span>{items.length}</span>
      </div>
      <div className="activity-list">
        {items.length === 0 && <p className="muted">No activity yet.</p>}
        {items.map((item, index) => (
          <div className="activity-row" key={`${item.title}-${index}`}>
            <span className={`dot ${item.type}`} />
            <div>
              <strong>{item.type}</strong>
              <p>{shortHash(item.title, 14)}</p>
            </div>
          </div>
        ))}
      </div>
    </Card>
  )
}
