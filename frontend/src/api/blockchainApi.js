import axios from 'axios'

const api = axios.create({
  baseURL: 'http://localhost:8080/api',
  timeout: 3000,
})

api.interceptors.response.use(
  (response) => response,
  (error) => {
    const message = error.response?.data?.error || error.message || 'Request failed'
    return Promise.reject(new Error(message))
  },
)

export const blockchainApi = {
  getStatus: () => api.get('/blockchain/status').then((res) => res.data),
  getBlocks: () => api.get('/blockchain/blocks').then((res) => res.data),
  getBlock: (hash) => api.get(`/blockchain/blocks/${hash}`).then((res) => res.data),
  mineBlock: (minerWalletId) => api.post('/blockchain/mine', { minerWalletId }).then((res) => res.data),
  createWallet: (name) => api.post('/wallets', { name }).then((res) => res.data),
  getWallets: () => api.get('/wallets').then((res) => res.data),
  getWallet: (id) => api.get(`/wallets/${id}`).then((res) => res.data),
  getUtxos: (id) => api.get(`/wallets/${id}/utxos`).then((res) => res.data),
  createTransaction: (payload) => api.post('/transactions', payload).then((res) => res.data),
  getPendingTransactions: () => api.get('/transactions/pending').then((res) => res.data),
  getTransaction: (hash) => api.get(`/transactions/${hash}`).then((res) => res.data),
}
