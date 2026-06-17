import { whitePaperTitle, whitePaperSubtitle, whitePaperAnnotation, whitePaperSections, researchReferences } from '../data/WhitePaperData';

export function WhitePaperReader() {
    return (
        <div style={{ maxWidth: '900px', margin: '0 auto', padding: '1rem' }}>
            <div style={{ textAlign: 'center', marginBottom: '3rem', borderBottom: '1px solid #334155', paddingBottom: '2rem' }}>
                <h2 style={{ fontSize: '2.5rem', color: '#E040FB', marginBottom: '0.5rem' }}>{whitePaperTitle}</h2>
                <h3 style={{ color: '#a855f7', fontWeight: 'normal', margin: 0 }}>{whitePaperSubtitle}</h3>
            </div>

            <div style={{ backgroundColor: '#1e293b', padding: '2rem', borderRadius: '16px', marginBottom: '3rem', borderLeft: '4px solid #E040FB' }}>
                <p style={{ color: '#cbd5e1', fontSize: '1.2rem', lineHeight: '1.8', margin: 0, fontStyle: 'italic' }}>
                    {whitePaperAnnotation}
                </p>
            </div>

            <div style={{ display: 'flex', flexDirection: 'column', gap: '3rem', marginBottom: '4rem' }}>
                {whitePaperSections.map((sec, idx) => (
                    <div key={idx}>
                        <h4 style={{ color: '#c084fc', fontSize: '1.5rem', borderBottom: '1px dashed #334155', paddingBottom: '0.5rem', marginBottom: '1.5rem' }}>{sec.title}</h4>
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
                ))}
            </div>

            <div style={{ backgroundColor: '#0f172a', padding: '2rem', borderRadius: '16px', border: '1px solid #334155' }}>
                <h3 style={{ color: '#E040FB', marginTop: 0, marginBottom: '1.5rem' }}>Список литературы и ссылок</h3>
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
        </div>
    );
}
