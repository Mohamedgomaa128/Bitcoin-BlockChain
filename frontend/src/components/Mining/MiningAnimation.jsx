export default function MiningAnimation({ active }) {
  return (
    <div className={`mining-animation ${active ? 'active' : ''}`}>
      {Array.from({ length: 18 }, (_, index) => <span key={index}>{active ? '1' : '0'}</span>)}
    </div>
  )
}
