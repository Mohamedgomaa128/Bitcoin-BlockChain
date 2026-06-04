import { btc } from '../../utils/formatters'

export default function BalanceDisplay({ balance }) {
  return <strong className="balance">{btc(balance)}</strong>
}
