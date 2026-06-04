export const btc = (value = 0) => `${Number(value).toFixed(4)} BTC`

export const shortHash = (hash = '', size = 10) => {
  if (!hash) return 'genesis'
  return hash.length <= size * 2 ? hash : `${hash.slice(0, size)}...${hash.slice(-size)}`
}

export const plural = (count, label) => `${count} ${label}${count === 1 ? '' : 's'}`
