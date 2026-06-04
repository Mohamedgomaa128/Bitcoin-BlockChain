import { useBlockchainContext } from '../context/BlockchainContext.jsx'

export function useBlockchain() {
  return useBlockchainContext()
}
