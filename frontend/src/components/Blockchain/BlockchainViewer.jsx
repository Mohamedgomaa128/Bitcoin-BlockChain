import { useState } from 'react'
import Card from '../common/Card'
import BlockCard from './BlockCard'
import BlockDetail from './BlockDetail'
import { useBlockchain } from '../../hooks/useBlockchain'

export default function BlockchainViewer() {
  const { blocks } = useBlockchain()
  const [selected, setSelected] = useState(null)

  return (
    <div className="page-grid">
      <Card>
        <div className="section-head">
          <h2>Blockchain</h2>
          <span>{blocks.length} blocks</span>
        </div>
        <div className="chain-scroll">
          {blocks.map((block, index) => (
            <div className="chain-node" key={block.hash}>
              <BlockCard block={block} selected={selected?.hash === block.hash} onSelect={setSelected} />
              {index < blocks.length - 1 && <span className="connector" />}
            </div>
          ))}
        </div>
      </Card>
      <BlockDetail block={selected} onClose={() => setSelected(null)} />
    </div>
  )
}
