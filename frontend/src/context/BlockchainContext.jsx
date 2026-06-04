import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react'
import { Client } from '@stomp/stompjs'
import { blockchainApi } from '../api/blockchainApi'

const BlockchainContext = createContext(null)

export function BlockchainProvider({ children }) {
  const [status, setStatus] = useState(null)
  const [blocks, setBlocks] = useState([])
  const [wallets, setWallets] = useState([])
  const [pendingTxs, setPendingTxs] = useState([])
  const [activity, setActivity] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')

  const capture = useCallback(async (task) => {
    setError('')
    try {
      return await task()
    } catch (err) {
      setError(err.message)
      throw err
    }
  }, [])

  const refreshStatus = useCallback(() => capture(async () => setStatus(await blockchainApi.getStatus())), [capture])
  const refreshBlocks = useCallback(() => capture(async () => setBlocks(await blockchainApi.getBlocks())), [capture])
  const refreshWallets = useCallback(() => capture(async () => setWallets(await blockchainApi.getWallets())), [capture])
  const refreshPendingTxs = useCallback(() => capture(async () => setPendingTxs(await blockchainApi.getPendingTransactions())), [capture])

  const refreshAll = useCallback(async () => {
    setLoading(true)
    try {
      await Promise.all([refreshStatus(), refreshBlocks(), refreshWallets(), refreshPendingTxs()])
    } finally {
      setLoading(false)
    }
  }, [refreshBlocks, refreshPendingTxs, refreshStatus, refreshWallets])

  const createWallet = useCallback(async (name) => {
    const wallet = await capture(() => blockchainApi.createWallet(name))
    setActivity((items) => [{ type: 'wallet', title: `Created ${wallet.name}` }, ...items].slice(0, 12))
    await refreshAll()
    return wallet
  }, [capture, refreshAll])

  const createTransaction = useCallback(async (payload) => {
    const tx = await capture(() => blockchainApi.createTransaction(payload))
    setActivity((items) => [{ type: 'transaction', title: `Queued ${tx.hash}` }, ...items].slice(0, 12))
    await refreshAll()
    return tx
  }, [capture, refreshAll])

  const mineBlock = useCallback(async (minerWalletId) => {
    const block = await capture(() => blockchainApi.mineBlock(minerWalletId))
    setActivity((items) => [{ type: 'block', title: `Mined block ${block.hash}` }, ...items].slice(0, 12))
    await refreshAll()
    return block
  }, [capture, refreshAll])

  useEffect(() => {
    refreshAll()
  }, [refreshAll])

  useEffect(() => {
    let cancelled = false
    const client = new Client({
      reconnectDelay: 4000,
      onConnect: () => {
        client.subscribe('/topic/blocks', (message) => {
          const block = JSON.parse(message.body)
          setActivity((items) => [{ type: 'block', title: `New block ${block.hash}` }, ...items].slice(0, 12))
          refreshAll()
        })
        client.subscribe('/topic/transactions', (message) => {
          const tx = JSON.parse(message.body)
          setActivity((items) => [{ type: 'transaction', title: `New pending tx ${tx.hash}` }, ...items].slice(0, 12))
          refreshAll()
        })
      },
    })

    import('sockjs-client')
      .then((module) => {
        if (cancelled) return
        const SockJS = module.default
        client.webSocketFactory = () => new SockJS('http://localhost:8080/ws')
        client.activate()
      })
      .catch(() => {
        if (!cancelled) {
          setActivity((items) => [{ type: 'socket', title: 'Live updates unavailable' }, ...items].slice(0, 12))
        }
      })

    return () => {
      cancelled = true
      client.deactivate()
    }
  }, [refreshAll])

  const value = useMemo(() => ({
    status,
    blocks,
    wallets,
    pendingTxs,
    activity,
    loading,
    error,
    refreshAll,
    refreshBlocks,
    refreshWallets,
    refreshPendingTxs,
    createWallet,
    createTransaction,
    mineBlock,
  }), [activity, blocks, createTransaction, createWallet, error, loading, mineBlock, pendingTxs, refreshAll, refreshBlocks, refreshPendingTxs, refreshWallets, status, wallets])

  return <BlockchainContext.Provider value={value}>{children}</BlockchainContext.Provider>
}

export function useBlockchainContext() {
  const context = useContext(BlockchainContext)
  if (!context) {
    throw new Error('useBlockchainContext must be used inside BlockchainProvider')
  }
  return context
}
