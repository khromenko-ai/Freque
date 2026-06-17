import { useState } from 'react';
import { journalName, reviewerName, criticismIntro, criticismItems } from '../data/CriticismData';
import { NodeVisualization } from './Visualizations';

export function Criticism() {
    const [expandedIndex, setExpandedIndex] = useState<number | null>(null);

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto', padding: '1rem' }}>
            <div style={{ backgroundColor: '#1e293b', padding: '2rem', borderRadius: '16px', marginBottom: '3rem', border: '1px solid #334155' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '1.5rem' }}>
                    <span style={{ fontSize: '2rem' }}>⚖️</span>
                    <div>
                        <h2 style={{ color: '#FF9800', margin: 0 }}>Научная Критика</h2>
                        <div style={{ color: '#94a3b8', fontSize: '0.9rem', marginTop: '0.25rem' }}>{journalName}</div>
                    </div>
                </div>
                <div style={{ padding: '1rem', backgroundColor: '#0f172a', borderRadius: '8px', color: '#fb923c', fontStyle: 'italic', marginBottom: '1.5rem' }}>
                    {reviewerName}
                </div>
                <p style={{ color: '#cbd5e1', lineHeight: '1.6', margin: 0 }}>{criticismIntro}</p>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
                {criticismItems.map((item, index) => {
                    const isExpanded = expandedIndex === index;
                    return (
                        <div key={item.id} style={{ backgroundColor: '#0f172a', borderRadius: '16px', overflow: 'hidden', border: `1px solid ${item.accentColor}40`, transition: 'all 0.3s' }}>
                            <div 
                                onClick={() => setExpandedIndex(isExpanded ? null : index)}
                                style={{ 
                                    backgroundColor: `${item.accentColor}20`, 
                                    padding: '1.5rem', 
                                    borderBottom: isExpanded ? `1px solid ${item.accentColor}40` : 'none',
                                    cursor: 'pointer',
                                    display: 'flex',
                                    justifyContent: 'space-between',
                                    alignItems: 'center'
                                }}
                            >
                                <h3 style={{ color: item.accentColor, margin: 0, fontSize: '1.3rem' }}>{item.title}</h3>
                                <div style={{ 
                                    fontSize: '1.5rem', 
                                    color: item.accentColor,
                                    transform: isExpanded ? 'rotate(180deg)' : 'rotate(0deg)',
                                    transition: 'transform 0.3s'
                                }}>
                                    ▼
                                </div>
                            </div>
                            
                            {isExpanded && (
                                <div style={{ padding: '1.5rem', display: 'flex', flexDirection: 'column', gap: '1.5rem', animation: 'fadeIn 0.3s ease-in-out' }}>
                                    <div style={{ width: '100%', height: '220px', borderRadius: '12px', overflow: 'hidden', border: `1px solid ${item.accentColor}40` }}>
                                        <NodeVisualization nodeId={item.id} accentColor={item.accentColor} />
                                    </div>
                                    <div>
                                        <div style={{ color: '#94a3b8', fontSize: '0.8rem', textTransform: 'uppercase', letterSpacing: '1px', marginBottom: '0.5rem' }}>Рецензия</div>
                                        <div style={{ color: '#e2e8f0', lineHeight: '1.7', whiteSpace: 'pre-line' }}>{item.reviewerOpinion}</div>
                                    </div>
                                    
                                    <div style={{ borderTop: '1px dashed #334155', paddingTop: '1.5rem' }}>
                                        <div style={{ color: item.accentColor, fontSize: '0.8rem', textTransform: 'uppercase', letterSpacing: '1px', marginBottom: '0.5rem' }}>Ответ Авторов</div>
                                        <div style={{ color: '#cbd5e1', lineHeight: '1.7', whiteSpace: 'pre-line' }}>{item.authorResponse}</div>
                                    </div>
                                </div>
                            )}
                        </div>
                    );
                })}
            </div>
            <style>
                {`
                @keyframes fadeIn {
                    from { opacity: 0; transform: translateY(-10px); }
                    to { opacity: 1; transform: translateY(0); }
                }
                `}
            </style>
        </div>
    );
}
