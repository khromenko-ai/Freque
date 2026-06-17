import { firstSourceTitle, firstSourceIntroduction, firstSourceSections } from '../data/FirstSourceData';

export function FirstSourceReader() {
    return (
        <div style={{ maxWidth: '800px', margin: '0 auto', padding: '1rem' }}>
            <h2 style={{ color: '#4CAF50', marginBottom: '1rem', fontSize: '2rem' }}>{firstSourceTitle}</h2>
            <div style={{ backgroundColor: '#1e293b', padding: '1.5rem', borderRadius: '12px', marginBottom: '2rem', borderLeft: '4px solid #4CAF50' }}>
                <p style={{ color: '#e2e8f0', margin: 0, fontSize: '1.1rem', lineHeight: '1.6' }}>{firstSourceIntroduction}</p>
            </div>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '2rem' }}>
                {firstSourceSections.map((section, idx) => (
                    <div key={idx} style={{ backgroundColor: '#0f172a', padding: '2rem', borderRadius: '16px', border: '1px solid #334155' }}>
                        <h3 style={{ color: '#38bdf8', marginTop: 0, fontSize: '1.4rem' }}>{idx + 1}. {section.title}</h3>
                        <p style={{ color: '#cbd5e1', lineHeight: '1.8', fontSize: '1.1rem' }}>{section.text}</p>
                        
                        {(section.quote || section.refs) && (
                            <div style={{ marginTop: '1.5rem', paddingTop: '1.5rem', borderTop: '1px solid #1e293b' }}>
                                {section.quote && (
                                    <blockquote style={{ margin: '0 0 1rem 0', paddingLeft: '1rem', borderLeft: '3px solid #38bdf8', color: '#94a3b8', fontStyle: 'italic' }}>
                                        "{section.quote}"
                                    </blockquote>
                                )}
                                {section.refs && (
                                    <div style={{ color: '#64748b', fontSize: '0.9rem' }}>
                                        Источники: {section.refs}
                                    </div>
                                )}
                            </div>
                        )}
                    </div>
                ))}
            </div>
        </div>
    );
}
