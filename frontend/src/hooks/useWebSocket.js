import { useBlockchain } from './useBlockchain'

export function useWebSocket() {
  const { activity } = useBlockchain()
  return { activity }
}
