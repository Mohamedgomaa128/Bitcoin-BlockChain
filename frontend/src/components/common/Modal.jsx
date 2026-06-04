import { X } from 'lucide-react'
import Button from './Button'

export default function Modal({ open, title, children, onClose }) {
  if (!open) return null
  return (
    <div className="modal-backdrop" role="presentation" onClick={onClose}>
      <div className="modal" role="dialog" aria-modal="true" aria-label={title} onClick={(event) => event.stopPropagation()}>
        <div className="modal-head">
          <h2>{title}</h2>
          <Button variant="ghost" icon={X} onClick={onClose}>Close</Button>
        </div>
        {children}
      </div>
    </div>
  )
}
