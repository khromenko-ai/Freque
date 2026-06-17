import { useState } from 'react';
import { cosmologyNodes } from '../data/CosmologyData';
import { essayIntroduction, essayChapters, essayConclusion } from '../data/EssayData';
import { NodeVisualization } from './Visualizations';

export function MainTabWorkspace() {
    const [activeTab, setActiveTab] = useState<'map' | 'essay'>('map');

    return (
        <div style={{ display: 'flex', flexDirection: 'column', height: '100%' }}>
            <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem', borderBottom: '1px solid #334155', paddingBottom: '1rem' }}>
                <button 
                    onClick={() => setActiveTab('map')}
                    style={{
                        backgroundColor: activeTab === 'map' ? '#00BCD420' : 'transparent',
                        color: activeTab === 'map' ? '#00BCD4' : '#94a3b8',
                        border: 'none',
                        padding: '0.5rem 1rem',
                        borderRadius: '8px',
                        cursor: 'pointer',
                        fontWeight: activeTab === 'map' ? 'bold' : 'normal',
                        transition: 'all 0.2s'
                    }}
                >
                    Карта смыслов
                </button>
                <button 
                    onClick={() => setActiveTab('essay')}
                    style={{
                        backgroundColor: activeTab === 'essay' ? '#00BCD420' : 'transparent',
                        color: activeTab === 'essay' ? '#00BCD4' : '#94a3b8',
                        border: 'none',
                        padding: '0.5rem 1rem',
                        borderRadius: '8px',
                        cursor: 'pointer',
                        fontWeight: activeTab === 'essay' ? 'bold' : 'normal',
                        transition: 'all 0.2s'
                    }}
                >
                    Эссе
                </button>
            </div>

            {activeTab === 'map' ? (
                <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(300px, 1fr))', gap: '1.5rem' }}>
                    {cosmologyNodes.map(node => (
                        <div key={node.id} style={{ backgroundColor: '#0f172a', padding: '1.5rem', borderRadius: '16px', border: `1px solid ${node.accentColor}40`, borderTop: `4px solid ${node.accentColor}` }}>
                            <h3 style={{ color: node.accentColor, marginTop: 0 }}>{node.title}</h3>
                            <div style={{ width: '100%', height: '180px', marginBottom: '1.5rem', borderRadius: '12px', overflow: 'hidden' }}>
                                <NodeVisualization nodeId={node.id} accentColor={node.accentColor} />
                            </div>
                            <p style={{ color: '#cbd5e1', lineHeight: '1.6' }}>{node.description}</p>
                            <blockquote style={{ margin: '1rem 0 0', paddingLeft: '1rem', borderLeft: `2px solid ${node.accentColor}80`, color: '#94a3b8', fontStyle: 'italic', fontSize: '0.9rem' }}>
                                {node.quote}
                            </blockquote>
                            <div style={{ marginTop: '1rem', color: '#64748b', fontSize: '0.8rem' }}>Ref: {node.refs}</div>
                        </div>
                    ))}
                </div>
            ) : (
                <div style={{ maxWidth: '800px', margin: '0 auto', paddingBottom: '4rem' }}>
                    <div style={{ textAlign: 'center', marginBottom: '3rem' }}>
                        <h2 style={{ fontSize: '2.5rem', color: '#00BCD4', marginBottom: '1rem' }}>Частотная Космология</h2>
                        <h3 style={{ color: '#94a3b8', fontWeight: 'normal' }}>Сборник эссе и размышлений</h3>
                    </div>

                    <div style={{ backgroundColor: '#1e293b', padding: '2rem', borderRadius: '16px', marginBottom: '3rem', borderLeft: '4px solid #00BCD4' }}>
                        <h4 style={{ color: '#e2e8f0', marginTop: 0, fontSize: '1.5rem' }}>{essayIntroduction.title}</h4>
                        {essayIntroduction.paragraphs.map((p, i) => (
                            <p key={i} style={{ color: '#cbd5e1', lineHeight: '1.8', fontSize: '1.1rem' }}>{p}</p>
                        ))}
                    </div>

                    <div style={{ display: 'flex', flexDirection: 'column', gap: '4rem' }}>
                        {essayChapters.map((chapter, idx) => (
                            <div key={idx}>
                                <h4 style={{ color: '#00BCD4', fontSize: '1.8rem', margin: '0 0 0.5rem 0' }}>{chapter.title}</h4>
                                {chapter.subtitle && <h5 style={{ color: '#94a3b8', fontSize: '1.2rem', margin: '0 0 1.5rem 0', fontWeight: 'normal' }}>{chapter.subtitle}</h5>}
                                
                                {chapter.vizId && (
                                    <div style={{ width: '100%', height: '240px', marginBottom: '2rem', borderRadius: '16px', overflow: 'hidden', border: '1px solid #334155' }}>
                                        <NodeVisualization nodeId={chapter.vizId} accentColor={chapter.vizColor || '#00BCD4'} />
                                    </div>
                                )}
                                
                                <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                                    {chapter.paragraphs.map((p, pIdx) => (
                                        <p key={pIdx} style={{ color: '#cbd5e1', lineHeight: '1.8', margin: 0, fontSize: '1.1rem' }}>{p}</p>
                                    ))}
                                </div>
                            </div>
                        ))}
                    </div>

                    <div style={{ marginTop: '4rem', padding: '2rem', backgroundColor: '#0f172a', borderRadius: '16px', border: '1px dashed #334155' }}>
                        <h4 style={{ color: '#e2e8f0', marginTop: 0, fontSize: '1.5rem' }}>{essayConclusion.title}</h4>
                        {essayConclusion.paragraphs.map((p, i) => (
                            <p key={i} style={{ color: '#94a3b8', lineHeight: '1.8', fontStyle: 'italic' }}>{p}</p>
                        ))}
                    </div>
                </div>
            )}
        </div>
    );
}
