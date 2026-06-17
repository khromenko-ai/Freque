import { useState } from 'react';
import { whitePaperTitle, whitePaperSubtitle, whitePaperAnnotation, whitePaperSections, researchReferences } from '../data/WhitePaperData';
import { NodeVisualization } from './Visualizations';

export function WhitePaperReader() {
    const [selectedWhitePaperIndex, setSelectedWhitePaperIndex] = useState(0); // 0 is Annotation, 1-N are sections, N+1 is references

    const tabList = [
        "Аннотация",
        ...whitePaperSections.map((_, idx) => `Глава ${idx + 1}`),
        "Источники"
    ];

    return (
        <div style={{ maxWidth: '900px', margin: '0 auto', padding: '1rem', display: 'flex', flexDirection: 'column', height: '100%' }}>
            <div style={{ textAlign: 'center', marginBottom: '1.5rem', borderBottom: '1px solid #334155', paddingBottom: '1rem' }}>
                <h2 style={{ fontSize: '2rem', color: '#E040FB', margin: '0 0 0.5rem 0' }}>{whitePaperTitle}</h2>
                <h3 style={{ color: '#a855f7', fontWeight: 'normal', margin: 0, fontSize: '1.1rem' }}>{whitePaperSubtitle}</h3>
            </div>

            {/* Scrollable Tabs */}
            <div style={{ display: 'flex', overflowX: 'auto', gap: '0.5rem', paddingBottom: '1rem', marginBottom: '2rem', flexShrink: 0, borderBottom: '1px solid #1e293b' }}>
                {tabList.map((tab, idx) => (
                    <button
                        key={idx}
                        onClick={() => setSelectedWhitePaperIndex(idx)}
                        style={{
                            padding: '0.5rem 1rem',
                            backgroundColor: selectedWhitePaperIndex === idx ? '#E040FB20' : 'transparent',
                            color: selectedWhitePaperIndex === idx ? '#E040FB' : '#94a3b8',
                            border: 'none',
                            borderRadius: '8px',
                            cursor: 'pointer',
                            whiteSpace: 'nowrap',
                            fontWeight: selectedWhitePaperIndex === idx ? 'bold' : 'normal',
                            transition: 'all 0.2s ease-in-out'
                        }}
                    >
                        {tab}
                    </button>
                ))}
            </div>

            <div style={{ flex: 1, overflowY: 'auto', paddingRight: '0.5rem' }}>
                {selectedWhitePaperIndex === 0 && (
                    <div style={{ backgroundColor: '#1e293b', padding: '2rem', borderRadius: '16px', borderLeft: '4px solid #E040FB', animation: 'fadeIn 0.3s ease-in-out' }}>
                        <p style={{ color: '#cbd5e1', fontSize: '1.2rem', lineHeight: '1.8', margin: 0, fontStyle: 'italic' }}>
                            {whitePaperAnnotation}
                        </p>
                    </div>
                )}

                {selectedWhitePaperIndex > 0 && selectedWhitePaperIndex <= whitePaperSections.length && (
                    <div style={{ animation: 'fadeIn 0.3s ease-in-out' }}>
                        {(() => {
                            const sec = whitePaperSections[selectedWhitePaperIndex - 1];
                            const wpVizId = (() => {
                                switch(selectedWhitePaperIndex) {
                                    case 1: return "intro";
                                    case 2: return "time_reality";
                                    case 3: return "observer";
                                    case 4: return "ring";
                                    case 5: return "hierarchy";
                                    case 6: return "holography";
                                    case 7: return "spectral";
                                    case 8: return "observer";
                                    case 9: return "holography";
                                    case 10: return "intro";
                                    case 11: return "ring";
                                    default: return "intro";
                                }
                            })();

                            return (
                                <div>
                                    <h4 style={{ color: '#c084fc', fontSize: '1.5rem', borderBottom: '1px dashed #334155', paddingBottom: '0.5rem', marginBottom: '1.5rem', marginTop: 0 }}>{sec.title}</h4>
                                    
                                    <div style={{ width: '100%', height: '200px', marginBottom: '2rem', borderRadius: '16px', overflow: 'hidden', border: '1px solid #E040FB50' }}>
                                        <NodeVisualization nodeId={wpVizId} accentColor="#E040FB" />
                                    </div>

                                    <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
                                        {sec.paragraphs.map((p, pIdx) => {
                                            const splitP = p.split(':');
                                            const boldPart = splitP.length > 1 ? splitP[0] + ':' : '';
                                            const restPart = splitP.length > 1 ? splitP.slice(1).join(':') : p;

                                            return (
                                                <p key={pIdx} style={{ color: '#e2e8f0', lineHeight: '1.8', margin: 0, fontSize: '1.1rem' }}>
                                                    {boldPart && <strong style={{ color: '#e879f9' }}>{boldPart}</strong>}
                                                    {restPart}
                                                </p>
                                            );
                                        })}
                                    </div>
                                </div>
                            );
                        })()}
                    </div>
                )}

                {selectedWhitePaperIndex === whitePaperSections.length + 1 && (
                    <div style={{ backgroundColor: '#0f172a', padding: '2rem', borderRadius: '16px', border: '1px solid #334155', animation: 'fadeIn 0.3s ease-in-out' }}>
                        <h3 style={{ color: '#E040FB', marginTop: 0, marginBottom: '1.5rem', fontSize: '1.5rem' }}>Список литературы и ссылок</h3>
                        <div style={{ display: 'flex', flexDirection: 'column', gap: '1.5rem' }}>
                            {researchReferences.map((ref, idx) => (
                                <div key={idx}>
                                    <a href={ref.url} target="_blank" rel="noopener noreferrer" style={{ color: '#60a5fa', textDecoration: 'none', fontWeight: 'bold', fontSize: '1.1rem' }}>
                                        [{idx + 1}] {ref.title}
                                    </a>
                                    <p style={{ color: '#94a3b8', margin: '0.5rem 0 0 0', lineHeight: '1.5' }}>{ref.description}</p>
                                </div>
                            ))}
                        </div>
                    </div>
                )}
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
