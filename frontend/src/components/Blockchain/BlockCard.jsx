import { Box } from 'lucide-react'
import { shortHash } from '../../utils/formatters'

export default function BlockCard({ block, selected, onSelect }) {
  return (
    <button className={`block-card ${selected ? 'selected' : ''}`} onClick={() => onSelect(block)}>
      <Box size={20} />
      <strong>Height {block.height}</strong>
      <span>{shortHash(block.hash, 8)}</span>
      <small>{block.transactionCount} txs</small>
    </button>
  )
}
